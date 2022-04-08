package com.tcb.auto.subprocess.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.junit.Test;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

/**
 * 
 * @author anhptn14
 * @deprecated dung jsonpath can dung 2 thu vien minidev gay conflict voi thu
 *             vien cua serenity core --> Khong chay duoc @Steps
 */
@Deprecated
public class JsonPathProcess {
	/**
	 * Convert string file to Json Document (DocumentContext)
	 * 
	 * @param filePath absolute path to the file
	 * @return DocumentContext
	 */
	public DocumentContext jsonDocumentFromFile(String filePath) {
		DocumentContext document = null;
		try {
			String joString = new String(Files.readAllBytes(Paths.get(filePath)));
			document = JsonPath.parse(joString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * Convert string to Json Document (DocumentContext)
	 * 
	 * @param jsString
	 * @return
	 */
	public DocumentContext jsonDocumentFromString(String jsString) {
		return JsonPath.parse(jsString);
	}

	/**
	 * Update Json node
	 * 
	 * @param document DocumentContext
	 * @param jsPath   jsonpath of the node. ex $ for root. $..age for a node;
	 *                 $..phone[0].type where phone is an array
	 * @param newVal   value of the new node
	 * @return
	 */
	public DocumentContext updateJsons(DocumentContext document, List<String> jsPath, List<Object> newVal) {
		if (jsPath.size() != newVal.size()) {
			System.err.println("updateJsons: Invalid input, jsPath lengh != newVal lenght");
			return null;
		}
		for (int i = 0; i < jsPath.size(); i++) {
			document.set(jsPath.get(i), newVal.get(i));
		}
		return document;
	}

	/**
	 * Update Json content
	 * 
	 * @param document DocumentContext
	 * @param jsPath   jsonpath to the node
	 * @param newVal   can be String, int, long
	 * @return
	 */
	public DocumentContext updateJson(DocumentContext document, String jsPath, Object newVal) {
		return document.set(jsPath, newVal);
	}

	/**
	 * Add node to Json.
	 * 
	 * @param document   DocumentContext
	 * @param parentPath parent node. If add to root, so parent node is $
	 * @param newKey     key name
	 * @param newVal     value of node, can be a Map
	 * @return
	 */
	public DocumentContext addToJson(DocumentContext document, String parentPath, String newKey, Object newVal) {
		return document.put(parentPath, newKey, newVal);
	}

	/**
	 * Convert {@link DocumentContext} to {@link String}
	 * 
	 * @param document
	 * @return
	 */
	public String toString(DocumentContext document) {
		return document.jsonString();
	}

	/**
	 * Remove a node
	 * 
	 * @param document
	 * @param jsPath   path to the node
	 * @return
	 */
	public DocumentContext removeJson(DocumentContext document, String jsPath) {
		return document.delete(jsPath);
	}

	@Test
	public void an_filter_can_update() {
		String filePath = "D:\\workspace\\Payment\\data\\outward\\Request\\json2.json";
		String joString;
		try {
			joString = new String(Files.readAllBytes(Paths.get(filePath)));
			Object o = Configuration.defaultConfiguration().jsonProvider().parse(joString);
		} catch (IOException e) {
			e.printStackTrace();
		}

		DocumentContext document = this.jsonDocumentFromFile(filePath);
		this.updateJson(document, "$..book[0].author", "AAAA");
		this.removeJson(document, "$..book");
		this.addToJson(document, "$..store", "newTag", "newVal");
		System.out.println(this.toString(document));

		String filePath2 = "D:\\workspace\\Payment\\data\\outward\\Request\\json.json";
		DocumentContext doc = this.jsonDocumentFromFile(filePath2);
//		this.removeJson(doc, "$.phoneNumbers");
		Map x = new HashedMap<String, String>();
		x.put("addkey1", "addVal1");
		x.put("addkey2", "addVal2");
		this.addToJson(doc, "$", "phoneNumbers", x);
		this.addToJson(doc, "$", "firstName", x);
		this.updateJson(document, "$.phoneNumbers", x);
		System.out.println(this.toString(doc));
	}

}

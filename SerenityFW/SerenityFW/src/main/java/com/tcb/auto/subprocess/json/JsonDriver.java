package com.tcb.auto.subprocess.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 
 * @author anhptn14
 *
 */
public class JsonDriver {
	private static final String JSON_XPATH_ABSOLUTE_SEPARATE_STRING = "\\.";

	/**
	 * Convert from String to Json Object
	 * 
	 * @param jsonString
	 * @return
	 */
	public JsonElement jsonFromString(String jsonString) {
		return new Gson().fromJson(jsonString, JsonElement.class);

	}

	/**
	 * Convert text from file to JsonElement
	 * 
	 * @param filePath
	 * @return JsonElement
	 */
	public JsonElement jsonElementFromFile(String filePath) {
		String jsonString = null;
		try {
			jsonString = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JsonElement je = new Gson().fromJson(jsonString, JsonElement.class);
		return je;
	}

	/**
	 * Add key-val vao root hoac parentKey Neu key trung voi key da co --> se update
	 * gia tri moi theo key
	 * 
	 * @param je        JsonElement need to edit
	 * @param key       key to add
	 * @param val       Val to add, can be String/Number/Boolean or JsonElement
	 *                  object
	 * @param parentKey if no parentKey, then add to Root node
	 * @return
	 */
	public JsonElement addElement(JsonElement je, String key, Object val, String... parentKey) {
		JsonObject rootObject = je.getAsJsonObject();
		JsonObject jo = null;
		if (parentKey.length != 0) {
			JsonElement parentObject = rootObject.get(parentKey[0]);
			jo = parentObject.getAsJsonObject();
		} else {
			jo = rootObject;
		}
		if (val instanceof String)
			jo.addProperty(key, String.valueOf(val));
		else if (val instanceof Number)
			jo.addProperty(key, Double.parseDouble(String.valueOf(val)));
		else if (val instanceof Boolean)
			jo.addProperty(key, (Boolean) val);
		else
			jo.add(key, (JsonElement) val);

		return je;
	}

	/**
	 * Divide the xpath to List of tagName
	 * 
	 * @param abXpath  ex
	 *                 .Payload.Document.FIToFICstmrCdtTrf.CdtTrfTxInf[0].DbtrAcct.Id.Othr.Id
	 * @param sPattern
	 * @return
	 */
	public List<String> getXpathLevelList(String abXpath, String sPattern) {
		Pattern pattern = Pattern.compile(sPattern);
		String[] tempLevels = pattern.split(abXpath);
		List<String> levels = new ArrayList<String>();
		for (String string : tempLevels) {
			if (!StringUtils.isBlank(string))
				levels.add(string);
		}
		return levels;
	}

	/**
	 * Add/Edit Element via xpath
	 * 
	 * @param je
	 * @param sXpath abs xpath to the element; if path is exist --> edit the val, if
	 *               path is not exist --> add to the parent node with the val
	 * @param val    new value, can be JsonElement or String/Number/Boolean
	 * @return
	 */
	public JsonElement addElementViaXpath(JsonElement je, String sXpath, Object val) {
		List<String> levels = this.getXpathLevelList(sXpath, JSON_XPATH_ABSOLUTE_SEPARATE_STRING);
		JsonElement jParent = this.getParentElement(je, sXpath);
		String childTag = levels.get(levels.size() - 1);
		if (!childTag.contains("[")) {
			if (jParent.getAsJsonObject().get(childTag) instanceof JsonArray)
				this.addToArray(jParent.getAsJsonObject().get(childTag), val);
			else
				this.addElement(jParent, childTag, val);

		} else {
			List<String> listChild = this.getKeynIndex(childTag);
			JsonArray jChild = jParent.getAsJsonObject().get(listChild.get(0)).getAsJsonArray();
			if (val instanceof JsonElement)
				this.setToArray(jChild, Integer.parseInt(listChild.get(1)), (JsonElement) val);
			else
				this.addElement(jChild, listChild.get(0), (String) val);
		}

		return je;

	}

	/**
	 * Remove the node via its absolute path
	 * 
	 * @param je
	 * @param sXpath
	 * @return
	 */
	public JsonElement removeElementViaXpath(JsonElement je, String sXpath) {
		List<String> levels = this.getXpathLevelList(sXpath, JSON_XPATH_ABSOLUTE_SEPARATE_STRING);
		JsonElement jhere = this.getParentElement(je, sXpath);
		String childTag = levels.get(levels.size() - 1);
		this.removeElement(jhere, childTag);
		return je;
	}

	/**
	 * Remove element khoi JsonElement
	 * 
	 * @param je        JsonElement can thay doi
	 * @param key       KeyName cua element
	 * @param parentKey Neu parentKey = null --> Remove from root
	 * @return
	 */
	public JsonElement removeElement(JsonElement je, String key, String... parentKey) {
		JsonObject rootObject = je.getAsJsonObject();
		JsonObject jo = null;
		if (parentKey.length != 0) {
			JsonElement parentObject = rootObject.get(parentKey[0]);
			jo = parentObject.getAsJsonObject();
		} else {
			jo = rootObject;
		}
		jo.remove(String.valueOf(key));
		return je;
	}

	/**
	 * Remove element out of an array by its index
	 * 
	 * @param je        JsonElement can thay doi
	 * @param index     index of array need to remove
	 * @param parentKey if no parentKey, then remove from root
	 * @return
	 */
	public JsonElement removeArray(JsonElement je, int index, String... parentKey) {
		JsonObject rootObject = je.getAsJsonObject();
		JsonArray array = null;
		if (parentKey.length != 0) {
			JsonElement parentObject = rootObject.get(parentKey[0]);
			array = parentObject.getAsJsonArray();
		} else {
			array = je.getAsJsonArray();
		}
		array.remove(index);
		return je;
	}

	/**
	 * Add element (String/Number/Boolean/JsonElement) to an Array
	 * 
	 * @param je        must be JsonArray
	 * @param val       can be String/Number/Boolean/JsonElement
	 * @param parentKey if null --> add to root
	 * @return
	 */
	public JsonElement addToArray(JsonElement je, Object val /* , String... parentKey */) {
		JsonArray array = je.getAsJsonArray();
		if (val instanceof String)
			array.add(String.valueOf(val));
		else if (val instanceof Number)
			array.add(Double.parseDouble(String.valueOf(val)));
		else if (val instanceof Boolean)
			array.add((Boolean) val);
		else
			array.add((JsonElement) val);
		return je;
	}

	/**
	 * Set new JsonElement to an JsonArray
	 * 
	 * @param je       must be jsonArray
	 * @param index
	 * @param val
	 * @param arrayKey if no arraykey --> add to top
	 * @return
	 */
	public JsonElement setToArray(JsonElement je, int index, JsonElement val /* , String... arrayKey */) {
//		JsonObject rootObject = null;
//		if (je instanceof JsonObject)
//			je.getAsJsonObject();
//		if (je instanceof JsonArray)
//			je.getAsJsonArray();
//		JsonArray array = null;
//		if (arrayKey.length != 0) {
//			JsonElement parentObject = rootObject.get(arrayKey[0]);
//			array = parentObject.getAsJsonArray();
//		} else {
//			array = je.getAsJsonArray();
//		}
		JsonArray array = je.getAsJsonArray();

		array.set(index, val);
		return je;
	}

	/**
	 * 
	 * @param abXpath
	 * @param sPattern String separator between each level. ex . (\\. in java)
	 * @return
	 */
	private List<String> getLevelList(String abXpath, String sPattern) {
		Pattern pattern = Pattern.compile(sPattern);
		String[] tempLevels = pattern.split(abXpath);
		List<String> levels = new ArrayList<String>();
		for (String string : tempLevels) {
			if (!StringUtils.isBlank(string))
				levels.add(string);
		}
		return levels;
	}

	/***
	 * Divide ctag[0] to tagname:ctag and index 0
	 * 
	 * @param rawKey
	 * @return
	 */
	public List<String> getKeynIndex(String rawKey) {
		rawKey = rawKey.replace("[", ".").replace("]", ".");
		List<String> keyIndex = this.getLevelList(rawKey, JSON_XPATH_ABSOLUTE_SEPARATE_STRING);
		return keyIndex;
	}

	/**
	 * Get parent node or currentNode
	 * 
	 * @param jroot
	 * @param abXpath
	 * @param getCurrent if not null, then get the current not of the expath; If
	 *                   null, get the parent node
	 * @return
	 */
	public JsonElement getParentElement(JsonElement jroot, String abXpath) {
		List<String> levels = this.getLevelList(abXpath, JSON_XPATH_ABSOLUTE_SEPARATE_STRING);
		/**
		 * Xly xpath
		 */
		int i = 0;
		String temPath = abXpath;
		JsonElement jhere = null;
		if (jroot.isJsonObject())
			jhere = jroot.getAsJsonObject();
		if (jroot.isJsonArray())
			jhere = jroot.getAsJsonArray();
		while (i < levels.size() - 1) {
			if (!levels.get(i).contains("["))
				jhere = jhere.getAsJsonObject().get(levels.get(i));
			else {
				List<String> keyIndex = getKeynIndex(levels.get(i));
				jhere = jhere.getAsJsonObject().get(keyIndex.get(0)).getAsJsonArray()
						.get(Integer.parseInt(keyIndex.get(1)));
			}
			i++;
		}
		return jhere;
	}

	/**
	 * Get String value of the Node via abXpath
	 * 
	 * @param messString
	 * @param abXpath
	 * @return
	 */
	public String getNodeValue(String messString, String abXpath) {
		JsonElement je = new Gson().fromJson(messString, JsonElement.class);
		JsonElement jparent = getParentElement(je, abXpath);
		List<String> list = getLevelList(abXpath, JSON_XPATH_ABSOLUTE_SEPARATE_STRING);
		String chilTag = list.get(list.size() - 1);
		JsonElement jcur = null;
		if (chilTag.contains("[")) {
			List<String> chiTagList = getKeynIndex(chilTag);
			jcur = jparent.getAsJsonObject().get(chiTagList.get(0)).getAsJsonArray()
					.get(Integer.parseInt(chiTagList.get(1)));
			return jcur.toString();
		} else
			return jparent.getAsJsonObject().get(chilTag).toString();

	}

	@Test
	public void test2() {
		String mess = "{\"Header\":{\"SenderReference\":\"0200970407070317383220200001000195\",\"MessageIdentifier\":\"pacs.008.001.07\",\"Format\":\"MX\",\"Sender\":{\"ID\":\"VTCBVNVN\",\"Name\":\"Techcombank\"},\"Receiver\":{\"ID\":\"NAPASVNV\",\"Name\":\"Napas\"},\"Timestamp\":\"2020-07-03T17:38:32.562+07:00\"},\"Payload\":{\"AppHdr\":{\"Fr\":{\"FIId\":{\"FinInstnId\":{\"ClrSysMmbId\":{\"MmbId\":\"VTCBVNVN\"}}}},\"To\":{\"FIId\":{\"FinInstnId\":{\"ClrSysMmbId\":{\"MmbId\":\"NAPASVNV\"}}}},\"BizMsgIdr\":\"0200970407070317383220200001000195\",\"MsgDefIdr\":\"pacs.008.001.07\",\"BizSvc\":\"ACH\",\"CreDt\":\"2020-07-03T10:38:32.562Z\"},\"Document\":{\"FIToFICstmrCdtTrf\":{\"GrpHdr\":{\"MsgId\":\"0200970407070317383220200001000195\",\"CreDtTm\":\"2020-07-03T17:38:32.562+07:00\",\"NbOfTxs\":\"1\",\"TtlIntrBkSttlmAmt\":{\"Value\":\"100.00\",\"Ccy\":\"VND\"},\"IntrBkSttlmDt\":\"2020-07-03\",\"SttlmInf\":{\"SttlmMtd\":\"CLRG\"}},\"CdtTrfTxInf\":[{\"PmtId\":{\"InstrId\":\"9120209704070703173832100000000195\",\"EndToEndId\":\"070300IF_DEP0000100000000195\",\"TxId\":\"0200970407070317383220200001000195\"},\"PmtTpInf\":{\"ClrChanl\":\"RTNS\",\"SvcLvl\":{\"Prtry\":\"0100\"},\"LclInstrm\":{\"Prtry\":\"CSDC\"},\"CtgyPurp\":{\"Prtry\":\"001\"}},\"IntrBkSttlmAmt\":{\"Value\":\"100.00\",\"Ccy\":\"VND\"},\"ChrgBr\":\"SLEV\",\"InstgAgt\":{\"FinInstnId\":{\"ClrSysMmbId\":{\"MmbId\":\"VTCBVNVN\"}}},\"InstdAgt\":{\"FinInstnId\":{\"ClrSysMmbId\":{\"MmbId\":\"HLBBVNVN\"}}},\"Dbtr\":{\"Nm\":\"Nguyen Ngoc Loan\",\"PstlAdr\":{\"AdrLine\":[\"Nguyen Ngoc Loan\"]}},\"DbtrAcct\":{\"Id\":{\"Othr\":{\"Id\":\"19029747378011\"}},\"Tp\":{\"Prtry\":\"ACC\"}},\"DbtrAgt\":{\"FinInstnId\":{\"ClrSysMmbId\":{\"MmbId\":\"VTCBVNVN\"}}},\"CdtrAgt\":{\"FinInstnId\":{\"ClrSysMmbId\":{\"MmbId\":\"HLBBVNVN\"}}},\"Cdtr\":{\"Nm\":\"Ha Nam Ninh\",\"PstlAdr\":{\"AdrLine\":[\"Ha Nam Ninh\"]}},\"CdtrAcct\":{\"Id\":{\"Othr\":{\"Id\":\"0129837294\"}},\"Tp\":{\"Prtry\":\"ACC\"}},\"InstrForNxtAgt\":[{\"InstrInf\":\"/TAM/000000010000/TDT/0703103832/SCR/00000001/MCC/9999/AIC/704/PEM/000/PCD/00/FID/970407/MID/800000000000000\"},{\"InstrInf\":\"/MNM/Techcombank                          VNM/SCC/704/BID/970442/FAI/19029747378011/TAI/0129837294\"},{\"InstrInf\":\"/CTR/Chuyen tien lien ngan hang\"},{\"InstrInf\":\"/MAC/EAA4BC0000004C12\"}]}]}}}}";
//		JsonElement je = new Gson().fromJson(mess, JsonElement.class);
//		// .Payload.Document.FIToFICstmrCdtTrf.CdtTrfTxInf[0].PmtId.TxId
//		JsonElement jparent = getParentElement(je, ".Payload.Document.FIToFICstmrCdtTrf.CdtTrfTxInf[0]");
//		JsonElement x = jparent.getAsJsonObject().get("CdtTrfTxInf").getAsJsonArray().get(0);
//		System.out.println(x.toString());

		System.out.println(getNodeValue(mess, ".Payload.Document.FIToFICstmrCdtTrf.CdtTrfTxInf[0].PmtId.TxId"));

	}

//	@Test
	public void test() {
		// .CdtTrfTxInf.InstrForNxtAgt[0].InstrInf
		// .Payload.Document.FIToFICstmrCdtTrf.CdtTrfTxInf[0].InstrForNxtAgt[0]
		String filePath = System.getProperty("user.dir") + "/data/json/json.json";
		// doc file
		JsonElement je = this.jsonElementFromFile(filePath);
		/**
		 * a new Araray
		 */
		JsonObject newArrayToAdd = new JsonObject();
		newArrayToAdd.addProperty("type1", "type 1");
		newArrayToAdd.addProperty("type2", "type 2");

		/**
		 * to root
		 */
		this.addElement(je, "root1", "Root 1 string");
		this.addElement(je, "rootNum", 66);
		this.addElement(je, "rootBoolean", Boolean.TRUE);
		this.removeElement(je, "lastName");
		this.addElement(je, "rootArray", newArrayToAdd);

		System.out.println(je.toString());

		/**
		 * to an element
		 */
		this.addElement(je, "addProp1", "addVal1", "address");
		this.addElement(je, "rootNum", 77, "address");
		this.addElement(je, "rootBoolean", Boolean.FALSE, "address");
		this.addElement(je, "addressArray", newArrayToAdd, "address");
		this.removeElement(je, "state", "address");

		/**
		 * To array
		 */

		JsonArray phone = je.getAsJsonObject().get("phoneNumbers").getAsJsonArray();
		this.addToArray(phone, "Hello");
		this.addToArray(phone, 888);
		this.addToArray(phone, Boolean.FALSE);
		this.addToArray(phone, newArrayToAdd);
		this.setToArray(phone, 0, newArrayToAdd);
		this.removeArray(je, 3, "phoneNumbers");

		// KQ cuoi
		System.out.println(je.toString());

	}
}

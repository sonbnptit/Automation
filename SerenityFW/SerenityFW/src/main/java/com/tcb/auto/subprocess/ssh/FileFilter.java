package com.tcb.auto.subprocess.ssh;

import org.apache.sshd.client.subsystem.sftp.SftpClient.DirEntry;

import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FileFilter {
	public static final String NAME = "name";
	public static final String DATE = "date";
	
	public static final String START_DATE_FIELD = "start_date";
	public static final String END_DATE_FIELD = "end_date";
	
	private Map<String, Object> mapFilter = new LinkedHashMap<String, Object>();
	
	public FileFilter(){
		
	}
	
	public FileFilter setNameCondition(String nameRegex){
		mapFilter.put(NAME, nameRegex);
		return this;
	}
	
	public FileFilter setDateCondition(Date startDate, Date endDate){
		Map<String, Date> mapDate = new HashMap<String, Date>();
		mapDate.put(START_DATE_FIELD, startDate);
		mapDate.put(END_DATE_FIELD, endDate);
		mapFilter.put(DATE, mapDate);
		return this;
	}
	
	private boolean checkName(DirEntry dirEntry){
		if(!mapFilter.containsKey(NAME)) return true;
		String nameRegex = (String) mapFilter.get(NAME);
		return dirEntry.getFilename().matches(nameRegex);
	}
	
	private boolean checkDate(DirEntry dirEntry){
		if(!mapFilter.containsKey(DATE)) return true;
		Map<String, Date> mapDate = (Map<String, Date>) mapFilter.get(DATE);
		Date startDate = mapDate.get(START_DATE_FIELD);
		Date endDate = mapDate.get(END_DATE_FIELD);
		FileTime mTime = dirEntry.getAttributes().getModifyTime();
		Date mDate = new Date(mTime.toMillis());
		return mDate.compareTo(startDate) >= 0 && mDate.compareTo(endDate) <=0;
	}
	
	public boolean checkFilter(DirEntry dirEntry){
		boolean check = true;
		for (Entry<String, Object> entry : mapFilter.entrySet()) {
			switch (entry.getKey()) {
			case NAME:
				check = check && checkName(dirEntry);
				break;
			case DATE:
				check = check && checkDate(dirEntry);
				break;
			}
			if(!check) break;
		}
		return check;
	}
}

package com.tcb.auto.subprocess.ssh;

import org.apache.sshd.client.subsystem.sftp.SftpClient.DirEntry;

import java.util.Comparator;

public class CustomSort implements Comparator<DirEntry>{

	public enum SortBy{
		NAME,
		NAME_DESC,
		DATE,
		DATE_DESC
	}
	
	private SortBy by;
	
	public CustomSort(SortBy by){
		this.by = by;
	}
	
	@Override
	public int compare(DirEntry i1, DirEntry i2) {
		if(SortBy.DATE == by) return compareDate(i1, i2);
		if(SortBy.DATE_DESC == by) return compareDateDESC(i1, i2);
		if(SortBy.NAME == by) return compareName(i1, i2);
		if(SortBy.NAME_DESC == by) return compareNameDESC(i1, i2);
		//Default
		return compareDate(i1, i2);
	}
	
	private int compareName(DirEntry i1, DirEntry i2){
		return i1.getFilename().compareTo(i2.getFilename());
	}

	private int compareNameDESC(DirEntry i1, DirEntry i2){
		return 0 - i1.getFilename().compareTo(i2.getFilename());
	}
	
	private int compareDate(DirEntry i1, DirEntry i2){
		return i1.getAttributes().getModifyTime().compareTo(i2.getAttributes().getModifyTime());
	}

	private int compareDateDESC(DirEntry i1, DirEntry i2){
		return 0 - i1.getAttributes().getModifyTime().compareTo(i2.getAttributes().getModifyTime());
	}

}

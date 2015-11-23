package com.cardpay.pccredit.tools;

import java.util.Date;

/**
 * @author chenzhifang
 *
 * 2014-12-12下午3:40:05
 */
public class BankDataFileProcess {
	private String id;
	
	private String fileName;
	
	private String status;
	
	private Date processTime;
	
	private String type;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

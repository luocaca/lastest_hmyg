package com.hldj.hmyg.bean;

public class Message {

	String id;
	String message;
	String contentTypeName;
	String createDate;
	private boolean isChecked;
	private boolean isRead;
	String sourceId;
	String contentType;
	String modelType;
	String targetUserType;

	public String getTargetUserType() {
		return targetUserType;
	}

	public void setTargetUserType(String targetUserType) {
		this.targetUserType = targetUserType;
	}

	public Message(String id, String message, String contentTypeName,
			String createDate, boolean isChecked, boolean isRead,
			String sourceId, String contentType, String modelType, String targetUserType) {
		super();
		this.id = id;
		this.message = message;
		this.contentTypeName = contentTypeName;
		this.createDate = createDate;
		this.isChecked = isChecked;
		this.isRead = isRead;
		this.sourceId = sourceId;
		this.contentType = contentType;
		this.modelType = modelType;
		this.targetUserType =targetUserType;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getContentTypeName() {
		return contentTypeName;
	}

	public void setContentTypeName(String contentTypeName) {
		this.contentTypeName = contentTypeName;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

}

package com.linkin.mtv.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-24 下午2:42:16
 */
@DatabaseTable(tableName = "tb_noti_message")
public class NotiMessage {

	public static final String MESSAGE_ID = "messageId";
	@DatabaseField(generatedId = true)
	private int _id;
	@DatabaseField
	private String msgId;
	@DatabaseField
	private String content;
	@DatabaseField
	private String insertTime;
	@DatabaseField
	private String lastUpdateTime;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}

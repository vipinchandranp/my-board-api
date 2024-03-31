package com.myboard.userservice.controller.response;

import java.util.List;
import java.util.Map;

public class BoardMainResponse<T> {
	private T data;
	private Map<MessageType, List<String>> messages;

	public BoardMainResponse() {
	}

	public BoardMainResponse(T data, Map<MessageType, List<String>> messages) {
		this.data = data;
		this.messages = messages;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Map<MessageType, List<String>> getMessages() {
		return messages;
	}

	public void setMessages(Map<MessageType, List<String>> messages) {
		this.messages = messages;
	}
}

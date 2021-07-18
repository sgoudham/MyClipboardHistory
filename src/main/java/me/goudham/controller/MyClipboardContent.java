package me.goudham.controller;

public class MyClipboardContent<T> {
	private T content;

	public MyClipboardContent(T content) {
		this.content = content;
	}

	public void setContent(Object content) {
		this.content = (T) content;
	}

	public T getContent() {
		return content;
	}
}

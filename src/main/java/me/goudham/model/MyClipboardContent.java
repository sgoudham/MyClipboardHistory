package me.goudham.model;

import java.awt.Image;

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

	public Image getImage() {
		return (Image) content;
	}
}

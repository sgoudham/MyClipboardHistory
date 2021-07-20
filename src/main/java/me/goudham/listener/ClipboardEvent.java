package me.goudham.listener;

import java.awt.Image;

public interface ClipboardEvent {
	void onCopyString(String stringContent);
	void onCopyImage(Image imageContent);
}

package me.goudham.listener;

import java.awt.image.BufferedImage;

public interface ClipboardEvent {
	void onCopyString(String stringContent);
	void onCopyImage(BufferedImage imageContent);
}

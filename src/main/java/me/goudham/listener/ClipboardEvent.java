package me.goudham.listener;

import me.goudham.model.MyClipboardContent;

public interface ClipboardEvent {
	<T> void onCopy(MyClipboardContent<T> copiedContent);
}

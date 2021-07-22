package me.goudham.listener;

import java.util.ArrayList;
import java.util.List;

public abstract class ClipboardListener {
    List<ClipboardEvent> eventsListener = new ArrayList<>();

    public void addEventListener(ClipboardEvent clipboardEvent) {
        if (!eventsListener.contains(clipboardEvent)) {
            eventsListener.add(clipboardEvent);
        }
    }

    public void removeEventListener(ClipboardEvent clipboardEvent) {
        eventsListener.remove(clipboardEvent);
    }

    public abstract void execute();
}

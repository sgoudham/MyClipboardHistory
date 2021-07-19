package me.goudham;

import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.IOException;

public class ClipboardListener extends Thread implements ClipboardOwner {

    public interface EntryListener {
        void onCopy(String data);
    }

    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private EntryListener entryListener;

    @Override
    public void lostOwnership(Clipboard c, Transferable t) {
        try {
            sleep(200);
        } catch (InterruptedException ignored) { }

        Transferable contents = c.getContents(currentThread());
        processContents(contents);
        regainOwnership(c, contents);
    }

    public void processContents(Transferable t) {
        try {
            String what = (String) (t.getTransferData(DataFlavor.stringFlavor));

            if (entryListener != null) {
                entryListener.onCopy(what);
            }
        } catch (UnsupportedFlavorException | IOException ignored) { }
    }

    public void regainOwnership(Clipboard c, Transferable t) {
        c.setContents(t, this);
    }

    public void run() {
        Transferable transferable = clipboard.getContents(this);
        regainOwnership(clipboard, transferable);
    }

    public void setEntryListener(EntryListener entryListener) {
        this.entryListener = entryListener;
    }
}
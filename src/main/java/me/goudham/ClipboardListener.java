package me.goudham;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class ClipboardListener extends Thread implements ClipboardOwner {

    public interface EntryListener {
        void onCopy(String data);
    }

    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private EntryListener entryListener;

    @Override
    public void lostOwnership(Clipboard c, Transferable t) {
        try {
            System.out.println("Sleeping for 200 milliseconds");
            sleep(200);
        } catch (Exception ignored) { }

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
        } catch (Exception ignored) { }
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
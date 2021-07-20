package me.goudham;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import me.goudham.listener.ClipboardEvent;
import org.jetbrains.annotations.NotNull;

public class ClipboardListener extends Thread implements ClipboardOwner {
    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private @NotNull ClipboardEvent clipboardEvent;

    public ClipboardListener(@NotNull ClipboardEvent clipboardEvent) {
        this.clipboardEvent = clipboardEvent;
    }

    @Override
    public void lostOwnership(Clipboard oldClipboard, Transferable oldClipboardContents) {
        try {
            sleep(200);
        } catch (InterruptedException ignored) { }

        Transferable newClipboardContents = oldClipboard.getContents(currentThread());
        processContents(oldClipboard, newClipboardContents);
        regainOwnership(oldClipboard, newClipboardContents);
    }

    public void processContents(Clipboard oldClipboard, Transferable newClipboardContents) {
        try {
            if (oldClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                String stringContent = (String) newClipboardContents.getTransferData(DataFlavor.stringFlavor);
                clipboardEvent.onCopyString(stringContent);
            } else if (oldClipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
                Image imageContent = (Image) newClipboardContents.getTransferData(DataFlavor.imageFlavor);
                clipboardEvent.onCopyImage(imageContent);
            }
        } catch (UnsupportedFlavorException | IOException ignored) { }
    }

    public void regainOwnership(Clipboard clipboard, Transferable newClipboardContents) {
        try {
            clipboard.setContents(newClipboardContents, this);
        } catch (IllegalStateException ise) {
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            regainOwnership(clipboard, newClipboardContents);
        }
    }

    public void run() {
        Transferable currentClipboardContents = clipboard.getContents(currentThread());
        processContents(clipboard, currentClipboardContents);
        regainOwnership(clipboard, currentClipboardContents);
    }

    public void setClipboardEvent(@NotNull ClipboardEvent clipboardEvent) {
        this.clipboardEvent = clipboardEvent;
    }
}
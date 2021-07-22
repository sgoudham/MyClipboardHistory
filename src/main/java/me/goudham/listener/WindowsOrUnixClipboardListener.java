package me.goudham.listener;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import me.goudham.util.ClipboardUtils;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class WindowsOrUnixClipboardListener extends ClipboardListener implements Runnable, ClipboardOwner {
    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public WindowsOrUnixClipboardListener() { }

    @Override
    public void lostOwnership(Clipboard oldClipboard, Transferable oldClipboardContents) {
        try {
            sleep(200);
        } catch (InterruptedException ignored) {
        }

        Transferable newClipboardContents = oldClipboard.getContents(currentThread());
        processContents(oldClipboard, newClipboardContents);
        regainOwnership(oldClipboard, newClipboardContents);
    }

    public void processContents(Clipboard oldClipboard, Transferable newClipboardContents) {
        try {
            if (oldClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                String stringContent = (String) newClipboardContents.getTransferData(DataFlavor.stringFlavor);
                for (ClipboardEvent clipboardEvent : eventsListener) {
                    clipboardEvent.onCopyString(stringContent);
                }
            } else if (oldClipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
                BufferedImage imageContent = ClipboardUtils.convertToBufferedImage((Image) newClipboardContents.getTransferData(DataFlavor.imageFlavor));
                for (ClipboardEvent clipboardEvent : eventsListener) {
                    clipboardEvent.onCopyImage(imageContent);
                }
            }
        } catch (UnsupportedFlavorException | IOException ignored) {
        }
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

    @Override
    public void run() {
        Transferable currentClipboardContents = clipboard.getContents(null);
        processContents(clipboard, currentClipboardContents);
        regainOwnership(clipboard, currentClipboardContents);
    }

    @Override
    public void execute() {
        run();
    }
}
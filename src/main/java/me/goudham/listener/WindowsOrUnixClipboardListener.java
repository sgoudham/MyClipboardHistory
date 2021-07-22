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
import org.jetbrains.annotations.NotNull;

public class WindowsOrUnixClipboardListener extends Thread implements ClipboardListener, ClipboardOwner {
    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private final @NotNull ClipboardEvent clipboardEvent;

    public WindowsOrUnixClipboardListener(@NotNull ClipboardEvent clipboardEvent) {
        this.clipboardEvent = clipboardEvent;
    }

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
                clipboardEvent.onCopyString(stringContent);
            } else if (oldClipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
                BufferedImage imageContent = ClipboardUtils.convertToBufferedImage((Image) newClipboardContents.getTransferData(DataFlavor.imageFlavor));
                clipboardEvent.onCopyImage(imageContent);
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
    public void execute() {
        Transferable currentClipboardContents = clipboard.getContents(null);
        processContents(clipboard, currentClipboardContents);
        regainOwnership(clipboard, currentClipboardContents);
    }
}
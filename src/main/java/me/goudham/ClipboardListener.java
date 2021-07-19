package me.goudham;

import me.goudham.listener.ClipboardEvent;
import me.goudham.model.MyClipboardContent;
import org.jetbrains.annotations.NotNull;

import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        } catch (InterruptedException ignored) {
        }

        Transferable newClipboardContents = oldClipboard.getContents(currentThread());
        processContents(oldClipboard, newClipboardContents);
        regainOwnership(oldClipboard, newClipboardContents);
    }

    public void processContents(Clipboard oldClipboard, Transferable newClipboardContents) {
        MyClipboardContent<?> clipboardContent = null;

        try {
            if (oldClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                String stringContent = (String) newClipboardContents.getTransferData(DataFlavor.stringFlavor);
                clipboardContent = new MyClipboardContent<>(stringContent);
            } else if (oldClipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
                Image imageContent = (Image) newClipboardContents.getTransferData(DataFlavor.imageFlavor);
                clipboardContent = new MyClipboardContent<>(imageContent);
            }
        } catch (UnsupportedFlavorException | IOException ignored) {
        }

        clipboardEvent.onCopy(clipboardContent);
    }

    public void regainOwnership(Clipboard clipboard, Transferable newClipboardContents) {
        clipboard.setContents(newClipboardContents, this);
    }

    public void run() {
        Transferable currentClipboardContents = clipboard.getContents(null);

        final MyClipboardContent<String>[] recentContent = new MyClipboardContent[]{new MyClipboardContent<>("")};
        try {
            recentContent[0].setContent(currentClipboardContents.getTransferData(DataFlavor.stringFlavor));
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                Transferable newClipboard = clipboard.getContents(null);
                String data = (String) newClipboard.getTransferData(DataFlavor.stringFlavor);
                if (!data.equals(recentContent[0].getContent())) {
                    clipboardEvent.onCopy(new MyClipboardContent<>(data));
                    recentContent[0].setContent(data);
                }
            } catch (HeadlessException | IOException | UnsupportedFlavorException e) {
                e.printStackTrace();
            }
        }, 0, 200, TimeUnit.MILLISECONDS);
    }

    public void setClipboardEvent(@NotNull ClipboardEvent clipboardEvent) {
        this.clipboardEvent = clipboardEvent;
    }
}
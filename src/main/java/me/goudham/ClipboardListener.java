package me.goudham;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import me.goudham.listener.ClipboardEvent;
import me.goudham.model.MyClipboardContent;
import me.goudham.util.ClipboardUtils;
import org.jetbrains.annotations.NotNull;

import static me.goudham.util.Contents.IMAGE;
import static me.goudham.util.Contents.STRING;

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
                BufferedImage imageContent = ClipboardUtils.convertToBufferedImage((Image) newClipboardContents.getTransferData(DataFlavor.imageFlavor));
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
        Transferable oldClipboardContents = clipboard.getContents(null);
        final MyClipboardContent<?>[] myOldClipboardContentsArray = new MyClipboardContent[]{ ClipboardUtils.getClipboardContents(oldClipboardContents, clipboard) };

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            Transferable newClipboardContents = clipboard.getContents(null);
            MyClipboardContent<?> myNewClipboardContents = new MyClipboardContent<>("");

            try {
                if (STRING.isAvailable(clipboard)) {
                    myNewClipboardContents.setContent(newClipboardContents.getTransferData(STRING.getDataFlavor()));
                    if (!myNewClipboardContents.getContent().equals(myOldClipboardContentsArray[0].getContent())) {
                        clipboardEvent.onCopyString((String) myNewClipboardContents.getContent());
                        myOldClipboardContentsArray[0].setContent(myNewClipboardContents.getContent());
                    }
                } else if (IMAGE.isAvailable(clipboard)) {
                    BufferedImage bufferedImage = ClipboardUtils.convertToBufferedImage((Image) newClipboardContents.getTransferData(IMAGE.getDataFlavor()));
                    myNewClipboardContents.setContent(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
                    if (!myNewClipboardContents.getContent().equals(myOldClipboardContentsArray[0].getContent())) {
                        clipboardEvent.onCopyImage(bufferedImage);
                        myOldClipboardContentsArray[0].setContent(myNewClipboardContents.getContent());
                    }
                }
            } catch (UnsupportedFlavorException | IOException exp) {
                exp.printStackTrace();
            }
        }, 0, 350, TimeUnit.MILLISECONDS);
    }

    public void setClipboardEvent(@NotNull ClipboardEvent clipboardEvent) {
        this.clipboardEvent = clipboardEvent;
    }
}
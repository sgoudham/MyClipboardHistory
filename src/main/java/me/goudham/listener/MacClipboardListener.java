package me.goudham.listener;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import me.goudham.model.MyClipboardContent;
import me.goudham.util.ClipboardUtils;

import static me.goudham.model.Contents.IMAGE;
import static me.goudham.model.Contents.STRING;

public class MacClipboardListener extends ClipboardListener {
    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public MacClipboardListener() { }

    @Override
    public void execute() {
        Transferable oldClipboardContents = clipboard.getContents(null);
        final MyClipboardContent<?, ?>[] myClipboardContents = new MyClipboardContent[]{ ClipboardUtils.getClipboardContents(oldClipboardContents, clipboard) };

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            Transferable newClipboardContents = clipboard.getContents(null);

            try {
                if (STRING.isAvailable(clipboard)) {
                    myClipboardContents[0].setNewContent(newClipboardContents.getTransferData(STRING.getDataFlavor()));
                    if (!myClipboardContents[0].getNewContent().equals(myClipboardContents[0].getOldContent())) {
                        for (ClipboardEvent clipboardEvent : eventsListener) {
                            clipboardEvent.onCopyString((String) myClipboardContents[0].getNewContent());
                        }
                        myClipboardContents[0].setOldContent(myClipboardContents[0].getNewContent());
                    }
                } else if (IMAGE.isAvailable(clipboard)) {
                    BufferedImage bufferedImage = ClipboardUtils.convertToBufferedImage((Image) newClipboardContents.getTransferData(IMAGE.getDataFlavor()));
                    myClipboardContents[0].setNewContent(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
                    if (!myClipboardContents[0].getNewContent().equals(myClipboardContents[0].getOldContent())) {
                        for (ClipboardEvent clipboardEvent : eventsListener) {
                            clipboardEvent.onCopyImage(bufferedImage);
                        }
                        myClipboardContents[0].setOldContent(myClipboardContents[0].getNewContent());
                    }
                }
            } catch (UnsupportedFlavorException | IOException exp) {
                exp.printStackTrace();
            }
        }, 0, 350, TimeUnit.MILLISECONDS);
    }
}

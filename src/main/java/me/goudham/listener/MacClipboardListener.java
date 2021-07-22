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
import org.jetbrains.annotations.NotNull;

import static me.goudham.model.Contents.IMAGE;
import static me.goudham.model.Contents.STRING;

public class MacClipboardListener implements ClipboardListener {
    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private final @NotNull ClipboardEvent clipboardEvent;

    public MacClipboardListener(@NotNull ClipboardEvent clipboardEvent) {
        this.clipboardEvent = clipboardEvent;
    }

    @Override
    public void execute() {
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
}

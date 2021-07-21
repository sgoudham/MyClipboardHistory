package me.goudham.util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import me.goudham.model.MyClipboardContent;

import static me.goudham.util.Contents.IMAGE;
import static me.goudham.util.Contents.STRING;

public class ClipboardUtils {

    public static MyClipboardContent<?> getClipboardContents(Transferable contents, Clipboard clipboard) {
        MyClipboardContent<?> myClipboardContent = new MyClipboardContent<>("");

        try {
            if (STRING.isAvailable(clipboard)) {
                myClipboardContent.setContent(contents.getTransferData(STRING.getDataFlavor()));
            } else if (IMAGE.isAvailable(clipboard)) {
                BufferedImage bufferedImage = convertToBufferedImage((Image) contents.getTransferData(IMAGE.getDataFlavor()));
                myClipboardContent.setContent(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
            }
        } catch (UnsupportedFlavorException | IOException exp) {
            exp.printStackTrace();
        }

        return myClipboardContent;
    }

    public static BufferedImage convertToBufferedImage(Image image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = newImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        return newImage;
    }
}

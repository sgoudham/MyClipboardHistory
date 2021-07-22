package me.goudham.view;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import me.goudham.listener.ClipboardEvent;
import me.goudham.listener.MacClipboardListener;
import me.goudham.listener.WindowsOrUnixClipboardListener;
import org.apache.commons.lang3.SystemUtils;

public class ClipboardView implements ClipboardEvent {
    private JPanel clipboard;
    private JButton copySelectedTextButton;
    private JList<String> clipboardContentList;
    private final DefaultListModel<String> listModel;
    private JLabel title;
    private JScrollPane scrollPane;
    private JPanel buttonPane;
    private JButton clearAllHistoryButton;
    private JButton removeButton;
    private JLabel imageIconLabel;
    private JLabel textClipboardLabel;
    private JButton copyImageBelowButton;
    private JButton removeImageBelowButton;
    private JPanel imageButtonPanel;
    private JButton toggleImageButton;
    private JScrollPane anotherImagePanel;

    private boolean toggle = true;
    private BufferedImage storedImageContent;

    public ClipboardView() {
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        anotherImagePanel.setBorder(BorderFactory.createEmptyBorder());
        buttonPane.setBorder(BorderFactory.createEmptyBorder());
        listModel = new DefaultListModel<>();
        clipboardContentList.setModel(listModel);

        toggleImageButton.addActionListener(actionEvent -> {
            if (toggle) {
                imageIconLabel.setIcon(null);
//                anotherImagePanel.setPreferredSize(new Dimension(0, 0));
//                anotherImagePanel.setVisible(false);
//                imageIconLabel.setMaximumSize(new Dimension(0, 0));
//                anotherImagePanel.setPreferredSize(new Dimension(0, 0));
                toggle = false;
            } else {
//                anotherImagePanel.setPreferredSize(new Dimension(300, 300));
                imageIconLabel.setIcon(new ImageIcon(storedImageContent));
//                anotherImagePanel.setVisible(true);
                toggle = true;
            }
        });

//        final java.awt.datatransfer.clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//        final MyClipboardContent<?> previousContent = new MyClipboardContent<>("");
//        final int[] count = {0};
//
//        systemClipboard.addFlavorListener(e -> {
//            MyClipboardContent<?> clipboardContent = null;
//            try {
//                if (systemClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
//                    String stringContent = (String) systemClipboard.getData(DataFlavor.stringFlavor);
//                    clipboardContent = new MyClipboardContent<>(stringContent);
//                } else if (systemClipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
//                    Image imageContent = (Image) systemClipboard.getData(DataFlavor.imageFlavor);
//                    clipboardContent = new MyClipboardContent<>(imageContent);
//                }
//            } catch (UnsupportedFlavorException | IOException unsupportedFlavorException) {
//                unsupportedFlavorException.printStackTrace();
//            }
//
//            if (previousContent.getContent().equals(clipboardContent.getContent())) {
//                count[0]++;
//            }
//
//            if (!previousContent.getContent().equals(clipboardContent.getContent()) || count[0] > 1) {
//                systemClipboard.setContents(new TransferableImage((Image) clipboardContent.getContent()), null);
////					systemClipboard.setContents(new StringSelection((String) clipboardContent.getContent()), null);
//                System.out.println("The clipboard contains: " + clipboardContent.getContent());
//
//                DefaultListModel<String> demoList = new DefaultListModel<>();
//                int size = clipboardContentList.getModel().getSize();
//                demoList.addElement("0. " + clipboardContent.getContent());
//                for (int i = 0; i < size; i++) {
//                    demoList.addElement((i + 1) + ". " + clipboardContentList.getModel().getElementAt(i));
//                }
//                clipboardContentList.setModel(demoList);
//
//                previousContent.setContent(clipboardContent.getContent());
//                count[0] = 0;
//            }
//        });
    }

    public void createAndShowGUI() {
        me.goudham.listener.ClipboardListener clipboardListener = null;
        if (isMac()) {
            clipboardListener = new MacClipboardListener(this);
        } else if (isUnix() || isWindows()) {
            clipboardListener = new WindowsOrUnixClipboardListener(this);
        }
        clipboardListener.execute();

        JFrame jFrame = new JFrame();
        jFrame.setTitle("My Clipboard History");
        jFrame.setContentPane(clipboard);
        jFrame.setVisible(true);
        jFrame.setAlwaysOnTop(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(1200, 900));
        jFrame.setResizable(true);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
    }

    @Override
    public void onCopyString(String stringContent) {
        listModel.add(0, stringContent);
    }

    @Override
    public void onCopyImage(BufferedImage imageContent) {
        storedImageContent = imageContent;
        imageIconLabel.setIcon(new ImageIcon(imageContent));
        toggle = true;
//
//        if (imageContent.getWidth() > 1000 || imageContent.getHeight() > 1000) {
//            imageIconLabel.setIcon(new ImageIcon(new ImageIcon(imageContent).getImage().getScaledInstance(1000, 600, Image.SCALE_SMOOTH)));
//        } else {
//            imageIconLabel.setIcon(new ImageIcon(imageContent));
//        }
    }

    private boolean isMac() {
        return SystemUtils.IS_OS_MAC;
    }

    private boolean isUnix() {
        return SystemUtils.IS_OS_UNIX || SystemUtils.IS_OS_LINUX;
    }

    private boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }
}

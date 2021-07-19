package me.goudham.view;

import me.goudham.ClipboardListener;
import me.goudham.listener.ClipboardEvent;
import me.goudham.model.MyClipboardContent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ClipboardView implements ClipboardEvent {
    private JPanel Clipboard;
    private JButton copyButton;
    private JList<String> clipboardContentList;
    private final DefaultListModel<String> listModel;
    private JLabel title;
    private JScrollPane scrollPane;
    private JPanel buttonPane;
    private JButton clearButton;
    private JButton removeButton;

    public ClipboardView() {
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        buttonPane.setBorder(BorderFactory.createEmptyBorder());
        listModel = new DefaultListModel<>();
        clipboardContentList.setModel(listModel);

//        final java.awt.datatransfer.Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
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
        new ClipboardListener(this).start();

        JFrame jFrame = new JFrame();
        jFrame.setTitle("My Clipboard History");
        jFrame.setContentPane(Clipboard);
        jFrame.setVisible(true);
        jFrame.setAlwaysOnTop(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(1000, 680));
        jFrame.setMaximumSize(new Dimension(1000, 680));
        jFrame.setResizable(true);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
    }

    @Override
    public <T> void onCopy(MyClipboardContent<T> copiedContent) {
        listModel.add(0, copiedContent.getContent().toString());
    }
}

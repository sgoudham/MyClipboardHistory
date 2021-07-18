package me.goudham.view;

import me.goudham.ClipboardListener;
import me.goudham.controller.MyClipboardContent;
import me.goudham.model.TransferableImage;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.IOException;

public class ClipboardView implements ClipboardListener.EntryListener {
    private JPanel Clipboard;
    private JButton copyButton;
    private JList<String> clipboardContentList;
    private final DefaultListModel<String> listModel;
    private JLabel title;
    private JScrollPane scrollPane;

    public ClipboardView() {
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
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

    public JPanel getClipboard() {
        return Clipboard;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClipboardView().createAndShowGUI());
    }

    private void createAndShowGUI() {
        ClipboardListener listener = new ClipboardListener();
        listener.setEntryListener(this);
        listener.start();

        JFrame jFrame = new JFrame();
        jFrame.setTitle("My Clipboard History");
        jFrame.setContentPane(Clipboard);
        jFrame.setVisible(true);
        jFrame.setAlwaysOnTop(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.getContentPane().setBackground(new Color(1, 3, 25));
        jFrame.setPreferredSize(new Dimension(1000, 680));
        jFrame.setMaximumSize(new Dimension(1000, 680));
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
    }

    @Override
    public void onCopy(String data) {
        listModel.add(0, data);
    }
}

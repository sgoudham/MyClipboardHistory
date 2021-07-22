package me.goudham.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
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
import javax.swing.ListSelectionModel;
import me.goudham.listener.ClipboardEvent;
import me.goudham.listener.ClipboardListener;
import me.goudham.listener.MacClipboardListener;
import me.goudham.listener.WindowsOrUnixClipboardListener;
import org.apache.commons.lang3.SystemUtils;

public class ClipboardView {
    private JPanel clipboard;
    private JButton copySelectedTextButton;
    private JList<String> clipboardContentList;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private ListSelectionModel listSelectionModel = clipboardContentList.getSelectionModel();
    private JLabel title;
    private JPanel textButtonPanel;
    private JButton clearAllHistoryButton;
    private JButton removeButton;
    private JButton copyImageBelowButton;
    private JButton removeImageBelowButton;
    private JPanel imageButtonPanel;
    private JButton toggleImageButton;
    private JLabel imageLabel;
    private JScrollPane imageScrollPane;
    private JScrollPane clipboardContentScrollPane;

    private boolean toggle = true;
    private BufferedImage storedImageContent;
    private ClipboardListener clipboardListener;

    public ClipboardView() {
        clipboardContentScrollPane.setBorder(BorderFactory.createEmptyBorder());
        imageScrollPane.getVerticalScrollBar().setUnitIncrement(35);
        imageScrollPane.getHorizontalScrollBar().setUnitIncrement(35);
        clipboardContentScrollPane.getVerticalScrollBar().setUnitIncrement(200);
        clipboardContentScrollPane.getHorizontalScrollBar().setUnitIncrement(200);
        clipboardContentList.setModel(listModel);
        listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        toggleImageButton.addActionListener(actionEvent -> {
            if (storedImageContent != null) {
                if (toggle) {
                    toggleImageButton.setText("Show Image");
                    imageLabel.setIcon(null);
                    toggle = false;
                } else {
                    imageLabel.setIcon(new ImageIcon(storedImageContent));
                    toggleImageButton.setText("Hide Image");
                    toggle = true;
                }
            }
        });

        copySelectedTextButton.addActionListener(actionEvent -> {
            String selectedValue = clipboardContentList.getSelectedValue();
            int selectedIndex = clipboardContentList.getSelectedIndex();
            listModel.remove(selectedIndex);
            Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            systemClipboard.setContents(new StringSelection(selectedValue), null);
        });
    }

    public void createAndShowGUI() {
        if (isMac()) {
            clipboardListener = new MacClipboardListener();
        } else if (isUnix() || isWindows()) {
            clipboardListener = new WindowsOrUnixClipboardListener();
        }
        clipboardListener.execute();

        clipboardListener.addEventListener(new ClipboardEvent() {
            @Override
            public void onCopyString(String stringContent) {
                listModel.add(0, stringContent);
            }

            @Override
            public void onCopyImage(BufferedImage imageContent) {
                storedImageContent = imageContent;
                imageLabel.setIcon(new ImageIcon(imageContent));
                toggleImageButton.setText("Hide Image");
                toggle = true;
            }
        });

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

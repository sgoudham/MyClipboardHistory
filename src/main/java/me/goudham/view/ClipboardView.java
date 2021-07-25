package me.goudham.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
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
    private JLabel title;
    private JPanel textButtonPanel;
    private JButton clearAllHistoryButton;
    private JButton removeButton;
    private JButton removeImageBelowButton;
    private JPanel imageButtonPanel;
    private JButton toggleImageButton;
    private JLabel imageLabel;
    private JScrollPane imageScrollPane;
    private JScrollPane clipboardContentScrollPane;
    private JButton copyImageBelowButton;

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
        ListSelectionModel listSelectionModel = clipboardContentList.getSelectionModel();
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        clipboard = new JPanel();
        clipboard.setLayout(new GridBagLayout());
        clipboard.setBackground(new Color(-16710887));
        clipboard.putClientProperty("html.disable", Boolean.FALSE);
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setAutoscrolls(true);
        splitPane1.setBackground(new Color(-263169));
        splitPane1.setContinuousLayout(true);
        splitPane1.setForeground(new Color(-263169));
        splitPane1.setOneTouchExpandable(true);
        splitPane1.setOpaque(true);
        splitPane1.setOrientation(1);
        splitPane1.setPreferredSize(new Dimension(300, 250));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        clipboard.add(splitPane1, gbc);
        splitPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        imageScrollPane = new JScrollPane();
        imageScrollPane.setAutoscrolls(false);
        imageScrollPane.setBackground(new Color(-16710887));
        imageScrollPane.setDoubleBuffered(true);
        imageScrollPane.setForeground(new Color(-16710887));
        imageScrollPane.setHorizontalScrollBarPolicy(30);
        imageScrollPane.setMaximumSize(new Dimension(300, 300));
        imageScrollPane.setMinimumSize(new Dimension(0, 0));
        imageScrollPane.setOpaque(true);
        imageScrollPane.setPreferredSize(new Dimension(300, 300));
        imageScrollPane.setVerticalScrollBarPolicy(20);
        splitPane1.setRightComponent(imageScrollPane);
        imageScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        imageLabel = new JLabel();
        imageLabel.setAutoscrolls(true);
        imageLabel.setBackground(new Color(-16710887));
        imageLabel.setHorizontalAlignment(0);
        imageLabel.setHorizontalTextPosition(0);
        imageLabel.setOpaque(true);
        imageLabel.setVerifyInputWhenFocusTarget(true);
        imageScrollPane.setViewportView(imageLabel);
        clipboardContentScrollPane = new JScrollPane();
        clipboardContentScrollPane.setDoubleBuffered(true);
        clipboardContentScrollPane.setMinimumSize(new Dimension(0, 0));
        splitPane1.setLeftComponent(clipboardContentScrollPane);
        clipboardContentList = new JList();
        clipboardContentList.setAlignmentX(0.5f);
        clipboardContentList.setAlignmentY(0.5f);
        clipboardContentList.setBackground(new Color(-16710887));
        clipboardContentList.setDragEnabled(false);
        clipboardContentList.setFixedCellHeight(-1);
        clipboardContentList.setFixedCellWidth(-1);
        clipboardContentList.setFocusable(true);
        Font clipboardContentListFont = this.$$$getFont$$$(null, Font.BOLD, 14, clipboardContentList.getFont());
        if (clipboardContentListFont != null) clipboardContentList.setFont(clipboardContentListFont);
        clipboardContentList.setForeground(new Color(-263169));
        clipboardContentList.setInheritsPopupMenu(false);
        clipboardContentList.setLayoutOrientation(0);
        clipboardContentList.setOpaque(true);
        clipboardContentList.putClientProperty("List.isFileList", Boolean.FALSE);
        clipboardContentScrollPane.setViewportView(clipboardContentList);
        imageButtonPanel = new JPanel();
        imageButtonPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, true, true));
        imageButtonPanel.setBackground(new Color(-16710887));
        imageButtonPanel.setPreferredSize(new Dimension(389, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        clipboard.add(imageButtonPanel, gbc);
        removeImageBelowButton = new JButton();
        removeImageBelowButton.setText("Remove Image Below");
        imageButtonPanel.add(removeImageBelowButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        toggleImageButton = new JButton();
        toggleImageButton.setOpaque(true);
        toggleImageButton.setText("No Image Stored");
        imageButtonPanel.add(toggleImageButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyImageBelowButton = new JButton();
        copyImageBelowButton.setText("Copy Image Below");
        imageButtonPanel.add(copyImageBelowButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textButtonPanel = new JPanel();
        textButtonPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, true, true));
        textButtonPanel.setBackground(new Color(-16710887));
        textButtonPanel.setMinimumSize(new Dimension(471, 30));
        textButtonPanel.setPreferredSize(new Dimension(389, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        clipboard.add(textButtonPanel, gbc);
        copySelectedTextButton = new JButton();
        copySelectedTextButton.setBorderPainted(true);
        copySelectedTextButton.setDoubleBuffered(true);
        copySelectedTextButton.setText("Copy Selected Text");
        copySelectedTextButton.putClientProperty("hideActionText", Boolean.FALSE);
        textButtonPanel.add(copySelectedTextButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clearAllHistoryButton = new JButton();
        clearAllHistoryButton.setBorderPainted(true);
        clearAllHistoryButton.setDoubleBuffered(true);
        clearAllHistoryButton.setText("Clear All History");
        textButtonPanel.add(clearAllHistoryButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeButton = new JButton();
        removeButton.setActionCommand("Button");
        removeButton.setText("Remove Selected Text");
        textButtonPanel.add(removeButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        title = new JLabel();
        Font titleFont = this.$$$getFont$$$("Berlin Sans FB Demi", Font.BOLD, 48, title.getFont());
        if (titleFont != null) title.setFont(titleFont);
        title.setForeground(new Color(-7401958));
        title.setText("My Clipboard History");
        title.setVerticalTextPosition(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        clipboard.add(title, gbc);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() { return clipboard; }

}

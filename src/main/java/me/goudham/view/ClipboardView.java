package me.goudham.view;

import me.goudham.controller.MyClipboardContent;
import me.goudham.model.TransferableImage;

import javax.swing.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.IOException;

public class ClipboardView {
	private JPanel Clipboard;
	private JButton copyButton;
	private JList<String> clipboardContentList;
	private JLabel title;
	private JScrollPane scrollPane;

	public ClipboardView() {
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		final java.awt.datatransfer.Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		final MyClipboardContent<?> previousContent = new MyClipboardContent<>("");
		final int[] count = { 0 };

		systemClipboard.addFlavorListener(new FlavorListener() {
			@Override
			public void flavorsChanged(FlavorEvent e) {
				 MyClipboardContent<?> clipboardContent = null;
				try {
					if (systemClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
						String stringContent = (String) systemClipboard.getData(DataFlavor.stringFlavor);
						clipboardContent = new MyClipboardContent<>(stringContent);
					} else if (systemClipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
						Image imageContent = (Image) systemClipboard.getData(DataFlavor.imageFlavor);
						clipboardContent = new MyClipboardContent<>(imageContent);
					}
				} catch (UnsupportedFlavorException | IOException unsupportedFlavorException) {
					unsupportedFlavorException.printStackTrace();
				}

				if (previousContent.getContent().equals(clipboardContent.getContent())) {
					count[0]++;
				}

				if (!previousContent.getContent().equals(clipboardContent.getContent()) || count[0] > 1) {
					systemClipboard.setContents(new TransferableImage((Image) clipboardContent.getContent()), null);
//					systemClipboard.setContents(new StringSelection((String) clipboardContent.getContent()), null);
					System.out.println("The clipboard contains: " + clipboardContent.getContent());

					DefaultListModel<String> demoList = new DefaultListModel<>();
					int size = clipboardContentList.getModel().getSize();
					demoList.addElement("0. " + clipboardContent.getContent());
					for (int i = 0; i < size; i++) {
						demoList.addElement((i + 1) + ". " + clipboardContentList.getModel().getElementAt(i));
					}
					clipboardContentList.setModel(demoList);

					previousContent.setContent(clipboardContent.getContent());
					count[0] = 0;
				}
			}
		});
	}

	public JPanel getClipboard() {
		return Clipboard;
	}
}

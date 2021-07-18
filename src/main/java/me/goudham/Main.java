package me.goudham;

import me.goudham.view.ClipboardView;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Hello world!
 *
 */
public class Main {
    public static void main( String[] args ) {
        JFrame jFrame = new JFrame();
        jFrame.setTitle("My Clipboard History");
        jFrame.setVisible(true);
        jFrame.setContentPane(new ClipboardView().getClipboard());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.getContentPane().setBackground(new Color(1, 3, 25));
        jFrame.setPreferredSize(new Dimension(1000, 680));
        jFrame.setMaximumSize(new Dimension(1000, 680));
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
    }
}

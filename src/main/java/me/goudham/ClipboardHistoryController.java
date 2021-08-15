package me.goudham;

import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;

public class ClipboardHistoryController {
    public TabPane tabPane;
    public ListView<String> textList;
    public ListView<String> fileList;
    public ImageView clipboardImage;

    private MyClipboard myClipboard;

    public MyClipboard getMyClipboard() {
        return myClipboard;
    }

    public void setMyClipboard(MyClipboard myClipboard) {
        this.myClipboard = myClipboard;
    }
}

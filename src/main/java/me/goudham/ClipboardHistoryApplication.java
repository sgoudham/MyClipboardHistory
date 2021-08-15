package me.goudham;

import dorkbox.systemTray.Checkbox;
import dorkbox.systemTray.Separator;
import dorkbox.systemTray.SystemTray;
import java.io.IOException;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import me.goudham.event.ImageEvent;
import me.goudham.event.TextEvent;
import me.goudham.exception.UnsupportedSystemException;

public class ClipboardHistoryApplication extends Application {

    private MyClipboard myClipboard;

    @Override
    public void start(Stage stage) throws IOException {
        SystemTray.DEBUG = true;
        Platform.setImplicitExit(false);

        SystemTray systemTray = SystemTray.get();
        if (systemTray == null) {
            throw new RuntimeException("Unable to load SystemTray!");
        }

        systemTray.setImage(Objects.requireNonNull(getClass().getClassLoader().getResource("clipboard-white.png")));

        systemTray.getMenu().add(new dorkbox.systemTray.MenuItem("Open MyClipboardHistory", e -> {
            Platform.runLater(stage::show);
        }));
        systemTray.getMenu().add(new Separator());

        systemTray.getMenu().add(new Checkbox("Monitor Text", e -> System.err.println("Am i checked? " + ((Checkbox) e.getSource()).getChecked())));

        systemTray.getMenu().add(new Separator());
        systemTray.getMenu().add(new dorkbox.systemTray.MenuItem("Quit", e -> {
            systemTray.shutdown();
            System.exit(0);
        })).setShortcut('Q');


        try {
            myClipboard = MyClipboard.getSystemClipboard();
            myClipboard.startListening();
        } catch (UnsupportedSystemException e) {
            e.printStackTrace();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("clipboard-history.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image(String.valueOf(getClass().getClassLoader().getResource("clipboard-black.png"))));
        stage.setTitle("MyClipboardHistory");
        stage.setHeight(529);
        stage.setWidth(765);
        stage.setResizable(false);

        stage.setScene(scene);
        ClipboardHistoryController clipboardHistoryController = fxmlLoader.getController();
        clipboardHistoryController.setMyClipboard(myClipboard);

        myClipboard.addEventListener((TextEvent) (oldContent, newContent) -> {
            Platform.runLater(() -> clipboardHistoryController.textList.getItems().add(0, newContent));
        });

        myClipboard.addEventListener((ImageEvent) (oldContent, newContent) -> {
            Platform.runLater(() -> {
                clipboardHistoryController.clipboardImage.setImage(SwingFXUtils.toFXImage(newContent, null));
            });
        });

        clipboardHistoryController.textList.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Copy", cell.itemProperty()));
            editItem.setOnAction(event -> {
                String contentToCopy = cell.getItem();
                clipboardHistoryController.textList.getItems().remove(contentToCopy);
                myClipboard.insertAndNotify(contentToCopy);
            });
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Remove", cell.itemProperty()));
            deleteItem.setOnAction(event -> clipboardHistoryController.textList.getItems().remove(cell.getItem()));
            contextMenu.getItems().addAll(editItem, deleteItem);

            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell ;
        });

        stage.setOnCloseRequest(event -> stage.hide());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
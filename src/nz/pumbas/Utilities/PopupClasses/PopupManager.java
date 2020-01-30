package nz.pumbas.Utilities.PopupClasses;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import nz.pumbas.DifferentScenes.SceneController;
import nz.pumbas.Main;

public class PopupManager {

    public static void createPopupAlert(String alert) {
        Popup popup = new Popup();
        Label label = new Label(alert);
        label.setFont(new Font(16));
        HBox hBox = new HBox(label);
        hBox.setPadding(new Insets(10));
        hBox.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        hBox.setBackground(new Background(new BackgroundFill(
                Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        popup.getContent().add(hBox);
        popup.setAutoHide(true);
        popup.setX(Main.primaryStage.getX() + (Main.primaryStage.getWidth() / 4));
        popup.setY(Main.primaryStage.getY() + (Main.primaryStage.getHeight() / 3));
        popup.show(Main.primaryStage);
    }
}

package nz.pumbas.Utilities.PopupClasses;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import nz.pumbas.Main;

public class PopupManager {

    public static void createPopupAlert(String alert) {
        Popup popup = new Popup();
        Label label = new Label(alert);
        label.setPadding(new Insets(10));
        HBox hBox = new HBox(label);
        hBox.setPadding(new Insets(20));
        hBox.setBackground(new Background(new BackgroundFill(
                Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY
        )));

        popup.getContent().add(hBox);
        popup.setAutoHide(true);
        popup.show(Main.primaryStage);
    }
}

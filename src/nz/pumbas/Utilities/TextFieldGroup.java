package nz.pumbas.Utilities;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class TextFieldGroup {

    private HBox hBox;
    private TextField textField;

    public TextFieldGroup(String text, double defaultValue) {
        hBox = new HBox();
        hBox.setSpacing(5d);

        Label label = new Label(text);
        label.setStyle("-fx-font-size: 15");

        textField = new TextField(String.valueOf(defaultValue));

        hBox.getChildren().addAll(label, textField);
        hBox.setPadding(new Insets(5));

    }

    public HBox gethBox() {
        return hBox;
    }

    public double getValue() {
        return Double.parseDouble(textField.getText());
    }
}

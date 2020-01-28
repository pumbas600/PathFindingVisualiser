package nz.pumbas.Utilities;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Popup;
import nz.pumbas.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IOManager {
    public static void saveImageToFile(BufferedImage image) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        File selectedDirectory = directoryChooser.showDialog(Main.primaryStage);
        if (selectedDirectory != null) {

            Popup popup = new Popup();
            TextField textField = new TextField();
            Button submit = new Button("Submit");
            submit.setOnAction(actionEvent -> {
                if (textField.getText() != null && !textField.getText().equals("")) {
                    File file = new File(selectedDirectory.getAbsolutePath()
                            + System.getProperty("file.separator") + textField.getText() + ".png");
                    if(!file.exists()) {
                        saveImageAs(image, file);
                        popup.hide();
                    }
                    else {
                        textField.setBorder(new Border(new BorderStroke(javafx.scene.paint.Color.RED, BorderStrokeStyle.SOLID,
                                CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                    }
                }
            });
            HBox hBox = new HBox(new Label("Image Name:"), textField, submit);
            hBox.setSpacing(10d);
            hBox.setPadding(new Insets(20));
            hBox.setBorder(new Border(new BorderStroke(javafx.scene.paint.Color.BLACK, BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            hBox.setBackground(new Background(new BackgroundFill(
                    javafx.scene.paint.Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY
            )));
            popup.getContent().add(hBox);
            popup.setAutoHide(true);
            popup.show(Main.primaryStage);

        }
    }

    private static void saveImageAs(BufferedImage image, File file) {
        try {
            ImageIO.write(image, "png", file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveNodeGridAsImage(Node[][] nodeGrid) {
        BufferedImage bufferedImage = new BufferedImage(GlobalConstants.WIDTH, GlobalConstants.HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < GlobalConstants.WIDTH; x++) {
            for (int y = 0; y < GlobalConstants.HEIGHT; y++) {
                if (nodeGrid[x][y].getTag() != Tag.BARRIER) bufferedImage.setRGB(x, y, Color.WHITE.getRGB());
            }
        }
        saveImageToFile(bufferedImage);
    }
}

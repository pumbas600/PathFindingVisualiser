package nz.pumbas;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import nz.pumbas.Utilities.TextFieldGroup;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends Application {
    public static Scene menuScene;
    private static Stage primaryStage;

    private BufferedImage image;
    private PathFinderScene pathFinderScene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;

        initialiseMenuScene();

        primaryStage.setResizable(false);

        primaryStage.setTitle("Path Finding");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setMenuScene() {
        primaryStage.setScene(menuScene);
    }

    private void initialiseMenuScene() {
        Rectangle2D screenSize = Screen.getPrimary().getBounds();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        TextFieldGroup tileSize = new TextFieldGroup("Input a tile size (in pixels)", 35);
        TextFieldGroup widthSize = new TextFieldGroup("Input a width (in tiles)", 30);
        TextFieldGroup heightSize = new TextFieldGroup("Input a height (in tiles)", 20);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        Button openResource = new Button("Open Resource Image");
        openResource.setOnMouseClicked(event -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                openResourceFile(file, width, height);
            }
        });
        Button start = new Button("Start");
        start.setOnMouseClicked(event -> {
            changeToPathFindingScene(widthSize.getValue(), heightSize.getValue(), tileSize.getValue());
        });


        VBox vBox = new VBox(tileSize.gethBox(), widthSize.gethBox(), heightSize.gethBox());

        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(openResource, start);
        vBox.setPadding(new Insets(5));
        vBox.setSpacing(10d);
        menuScene = new Scene(vBox);
    }

    private void openResourceFile(File file, double width, double height) {
        try {
            image = ImageIO.read(file);
            if (image.getHeight() > (height - 120) / 10f || image.getWidth() > width / 10f) {
                image = null;
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void changeToPathFindingScene(double width, double height, double tileSize) {
        if (image != null) {
            Rectangle2D screenSize = Screen.getPrimary().getBounds();
            int tempTileSize = (int) Math.min((screenSize.getHeight() - 120) / image.getHeight(), screenSize.getWidth() / image.getWidth());

            pathFinderScene = new PathFinderScene(menuScene, image.getWidth(),  image.getHeight(), tempTileSize);
            pathFinderScene.initialiseImageGrid(image);
        }
        else {
            pathFinderScene = new PathFinderScene(menuScene, (int) width, (int) height, (int) tileSize);
            pathFinderScene.initialisePathFinderScene();
        }
        primaryStage.setScene(pathFinderScene.getPathFinderScene());
    }

}

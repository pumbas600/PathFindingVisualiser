package nz.pumbas.DifferentScenes;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import nz.pumbas.Main;
import nz.pumbas.Utilities.GlobalConstants;
import nz.pumbas.Utilities.TextFieldGroup;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuScene implements CustomScene {
    private Scene scene;
    private BufferedImage image;
    private ImageView imageView;
    private boolean generateRandomMaze;

    public MenuScene() {
        TextFieldGroup tileSize = new TextFieldGroup("Tile size (In pixels)", GlobalConstants.TILE_SIZE);
        TextFieldGroup widthSize = new TextFieldGroup("Width (In tiles)", GlobalConstants.WIDTH);
        TextFieldGroup heightSize = new TextFieldGroup("Height (In tiles)", GlobalConstants.HEIGHT);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        Button openResource = new Button("Open Resource Image");
        openResource.setOnMouseClicked(event -> {
            File file = fileChooser.showOpenDialog(Main.primaryStage);
            if (file != null) {
                openResourceFile(file);
            }
        });
        Button start = new Button("Start");
        start.setOnAction(actionEvent -> {
            changeToPathFindingScene(widthSize.getValue(), heightSize.getValue(), tileSize.getValue());
        });

        HBox resourceBox = new HBox();
        resourceBox.setSpacing(10);
        resourceBox.setAlignment(Pos.CENTER);
        imageView = new ImageView();
        resourceBox.getChildren().addAll(openResource, imageView);

        CheckBox canMoveDiagonallyCheckBox = new CheckBox("Allow Diagonal Movement");
        canMoveDiagonallyCheckBox.setOnAction(actionEvent -> GlobalConstants.CAN_MOVE_DIAGONALLY = canMoveDiagonallyCheckBox.isSelected());

        CheckBox generateMazeCheckBox = new CheckBox("Generate Random Maze");
        generateMazeCheckBox.setOnAction(actionEvent -> {
            generateRandomMaze = generateMazeCheckBox.isSelected();
            resourceBox.setDisable(generateRandomMaze);
            if (generateRandomMaze && image != null) {
                image = null;
                imageView.setImage(null);
            }
        });

        VBox vBox = new VBox(tileSize.gethBox(), widthSize.gethBox(), heightSize.gethBox());

        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(canMoveDiagonallyCheckBox, generateMazeCheckBox, resourceBox, start);
        vBox.setPadding(new Insets(5));
        vBox.setSpacing(10d);
        scene = new Scene(vBox);
    }

    private void openResourceFile(File file) {
        double maxVerticalHeight = GlobalConstants.SCREEN_HEIGHT - GlobalConstants.TOP_BAR_HEIGHT - PathFinderScene.PATHFINDER_OPTION_BAR_SIZE;

        try {
            image = ImageIO.read(file);
            if (image.getHeight() > maxVerticalHeight / GlobalConstants.MIN_TILE_SIZE
                    || image.getWidth() > GlobalConstants.SCREEN_WIDTH / GlobalConstants.MIN_TILE_SIZE) {
                image = null;
            }
            else {
                imageView.setImage(SwingFXUtils.toFXImage(image, null));
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void changeToPathFindingScene(double width, double height, double tileSize) {
        PathFinderScene pathFinderScene;
        if(image != null) {
            pathFinderScene = new PathFinderScene(image);
        }
        else {
            pathFinderScene = new PathFinderScene((int) width, (int) height, (int) tileSize, generateRandomMaze);
        }
        SceneController.changeSceneTo(pathFinderScene);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}

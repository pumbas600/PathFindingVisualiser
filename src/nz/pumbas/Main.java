package nz.pumbas;

import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import nz.pumbas.DifferentScenes.MenuScene;
import nz.pumbas.DifferentScenes.SceneController;

import java.io.File;

//TODO: Add Maze generated images

public class Main extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        SceneController.css  = this.getClass().getResource("styleSheet.css").toExternalForm();
        Main.primaryStage = primaryStage;
        SceneController.changeSceneTo(new MenuScene());

        primaryStage.setResizable(false);
        primaryStage.setTitle("Path Finding");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

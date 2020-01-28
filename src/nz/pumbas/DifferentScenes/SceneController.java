package nz.pumbas.DifferentScenes;

import nz.pumbas.Main;

public class SceneController {
    public static CustomScene currentScene;
    public static String css;

    public static void changeSceneTo(CustomScene scene) {
        currentScene = scene;
        currentScene.getScene().getStylesheets().add(css);
        Main.primaryStage.setScene(scene.getScene());
        Main.primaryStage.centerOnScreen();
    }
}

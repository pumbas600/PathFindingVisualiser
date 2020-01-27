package nz.pumbas.DifferentScenes;

import nz.pumbas.Main;

public class SceneController {
    public static CustomScene currentScene;

    public static void changeSceneTo(CustomScene scene) {
        currentScene = scene;
        Main.primaryStage.setScene(scene.getScene());
        Main.primaryStage.centerOnScreen();
    }
}

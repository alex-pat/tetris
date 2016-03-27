package by.alexpat.tetris;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Tetris");
        primaryStage.setResizable(false);
        Scene scene = new Scene(new Tetris());
        scene.getStylesheets().add("/res/styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

package by.alexpat.tetris;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.concurrent.Callable;

public class InfoBox extends VBox {

    public InfoBox(final GameController gameController) {
        setPadding(new Insets(20, 20, 20, 20));
        setSpacing(10);

        CheckBox muteCheckBox = new CheckBox();
        muteCheckBox.getStyleClass().add("mute");
        muteCheckBox.setMinSize(64, 64);
        muteCheckBox.setMaxSize(64, 64);
        muteCheckBox.selectedProperty().set(false);
        muteCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                gameController.getBoard().requestFocus();
            }
        });
        gameController.getSoundManager().muteProperty().bind( muteCheckBox.selectedProperty() );

        CheckBox botCheckBox = new CheckBox();
        botCheckBox.getStyleClass().add("bot");
        botCheckBox.setMinSize(64, 64);
        botCheckBox.setMaxSize(64, 64);
        botCheckBox.selectedProperty().set(false);
        botCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    gameController.start('?');
                    gameController.getBoard().startBot(null);
                } else {
                    gameController.getBoard().stopBot();
                }
                gameController.getBoard().requestFocus();
            }
        });

        Button savesButton = new Button();
        savesButton.getStyleClass().add("saves");
        savesButton.setMinSize(64, 64);
        savesButton.setMaxSize(64, 64);
        savesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gameController.getSaveManager().showRecords();
            }
        });

        HBox checkBoxes = new HBox();
        checkBoxes.setAlignment(Pos.CENTER);
        checkBoxes.getChildren().add(muteCheckBox);
        checkBoxes.getChildren().add(botCheckBox);
        checkBoxes.getChildren().add(savesButton);

        Slider sliderMusicVolume = new Slider();
        sliderMusicVolume.setMin(0);
        sliderMusicVolume.setMax(1);
        sliderMusicVolume.setValue(0.5);
        sliderMusicVolume.setFocusTraversable(false);
        sliderMusicVolume.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if (!aBoolean2) {
                    gameController.getBoard().requestFocus();
                }
            }
        });
        gameController.getSoundManager().volumeProperty().bind(sliderMusicVolume.valueProperty());

        Slider sliderSoundVolume = new Slider();
        sliderSoundVolume.setMin(0);
        sliderSoundVolume.setMax(1);
        sliderSoundVolume.setValue(0.5);
        sliderSoundVolume.setFocusTraversable(false);
        gameController.getSoundManager().soundVolumeProperty().bind(sliderSoundVolume.valueProperty());
        sliderSoundVolume.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    gameController.getBoard().requestFocus();
                }
            }
        });

        final ImageView playImageView = new ImageView(new Image(getClass().getResourceAsStream("/res/play.png")));
        final ImageView pauseImageView = new ImageView(new Image(getClass().getResourceAsStream("/res/pause.png")));

        Button btnStart = new Button("New Game");
        btnStart.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/res/rotate-left.png"))));
        setMargin(btnStart, new Insets(0, 15, 15, 0));
        btnStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gameController.start('?');
            }
        });

        Button btnStop = new Button("Stop");
        btnStop.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/res/stop.png"))));
        setMargin(btnStop, new Insets(0, 15, 15, 0));
        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gameController.stop();
            }
        });
        btnStop.setMaxWidth(Double.MAX_VALUE);
        btnStop.setAlignment(Pos.CENTER_LEFT);

        btnStart.setMaxWidth(Double.MAX_VALUE);
        btnStart.setAlignment(Pos.CENTER_LEFT);
        Button btnPause = new Button("Pause");
        setMargin(btnPause, new Insets(0, 15, 15, 0));
        btnPause.graphicProperty().bind(new ObjectBinding<Node>() {
            {
                super.bind(gameController.pausedProperty());
            }

            @Override
            protected Node computeValue() {
                if (gameController.pausedProperty().get()) {
                    return playImageView;
                } else {
                    return pauseImageView;
                }
            }
        });

        btnPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (gameController.pausedProperty().get()) {
                    gameController.pausedProperty().set(false);
                } else {
                    gameController.pausedProperty().set(true);

                }
            }
        });
        btnPause.setMaxWidth(Double.MAX_VALUE);
        btnPause.setAlignment(Pos.CENTER_LEFT);
        Preview preview = new Preview(gameController);


        getChildren().add(checkBoxes);
        getChildren().add(sliderMusicVolume);
        getChildren().add(sliderSoundVolume);

        Label lblPoints = new Label();
        lblPoints.getStyleClass().add("score");
        lblPoints.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return String.valueOf(gameController.getScoreManager().scoreProperty().get());
            }
        }, gameController.getScoreManager().scoreProperty()));
        lblPoints.setAlignment(Pos.CENTER_RIGHT);
        lblPoints.setMaxWidth(Double.MAX_VALUE);
        lblPoints.setEffect(new Reflection());

        getChildren().add(preview);
        getChildren().add(btnStart);
        getChildren().add(btnPause);
        getChildren().add(btnStop);

        Label lblInfo = new Label("▲ ▼ ◀ ▶ SPACE for movement");

        getChildren().add(lblInfo);

        getChildren().addAll(lblPoints);


    }

}

package by.alexpat.tetris;

import javafx.event.EventHandler;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class Tetris extends HBox {

    private boolean movingDown = false;

    public Tetris() {

        setId("tetris");

        final GameController gameController = new GameController();

        StackPane stackPane = new StackPane();

        stackPane.getChildren().add( gameController.getBoard() );

        stackPane.getChildren().add( gameController.getNotificationOverlay() );
        stackPane.setAlignment(Pos.TOP_CENTER);
        setMargin(stackPane, new Insets(40, 40, 40, 40));

        getChildren().add(stackPane);

        InfoBox infoBox = new InfoBox(gameController);
        infoBox.setMaxHeight(Double.MAX_VALUE);

        HBox.setHgrow(infoBox, Priority.ALWAYS);
        getChildren().add(infoBox);

        String image = getClass().getResource( "/res/background.png" ).toExternalForm();
//        setStyle( "-fx-background-image: url('" + image + "');"
//                + "-fx-background-position: top;"
//                + "-fx-background-repeat: no-repeat;");

        setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.P) {
                    gameController.pausedProperty().set( !gameController.pausedProperty().get() );
                    keyEvent.consume();
                }
            }
        });
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                gameController.getBoard().requestFocus();

            }
        });

        setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {

                if (keyEvent.getCode() == KeyCode.LEFT && !gameController.pausedProperty().get()) {
                    gameController.getBoard().move(HorizontalDirection.LEFT);
                    keyEvent.consume();
                }

                if (keyEvent.getCode() == KeyCode.RIGHT && !gameController.pausedProperty().get()) {
                    gameController.getBoard().move(HorizontalDirection.RIGHT);
                    keyEvent.consume();
                }

                if (keyEvent.getCode() == KeyCode.UP && !gameController.pausedProperty().get()) {
                    gameController.getBoard().rotate(HorizontalDirection.LEFT);
                    keyEvent.consume();
                }

                if (keyEvent.getCode() == KeyCode.DOWN) {
                    if (!movingDown) {
                        if (!gameController.pausedProperty().get()) {
                            gameController.getBoard().moveDownFast();
                        }
                        movingDown = true;
                        keyEvent.consume();
                    }
                }
                if (keyEvent.getCode() == KeyCode.SPACE && !gameController.pausedProperty().get()) {
                    gameController.getBoard().dropDown();
                    keyEvent.consume();
                }
            }
        });

        setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.DOWN) {
                    movingDown = false;
                    gameController.getBoard().moveDown();
                }
            }
        });

    }
}

package by.alexpat.tetris;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.Map;

public class Preview extends StackPane {

    private Map<Tetramino, Node> cloneToNode = new HashMap<>();

    private Map<Tetramino, Tetramino> tetraminoToClone = new HashMap<>();

    public Preview(GameController gameController) {

        final ObservableList<Tetramino> tetraminos = gameController.getBoard().getWaitingTetraminos();

        tetraminos.addListener(new ListChangeListener<Tetramino>() {
            @Override
            public void onChanged(Change<? extends Tetramino> change) {

                while (change.next()) {
                    if (change.wasRemoved()) {
                        for (final Tetramino tetramino : change.getRemoved()) {
                            final Tetramino clone = tetraminoToClone.remove(tetramino);
                            final Node group = cloneToNode.remove(clone);
                            FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(0.1), group);
                            fadeOutTransition.setToValue(0);
                            fadeOutTransition.setFromValue(1);
                            fadeOutTransition.setOnFinished(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    getChildren().remove(group);
                                }
                            });
                            fadeOutTransition.playFromStart();
                        }
                    }
                    if (change.wasAdded()) {
                        if (change.getList().size() == 1) return;
                        for (Tetramino added : change.getAddedSubList()) {

                            SequentialTransition sequentialTransition = new SequentialTransition();
                            Tetramino clone = added.clone();

                            Group group = new Group();
                            DropShadow dropShadow = new DropShadow();
                            dropShadow.setColor(Color.DARKGREY);
                            dropShadow.setRadius(20);
                            group.setEffect(dropShadow);
                            group.setOpacity(0);
                            group.getChildren().add(clone);
                            getChildren().add(group);
                            //group.setScaleX(0);
                            //group.setScaleY(0);
                            //group.setScaleZ(0);
                            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), group);
                            scaleTransition.setFromX(0);
                            scaleTransition.setFromZ(0);
                            scaleTransition.setFromY(0);

                            scaleTransition.setToX(1);
                            scaleTransition.setToZ(1);
                            scaleTransition.setToY(1);

                            //scaleTransition.play();

                            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.1), group);
                            fadeTransition.setFromValue(0);
                            fadeTransition.setToValue(1);
                            sequentialTransition.getChildren().add(new PauseTransition(Duration.seconds(0.1)));
                            sequentialTransition.getChildren().add(fadeTransition);
                            sequentialTransition.playFromStart();

                            tetraminoToClone.put(added, clone);
                            cloneToNode.put(clone, group);
                        }


                    }
                }
            }
        });

        setPrefHeight(140);
        setPrefWidth(140);
        setAlignment(Pos.CENTER);

        if (!tetraminos.isEmpty()) {
            getChildren().addAll(tetraminos.get(0));
        }

    }

}

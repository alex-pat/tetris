package by.alexpat.tetris;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class GameController {

    private final Board board;

    private final NotificationOverlay notificationOverlay;

    private final ScoreManager scoreManager;

    private final SoundManager soundManager;

    private final SaveManager saveManager;

    private final BooleanProperty paused = new SimpleBooleanProperty();

    public GameController() {
        this.board = new Board(this);
        this.soundManager = new SoundManager(this);
        this.scoreManager = new ScoreManager(this);
        this.saveManager = new SaveManager(this);

        notificationOverlay = new NotificationOverlay(this);
        paused.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    pause();
                } else {
                    play();
                }
            }
        });
    }

    public BooleanProperty pausedProperty() {
        return paused;
    }

    public void start(char firstTetramino) {
        soundManager.playFromStart();
        board.start(firstTetramino);
        saveManager.newGame();
        scoreManager.scoreProperty().set(0);
        paused.set(false);
    }

    private void pause() {
        board.pause();
    }

    public void stop() {
        soundManager.stop();
        board.clear();
        scoreManager.scoreProperty().set(0);
        paused.set(false);
    }

    public Board getBoard() {
        return board;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public SaveManager getSaveManager() {
        return saveManager;
    }

    public void play() {
        paused.set(false);
        soundManager.play();
        board.play();
    }

    public NotificationOverlay getNotificationOverlay() {
        return notificationOverlay;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

}

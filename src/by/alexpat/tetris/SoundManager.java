package by.alexpat.tetris;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HorizontalDirection;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

final class SoundManager implements Board.BoardListener {

    private final MediaPlayer mediaPlayer;

    private DoubleProperty volume = new SimpleDoubleProperty();

    private DoubleProperty soundVolume = new SimpleDoubleProperty();

    private BooleanProperty mute = new SimpleBooleanProperty();

    public SoundManager(GameController gameController) {

        URL resource = getClass().getResource("/res/theme.wav");
        Media media = new Media(resource.toString());

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.volumeProperty().bind(volumeProperty());
        mediaPlayer.muteProperty().bind(muteProperty());
        gameController.getBoard().addBoardListener(this);

        for (Sound sound : Sound.values()) {
            sound.getAudioClip().volumeProperty().bind(SoundManager.this.soundVolumeProperty());
        }
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }

    public DoubleProperty soundVolumeProperty() {
        return soundVolume;
    }

    public BooleanProperty muteProperty() {
        return mute;
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void play() {
        mediaPlayer.play();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void playFromStart() {
        mediaPlayer.stop();
        mediaPlayer.play();
    }

    public void onDropped() {
        if (!mute.get()) {
            Sound.DROPPED.getAudioClip().play();
        }
    }

    @Override
    public void onRowsEliminated(int rows) {
        if (!mute.get()) {
            if (rows < 4) {
                Sound.VANISH.getAudioClip().play();
            } else {
                Sound.TETRIS.getAudioClip().play();
            }
        }
    }

    @Override
    public void onSpawned(char newTetramino) {

    }

    @Override
    public void onGameOver() {
        mediaPlayer.stop();
        if (!mute.get()) {
            Sound.GAME_OVER.getAudioClip().play();
        }
    }

    @Override
    public void onInvalidMove() {
        if (!mute.get()) {
            Sound.INVALID_MOVE.getAudioClip().play();
        }
    }

    @Override
    public void onMove(HorizontalDirection horizontalDirection) {
        if (!mute.get()) {
            Sound.MOVE.getAudioClip().play();
        }
    }

    @Override
    public void onRotate(HorizontalDirection horizontalDirection) {
        if (!mute.get()) {
            Sound.ROTATE.getAudioClip().play();
        }
    }

    private enum Sound {

        ROTATE("res/cartoon130.wav"),
        TETRIS("res/cartoon034.wav"),
        DROPPED("res/cartoon035.wav"),
        INVALID_MOVE("res/cartoon155.wav"),
        MOVE("res/cartoon136.wav"),
        VANISH("res/cartoon017.wav"),
        GAME_OVER("res/cartoon014.wav");

        private AudioClip audioClip;

        private Sound(String name) {
            audioClip = new AudioClip(getClass().getResource("/" + name).toExternalForm());
        }

        public AudioClip getAudioClip() {
            return audioClip;
        }

    }
}


package by.alexpat.tetris;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HorizontalDirection;

public class ScoreManager implements Board.BoardListener {

    private final IntegerProperty score = new SimpleIntegerProperty();

    public ScoreManager(GameController gameController) {
        gameController.getBoard().addBoardListener(this);
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    private void addScore(int score) {
        this.score.set(this.score.get() + score);
    }

    @Override
    public void onDropped() {
    }

    @Override
    public void onSpawned(char newTetramino) {
    }

    @Override
    public void onRowsEliminated(int rows) {
        switch (rows) {
            case 1:
                addScore(40);
                break;
            case 2:
                addScore(100);
                break;
            case 3:
                addScore(300);
                break;
            default:
                addScore(1200);
                break;
        }
    }

    @Override
    public void onGameOver() {
    }

    @Override
    public void onInvalidMove() {
    }

    @Override
    public void onMove(HorizontalDirection horizontalDirection) {
    }

    @Override
    public void onRotate(HorizontalDirection horizontalDirection) {
    }

}

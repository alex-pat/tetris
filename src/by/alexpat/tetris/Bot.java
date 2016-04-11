package by.alexpat.tetris;

import javafx.geometry.HorizontalDirection;

import java.util.concurrent.TimeUnit;

public class Bot implements Runnable {

    private static final int SLEEP_TIME = 700;

    private Board board;

    public Bot(Board board) {
        this.board = board;
    }

    @Override
    public void run() {
        try {
            while (board.getBotState() == true) {
                int randomStepCount = (int) (5.0 * Math.random());
                HorizontalDirection direction =
                        (Math.random() < 0.5)
                                ? HorizontalDirection.LEFT
                                : HorizontalDirection.RIGHT;
                for (int i = 0; i < randomStepCount; i++) {
                    board.move(direction);
                    Thread.yield();
                    TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                }
                if (Math.random() < 0.5)
                    for (int i = 0; i < 3.0 * Math.random(); i++) {
                        board.rotate(HorizontalDirection.LEFT);
                        TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                        Thread.yield();
                    }
                board.dropDown();
                TimeUnit.MILLISECONDS.sleep(1000);
                Thread.yield();
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted");
        }
    }
}

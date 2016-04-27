package by.alexpat.tetris;

import javafx.geometry.HorizontalDirection;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Bot implements Runnable {

    private static final int SLEEP_TIME = 70*4;

    private Board board;

    Iterator<String> commandsIter = null;

    public Bot(Board board, Iterator<String> commandsIter) {
        this.board = board;
        this.commandsIter = commandsIter;
    }

    @Override
    public void run() {
        try {
            if (board.isReplay() == true && commandsIter != null) {
                while (board.getBotState() == true && commandsIter.hasNext()) {
                    String command = commandsIter.next();
                    switch (command) {
                        case "KA":
                            board.move(HorizontalDirection.LEFT);
                            break;
                        case "KD":
                            board.move(HorizontalDirection.RIGHT);
                            break;
                        case "KR":
                            board.rotate(HorizontalDirection.LEFT);
                            break;
                        default:
                            board.dropDown(command.charAt(1));
                            Thread.yield();
                            TimeUnit.MILLISECONDS.sleep(SLEEP_TIME*4);
                    }
                    Thread.yield();
                    TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                }
            }
            else {
                TimeUnit.MILLISECONDS.sleep(1000);
                Thread.yield();
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

                    for (int i = 0; i < 3.0 * Math.random(); i++) {
                        board.rotate(HorizontalDirection.LEFT);
                        TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
                        Thread.yield();
                    }
                    board.dropDown('?');
                    TimeUnit.MILLISECONDS.sleep(1000);
                    Thread.yield();
                }
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted");
        }
    }
}

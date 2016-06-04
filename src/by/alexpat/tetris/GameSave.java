package by.alexpat.tetris;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;

final public class GameSave {

    private int gameNumber;

    private int gameScore;

    private int gameLength;

    public int getGameScore() {
        return gameScore;
    }

    public int getGameLength() {
        return gameLength;
    }

    public int getGameNumber() {
        return gameNumber;
    }

    private LinkedList<String> gameActions;

    public GameSave(int gameNumber, int gameScore) {
        this.gameNumber = gameNumber;
        this.gameScore = gameScore;
        gameLength = 0;
        gameActions = new LinkedList<>();
    }

    public void addAction (String action) {
        gameActions.add(action);
        if (action.charAt(0) == 'K') {
            gameLength++;
        }
    }

    public void write (PrintWriter out) throws IOException {
        out.println(String.valueOf(gameNumber));
        out.println(String.valueOf(gameScore));

        Iterator<String> iter = gameActions.iterator();
        while (iter.hasNext()) {
            out.println(iter.next());
        }
        out.println("END");
    }

    public void addScore (int score) {
        gameScore += score;
    }

    Iterator<String> getInterator () {
        return gameActions.iterator();
    }

    int getComparableScore() {
        return - gameScore - gameLength / 2;
    }

    String[] getActions() {
        return gameActions.toArray(new String[gameActions.size()]);
    }
}

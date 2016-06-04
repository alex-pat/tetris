package by.alexpat.tetris;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HorizontalDirection;

import java.io.*;
import java.util.ListIterator;

final class SaveManager implements Board.BoardListener {

    private int savesCount = 0;

    private ObservableList<GameSave> saves;

    private GameSave currentGame;

    private File base = new File("../savetris.tsb");

    private GameController gameController;

    public SaveManager(GameController gameController) {
        this.gameController = gameController;
        gameController.getBoard().addBoardListener(this);

        saves = FXCollections.observableArrayList();
        try {
            if (!base.exists()) {
                base.createNewFile();
            }

            BufferedReader in = new BufferedReader(new FileReader( base.getAbsoluteFile()));
            try {
                String command;
                boolean isNewGameSave = true;
                while ((command = in.readLine()) != null) {
                    if (isNewGameSave) {
                        isNewGameSave = false;
                        int gameScore = Integer.parseInt(in.readLine());
                        int gameNumber = Integer.parseInt(command);
                        saves.add(new GameSave( gameNumber, gameScore));
                        savesCount++;
                        continue;
                    }
                    switch (command.charAt(0)) {
                        case 'E':
                            isNewGameSave = true;
                            break;
                        case 'K':
                        case 'T':
                            saves.get(savesCount - 1).addAction(command);
                            break;
                        default:
                            System.err.println("Unknown string in base");
                    }
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        ListIterator<GameSave> iter = saves.listIterator();
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream(base, false));

            try {
                while (iter.hasNext()) {
                    iter.next().write(out);
                }
            } finally {
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GameSave getGame() {
        return currentGame;
    }

    public void newGame() {
        if (gameController.getBoard().isReplay() == false) {
            currentGame = new GameSave(savesCount++, 0);
        }
    }

    @Override
    public void onDropped() {
    }

    @Override
    public void onSpawned(char newTetramino) {
        if (currentGame == null) {
            newGame();
        }
        if (gameController.getBoard().isReplay() == false) {
            currentGame.addAction("T" + newTetramino);
        }
    }

    @Override
    public void onRowsEliminated(int rows) {
        if (gameController.getBoard().isReplay() == false) {
            int score = 0;
            switch (rows) {
                case 1:
                    score = 40;
                    break;
                case 2:
                    score = 100;
                    break;
                case 3:
                    score = 300;
                    break;
                default:
                    score = 1200;
                    break;
            }
            currentGame.addScore(score);
        }
    }

    @Override
    public void onGameOver() {
        if (gameController.getBoard().isReplay() == false) {
            gameController.getBoard().setBotState(false);
            saves.add(currentGame);
            currentGame = null;
            savesCount++;
            save();
        } else {
            gameController.getBoard().setReplaying(false);
        }
    }

    @Override
    public void onInvalidMove() {
    }

    @Override
    public void onMove(HorizontalDirection horizontalDirection) {
        if (gameController.getBoard().isReplay() == false) {
            if (horizontalDirection.equals(HorizontalDirection.LEFT)) {
                currentGame.addAction("KA");
            } else if (horizontalDirection.equals(HorizontalDirection.RIGHT)) {
                currentGame.addAction("KD");
            }
        }
    }

    @Override
    public void onRotate(HorizontalDirection horizontalDirection) {
        if (gameController.getBoard().isReplay() == false) {
            currentGame.addAction("KR");
        }
    }

    public void showRecords () {
        new RecordsScene(gameController, saves);
    }



}

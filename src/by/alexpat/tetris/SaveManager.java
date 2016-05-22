package by.alexpat.tetris;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.Comparator;
import java.util.Iterator;
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
        Stage window = new Stage();
        window.setTitle("Records");

        VBox recordsBox = new VBox();

        TableView<GameSave> table = new TableView<>();

        TableColumn<GameSave, Integer> numberColumn = new TableColumn<>("#");
        numberColumn.setMinWidth(100);
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("gameNumber"));

        TableColumn<GameSave, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setMinWidth(100);
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("gameScore"));

        TableColumn<GameSave, Integer> lengthColumn = new TableColumn<>("Length");
        lengthColumn.setMinWidth(100);
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("gameLength"));

        table.setItems(saves);
        table.getColumns().addAll(numberColumn, scoreColumn, lengthColumn);
        recordsBox.getChildren().add(table);

        Button replayButton = new Button("Replay");
        replayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gameController.getBoard().setReplaying(true);
                int replayNumber = table.getFocusModel().getFocusedIndex();
                Iterator<String> cmd = saves.get(replayNumber).getInterator();
                gameController.start(cmd.next().charAt(1));
                gameController.getBoard().startBot( cmd );
                window.close();
            }
        });

        Button sortButton = new Button("Sort");
        sortButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sorting();
                saves.sort(Comparator.comparing(GameSave::getComparableScore));
            }
        });

        Button bestGameButton = new Button("Best");
        bestGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                int indexBest = 0;
                for (int i = 1; i < saves.size(); i++) {
                    if (saves.get(i).getComparableScore() < saves.get(indexBest).getComparableScore()) {
                        indexBest = i;
                    }
                }
                Iterator<String> cmd = saves.get(indexBest).getInterator();

                gameController.getBoard().setReplaying(true);
                gameController.start(cmd.next().charAt(1));
                gameController.getBoard().startBot( cmd );
                window.close();
            }
        });

        Button worstGameButton = new Button("Worst");
        worstGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                int indexWorst = 0;
                for (int i = 1; i < saves.size(); i++) {
                    if (saves.get(i).getComparableScore() > saves.get(indexWorst).getComparableScore()) {
                        indexWorst = i;
                    }
                }
                Iterator<String> cmd = saves.get(indexWorst).getInterator();
                
                gameController.getBoard().setReplaying(true);
                gameController.start(cmd.next().charAt(1));
                gameController.getBoard().startBot( cmd );
                window.close();
            }
        });

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(
                replayButton,
                sortButton,
                bestGameButton,
                worstGameButton);
        buttonBox.setSpacing(8.0);
        buttonBox.setAlignment(Pos.CENTER);

        recordsBox.getChildren().add(buttonBox);

        Scene scene = new Scene(recordsBox);
        window.setScene(scene);
        window.show();
    }

    private void sorting() {
        int[] comparableSaves = new int[saves.size()];

        long sortStartTime = 0;
        long sortEndTime = 0;
        long sortTraceTime = 0;

        for (int i = 0; i < saves.size(); i++) {
            comparableSaves[i] = saves.get(i).getComparableScore();
        }
        sortStartTime = System.nanoTime();
        sort(comparableSaves, 0, comparableSaves.length - 1);
        sortEndTime = System.nanoTime();
        sortTraceTime = sortEndTime - sortStartTime;
        System.out.println("Sorting time in Java:  " + sortTraceTime);

        for (int i = 0; i < saves.size(); i++) {
            comparableSaves[i] = saves.get(i).getComparableScore();
        }
        ScalaExtensions scala = new ScalaExtensions();
        sortStartTime = System.nanoTime();
        scala.qsort(comparableSaves, 0, comparableSaves.length - 1);
        sortEndTime = System.nanoTime();
        sortTraceTime = sortEndTime - sortStartTime;
        System.out.println("Sorting time in Scala: " + sortTraceTime);

    }

    void sort(int[] array, int left, int right) {
        int pivot = array[(left + right) / 2];
        int a = left;
        int b = right;
        while (a <= b) {
            while (array[a] < pivot) {
                ++a;
            }
            while (array[b] > pivot) {
                --b;
            }
            if (a <= b) {
                swap(array, a, b);
                ++a;
                --b;
            }
        }
        if (left < b) sort(array, left, b);
        if (b < right) sort(array, a, right);
    }

    void swap(int[] array, int i, int j) {
        int t = array[i];
        array[i] = array[j];
        array[j] = t;
    }


}

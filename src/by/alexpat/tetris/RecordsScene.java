package by.alexpat.tetris;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.Iterator;

public class RecordsScene {

    private static Stage primaryStage;

    private static Scene gameScene;

    private Scene tableScene;

    private Scene textScene;

    public RecordsScene(GameController gameController,
                        ObservableList<GameSave> saves) {

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
                primaryStage.setScene(gameScene);
            }
        });

        Button sortButton = new Button("Sort");
        sortButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sorting(saves);
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
                primaryStage.setScene(gameScene);
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
                primaryStage.setScene(gameScene);
            }
        });

        Button statsButton = new Button("Stats");
        statsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ScalaExtensions scala = new ScalaExtensions();

                String[][] actions = new String[saves.size()][];
                for (int i = 0; i < saves.size(); i++) {
                    actions[i] = saves.get(i).getActions();
                }

                int leftCount = scala.getLeftCount(actions);
                int rightCount = scala.getRightCount(actions);
                int rotatesCount = scala.getRotatesCount(actions);
                int figuresCount = scala.getTetraminoesCount(actions);
                float movesPG = (float) (leftCount + rightCount) / saves.size();
                float figPG = (float) figuresCount / saves.size();
                float movesPF = movesPG / figPG;

                String statsText =
                        "In " + saves.size() + " games:\n" +
                        "We have " + leftCount+ " moves left\n" +
                        "We have " + rightCount + " moves right\n" +
                        "We have " + rotatesCount + " rotates\n" +
                        "We have " + figuresCount + " tetramino figures\n" +
                        "So...\nIt\'s " + movesPG + " moves per game\n" +
                        "It\'s " + figPG + " figures per game\n" +
                        "And " + movesPF + " moves of one figure";

                textScene = createTextScene(statsText);
                primaryStage.setScene(textScene);
            }
        });

        Button pseudoCodeButton = new Button("Pseudocode");
        pseudoCodeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int replayNumber = table.getFocusModel().getFocusedIndex();
                String[] rawText = saves.get(replayNumber).getActions();
                String pseudoText = new ScalaExtensions().getPseudocode(rawText);
                textScene = createTextScene(pseudoText);
                primaryStage.setScene(textScene);
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                primaryStage.setScene(gameScene);
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

        HBox secButtonBox = new HBox();
        secButtonBox.getChildren().addAll(
                statsButton,
                pseudoCodeButton,
                backButton);

        secButtonBox.setSpacing(8.0);
        secButtonBox.setAlignment(Pos.CENTER);

        recordsBox.getChildren().addAll(buttonBox, secButtonBox);

        tableScene = new Scene(recordsBox);

        primaryStage.setScene(tableScene);
    }

    private Scene createTextScene(String text) {

        TextArea textArea = new TextArea();
        textArea.setText(text);
        textArea.setEditable(false);
        textArea.setMinSize(300,400);
        textArea.setMaxSize(300,400);
        Button backButton = new Button("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                primaryStage.setScene(tableScene);
            }
        });

        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(backButton);
        buttonBox.setSpacing(8.0);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        VBox sceneBox = new VBox();
        sceneBox.getChildren().addAll(textArea, buttonBox);

        return new Scene(sceneBox);
    }

    static void setGameScene(Scene scene) {
        gameScene = scene;
    }

    static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    private void sorting(ObservableList<GameSave> saves) {
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

package by.alexpat.tetris;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.Group;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Tetramino extends Group implements Cloneable {

    private static final Random RANDOM = new Random();

    private static final TetraminoDefinition I = new TetraminoDefinition(new int[][]{
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    }, Color.CYAN);

    private static final TetraminoDefinition J = new TetraminoDefinition(new int[][]{
            {1, 0, 0},
            {1, 1, 1},
            {0, 0, 0}
    }, Color.BLUE);

    private static final TetraminoDefinition L = new TetraminoDefinition(new int[][]{
            {0, 0, 1},
            {1, 1, 1},
            {0, 0, 0}
    }, Color.ORANGE);

    private static final TetraminoDefinition O = new TetraminoDefinition(new int[][]{
            {1, 1},
            {1, 1}
    }, Color.YELLOW);

    private static final TetraminoDefinition S = new TetraminoDefinition(new int[][]{
            {0, 1, 1},
            {1, 1, 0},
            {0, 0, 0}
    }, Color.GREENYELLOW);

    private static final TetraminoDefinition T = new TetraminoDefinition(new int[][]{
            {0, 1, 0},
            {1, 1, 1},
            {0, 0, 0}
    }, Color.PURPLE);

    private static final TetraminoDefinition Z = new TetraminoDefinition(new int[][]{
            {1, 1, 0},
            {0, 1, 1},
            {0, 0, 0}
    }, Color.ORANGERED);

    private static final TetraminoDefinition[] TETRAMINO_DEFINITIONS = new TetraminoDefinition[]{I, J, L, O, S, T, Z};

    /**
     * The light. This has to be rotated, too, as the tetraminos rotate.
     */
    private Lighting lighting = new Lighting(new Light.Distant(245, 50, Color.WHITE));

    private int[][] matrix;

    private Paint paint;

    private TetraminoDefinition tetraminoDefinition;

    private ReadOnlyDoubleProperty squareSize;

    private Tetramino(TetraminoDefinition tetraminoDefinition, ReadOnlyDoubleProperty squareSize) {
        this.matrix = tetraminoDefinition.matrix;
        this.tetraminoDefinition = tetraminoDefinition;
        this.squareSize = squareSize;
        paint = tetraminoDefinition.color;

        lighting = new Lighting(new Light.Distant(225, 55, Color.WHITE));


        lighting.setSurfaceScale(0.8);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {

                final Rectangle rectangle = new Rectangle();
                final int finalI = i;
                final int finalJ = j;
                ChangeListener<Number> changeListener = new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                        rectangle.setWidth(number2.doubleValue());
                        rectangle.setHeight(number2.doubleValue());
                        rectangle.setTranslateY(number2.doubleValue() * finalI);
                        rectangle.setTranslateX(number2.doubleValue() * finalJ);
                    }
                };
                rectangle.setUserData(changeListener);
                //changeListeners.add(changeListener);
                // Don't use binding to squareSize because this will cause memory leaks due to a bug in JavaFX 2.
                squareSize.addListener(new WeakChangeListener<>(changeListener));
                rectangle.setWidth(squareSize.doubleValue());
                rectangle.setHeight(squareSize.get());
                rectangle.setTranslateY(squareSize.get() * finalI);
                rectangle.setTranslateX(squareSize.get() * finalJ);

                if (matrix[i][j] == 1) {
                    rectangle.setEffect(lighting);
                    rectangle.setFill(tetraminoDefinition.color);
                    rectangle.setFill(paint);
                    rectangle.setArcHeight(7);
                    rectangle.setArcWidth(7);
                } else {
                    rectangle.setOpacity(0);
                }
                getChildren().add(rectangle);

            }
        }

        //setCacheHint(CacheHint.SPEED);
        //setCache(true);
    }

    public static Tetramino random(ReadOnlyDoubleProperty squareSize) {
        TetraminoDefinition tetraminoDefinition = TETRAMINO_DEFINITIONS[RANDOM.nextInt(7)];

        return new Tetramino(tetraminoDefinition, squareSize);
    }

    public static Tetramino getPlanned(char newTetramino, ReadOnlyDoubleProperty squareSize) {
        TetraminoDefinition tetraminoDefinition;
        switch (newTetramino) {
            case 'I':
                tetraminoDefinition = I;
                break;
            case 'J':
                tetraminoDefinition = J;
                break;
            case 'L':
                tetraminoDefinition = L;
                break;
            case 'O':
                tetraminoDefinition = O;
                break;
            case 'S':
                tetraminoDefinition = S;
                break;
            case 'T':
                tetraminoDefinition = T;
                break;
            case 'Z':
                tetraminoDefinition = Z;
                break;
            default:
                System.err.println("Unknown tetramino: ");
                tetraminoDefinition = I;
        }
        return new Tetramino(tetraminoDefinition, squareSize);
    }

    char getLetter() {
        if (tetraminoDefinition.color.equals(Color.CYAN)) {
            return 'I';
        }
        else if (tetraminoDefinition.color.equals(Color.BLUE)) {
            return 'J';
        }
        else if (tetraminoDefinition.color.equals(Color.ORANGE)) {
            return 'L';
        }
        else if (tetraminoDefinition.color.equals(Color.YELLOW)) {
            return 'O';
        }
        else if (tetraminoDefinition.color.equals(Color.GREENYELLOW)) {
            return 'S';
        }
        else if (tetraminoDefinition.color.equals(Color.PURPLE)) {
            return 'T';
        }
        else if (tetraminoDefinition.color.equals(Color.ORANGERED)) {
            return 'Z';
        }
        return 'I';
    }

    @Override
    public Tetramino clone() {
        return new Tetramino(tetraminoDefinition, squareSize);
    }

    public Paint getFill() {
        return paint;
    }

    public Lighting getLighting() {
        return lighting;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    private static class TetraminoDefinition {
        private final Color color;

        private final int[][] matrix;

        private TetraminoDefinition(int[][] matrix, Color color) {
            this.color = color;
            this.matrix = matrix;
        }
    }

}

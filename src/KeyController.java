import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyController {
    KeyController(Scene scene, MainWindow mainWindow, ControlFormula controlFormula) {
        scene.setOnKeyPressed(keyEvent -> {
            System.out.println("KeyPressed " + keyEvent.getCode().toString());
            switch (keyEvent.getCode()) {
                case DIGIT0:
                    controlFormula.addNumber(0);
                    break;
                case DIGIT1:
                    controlFormula.addNumber(1);
                    break;
                case DIGIT2:
                    controlFormula.addNumber(2);
                    break;
                case DIGIT3:
                    controlFormula.addNumber(3);
                    break;
                case DIGIT4:
                    controlFormula.addNumber(4);
                    break;
                case DIGIT5:
                    controlFormula.addNumber(5);
                    break;
                case DIGIT6:
                    controlFormula.addNumber(6);
                    break;
                case DIGIT7:
                    controlFormula.addNumber(7);
                    break;
                case DIGIT8:
                    controlFormula.addNumber(8);
                    break;
                case DIGIT9:
                    controlFormula.addNumber(9);
                    break;
                case PERIOD:
                    controlFormula.addOperator(Constants.Type.Point);
                    break;
                case PLUS:
                    controlFormula.addOperator(Constants.Type.Addition);
                    break;
                case MINUS:
                    controlFormula.addOperator(Constants.Type.Subtraction);
                    break;
                case STAR:
                    controlFormula.addOperator(Constants.Type.Multiplication);
                    break;
                case DIVIDE:
                    controlFormula.addOperator(Constants.Type.Division);
                    break;
                case BACK_SPACE:
                    controlFormula.deleteCurrent();
                    break;
                case DELETE:
                    controlFormula.clearAll();
                    break;
                case ESCAPE:
                    mainWindow.closeWithStyle(mainWindow.primaryStage);
                    break;
            }
        });
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                controlFormula.calculate();
                event.consume(); // Damit kein fokussierter Button gedrückt wird
            }if (event.getCode() == KeyCode.LEFT) {
                controlFormula.moveCursorLeft();
                event.consume(); // Damit kein fokussierter Button gedrückt wird
            } if (event.getCode() == KeyCode.RIGHT) {
                controlFormula.moveCursorRight();
                event.consume(); // Damit kein fokussierter Button gedrückt wird
            }
        });

    }
}

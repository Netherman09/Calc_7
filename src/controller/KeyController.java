package controller;

import calculation.ControlFormula;
import core.Constants;
import core.MainWindow;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyController {
    boolean isPressingShift = false;
    boolean isPressingControl = false;
    public KeyController(Scene scene, MainWindow mainWindow, ControlFormula controlFormula) {
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
                    if (!isPressingShift && !isPressingControl) controlFormula.addNumber(7);
                    else if (!isPressingControl) controlFormula.addOperator(Constants.Type.Division);
                    else controlFormula.addOperator(Constants.Type.Fraction);
                    break;
                case DIGIT8:
                    if (!isPressingShift) controlFormula.addNumber(8);
                    else controlFormula.addOperator(Constants.Type.OpeningBracket);
                    break;
                case DIGIT9:
                    if (!isPressingShift) controlFormula.addNumber(9);
                    else controlFormula.addOperator(Constants.Type.ClosingBracket);
                    break;
                case PERIOD:
                    controlFormula.addOperator(Constants.Type.Point);
                    break;
                case PLUS:
                    if (!isPressingShift) controlFormula.addOperator(Constants.Type.Addition);
                    else controlFormula.addOperator(Constants.Type.Multiplication);
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
                case SHIFT:
                    isPressingShift = true;
                    break;
                case CONTROL:
                    isPressingControl = true;
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
        scene.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.SHIFT) {
                isPressingShift = false;
            }
            if (keyEvent.getCode() == KeyCode.CONTROL) {
                isPressingControl = false;
            }
        });
    }
}

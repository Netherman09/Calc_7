public class ControlButtons {
    MainWindow mainWindow;
    ControlFormula controlFormula;

    double[] position;
    double[] dragStartPosition;
    double[] dragWindowStartPosition;

    private double xOffset = 0;
    private double yOffset = 0;

    ControlButtons(MainWindow mainWindow, ControlFormula controlFormula) {
        this.mainWindow = mainWindow;
        this.controlFormula = controlFormula;

        position = new double[2];
        dragStartPosition = new double[2];
        dragWindowStartPosition = new double[2];

        initializeLogic();
    }

    private void initializeLogic() {
        mainWindow.mainLayout.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                mainWindow.showContextMenu();
            }

            if (event.getClickCount() == 1) {
                mainWindow.hideContextMenu();
            }
        });

        mainWindow.zeroButton.setOnAction(event -> controlFormula.addNumber(0));
        mainWindow.oneButton.setOnAction(event -> controlFormula.addNumber(1));
        mainWindow.twoButton.setOnAction(event -> controlFormula.addNumber(2));
        mainWindow.threeButton.setOnAction(event -> controlFormula.addNumber(3));
        mainWindow.fourButton.setOnAction(event -> controlFormula.addNumber(4));
        mainWindow.fiveButton.setOnAction(event -> controlFormula.addNumber(5));
        mainWindow.sixButton.setOnAction(event -> controlFormula.addNumber(6));
        mainWindow.sevenButton.setOnAction(event -> controlFormula.addNumber(7));
        mainWindow.eightButton.setOnAction(event -> controlFormula.addNumber(8));
        mainWindow.nineButton.setOnAction(event -> controlFormula.addNumber(9));
        mainWindow.pointButton.setOnAction(event -> controlFormula.addOperator(Constants.Type.Point));

        mainWindow.piButton.setOnAction(event -> controlFormula.addConstant(Math.PI, "𝝅"));
        mainWindow.eButton.setOnAction(event -> controlFormula.addConstant(Math.E, "e"));

        mainWindow.addButton.setOnAction(event -> controlFormula.addOperator(Constants.Type.Addition));
        mainWindow.subtractButton.setOnAction(event -> controlFormula.addOperator(Constants.Type.Subtraction));
        mainWindow.multiplicationButton.setOnAction(event -> controlFormula.addOperator(Constants.Type.Multiplication));
        mainWindow.divisionButton.setOnAction(event -> controlFormula.addOperator(Constants.Type.Division));
        mainWindow.openingBracketButton.setOnAction(event -> controlFormula.addOperator(Constants.Type.OpeningBracket));
        mainWindow.closingBracketButton.setOnAction(event -> controlFormula.addOperator(Constants.Type.ClosingBracket));
        mainWindow.fractionButton.setOnAction(event -> controlFormula.addOperator(Constants.Type.Fraction));
        mainWindow.rootButton.setOnAction(event -> controlFormula.addOperator(Constants.Type.Root));
        mainWindow.exponentButton.setOnAction(event -> controlFormula.addOperator(Constants.Type.Exponent));
        mainWindow.sineButton.setOnAction(event -> controlFormula.addOperator(Constants.Type.Sine));
        mainWindow.cosineButton.setOnAction(event -> controlFormula.addOperator(Constants.Type.Cosine));
        mainWindow.tangentButton.setOnAction(event -> controlFormula.addOperator(Constants.Type.Tangent));

        mainWindow.calculateButton.setOnAction(event -> controlFormula.calculate());

        mainWindow.ansButton.setOnAction(event -> controlFormula.addVariable("Ans", Constants.VarType.Ans));

        mainWindow.clearButton.setOnAction(event -> controlFormula.clearAll());
        mainWindow.deleteButton.setOnAction(event -> controlFormula.deleteCurrent());

        mainWindow.leftButton.setOnAction(event -> controlFormula.moveCursorLeft());
        mainWindow.rightButton.setOnAction(event -> controlFormula.moveCursorRight());



        mainWindow.mainLayout.setOnMousePressed(event -> {
            // getSceneX/Y ist die Position innerhalb deines Fensters
            mainWindow.hideContextMenu();
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        mainWindow.mainLayout.setOnMouseDragged(event -> {
            // getScreenX/Y ist die absolute Position auf dem Monitor
            // Wir setzen das Fenster so, dass der Abstand zum Mauszeiger gleich bleibt
            mainWindow.primaryStage.setX(event.getScreenX() - xOffset);
            mainWindow.primaryStage.setY(event.getScreenY() - yOffset);
        });
    }


}

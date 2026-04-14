import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class ControlFormula {
    Field startField;
    Field currentField;
    int cursorPosition;
    //RootNode startRootNode;
    MainWindow mainWindow;
    Field lastEquation;
    boolean showsResult = false;
    double currentResult;

    ControlFormula(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        startField = new Field();
        currentField = startField;
        cursorPosition = 0;
        renderAll();
    }
    public void addNumber(int value) {
        ValueNode valueNode = new ValueNode(value, currentField);
        currentField.add(valueNode, cursorPosition);
        incrementCursorPosition();

        renderAll();
    }

    public void addOperator(Constants.Type type) {
        Node node = null;
        if (type == Constants.Type.Addition) node = new AdditionNode(currentField);
        else if (type == Constants.Type.Subtraction) node = new SubtractionNode(currentField);
        else if (type == Constants.Type.Multiplication) node = new MultiplicationNode(currentField);
        else if (type == Constants.Type.Division) node = new DivisionNode(currentField);
        else if (type == Constants.Type.OpeningBracket) node = new OpeningBracketNode(currentField);
        else if (type == Constants.Type.ClosingBracket) node = new ClosingBracketNode(currentField);
        else if (type == Constants.Type.Point) node = new PointNode(currentField);

        if (node != null) currentField.add(node, cursorPosition);

        if (type == Constants.Type.Fraction) {
            FractionNode fractionNode = new FractionNode(currentField);
            currentField.add(fractionNode, cursorPosition);
            cursorPosition = 0;
            currentField = fractionNode.getFirstChild();
        } else if (type == Constants.Type.Root)  {
            RootNode rootNode = new RootNode(currentField);
            currentField.add(rootNode, cursorPosition);
            cursorPosition = 0;
            currentField = rootNode.getFirstChild();
        } else if (type == Constants.Type.Exponent)  {
            PowerNode exponent = new PowerNode(currentField);
            currentField.add(exponent, cursorPosition);
            cursorPosition = 0;
            currentField = exponent.getChildField();
        } else if (type == Constants.Type.Sine)  {
            SineNode sineNode = new SineNode(currentField);
            currentField.add(sineNode, cursorPosition);
            cursorPosition = 0;
            currentField = sineNode.getChildField();
        } else if (type == Constants.Type.Cosine)  {
            CosineNode cosineNode = new CosineNode(currentField);
            currentField.add(cosineNode, cursorPosition);
            cursorPosition = 0;
            currentField = cosineNode.getChildField();
        } else if (type == Constants.Type.Tangent)  {
            TangentNode tangentNode = new TangentNode(currentField);
            currentField.add(tangentNode, cursorPosition);
            cursorPosition = 0;
            currentField = tangentNode.getChildField();
        } else {
            incrementCursorPosition();
        }

        renderAll();
    }

    public void addConstant(double value, String symbol) {
        ConstantsNode constantsNode = new ConstantsNode(value, symbol, currentField);
        currentField.add(constantsNode, cursorPosition);
        incrementCursorPosition();

        renderAll();
    }

    public void addVariable(String symbol, Constants.VarType varType) {
        VarNode varNode = new VarNode(symbol, varType, currentField);
        currentField.add(varNode, cursorPosition);
        incrementCursorPosition();

        renderAll();
    }

    public void storeCustomVar(String symbol, Constants.VarType varType) {
        if (showsResult) Constants.customResult = currentResult;
        else addVariable(symbol, varType);
    }

    private void incrementCursorPosition() {
        cursorPosition++;
    }

    private void decrementCursorPosition() {
        cursorPosition--;
    }

    public void moveCursorRight() {
        System.out.println("Move Cursor Right");
        if (cursorPosition == currentField.getLength() - 1 && currentField.hasParent()) {
            System.out.println("At Field End");
            if (currentField.getParent().getNodeType() == Constants.NodeType.Special) {
                SpecialNode parentNode = (SpecialNode) currentField.getParent(); // Single Special Node wird noch nicht beachtet
                if (currentField.equals(parentNode.getFirstChild())) { // Move Cursor to Second Field
                    System.out.println("Move to Second Field");
                    currentField = parentNode.getSecondChild();
                    cursorPosition = 0;
                } else { // Move Cursor out of Field
                    System.out.println("Move out of Field");
                    currentField = parentNode.getParentField();
                    cursorPosition = parentNode.getParentField().getIndexOf(parentNode) + 1;
                }
            } else if (currentField.getParent().getNodeType() == Constants.NodeType.ExponentSpecial) {
                ExponentSpecialNode parentNode = (ExponentSpecialNode) currentField.getParent();
                System.out.println("Move out of Field");
                currentField = parentNode.getParentField();
                cursorPosition = parentNode.getParentField().getIndexOf(parentNode) + 1;
            } else if (currentField.getParent().getNodeType() == Constants.NodeType.SingleSpecial) {
                SingleSpecialNode parentNode = (SingleSpecialNode) currentField.getParent();
                System.out.println("Move out of Field");
                currentField = parentNode.getParentField();
                cursorPosition = parentNode.getParentField().getIndexOf(parentNode) + 1;
            }
        }
        else if (cursorPosition < currentField.getLength() - 1) {
            System.out.println("Increment");
            incrementCursorPosition();

            if (currentField.getNode(cursorPosition - 1).getNodeType() == Constants.NodeType.Special) { // Move Cursor in Field
                System.out.println("Move Cursor in Field");
                SpecialNode specialNode = (SpecialNode)currentField.getNode(cursorPosition - 1);
                currentField = specialNode.getFirstChild();
                cursorPosition = 0;
            } else if (currentField.getNode(cursorPosition - 1).getNodeType() == Constants.NodeType.ExponentSpecial) {
                System.out.println("Move Cursor in Field");
                ExponentSpecialNode specialNode = (ExponentSpecialNode)currentField.getNode(cursorPosition - 1);
                currentField = specialNode.getChildField();
                cursorPosition = 0;
            } else if (currentField.getNode(cursorPosition - 1).getNodeType() == Constants.NodeType.SingleSpecial) {
                System.out.println("Move Cursor in Field");
                SingleSpecialNode specialNode = (SingleSpecialNode)currentField.getNode(cursorPosition - 1);
                currentField = specialNode.getChildField();
                cursorPosition = 0;
            }
        }
        System.out.println("End");
        renderAll();
    }

    public void moveCursorLeft() {
        System.out.println("Mov Cursor left");
        if (cursorPosition == 0 && currentField.hasParent()) {
            System.out.println("At Field Beginning");
            if (currentField.getParent().getNodeType() == Constants.NodeType.Special) {
                SpecialNode parentNode = (SpecialNode) currentField.getParent();
                if (currentField.equals(parentNode.getSecondChild())) { // Move Cursor to First Field
                    System.out.println("Move to First Field");
                    currentField = parentNode.getFirstChild();
                    cursorPosition = parentNode.getFirstChild().getLength() - 1;
                } else { // Move Cursor out of Field
                    System.out.println("Move out of Field");
                    currentField = parentNode.getParentField();
                    cursorPosition = parentNode.getParentField().getIndexOf(parentNode);
                }
            } else if (currentField.getParent().getNodeType() == Constants.NodeType.ExponentSpecial) {
                ExponentSpecialNode parentNode = (ExponentSpecialNode) currentField.getParent(); // Single Special Node wird noch nicht beachtet
                System.out.println("Move out of Field");
                currentField = parentNode.getParentField();
                cursorPosition = parentNode.getParentField().getIndexOf(parentNode);
            } else if (currentField.getParent().getNodeType() == Constants.NodeType.SingleSpecial) {
                SingleSpecialNode parentNode = (SingleSpecialNode) currentField.getParent(); // Single Special Node wird noch nicht beachtet
                System.out.println("Move out of Field");
                currentField = parentNode.getParentField();
                cursorPosition = parentNode.getParentField().getIndexOf(parentNode);
            }
        }
        else if (cursorPosition > 0) {
            System.out.println("Decrement");
            decrementCursorPosition();

            if (currentField.getNode(cursorPosition).getNodeType() == Constants.NodeType.Special) { // Move Cursor in Field
                System.out.println("Move Cursor in Field");
                SpecialNode specialNode = (SpecialNode)currentField.getNode(cursorPosition);
                currentField = specialNode.getSecondChild();
                cursorPosition = specialNode.getFirstChild().getLength() - 1;
            } else if (currentField.getNode(cursorPosition).getNodeType() == Constants.NodeType.ExponentSpecial) {
                System.out.println("Move Cursor in Field");
                ExponentSpecialNode specialNode = (ExponentSpecialNode)currentField.getNode(cursorPosition);
                currentField = specialNode.getChildField();
                cursorPosition = specialNode.getChildField().getLength() - 1;
            } else if (currentField.getNode(cursorPosition).getNodeType() == Constants.NodeType.SingleSpecial) {
                System.out.println("Move Cursor in Field");
                SingleSpecialNode singleSpecialNode = (SingleSpecialNode)currentField.getNode(cursorPosition);
                currentField = singleSpecialNode.getChildField();
                cursorPosition = singleSpecialNode.getChildField().getLength() - 1;
            }
        }
        System.out.println("End");
        renderAll();
    }

    public void setCursorPosition(Node clickedNode) {
        System.out.println("Set Cursor Position");
        currentField = clickedNode.getParentField();
        cursorPosition = clickedNode.getParentField().getIndexOf(clickedNode) + 1; // +1 damit man vor der Node ist auf die man Klickt
        renderAll();
    }

    public void calculate() {
        CopyUtility copyUtility = new CopyUtility();
        lastEquation = copyUtility.deepCopy(startField);
        reconnectParentFields(lastEquation);
        cleanAll(startField);

        currentField = startField;
        mainWindow.clearAll();
        renderAll();



        Equation equation = new Equation();
        System.out.println("--------Equation Start--------");
        double result = equation.calculate(startField);

        String textToShow;
        if (result % 1 == 0 && result < Long.MAX_VALUE) {
            textToShow = String.valueOf((long) result); // Zeigt "5"
        } else {
            textToShow = String.valueOf(result);      // Zeigt "5.5"
        }

        showsResult = true;
        currentResult = result;

        mainWindow.renderResult(textToShow);
        startField = lastEquation;
        currentField = startField;
        System.out.println("RESULT: " + result);
        Constants.lastResult = result;
    }

    private void reconnectParentFields(Field field) {
        for (int i = 0; i < field.getLength(); i++) {
            field.getNode(i).setParentField(field);

            if (field.getNode(i).getNodeType() == Constants.NodeType.Special) {
                SpecialNode specialNode = (SpecialNode) field.getNode(i);
                specialNode.getFirstChild().setParent(field.getNode(i));
                specialNode.getSecondChild().setParent(field.getNode(i));
                reconnectParentFields(specialNode.getFirstChild());
                reconnectParentFields(specialNode.getSecondChild());
            }
            else if (field.getNode(i).getNodeType() == Constants.NodeType.ExponentSpecial) {
                ExponentSpecialNode specialNode = (ExponentSpecialNode) field.getNode(i);
                specialNode.getChildField().setParent(field.getNode(i));
                specialNode.getValueField().setParent(field.getNode(i));
                reconnectParentFields(specialNode.getChildField());
                reconnectParentFields(specialNode.getValueField());
            } else if (field.getNode(i).getNodeType() == Constants.NodeType.SingleSpecial) {
                SingleSpecialNode specialNode = (SingleSpecialNode) field.getNode(i);
                specialNode.getChildField().setParent(field.getNode(i));
                reconnectParentFields(specialNode.getChildField());
            }
        }
    }

    private void cleanAll(Field field) {
        for (int i = 0; i < field.getLength(); i++) {
            if (field.getNode(i).getType() == Constants.Type.None) {
                field.getContent().remove(i);
            }
            else if (field.getNode(i).getNodeType() == Constants.NodeType.Special) {
                SpecialNode specialNode = (SpecialNode) field.getNode(i);
                cleanAll(specialNode.getFirstChild());
                cleanAll(specialNode.getSecondChild());
            }
            else if (field.getNode(i).getNodeType() == Constants.NodeType.ExponentSpecial) {
                ExponentSpecialNode specialNode = (ExponentSpecialNode) field.getNode(i);
                cleanAll(specialNode.getChildField());
                cleanAll(specialNode.getValueField());
            } else if (field.getNode(i).getNodeType() == Constants.NodeType.SingleSpecial) {
                SingleSpecialNode specialNode = (SingleSpecialNode) field.getNode(i);
                cleanAll(specialNode.getChildField());
            }
        }
    }

    private void renderAll() {
        showsResult = false;
        System.out.println("Render: " + cursorPosition);
        HBox renderNode = new HBox();
        renderNode.setAlignment(Pos.CENTER);
        for (int i = 0; i < startField.getLength(); i++) {
            javafx.scene.Node node = startField.getNode(i).render(currentField, cursorPosition, false, this);
            Node currentNode = startField.getNode(i);
            if (currentNode.getNodeType() == Constants.NodeType.Normal) node.setOnMouseClicked(e -> {
               setCursorPosition(currentNode);
            });
            renderNode.getChildren().add(node);
        }
        mainWindow.render(renderNode);
    }

    public void clearAll() {
        startField = new Field();
        cursorPosition = 0;
        currentField = startField;
        mainWindow.clearAll();
        renderAll();
    }

    public void deleteCurrent() {
        currentField.getContent().remove(cursorPosition - 1);
        decrementCursorPosition();
        renderAll();
    }
}

import java.util.ArrayList;

public class Equation {

    public double calculate(Field rootField) {
        System.out.println("-INPUT-");
        for (int i = 0; i < rootField.getLength(); i++) {
            System.out.println("I: " + rootField.getNode(i).getType() + " " + rootField.getNode(i).getValue());
        }

        rootField = combineValueNodes(rootField);
        rootField = combineExponents(rootField);
        rootField = placeMultiplicationNodes(rootField);
        rootField = replaceConstantsNodes(rootField);
        rootField = calculateSpecialNodes(rootField);
        rootField = invertNumbersWithNegativeNode(rootField);

        System.out.println("-NODES-");
        for (int i = 0; i < rootField.getLength(); i++) {
            System.out.println("N: " + rootField.getNode(i).getType() + " " + rootField.getNode(i).getValue());
        }

        ArrayList<Node> outputNodes = toReversePolishNotation(rootField);
        double result = calculatePolishNotation(outputNodes);

        if (result < 10) result = round(result, 5);
        else if (result < 100) result = round(result, 4);
        else if (result < Integer.MAX_VALUE) result = round(result, 3);
        else if (result < Long.MAX_VALUE) result = round(result, 1);
        return result;
    }

    // Zahl auf "places" Nachkommastellen runden
    private double round(double a, int places) {
        int factor = Math.powExact(10, places);
        double x = Math.round(a * factor);
        return x / factor;
    }

    // Verschiebt alle Nodes die, Teil eines Exponenten sein sollten in das zweite Feld der Exponenten-Node
    private Field combineExponents(Field rootField) {
        ArrayList<Node> newFieldNodes = new ArrayList<>();
        ExponentSpecialNode lastExponent = null;
        ArrayList<ExponentSpecialNode> lastExponents = new ArrayList<>();
        int bracketsCounter = 0;
        boolean isCurrentlyInExponent = false;

        for (int i = rootField.getLength() - 1; i >= 0; i--) {
            System.out.println(rootField.getNode(i));

            if (!isCurrentlyInExponent && rootField.getNode(i).getType() == Constants.Type.Power) {
                isCurrentlyInExponent = true;
                newFieldNodes.addFirst(rootField.getNode(i));
                lastExponent = (ExponentSpecialNode)newFieldNodes.getFirst();
                lastExponents.add(lastExponent);
                lastExponent.valueField.getContent().clear();
                continue;
            }

            if (isCurrentlyInExponent && rootField.getNode(i).getType() == Constants.Type.ClosingBracket) bracketsCounter++;
            if (isCurrentlyInExponent && rootField.getNode(i).getType() == Constants.Type.OpeningBracket) bracketsCounter--;

            if (isCurrentlyInExponent) {
                lastExponent.valueField.add(rootField.getNode(i), 0);
            } else {
                newFieldNodes.addFirst(rootField.getNode(i));
            }

            if (isCurrentlyInExponent && bracketsCounter == 0) isCurrentlyInExponent = false;
        }

        Field outputField = new Field();
        outputField.deleteEmptyNodes();
        outputField.getContent().addAll(newFieldNodes);
        return outputField;
    }

    // Prüft, ob es noch Exponenten gibt im übergebenen rootfield
    private boolean checkForExponents(Field rootfield) {
        for (int i = 0; i < rootfield.getLength(); i++) {
            if (rootfield.getNode(i).getType() == Constants.Type.Power) return true;
        }
        return false;
    }

    // Setzt Multiplication Nodes zwischen Value Nodes und Special Nodes sowie ConstantsNodes
    private Field placeMultiplicationNodes(Field rootfield) {
        ArrayList<Node> newFieldNodes = new ArrayList<>();
        for (int i = 0; i < rootfield.getLength(); i++) {
            if (i != 0 && (rootfield.getNode(i).getNodeType() == Constants.NodeType.Special || rootfield.getNode(i).getType() == Constants.Type.Constant) && rootfield.getNode(i - 1).getType() == Constants.Type.Value) {
                Node multiplicationNode = new MultiplicationNode(null);
                newFieldNodes.add(multiplicationNode);
            }
            newFieldNodes.add(rootfield.getNode(i));
        }

        Field outputField = new Field();
        outputField.deleteEmptyNodes();
        outputField.getContent().addAll(newFieldNodes);
        return outputField;
    }

    // Ersetzt die ConstantsNodes durch Value Nodes mit dem Wert der vorherigen ConstantsNode
    private Field replaceConstantsNodes(Field rootfield) {
        ArrayList<Node> newFieldNodes = new ArrayList<>();
        for (int i = 0; i < rootfield.getLength(); i++) {
            if (rootfield.getNode(i).getType() == Constants.Type.Constant) {
                Node valueNode = new ValueNode(rootfield.getNode(i).getValue() ,rootfield.getNode(i).getParentField());
                newFieldNodes.add(valueNode);
            } else {
                newFieldNodes.add(rootfield.getNode(i));
            }
        }

        Field outputField = new Field();
        outputField.deleteEmptyNodes();
        outputField.getContent().addAll(newFieldNodes);
        return outputField;
    }

    // Berechnet den Wert der Special Nodes und setzt ihn in die Gleichung ein
    private Field calculateSpecialNodes(Field rootfield) {
        ArrayList<Node> newFieldNodes = new ArrayList<>();
        for (int i = 0; i < rootfield.getLength(); i++) {
            if (rootfield.getNode(i).getNodeType() == Constants.NodeType.Normal) {
                newFieldNodes.add(rootfield.getNode(i));
            } else if (rootfield.getNode(i).getNodeType() == Constants.NodeType.Special) { // Special Nodes
                double value = 0;
                SpecialNode specialNode = (SpecialNode) rootfield.getNode(i);
                double num1 = calculate(specialNode.getFirstChild());
                double num2 = calculate(specialNode.getSecondChild());
                value = rootfield.getNode(i).getType() == Constants.Type.Fraction ? num1 / num2 : value; // Bruch
                value = rootfield.getNode(i).getType() == Constants.Type.Root ? Math.pow(num2, 1/num1) : value; // Wurzel
                System.out.println("VALUE: " + value + " NUM1: " + num1 + " NUM2: " + num2);
                Node valueNode = new ValueNode(value, null);
                newFieldNodes.add(valueNode);
            } else if (rootfield.getNode(i).getNodeType() == Constants.NodeType.ExponentSpecial) { // Exponenten
                double value = 0;
                ExponentSpecialNode exponentSpecialNode = (ExponentSpecialNode) rootfield.getNode(i);
                double num1 = calculate(exponentSpecialNode.getValueField());
                double num2 = calculate(exponentSpecialNode.getChildField());
                value = rootfield.getNode(i).getType() == Constants.Type.Power ? Math.pow(num1, num2) : value; // Exponent
                Node valueNode = new ValueNode(value, null);
                newFieldNodes.add(valueNode);
            } else if (rootfield.getNode(i).getNodeType() == Constants.NodeType.SingleSpecial) { // SingleSpecial Nodes
                double value = 0;
                SingleSpecialNode singleSpecialNode = (SingleSpecialNode) rootfield.getNode(i);
                double num = calculate(singleSpecialNode.getChildField());
                double numInRadians = Math.toRadians(num);
                System.out.println("SINGLE VALUE: " + value + " NUM1: " + num + " NUM2: " + numInRadians);
                value = rootfield.getNode(i).getType() == Constants.Type.Sine ? Math.sin(numInRadians) : value; // Sinus
                value = rootfield.getNode(i).getType() == Constants.Type.Cosine ? Math.cos(numInRadians) : value; // Sinus
                value = rootfield.getNode(i).getType() == Constants.Type.Tangent ? Math.tan(numInRadians) : value; // Sinus
                Node valueNode = new ValueNode(value, null);
                newFieldNodes.add(valueNode);
            }
        }

        Field outputField = new Field();
        outputField.deleteEmptyNodes();
        outputField.getContent().addAll(newFieldNodes);
        return outputField;
    }

    private Field combineValueNodes(Field rootfield) {
        ArrayList<Node> newFieldNodes = new ArrayList<>();
        ValueNode currentValueNode = null;
        int currentValueNodeIndex = 0;

        for (int i = 0; i < rootfield.getLength(); i++) {
            if (currentValueNode == null && rootfield.getNode(i).getType() == Constants.Type.Value) {
                currentValueNodeIndex = i;
                currentValueNode = (ValueNode) rootfield.getNode(i);
                continue;
            }
            else if (currentValueNode != null && rootfield.getNode(i).getType() != Constants.Type.Value && rootfield.getNode(i).getType() != Constants.Type.Point) {
                rootfield.getContent().set(currentValueNodeIndex, currentValueNode);
                Node valueNode = new ValueNode(currentValueNode.getValue(), null);
                newFieldNodes.add(valueNode);
                newFieldNodes.add(rootfield.getNode(i));
                currentValueNode = null;
                continue;
            }

            if (rootfield.getNode(i).getType() == Constants.Type.Value) {
                currentValueNode.setValue(currentValueNode.getValue() * 10 + (rootfield.getNode(i).getValue() / Math.pow(10, Math.max(currentValueNode.decimalPlaces - 1, 0))));
                rootfield.getContent().remove(i);
                i--;
            }

            if (rootfield.getNode(i).getType() == Constants.Type.Point) {
                currentValueNode.decimalPlaces++;
                rootfield.getContent().remove(i);
                i--;
            }
            else if (rootfield.getNode(i).getType() == Constants.Type.Value && currentValueNode.decimalPlaces > 0) {
                System.out.println("Divide");
                currentValueNode.setValue(currentValueNode.getValue() / 10);
                currentValueNode.decimalPlaces++;
            }

            System.out.println("IN:" + rootfield.getNode(i).getType() + " " + rootfield.getNode(i).getValue());

            if(rootfield.getNode(i).getType() != Constants.Type.Value && rootfield.getNode(i).getType() != Constants.Type.Point) {
                newFieldNodes.add(rootfield.getNode(i));
            }
        }
        if (currentValueNode != null) {
            Node valueNode = new ValueNode(currentValueNode.getValue(), null);
            newFieldNodes.add(valueNode);
        }

        Field outputField = new Field();
        outputField.deleteEmptyNodes();
        outputField.getContent().addAll(newFieldNodes);
        return outputField;
    }

    //
    private Field invertNumbersWithNegativeNode(Field rootfield) {
        ArrayList<Node> newFieldNodes = new ArrayList<>();

        for (int i = 0; i < rootfield.getLength(); i++) {
            if (i < rootfield.getLength() - 1 && rootfield.getNode(i).getType() == Constants.Type.Subtraction && rootfield.getNode(i + 1).getType() == Constants.Type.Value) {
                Node additionNode = new AdditionNode(null);
                if (i > 0) newFieldNodes.add(additionNode);

                rootfield.getNode(i + 1).setValue(rootfield.getNode(i + 1).getValue() * -1);
                newFieldNodes.add(rootfield.getNode(i + 1));

                i++; // Die aktuelle Value Node direkt überspringen damit sie nicht doppelt hinzugefügt wird
            }
            else newFieldNodes.add(rootfield.getNode(i));
        }

        Field outputField = new Field();
        outputField.deleteEmptyNodes();
        outputField.getContent().addAll(newFieldNodes);
        return outputField;
    }

    public ArrayList<Node> toReversePolishNotation(Field field) {
        ArrayList<Node> outputQueue = new ArrayList<>();
        ArrayList<Node> operatorStack = new ArrayList<>();
        ArrayList<Node> inputNodes = field.getContent();

        for (int i = 0; i < inputNodes.size(); i++) {
            if (inputNodes.get(i).getType() == Constants.Type.Value) { // Wenn die Node eine Value Node ist
                outputQueue.add(inputNodes.get(i));
            }
            else if (inputNodes.get(i).getNodeType() == Constants.NodeType.Normal) { // Wenn die Node eine Basic Node ist aber keine Value Node
                if (inputNodes.get(i).getType() == Constants.Type.OpeningBracket || inputNodes.get(i).getType() == Constants.Type.ClosingBracket) {
                    if (inputNodes.get(i).getType() == Constants.Type.OpeningBracket) operatorStack.add(inputNodes.get(i));

                    if (inputNodes.get(i).getType() == Constants.Type.ClosingBracket) {
                        while (!operatorStack.isEmpty() && operatorStack.getLast().getType() != Constants.Type.OpeningBracket) {
                            outputQueue.add(operatorStack.getLast());
                            operatorStack.removeLast();
                        }
                        if (!operatorStack.isEmpty()) {
                            operatorStack.removeLast();
                        }
                    }

                    continue;
                }
                if (!operatorStack.isEmpty()) {
                    while (!operatorStack.isEmpty() && operatorStack.getLast().getType() != Constants.Type.OpeningBracket && operatorStack.getLast().getPrecedence().ordinal() >= inputNodes.get(i).getPrecedence().ordinal()) {
                        outputQueue.add(operatorStack.getLast());
                        operatorStack.removeLast();
                    }
                }
                operatorStack.add(inputNodes.get(i));
            }
        }
        while (!operatorStack.isEmpty()) {
            outputQueue.add(operatorStack.getLast());
            operatorStack.removeLast();
        }

        return outputQueue;
    }

    public double calculatePolishNotation(ArrayList<Node> nodes) {
        ArrayList<Double> stack = new ArrayList<>();

        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getType() == Constants.Type.Value) {
                stack.add(nodes.get(i).getValue());
            }
            else if (!stack.isEmpty()){
                double x = stack.getLast();
                double y = stack.get(stack.size() - 2);
                Constants.Type operator = nodes.get(i).getType();

                double result = 0;

                if (operator == Constants.Type.Addition) result = y + x;
                else if (operator == Constants.Type.Subtraction) result = y - x;
                else if (operator == Constants.Type.Multiplication) result = y * x;
                else if (operator == Constants.Type.Division) result = y / x;

                stack.removeLast();
                stack.removeLast();
                stack.addLast(result);
            }
        }
        return stack.getFirst();
    }
}

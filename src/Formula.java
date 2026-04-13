import java.util.ArrayList;

public class Formula {

    // Genereller Berechnungsablauf
    public double calculate(Field rootField) {
        ArrayList<Field> splitFields = splitOperators(rootField);
        ArrayList<Node> finalNodes = new ArrayList<>();

        System.out.println("Print Final Nodes 2");
        System.out.println(splitFields.size());
        for (int i = 0; i < splitFields.size(); i++) {
            for (int j = 0; j < splitFields.get(i).getLength(); j++) {
                System.out.println(i + "; " + j + "; " + splitFields.get(i).getContent().get(j).getType());
            }
        }
        System.out.println("End Final Nodes 2");

        for (Field field : splitFields) {
            System.out.println("Splitfields 1");
            System.out.println(field.getLength());
            for (int i = 0; i < field.getLength(); i++) {
                System.out.println("SF: " + field.getNode(i).getType());
            }
            Node firstNode = null;
            Node lastNode = null;
            if (field.getContent().getFirst().getType() != Constants.Type.Value && field.getContent().getFirst().getType() != Constants.Type.OpeningBracket && field.getContent().getFirst().getType() != Constants.Type.ClosingBracket && field.getContent().getFirst().getNodeType() != Constants.NodeType.Special && field.getContent().getFirst().getNodeType() != Constants.NodeType.ExponentSpecial) {
                firstNode = field.getContent().getFirst();
                field.getContent().removeFirst();
            } else if (field.getContent().getLast().getType() != Constants.Type.Value && field.getContent().getLast().getType() != Constants.Type.OpeningBracket && field.getContent().getLast().getType() != Constants.Type.ClosingBracket && field.getContent().getLast().getNodeType() != Constants.NodeType.Special && field.getContent().getLast().getNodeType() != Constants.NodeType.ExponentSpecial) {
                lastNode = field.getContent().getLast();
                field.getContent().removeLast();
            }
            if (firstNode != null) finalNodes.add(firstNode);
            combineValueNodes(field);
            //field = addPowerNodes(field);
            ArrayList<Node> nodes = toPolishNotation(field);
            double value = calculatePolishNotation(nodes);
            ValueNode vn = new ValueNode();
            vn.setValue(value);
            finalNodes.add(vn);
            if (lastNode != null) finalNodes.add(lastNode);
        }

        System.out.println("Print Final Nodes");
        System.out.println(finalNodes.size());
        for (int i = 0; i < finalNodes.size(); i++) {
            System.out.println(finalNodes.get(i).getType());
        }
        System.out.println("End Final Nodes");
        Field finalField = new Field();
        finalField.getContent().clear();
        finalField.getContent().addAll(finalNodes);
        finalNodes = toPolishNotation(finalField);
        double result = calculatePolishNotation(finalNodes);
        return round(result, 8);
    }

    // Zahl auf "places" Nachkommastellen runden
    private double round(double a, int places) {
        double x = Math.round(a * Math.powExact(10, places));
        return x / Math.powExact(10, places);
    }

    // Splitet ein Feld in mehrere Felder an den Stellen wo eine Special Node ist damit man danach
    // mehrere Felder hat, die entweder eine Special Node beinhalten oder aus mehreren Normalen Nodes bestehen.
    // Fügt außerdem ein mal zwischen diese Felder ein, falls es eine Value Node und eine Special Node nebeneinander gibt.
    private ArrayList<Field> splitOperators(Field rootField) {
        ArrayList<Field> splitFields = new ArrayList<>();
        ArrayList<Node> nodeStack = new ArrayList<>();

        for (int i = 0; i < rootField.getLength(); i++) {
            if (rootField.getNode(i).getNodeType() == Constants.NodeType.Special) {
                Field tempField = new Field();
                tempField.clearContent();
                tempField.getContent().addAll(nodeStack);

                if (i > 0 && rootField.getContent().get(i - 1).getType() == Constants.Type.Value) {
                    System.out.println("Add Multiplication");
                    tempField.add(new MultiplicationNode(null));
                }

                if (tempField.getLength() > 0) splitFields.add(tempField);
                nodeStack.clear();
                nodeStack.add(rootField.getContent().get(i));
            } else {
                nodeStack.add(rootField.getContent().get(i));
            }
        }
        Field tempField = new Field();
        tempField.clearContent();
        tempField.getContent().addAll(nodeStack);
        splitFields.add(tempField);
        return splitFields;
    }

    // Verbindet benachbarte Value Nodes in eine einzige Value Node mit dem Wert von allen ursprünglich benachbarten Value Nodes.
    private void combineValueNodes(Field field) {
        System.out.println("Combine Value Nodes 1");
        System.out.println(field.getLength());
        for (int i = 0; i < field.getLength(); i++) {
            System.out.println("CVN: " + field.getNode(i).getType());
        }

        ValueNode currentValueNode = null;
        int currentValueNodeIndex = 0;

        for (int i = 0; i < field.getLength(); i++) {
            if (currentValueNode == null && field.getNode(i).getType() == Constants.Type.Value) {
                System.out.println("Set new Value Node");
                currentValueNodeIndex = i;
                currentValueNode = (ValueNode) field.getNode(i);
                continue;
            }
            else if (currentValueNode != null && field.getNode(i).getType() != Constants.Type.Value && field.getNode(i).getType() != Constants.Type.Point) {
                System.out.println("Reset Value Node");
                field.getContent().set(currentValueNodeIndex, currentValueNode);
                currentValueNode = null;
                continue;
            }

            if (field.getNode(i).getType() == Constants.Type.Value) {
                System.out.println("Add Number " + field.getNode(i).getValue() + " ; Current Val.: " + currentValueNode.getValue());
                currentValueNode.setValue(currentValueNode.getValue() * 10 + (field.getNode(i).getValue() / Math.pow(10, Math.max(currentValueNode.decimalPlaces - 1, 0))));
                field.getContent().remove(i);
                i--;
            }

            if (field.getNode(i).getType() == Constants.Type.Point) {
                System.out.println("Point");
                currentValueNode.decimalPlaces++;
                field.getContent().remove(i);
                i--;
            }
            else if (field.getNode(i).getType() == Constants.Type.Value && currentValueNode.decimalPlaces > 0) {
                System.out.println("Divide");
                currentValueNode.setValue(currentValueNode.getValue() / 10);
                currentValueNode.decimalPlaces++;
            }
        }

        System.out.println("Combine Value Nodes 2");
        System.out.println(field.getLength());
        for (int i = 0; i < field.getLength(); i++) {
            System.out.println("CVN: " + field.getNode(i).getType());
        }
    }

    private Field addPowerNodes(Field field) {
        ArrayList<Node> nodes = field.getContent();
        ArrayList<Node> innerNodeStack = new ArrayList<>();
        int exponentPosition = 0;

        boolean foundPower = false;
        boolean hadBrackets = false;

        int bracketCounter = 0;

        System.out.println("Power Node 1: ");
        System.out.println(field.getLength());
        for (int i = 0; i < field.getLength(); i++) {
            System.out.println("currentNode: " + field.getNode(i).getType());
        }

        for (int i = nodes.size() - 1; i >= 0; i--) {
            System.out.println("i: " + i);
            if (nodes.get(i).getType() == Constants.Type.Power && !foundPower) {
                foundPower = true;
                exponentPosition = i;
                continue;
            }

            if (nodes.get(i).getType() == Constants.Type.ClosingBracket && foundPower) {
                hadBrackets = true;
                bracketCounter++;
            }
            else if (nodes.get(i).getType() == Constants.Type.OpeningBracket && foundPower) bracketCounter--;

            if (foundPower) {
                innerNodeStack.add(nodes.get(i));
                nodes.remove(i);
                i++;
                exponentPosition--;
            }

            if (foundPower && bracketCounter == 0) break;
        }

        System.out.println("Power Node 2: ");
        System.out.println(field.getLength());
        for (int i = 0; i < nodes.size(); i++) {
            System.out.println("currentNode: " + nodes.get(i).getType());
        }

        System.out.println("Power Node 3: ");
        System.out.println(field.getLength());
        for (int i = 0; i < innerNodeStack.size(); i++) {
            System.out.println("currentNode: " + innerNodeStack.get(i).getType());
        }

        if (hadBrackets) {
            ExponentSpecialNode spn = null;
            spn.valueField = new Field(innerNodeStack);
            spn.valueField.deleteEmptyNodes();
            nodes.set(exponentPosition, spn);
            System.out.println("Single Special Node");
            for (int i = field.getLength() - 1; i >= 0; i--) {
                System.out.println("i" + i + " " + field.getNode(i).getType());
                if (field.getNode(i).getType() == Constants.Type.Power) {
                    spn = (ExponentSpecialNode)field.getNode(i);
                    System.out.println("Assign Signle Special Node");
                    break;
                }
            }

        }

        return new Field(nodes);
    }

    public ArrayList<Node> toPolishNotation(Field field) {
        ArrayList<Node> outputQueue = new ArrayList<>();
        ArrayList<Node> operatorStack = new ArrayList<>();
        ArrayList<Node> inputNodes = field.getContent();

        System.out.println("To Polish Notation: ");
        System.out.println(field.getLength());
        for (int i = 0; i < field.getLength(); i++) {
            System.out.println("currentNode: " + field.getNode(i).getType());
        }
        System.out.println("-----------");

        for (int i = 0; i < inputNodes.size(); i++) {
            if (inputNodes.get(i).getType() == Constants.Type.Value) { // Wenn die Node eine Value Node ist
                System.out.println("Value Node: " + inputNodes.get(i).getValue());
                outputQueue.add(inputNodes.get(i));
            }
            else if (inputNodes.get(i).getNodeType() == Constants.NodeType.Normal) { // Wenn die Node eine Basic Node ist aber keine Value Node
                System.out.println("Normale Node: " + inputNodes.get(i).getType());
                if (inputNodes.get(i).getType() == Constants.Type.OpeningBracket || inputNodes.get(i).getType() == Constants.Type.ClosingBracket) {
                    System.out.println("Bracket Node");
                    if (inputNodes.get(i).getType() == Constants.Type.OpeningBracket) operatorStack.add(inputNodes.get(i));

                    if (inputNodes.get(i).getType() == Constants.Type.ClosingBracket) {
                        while (!operatorStack.isEmpty() && operatorStack.getLast().getType() != Constants.Type.OpeningBracket) {
                            System.out.println("Current Last Item on Stack: " + operatorStack.getLast().getType());
                            outputQueue.add(operatorStack.getLast());
                            operatorStack.removeLast();
                        }
                        if (!operatorStack.isEmpty()) {
                            System.out.println("Last Item on Stack: " + operatorStack.getLast().getType());
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
            else if (inputNodes.get(i).getNodeType() == Constants.NodeType.Special) { // Wenn die Node eine Special Node ist
                System.out.println("Special Node");
                SpecialNode specialNode = (SpecialNode)inputNodes.get(i);
                double x = calculate(specialNode.getFirstChild());
                double y = calculate(specialNode.getSecondChild());
                ValueNode valueNode = new ValueNode();
                if (inputNodes.get(i).getType() == Constants.Type.Fraction) valueNode.setValue(x / y);
                if (inputNodes.get(i).getType() == Constants.Type.Root) valueNode.setValue(Math.pow(y, 1/x));
                outputQueue.add(valueNode);
            } else if (inputNodes.get(i).getNodeType() == Constants.NodeType.ExponentSpecial) { // Wenn die Node eine Special Node ist
                System.out.println("Single Special Node");
                ExponentSpecialNode exponentSpecialNode = (ExponentSpecialNode)inputNodes.get(i);

                System.out.println("Child Field");
                for (int j = 0; j < exponentSpecialNode.childField.getLength(); j++) {
                    System.out.println("CF" + exponentSpecialNode.childField.getNode(j).getType());
                }

                System.out.println("Value Field");
                for (int j = 0; j < exponentSpecialNode.valueField.getLength(); j++) {
                    System.out.println("VF" + exponentSpecialNode.valueField.getNode(j).getType());
                }

                double x = calculate(exponentSpecialNode.getChildField());
                double y = calculate(exponentSpecialNode.getValueField());
                ValueNode valueNode = new ValueNode();
                if (inputNodes.get(i).getType() == Constants.Type.Exponent) valueNode.setValue(Math.pow(y, x));
                outputQueue.add(valueNode);
            }
        }
        while (!operatorStack.isEmpty()) {
            outputQueue.add(operatorStack.getLast());
            operatorStack.removeLast();
        }

        System.out.println("-----------");
        System.out.println("Polish Notation Output: ");
        System.out.println(outputQueue.size());
        for (int i = 0; i < outputQueue.size(); i++) {
            System.out.println("output Node: " + outputQueue.get(i).getType());
        }
        return outputQueue;
    }

    public double calculatePolishNotation(ArrayList<Node> nodes) {
        ArrayList<Double> stack = new ArrayList<>();
        System.out.println("Calculate");
        System.out.println(nodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            System.out.print(nodes.get(i).getType());
            if (nodes.get(i).getType() == Constants.Type.Value) {
                System.out.println(" " + nodes.get(i).getValue());
            }
            else System.out.println();
        }

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

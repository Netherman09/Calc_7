import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public abstract class Node {
    private Constants.Precedence precedence;
    private Constants.Type type;
    private double value;
    private Constants.NodeType nodeType;
    private transient Field parentField;
    public transient boolean wasMouseListenerSet = false;

    Node(Constants.Precedence precedence, Constants.Type type, Constants.NodeType nodeType, Field parentField) {
        this.precedence = precedence;
        this.type = type;
        this.nodeType = nodeType;
        this.parentField = parentField;
    }

    public void setMouseListener() {
        wasMouseListenerSet = true;
    }

    public boolean getWasMouseListenerSet() {
        return wasMouseListenerSet;
    }

    public Constants.Precedence getPrecedence() {
        return precedence;
    }

    public Constants.Type getType() {
        return type;
    }

    public Constants.NodeType getNodeType() {
        return nodeType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Field getParentField() {
        return parentField;
    }
    public void setParentField(Field parentField) {
        this.parentField = parentField;
    }

    public abstract javafx.scene.Node render(Field currentField, int position, boolean renderSmaller, ControlFormula controlFormula);

    public boolean hasRightNode() {
        int currentIndex = parentField.getIndexOf(this);
        int nextIndex = currentIndex + 1;
        return nextIndex < parentField.getLength();
    }

    public Node getRightNode() {
        int currentIndex = parentField.getIndexOf(this);
        int nextIndex = currentIndex + 1;
        if (nextIndex < parentField.getLength()) {
            return parentField.getNode(nextIndex);
        } else {
            return null;
        }
    }

    public javafx.scene.Node drawCursor() {
        Line line = new Line(0, 0, 0, 22);
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(1);
        return line;
    }
}

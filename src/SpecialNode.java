import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public abstract class SpecialNode extends Node{
    Field[] childFields = new Field[2];
    SpecialNode(Constants.Precedence precedence, Constants.Type type, Field parentField) {
        super(precedence, type, Constants.NodeType.Special, parentField);
        childFields[0] = new Field();
        childFields[0].setParent(this);
        childFields[1] = new Field();
        childFields[1].setParent(this);
    }

    public Field getFirstChild() {
        return childFields[0];
    }

    public Field getSecondChild() {
        return childFields[1];
    }

    public abstract javafx.scene.Node render(Field currentField, int position, boolean renderSmaller, ControlFormula controlFormula);
}

public abstract class ExponentSpecialNode extends Node {
    Field childField;
    Field valueField;
    ExponentSpecialNode(Constants.Precedence precedence, Constants.Type type, Field parentField) {
        super(precedence, type, Constants.NodeType.ExponentSpecial, parentField);
        childField = new Field();
        childField.setParent(this);
        valueField = new Field();
        valueField.setParent(this);
    }

    public Field getChildField() {
        return childField;
    }
    public Field getValueField() {
        return valueField;
    }

    public abstract javafx.scene.Node render(Field currentField, int position, boolean renderSmaller);
}

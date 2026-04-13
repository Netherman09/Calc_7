public abstract class SingleSpecialNode extends Node {
    Field childField;
    SingleSpecialNode(Constants.Precedence precedence, Constants.Type type, Field parentField) {
        super(precedence, type, Constants.NodeType.SingleSpecial, parentField);
        childField = new Field();
        childField.setParent(this);
    }

    public Field getChildField() {
        return childField;
    }

    public abstract javafx.scene.Node render(Field currentField, int position, boolean renderSmaller);
}

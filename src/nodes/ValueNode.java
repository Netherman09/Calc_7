package nodes;

import calculation.ControlFormula;
import calculation.Field;
import core.Constants;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ValueNode extends Node {
    public int decimalPlaces = 0;
    public ValueNode() {
        super(Constants.Precedence.LeftToRight, Constants.Type.Value, Constants.NodeType.Normal, null);
    }

    public ValueNode(double value, Field parentField) {
        super(Constants.Precedence.LeftToRight, Constants.Type.Value, Constants.NodeType.Normal, parentField);
        setValue(value);
    }

    @Override
    public javafx.scene.Node render(Field currentField, int position, boolean renderSmaller, ControlFormula controlFormula) {
        HBox node = new HBox();
        node.setAlignment(Pos.CENTER);
        String textToShow;
        if (getValue() % 1 == 0) {
            textToShow = String.valueOf((int) getValue()); // Zeigt "5"
        } else {
            textToShow = String.valueOf(getValue());      // Zeigt "5.5"
        }
        Text text = new Text(textToShow);
        text.getStyleClass().add("calculation_text");
        if (renderSmaller) text.setStyle("-fx-font-size: 15px;");
        if (getParentField().getIndexOf(this) == position && getParentField().equals(currentField)) {
            javafx.scene.Node line = drawCursor();
            node.getChildren().addAll(line, text);
        } else {
            node.getChildren().add(text);
        }

        return node;
    }
}



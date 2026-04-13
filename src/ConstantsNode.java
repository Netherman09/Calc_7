import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ConstantsNode extends Node {
    int decimalPlaces = 0;
    String symbol;
    ConstantsNode() {
        super(Constants.Precedence.LeftToRight, Constants.Type.Constant, Constants.NodeType.Normal, null);
    }

    ConstantsNode(double value, String symbol, Field parentField) {
        super(Constants.Precedence.LeftToRight, Constants.Type.Constant, Constants.NodeType.Normal, parentField);
        setValue(value);
        this.symbol = symbol;
    }

    @Override
    public javafx.scene.Node render(Field currentField, int position, boolean renderSmaller) {
        HBox node = new HBox();
        node.setAlignment(Pos.CENTER);
        Text text = new Text(symbol);
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



import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class VarNode extends Node {
    int decimalPlaces = 0;
    String symbol;
    Constants.VarType varType;
    VarNode() {
        super(Constants.Precedence.LeftToRight, Constants.Type.Var, Constants.NodeType.Normal, null);
    }

    VarNode(String symbol, Constants.VarType varType, Field parentField) {
        super(Constants.Precedence.LeftToRight, Constants.Type.Var, Constants.NodeType.Normal, parentField);
        this.symbol = symbol;
        this.varType = varType;
    }

    public Constants.VarType getVarType() {
        return varType;
    }

    @Override
    public javafx.scene.Node render(Field currentField, int position, boolean renderSmaller, ControlFormula controlFormula) {
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



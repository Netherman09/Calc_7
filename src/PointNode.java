import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class PointNode extends Node{
    PointNode(Field parentField) {
        super(Constants.Precedence.LeftToRight, Constants.Type.Point, Constants.NodeType.Normal, parentField);
    }

    @Override
    public javafx.scene.Node render(Field currentField, int position, boolean renderSmaller, ControlFormula controlFormula) {
        HBox node = new HBox();
        node.setAlignment(Pos.CENTER);
        Text text = new Text(".");
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

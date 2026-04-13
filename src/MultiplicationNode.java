import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MultiplicationNode extends Node{
    MultiplicationNode(Field parentField) {
        super(Constants.Precedence.Points, Constants.Type.Multiplication, Constants.NodeType.Normal, parentField);
    }

    @Override
    public javafx.scene.Node render(Field currentField, int position, boolean renderSmaller) {
        HBox node = new HBox();
        node.setAlignment(Pos.CENTER);
        Text text = new Text("×");
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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class TangentNode extends SingleSpecialNode {
    TangentNode(Field parentField) {
        super(Constants.Precedence.Points, Constants.Type.Tangent, parentField);
    }

    @Override
    public javafx.scene.Node render(Field currentField, int position, boolean renderSmaller, ControlFormula controlFormula) {
        HBox node = new HBox();
        node.setAlignment(Pos.CENTER);
        Text text = new Text("tan(");
        text.getStyleClass().add("calculation_text");

        HBox childNode = new HBox();

        for (Node child : getChildField().getContent()) {
            if (child == null) continue;
            if (getChildField().equals(currentField) && getChildField().getIndexOf(child) == position) childNode.getChildren().add(drawCursor());
            javafx.scene.Node javaFXNode = child.render(currentField, position, renderSmaller, controlFormula);
            childNode.getChildren().add(javaFXNode);
            if (child.getNodeType() == Constants.NodeType.Normal) javaFXNode.setOnMouseClicked(e -> {
                controlFormula.setCursorPosition(child);
            });
        }
        childNode.setAlignment(Pos.CENTER);
        childNode.setMinWidth(Region.USE_PREF_SIZE);

        Text closingText = new Text(")");
        closingText.getStyleClass().add("calculation_text");

        if (renderSmaller) text.setStyle("-fx-font-size: 15px;");
        if (getParentField().getIndexOf(this) == position && getParentField().equals(currentField)) {
            javafx.scene.Node line = drawCursor();
            node.getChildren().addAll(line, text, childNode, closingText);
        } else {
            node.getChildren().addAll(text, childNode, closingText);
        }

        return node;
    }
}

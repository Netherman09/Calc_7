import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class FractionNode extends SpecialNode{
    FractionNode(Field parentField) {
        super(Constants.Precedence.Points, Constants.Type.Fraction, parentField);
    }

    @Override
    public javafx.scene.Node render(Field currentField, int position, boolean renderSmaller, ControlFormula controlFormula) {
        HBox fullRenderNode = new HBox(2);
        VBox renderNode = new VBox(2);
        renderNode.setAlignment(javafx.geometry.Pos.CENTER);
        renderNode.setMinWidth(Region.USE_PREF_SIZE);
        renderNode.setPrefWidth(Region.USE_COMPUTED_SIZE);
        renderNode.setMaxWidth(Region.USE_PREF_SIZE);



        HBox firstNode = new HBox();

        for (Node node : getFirstChild().getContent()) {
            if (node == null) continue;
            if (getFirstChild().equals(currentField) && getFirstChild().getIndexOf(node) == position) firstNode.getChildren().add(drawCursor());
            javafx.scene.Node javaFXNode = node.render(currentField, position, renderSmaller, controlFormula);
            firstNode.getChildren().add(javaFXNode);
            if (node.getNodeType() == Constants.NodeType.Normal) javaFXNode.setOnMouseClicked(e -> {
                controlFormula.setCursorPosition(node);
            });
        }
        firstNode.setAlignment(Pos.CENTER);
        firstNode.setMinWidth(Region.USE_PREF_SIZE);

        HBox secondNode = new HBox();
        for (Node node : getSecondChild().getContent()) {
            if (node == null) continue;
            if (getSecondChild().equals(currentField) && getSecondChild().getIndexOf(node) == position) secondNode.getChildren().add(drawCursor());
            javafx.scene.Node javaFXNode = node.render(currentField, position, renderSmaller, controlFormula);
            secondNode.getChildren().add(javaFXNode);
            if (node.getNodeType() == Constants.NodeType.Normal) javaFXNode.setOnMouseClicked(e -> {
                controlFormula.setCursorPosition(node);
            });
        }
        secondNode.setAlignment(Pos.CENTER);
        secondNode.setMinWidth(Region.USE_PREF_SIZE);


        javafx.scene.shape.Line line = new javafx.scene.shape.Line(0, 0, 20, 0);
        line.setStroke(Color.WHITE);

        // Trick: Strichbreite an das breiteste Element anpassen
        renderNode.widthProperty().addListener((obs, oldV, newV) -> line.setEndX(newV.doubleValue() - 1));

        renderNode.getChildren().addAll(firstNode, line, secondNode);
        fullRenderNode.setAlignment(Pos.CENTER);
        if (getParentField().equals(currentField) && getParentField().getIndexOf(this) == position) fullRenderNode.getChildren().addAll(drawCursor(), renderNode);
        else fullRenderNode.getChildren().addAll(renderNode);

        return fullRenderNode;
    }
}

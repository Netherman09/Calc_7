import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class PowerNode extends ExponentSpecialNode {

    PowerNode(Field parentField) {
        super(Constants.Precedence.Points, Constants.Type.Power, parentField);
    }

    @Override
    public javafx.scene.Node render(Field currentField, int position, boolean renderSmaller) {
        HBox fullRenderNode = new HBox(2);
        fullRenderNode.setAlignment(javafx.geometry.Pos.CENTER);
        fullRenderNode.setMinWidth(Region.USE_PREF_SIZE);
        fullRenderNode.setPrefWidth(Region.USE_COMPUTED_SIZE);
        fullRenderNode.setMaxWidth(Region.USE_PREF_SIZE);

        HBox firstNode = new HBox();
        for (Node node : getChildField().getContent()) {
            if (node == null) continue;
            if (getChildField().equals(currentField) && getChildField().getIndexOf(node) == position) firstNode.getChildren().add(drawCursor());
            firstNode.getChildren().add(node.render(currentField, position, true));
        }
        firstNode.setAlignment(Pos.CENTER);
        firstNode.setMinWidth(Region.USE_PREF_SIZE);

        fullRenderNode.setAlignment(Pos.CENTER);
        fullRenderNode.getChildren().add(firstNode);
        if (getParentField().equals(currentField) && getParentField().getIndexOf(this) == position) fullRenderNode.getChildren().addAll(drawCursor());

        HBox.setMargin(fullRenderNode, new Insets(0, 0, 20, 0));

        return fullRenderNode;
    }

}

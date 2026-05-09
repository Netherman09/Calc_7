package nodes;

import calculation.ControlFormula;
import calculation.Field;
import core.Constants;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;

public class LogaritmNode extends SpecialNode{
    public LogaritmNode(Field parentField) {
        super(Constants.Precedence.Points, Constants.Type.Logaritm, parentField);
    }

    public javafx.scene.Node render(Field currentField, int position, boolean renderSmaller, ControlFormula controlFormula) {
        HBox fullRenderNode = new HBox();
        fullRenderNode.setAlignment(Pos.BOTTOM_LEFT);
        fullRenderNode.setMinWidth(Region.USE_PREF_SIZE);
        fullRenderNode.setPrefWidth(Region.USE_COMPUTED_SIZE);
        fullRenderNode.setMaxWidth(Region.USE_PREF_SIZE);

        Text text = new Text("log");
        text.getStyleClass().add("calculation_text");

        // 1. Exponent (Wurzelgrad) rendern (z.B. die "3" bei der 3. Wurzel)
        HBox firstNode = new HBox();
        for (Node node : getFirstChild().getContent()) {
            if (node == null) continue;
            if (getFirstChild().equals(currentField) && getFirstChild().getIndexOf(node) == position) {
                //firstNode.getChildren().add(drawCursor());
            }
            javafx.scene.Node javaFXNode = node.render(currentField, position, true, controlFormula);
            firstNode.getChildren().add(javaFXNode);
            if (node.getNodeType() == Constants.NodeType.Normal) javaFXNode.setOnMouseClicked(e -> {
                controlFormula.setCursorPosition(node);
            });
        }
        firstNode.setAlignment(Pos.BOTTOM_LEFT);
        firstNode.setMinWidth(Region.USE_PREF_SIZE);

        // 2. Radikand (Inhalt unter der Wurzel) rendern
        HBox secondNode = new HBox();
        for (Node node : getSecondChild().getContent()) {
            if (node == null) continue;
            if (getSecondChild().equals(currentField) && getSecondChild().getIndexOf(node) == position) {
                secondNode.getChildren().add(drawCursor());
            }
            javafx.scene.Node javaFXNode = node.render(currentField, position, renderSmaller, controlFormula);
            secondNode.getChildren().add(javaFXNode);
            if (node.getNodeType() == Constants.NodeType.Normal) javaFXNode.setOnMouseClicked(e -> {
                controlFormula.setCursorPosition(node);
            });
        }
        // 2. Radikand (Inhalt unter der Wurzel) rendern
        secondNode.setAlignment(Pos.CENTER);

        // WICHTIG: Verhindert, dass die HBox höher wird als der Text darin
        secondNode.setFillHeight(false);
        secondNode.setMaxHeight(Region.USE_PREF_SIZE);
        secondNode.setMinHeight(Region.USE_PREF_SIZE);


        StackPane radicandStack = new StackPane();
        radicandStack.setAlignment(Pos.BOTTOM_LEFT);
        // Verhindert, dass der Stack durch das Eltern-Element gestreckt wird
        radicandStack.setMaxHeight(Region.USE_PREF_SIZE);
        radicandStack.getChildren().addAll(secondNode);

        // Exponenten einrücken
        if (!firstNode.getChildren().isEmpty()) {
            HBox.setMargin(firstNode, new Insets(0,   0.2, 0, 0));
            // Der Exponent sitzt nun relativ zur echten Höhe der Wurzel
            firstNode.translateYProperty().bind(secondNode.heightProperty().multiply(0.3));
        }

        // Die äußere nodes.Node zusammenbauen
        fullRenderNode.setAlignment(Pos.CENTER); // Wichtig für die Grundlinie
        fullRenderNode.setFillHeight(false); // Verhindert, dass alles wieder aufgebläht wird

        if (getParentField().equals(currentField) && getParentField().getIndexOf(this) == position) {
            fullRenderNode.getChildren().add(drawCursor());
        }
        fullRenderNode.getChildren().addAll(text, firstNode, radicandStack);
        HBox.setMargin(fullRenderNode, new Insets(0, 0, 0, 5));

        return fullRenderNode;
    }
}

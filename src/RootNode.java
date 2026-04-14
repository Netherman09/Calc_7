import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class RootNode extends SpecialNode{
    RootNode(Field parentField) {
        super(Constants.Precedence.Points, Constants.Type.Root, parentField);
    }

    public javafx.scene.Node render(Field currentField, int position, boolean renderSmaller, ControlFormula controlFormula) {
        HBox fullRenderNode = new HBox();
        fullRenderNode.setAlignment(Pos.TOP_LEFT);
        fullRenderNode.setMinWidth(Region.USE_PREF_SIZE);
        fullRenderNode.setPrefWidth(Region.USE_COMPUTED_SIZE);
        fullRenderNode.setMaxWidth(Region.USE_PREF_SIZE);

        // 1. Exponent (Wurzelgrad) rendern (z.B. die "3" bei der 3. Wurzel)
        HBox firstNode = new HBox();
        for (Node node : getFirstChild().getContent()) {
            if (node == null) continue;
            if (getFirstChild().equals(currentField) && getFirstChild().getIndexOf(node) == position) {
                firstNode.getChildren().add(drawCursor());
            }
            javafx.scene.Node javaFXNode = node.render(currentField, position, renderSmaller, controlFormula);
            firstNode.getChildren().add(javaFXNode);
            if (node.getNodeType() == Constants.NodeType.Normal) javaFXNode.setOnMouseClicked(e -> {
                controlFormula.setCursorPosition(node);
            });
        }
        firstNode.setAlignment(Pos.BOTTOM_CENTER);
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

        // --- NEUE WURZEL-LOGIK ---
        double tickWidth = renderSmaller ? 12.0 : 18.0;
        double topPadding = renderSmaller ? 2.0 : 3.0; // Etwas weniger Padding für engere Optik

        // Padding so setzen, dass der Text Platz für das Häkchen hat
        secondNode.setPadding(new Insets(topPadding, 4, 1, tickWidth + 2));

        Path rootPath = new Path();
        rootPath.setStroke(Color.WHITE);
        rootPath.setStrokeWidth(1.2); // Etwas dünner sieht oft edler aus
        rootPath.setStrokeLineJoin(StrokeLineJoin.ROUND);
        rootPath.setStrokeLineCap(StrokeLineCap.ROUND);
        rootPath.setManaged(false);

        // Bindings (bleiben gleich, reagieren jetzt aber auf die korrekte kleine Höhe)
        MoveTo start = new MoveTo();
        start.setX(0);
        start.yProperty().bind(secondNode.heightProperty().multiply(0.6));

        LineTo bottom = new LineTo();
        bottom.setX(tickWidth * 0.5);
        bottom.yProperty().bind(secondNode.heightProperty());

        LineTo top = new LineTo();
        top.setX(tickWidth);
        top.setY(1); // 1px Puffer nach oben

        LineTo overline = new LineTo();
        overline.xProperty().bind(secondNode.widthProperty());
        overline.setY(1);

        rootPath.getElements().addAll(start, bottom, top, overline);

        StackPane radicandStack = new StackPane();
        radicandStack.setAlignment(Pos.TOP_LEFT);
        // Verhindert, dass der Stack durch das Eltern-Element gestreckt wird
        radicandStack.setMaxHeight(Region.USE_PREF_SIZE);
        radicandStack.getChildren().addAll(rootPath, secondNode);

        // Exponenten einrücken
        if (!firstNode.getChildren().isEmpty()) {
            HBox.setMargin(firstNode, new Insets(0, -tickWidth * 0.4, 0, 0));
            // Der Exponent sitzt nun relativ zur echten Höhe der Wurzel
            firstNode.translateYProperty().bind(secondNode.heightProperty().multiply(-0.2));
        }

        // Die äußere Node zusammenbauen
        fullRenderNode.setAlignment(Pos.CENTER); // Wichtig für die Grundlinie
        fullRenderNode.setFillHeight(false); // Verhindert, dass alles wieder aufgebläht wird

        if (getParentField().equals(currentField) && getParentField().getIndexOf(this) == position) {
            fullRenderNode.getChildren().add(drawCursor());
        }
        fullRenderNode.getChildren().addAll(firstNode, radicandStack);
        HBox.setMargin(fullRenderNode, new Insets(0, 0, 0, 5));

        return fullRenderNode;
    }
}

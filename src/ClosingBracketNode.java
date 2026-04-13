import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ClosingBracketNode extends Node {
    ClosingBracketNode(Field parentField) {
        super(Constants.Precedence.Brackets, Constants.Type.ClosingBracket, Constants.NodeType.Normal, parentField);
    }

    @Override
    public javafx.scene.Node render(Field currentField, int position, boolean renderSmaller, ControlFormula controlFormula) {
        HBox node = new HBox();
        node.setAlignment(Pos.CENTER);
        Text text = new Text(")");
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

    public ArrayList<Node> getEnclosedNodes() {
        int seenNodes = 0;
        int ownPosition = getParentField().getIndexOf(this);
        ArrayList<Node> enclosedNodes = new ArrayList<>();
        System.out.println("Enclosed Nodes");
        for (int i = ownPosition - 1; i >= 0; i--) {
            System.out.println("EN: " + getParentField().getNode(i).getType());
            enclosedNodes.add(0, getParentField().getNode(i));
            if (getParentField().getNode(i).getType() == Constants.Type.ClosingBracket) seenNodes++;
            if (getParentField().getNode(i).getType() == Constants.Type.OpeningBracket && seenNodes == 0) return enclosedNodes;
            else if (getParentField().getNode(i).getType() == Constants.Type.OpeningBracket) seenNodes--;
        }
        System.out.println("Enclosed Nodes Not Found!");
        return enclosedNodes;
    }
}

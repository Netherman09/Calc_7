import java.util.ArrayList;
import java.util.Arrays;

public class Field {
    private ArrayList<Node> content;
    Node parent;

    Field(Node... nodes) {
        parent = null;
        content = new ArrayList<>();
        EmptyNode emptyNode = new EmptyNode(this);
        content.add(emptyNode);
        this.addAll(nodes);
    }

    Field(ArrayList<Node> nodes) {
        parent = null;
        content = new ArrayList<>();
        EmptyNode emptyNode = new EmptyNode(this);
        content.add(emptyNode);
        for (Node node : nodes) {
            this.add(node);
        }
    }

    public void deleteEmptyNodes() {
        content.removeIf(node -> node.getType() == Constants.Type.None);
    }

    public void add(Node newNode) {
        content.add(newNode);
    }

    public void add(Node newNode, int position) {
        if (content.size() > position && content.get(position).getType() == Constants.Type.None) {
            content.set(position, newNode);
            EmptyNode emptyNode = new EmptyNode(this);
            content.add(emptyNode);
        }
        else content.add(position, newNode);
    }

    public Node getParent() {
        return parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void addAll(Node... newNodes) {
        content.addAll(Arrays.asList(newNodes));
    }

    public int getLength() {
        return content.size();
    }

    public int getIndexOf(Node node) {
        return content.indexOf(node);
    }

    public ArrayList<Node> getContent() {
        return content;
    }

    public void clearContent() {
        content.clear();
    }

    public Node getNode(int index) {
        return content.get(index);
    }
}

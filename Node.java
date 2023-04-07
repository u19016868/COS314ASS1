import java.util.ArrayList;

public class Node {
    public Boolean visted;
    public ArrayList<Bin> data;
    public int weight;
    public ArrayList<Node> neighbours;

    public Node(ArrayList<Bin> data) {
        visted = false;
        this.data = data;
        this.weight = data.size();
        this.neighbours = null;
    }

    public void add(Node node) {
        if (this.neighbours == null) {
            this.neighbours = new ArrayList<>();
        }
        this.neighbours.add(node);
    }
}

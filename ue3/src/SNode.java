import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

public class SNode {
    public boolean tagged = false;
    public LinkedList<SNode> children = new LinkedList<>();
    public final char id;

    public SNode(char id0) {
        id = id0;
    }

    //adds child nodes
    public void cs(SNode... children0) {
        children.addAll(Arrays.asList(children0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SNode)) return false;
        SNode node = (SNode) o;
        return id == node.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
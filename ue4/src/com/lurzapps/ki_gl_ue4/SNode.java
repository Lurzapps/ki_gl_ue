package com.lurzapps.ki_gl_ue4;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SNode {
    boolean tagged = false,
            goal = false;
    List<SNode> children = new LinkedList<>();
    double cost = 1;

    int x, y;

    public SNode(int xx, int yy) {
        x = xx;
        y = yy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SNode)) return false;
        SNode sNode = (SNode) o;
        return x == sNode.x &&
                y == sNode.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", x, y);
    }
}

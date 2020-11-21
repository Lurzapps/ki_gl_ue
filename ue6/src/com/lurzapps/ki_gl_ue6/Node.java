package com.lurzapps.ki_gl_ue6;

import java.util.LinkedList;
import java.util.List;

public class Node {
    double value = 0.0;
    List<Node> children = new LinkedList<>();
    boolean leaf = false;

    //end node with heuristics
    public Node(double d) {
        leaf = true;
        value = d;
    }

    //node with children, standard
    public Node() {

    }
}

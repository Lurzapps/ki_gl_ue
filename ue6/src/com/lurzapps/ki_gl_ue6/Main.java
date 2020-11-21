package com.lurzapps.ki_gl_ue6;

import java.util.*;

import static java.lang.System.out;

public class Main {

    private static final double INF = 999999.0;
    private static final boolean MAX = true, MIN = false;
    private static final int MAX_DEPTH = 4;

    private static final int[] leafHeuristicsArray =  {7, 6, 2, 8, 8, 7, 2, 5, 1, 7, 7, 8, 8, 8, 2, 3};
    private static final List<Node> leafHeuristics = new LinkedList<>();

    public static void main(String[] args) {
        Node root = new Node();

        //generate tree with end values recursively
        for(int h : leafHeuristicsArray)
            leafHeuristics.add(new Node(h));
        generateTree(root, 1);

        //execute alpha beta
        double output = alphaBeta(root, -INF, INF, MAX);
        out.println("output: " + output);
    }

    private static void generateTree(Node root, int depth) {
        if(depth == MAX_DEPTH) {
            for(int i = 0; i < 2; i++)
                root.children.add(leafHeuristics.remove(0));
        } else {
            for(int i = 0; i < 2; i++) {
                root.children.add(new Node());
                generateTree(root, depth + 1);
            }
        }
    }

    private static double alphaBeta(Node node, double alpha, double beta, boolean player) {
        if (node.leaf) {
            return node.value; //heuristics
        }

        if (player == MAX) {
            for (Node child : node.children) {
                double v = alphaBeta(child, alpha, beta, MIN);
                alpha = Math.max(alpha, v);

                if(alpha >= beta) {
                    out.println("exit because alpha >= beta | " + alpha + " >= " + beta);
                    break;
                }
            }
            return alpha;
        } else {
            for (Node child : node.children) {
                double v = alphaBeta(child, alpha, beta, MAX);
                alpha = Math.min(beta, v);
                if(alpha >= beta) {
                    out.println("exit because alpha >= beta | " + alpha + " >= " + beta);
                    break;
                }
            }
            return beta;
        }
    }
}

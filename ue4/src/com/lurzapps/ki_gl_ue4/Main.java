package com.lurzapps.ki_gl_ue4;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static java.lang.System.out;

public class Main {

    static SNode[][] ns;
    static SNode startNode, goalNode;
    static boolean defaultHeuristics = false;

    public static void main(String[] args) {
        prepareNodes();

        out.println("ucs");
        printTableLines();
        ucs();
        printTableLines();

        prepareNodes();

        out.println("\ngbfs");
        printTableLines();
        gbfs();
        printTableLines();

        prepareNodes();

        out.println("\na*");
        printTableLines();
        aasterisk();
        printTableLines();
    }

    private static void gbfs() {
        boolean fail = true;

        startNode.tagged = true;
        PriorityQueue<SPath> L = new PriorityQueue<>(Comparator.comparing(SPath::getPriority));
        L.offer(path(null, startNode, true));

        out.printf("%5s | %10s | priority queue", "t", "expl.");
        out.printf("\n%5s | %10s | %s", "0", "", priorityQueueToString(L));

        int t = 1;

        while (!L.isEmpty()) {
            SPath p = L.remove();
            SNode u = p.u();

            if (u.goal) {
                out.printf("\nreturn goal: %s: %s", p.toString(), p.priority);
                fail = false;
                break;
            }

            for (SNode v : u.children) {
                if (!v.tagged) {
                    v.tagged = true;
                    insert(L, path(p, v, true), v);
                }
            }

            printQueue(t, u, L);

            t++;
        }

        if (fail)
            out.println("failure");
    }

    //a*
    private static void aasterisk() {
        boolean fail = true;

        PriorityQueue<APath> L = new PriorityQueue<>(Comparator.comparing(APath::f));
        Set<APath> E = new HashSet<>();
        L.offer(new APath(new SNode[]{startNode}, 0));

        int t = 1;
        out.printf("%5s\nL (open priority queue)\nE (closed set)\n", "t");

        while (!L.isEmpty()) {
            APath p_u = L.remove();
            if (p_u.u.goal) {
                out.printf("\nreturn goal: %s: %s", p_u.toString(), p_u.f);
                fail = false;
                break;
            }
            E.add(p_u);

            for (SNode v : p_u.u.children) {
                APath p_v = p_u.expand(v);

                if (p_v.compare(L) || p_v.compare(E))
                    continue;

                p_v.clean(L);
                p_v.clean(E);

                L.offer(p_v);
            }

            out.printf("\n%5s\nL: %s\nE: %s\n", t, aAsteriskCollToString(L), aAsteriskCollToString(E));

            t++;
        }

        if (fail)
            out.println("failure");
    }

    private static void ucs() {
        boolean fail = true;

        PriorityQueue<SPath> L = new PriorityQueue<>(Comparator.comparing(SPath::getPriority));
        L.offer(path(null, startNode, false));

        out.printf("%5s | %10s | priority queue", "t", "expl.");
        out.printf("\n%5s | %10s | %s", "0", "", priorityQueueToString(L));

        int t = 1;

        while (!L.isEmpty()) {
            SPath p = L.remove();
            SNode u = p.u();

            if (u.goal) {
                out.printf("\nreturn goal: %s: %s", p.toString(), p.priority);
                fail = false;
                break;
            }

            u.tagged = true;

            for (SNode v : u.children) {
                if (!v.tagged) {
                    insert(L, path(p, v, false), v);
                }
            }

            printQueue(t, u, L);

            t++;
        }

        if (fail)
            out.println("failure");
    }


    private static void insert(PriorityQueue<SPath> L, SPath p, SNode v) {
        L.offer(p);

        List<SPath> candidates = new LinkedList<>();
        for (SPath path : L) {
            if (path.u().equals(v))
                candidates.add(path);
        }

        SPath best = new SPath(null, Integer.MAX_VALUE);
        for (SPath sPath : candidates) {
            if (sPath.priority < best.priority)
                best = sPath;
        }

        for (SPath candidate : candidates) {
            if (!candidate.equals(best))
                L.remove(candidate);
        }
    }

    private static String priorityQueueToString(PriorityQueue<SPath> L) {
        StringBuilder out = new StringBuilder();

        int idx = 0;
        for (SPath path : L) {
            if (idx != 0)
                out.append("; ");
            out.append(path.toString()).append(": ").append(path.priority);
            idx++;
        }

        return out.toString();
    }

    private static String aAsteriskCollToString(Collection<APath> coll) {
        StringBuilder out = new StringBuilder();

        int idx = 0;
        for (APath path : coll) {
            if (idx != 0)
                out.append("; ");
            out.append(path.toString()).append(": ").append(path.f);
            idx++;
        }

        return out.toString();
    }

    private static @NotNull
    SPath path(SPath path, SNode append, boolean withHeuristics) {
        if (path == null)
            return new SPath(new SNode[]{append}, append.cost);

        SNode[] newP = new SNode[path.path.length + 1];
        System.arraycopy(path.path, 0, newP, 0, path.path.length);
        newP[path.path.length] = append;

        return new SPath(newP, withHeuristics ? h(append) : path.priority + append.cost);
    }

    //heuristics
    static double h(SNode n) {
        if (defaultHeuristics)
            return Math.abs(n.x - goalNode.x) + Math.abs(n.y - goalNode.y);
        else
            //test heuristics
            //return Math.sqrt(Math.abs(n.x - goalNode.x) ^ 2 + Math.abs(n.y - goalNode.y) ^ 2);
            return (Math.abs(n.x - goalNode.x) + Math.abs(n.y - goalNode.y)) * 1.4f;
    }

    private static void prepareNodes() {
        ns = new SNode[5][5];

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                ns[x][y] = new SNode(x + 1, y + 1);
            }
        }

        //connect the nodes properly and set costs
        ns[0][3].cost = 2;
        ns[1][1].cost = 2;

        ns[4][1].goal = true;
        goalNode = ns[4][1];

        startNode = ns[1][2];
        startNode.cost = 0;

        int[][] connections = {
                {1, 1, 2, 1}, {1, 1, 1, 2},
                {2, 1, 1, 1}, {2, 1, 2, 2},
                {3, 1, 3, 2}, {3, 1, 4, 1},
                {4, 1, 3, 1}, {4, 1, 4, 2}, {4, 1, 5, 1},
                {5, 1, 4, 1}, {5, 1, 5, 1},
                {1, 2, 1, 1}, {1, 2, 2, 2}, {1, 2, 1, 3},
                {2, 2, 1, 2}, {2, 2, 2, 1}, {2, 2, 3, 2},
                {3, 2, 2, 2}, {3, 2, 3, 1}, {3, 2, 3, 3},
                {4, 2, 4, 1}, {4, 2, 5, 2},
                {5, 2, 4, 2}, {5, 2, 5, 1}, {5, 2, 5, 3},
                {1, 3, 1, 2}, {1, 3, 1, 4}, {1, 3, 2, 3},
                {2, 3, 1, 3}, {2, 3, 2, 4},
                {3, 3, 3, 2}, {3, 3, 3, 4}, {3, 3, 4, 3},
                {4, 3, 3, 3}, {4, 3, 5, 3},
                {5, 3, 4, 3}, {5, 3, 5, 2}, {5, 3, 5, 4},
                {1, 4, 1, 3}, {1, 4, 1, 5}, {1, 4, 2, 4},
                {2, 4, 1, 4}, {2, 4, 2, 3}, {2, 4, 3, 4},
                {3, 4, 2, 4}, {3, 4, 3, 3}, {3, 4, 4, 4},
                {4, 4, 3, 4}, {4, 4, 4, 5}, {4, 4, 5, 4},
                {5, 4, 4, 4}, {5, 4, 5, 3}, {5, 4, 5, 5},
                {1, 5, 1, 4}, {1, 5, 2, 5},
                {2, 5, 1, 5}, {2, 5, 3, 5},
                {3, 5, 2, 5}, {3, 5, 4, 5},
                {4, 5, 3, 5}, {4, 5, 4, 4}, {4, 5, 5, 5},
                {5, 5, 4, 5}, {5, 5, 5, 4}
        };

        //connect all
        for (int[] cn : connections) {
            ns[cn[0] - 1][cn[1] - 1].children.add(ns[cn[2] - 1][cn[3] - 1]);
        }
    }

    private static void printTableLines() {
        out.println();
        for(int i = 0; i < 99; i++)
            out.print('-');
        out.println();
    }

    private static void printQueue(int t, SNode u, PriorityQueue<SPath> L) {
        out.printf("\n%5s | %10s | %s", t, u.toString(), priorityQueueToString(L));
    }
}

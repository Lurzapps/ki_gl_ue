import java.util.LinkedList;

public class BFS_DFS {

    public static void main(String[] args) {
	    //prepare some nodes
        /*char[] alphabet = ("abcdefghijklmnopqrstuvwxyz").toCharArray();
        SNode[] ns = new SNode[alphabet.length];
        for(int i = 0; i < alphabet.length; i++)
            ns[i] = new SNode(alphabet[i]);*/
        /*connect the nodes somehow (add children to each node)

        like this:

        ns[0].cs(ns[1], ns[2], ns[3]);
        ns[1].cs(ns[12], ns[25]);

        */

        //print out result for dfs and bfs (goal and start node can also be re-defined)
        //the reset of the nodes before / after each search is necessary, since else all nodes are still marked as discovered
        /*long dfsStart = System.nanoTime();
        SNode[] dfs = dfs(ns[0], ns[ns.length - 1]);
        long dfsEnd = System.nanoTime();
        resetNodes(ns);
        long bfsStart = System.nanoTime();
        SNode[] bfs = bfs(ns[0], ns[ns.length - 1]);
        long bfsEnd = System.nanoTime();
        resetNodes(ns);

        System.out.printf("\nDFS: %dns; p=%s", dfsEnd - dfsStart, dfs == null ? "Failure" : nodeArrToString(dfs));
        System.out.printf("\nBFS: %dns; p=%s", bfsEnd - bfsStart, bfs == null ? "Failure" : nodeArrToString(bfs));*/
        

    }

    public static void resetNodes(SNode[] allNodes) {
        for(SNode n : allNodes)
            n.tagged = false;
    }

    public static SNode[] dfs(SNode s, SNode g) {
        if(s.equals(g))
            return new SNode[]{s};

        LinkedList<SNode[]> L = new LinkedList<>();
        s.tagged = true;
        L.add(new SNode[]{s});
        while(L.size() > 0) {
            SNode[] p = L.get(0);
            SNode u = p[p.length - 1];
            L.remove(0);
            for(SNode v : u.children) {
                if(v.equals(g))
                    return path(p, v);
                else if(!v.tagged) {
                    v.tagged = true;
                    L.add(0, path(p, v));
                }
            }
        }

        return null;
    }

    public static SNode[] bfs(SNode s, SNode g) {
        if(s.equals(g))
            return new SNode[]{s};

        LinkedList<SNode[]> L = new LinkedList<>();
        s.tagged = true;
        L.add(new SNode[]{s});
        while(L.size() > 0) {
            SNode[] p = L.get(0);
            SNode u = p[p.length - 1];
            L.remove(0);
            for(SNode v : u.children) {
                if(v.equals(g))
                    return path(p, v);
                else if(!v.tagged) {
                    v.tagged = true;
                    L.add(path(p, v));
                }
            }
        }

        return null;
    }

    private static SNode[] path(SNode[] base, SNode newNode) {
        SNode[] newInstance = new SNode[base.length + 1];
        System.arraycopy(base, 0, newInstance, 0, base.length);
        newInstance[base.length] = newNode;
        return newInstance;
    }

    private static String nodeArrToString(SNode[] path) {
        StringBuilder o = new StringBuilder();
        for(int i = 0; i < path.length; i++) {
            if(i != 0)
                o.append(", ");
            o.append(path[i].id);
        }

        return o.toString();
    }
}

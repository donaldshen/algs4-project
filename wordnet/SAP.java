import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
    private Digraph g;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.NullPointerException();
        } else {
            g = new Digraph(G);
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validate(v);
        validate(w);
        int[] result = find(v, w);
        return result[1];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validate(v);
        validate(w);
        int[] result = find(v, w);
        return result[0];
    }

    private void validate(int v) {
        if (v < 0 || g.V() <= v) {
            throw new IndexOutOfBoundsException();
        }
    }


    private int[] find(int v, int w) {
        BreadthFirstDirectedPaths bv = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bw = new BreadthFirstDirectedPaths(g, w);
        int a = -1, l = Integer.MAX_VALUE;
        for (int i = 0; i < g.V(); i++) {
            if (bv.hasPathTo(i) && bw.hasPathTo(i)) {
                int t = bv.distTo(i) + bw.distTo(i);
                if (t < l) {
                    l = t;
                    a = i;
                }
            }
        }
        l = a == -1 ? -1 : l;
        return new int[]{a, l};
    }

    private int[] find(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bv = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bw = new BreadthFirstDirectedPaths(g, w);
        int a = -1, l = Integer.MAX_VALUE;
        for (int i = 0; i < g.V(); i++) {
            if (bv.hasPathTo(i) && bw.hasPathTo(i)) {
                int t = bv.distTo(i) + bw.distTo(i);
                if (t < l) {
                    l = t;
                    a = i;
                }
            }
        }
        l = a == -1 ? -1 : l;
        return new int[]{a, l};
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        for (int i : v) {
            validate(i);
        }
        for (int j : w) {
            validate(j);
        }
        int[] result = find(v, w);
        return result[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        for (int i : v) {
            validate(i);
        }
        for (int j : w) {
            validate(j);
        }
        int[] result = find(v, w);
        return result[0];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

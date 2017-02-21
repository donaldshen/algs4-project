import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Bag;

public class WordNet {
    private ST<String, Bag<Integer>> table;
    private SAP sap;
    private String[] synsets;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In in = new In(synsets);
        table = new ST<String, Bag<Integer>>();
        int n = 0;
        Queue<String> q = new Queue<String>();
        while (in.hasNextLine()) {
            String[] fields = in.readLine().split(",");
            int id = Integer.parseInt(fields[0]);
            for (String syn : fields[1].split(" ")) {
                if (!table.contains(syn)) {
                    table.put(syn, new Bag<Integer>());
                }
                table.get(syn).add(id);
            }
            n += 1;
            q.enqueue(fields[1]);
        }

        this.synsets = new String[n];
        for (int i = 0; i < n; i++) {
            this.synsets[i] = q.dequeue();
        }

        Digraph g = new Digraph(n);
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] ids = in.readLine().split(",");
            int from = Integer.parseInt(ids[0]);
            for (int i = 1; i < ids.length; i++) {
                int to = Integer.parseInt(ids[i]);
                g.addEdge(from, to);
            }
        }

        if (!singleRooted(g)) {
            throw new IllegalArgumentException();
        } else {
            sap = new SAP(g);
        }
    }

    private boolean singleRooted(Digraph g) {
        int n = 0;
        for (int i = 0; i < g.V(); i++) {
            if (g.outdegree(i) == 0) {
                n++;
            }
        }
        return n == 1 ? true : false;
    }



    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return table.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException();
        } else {
            return table.contains(word);
        }
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB)) {
            return sap.length(table.get(nounA), table.get(nounB));
        } else {
            throw new IllegalArgumentException();
        }
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB)) {
            int id = sap.ancestor(table.get(nounA), table.get(nounB));
            return synsets[id];
        } else {
            throw new IllegalArgumentException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}

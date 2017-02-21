import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wn;

    public Outcast(WordNet wordnet) {        // constructor takes a WordNet object
        wn = wordnet;
    }

    public String outcast(String[] nouns) {  // given an array of WordNet nouns, return an outcast
        int d = 0;
        String result = "";
        for (String n : nouns) {
            int t = 0;
            for (String m : nouns) {
                int tt = wn.distance(n, m);
                t += tt;
            }
            if (d < t) {
                result = n;
                d = t;
            }
        }
        return result;
    }

    public static void main(String[] args) { // see test client below
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

}

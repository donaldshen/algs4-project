import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Bag;
import java.util.Iterator;

public class BoggleSolver
{
    private TrieSET26 trie;
    private boolean[][] marked;
    private StringBuilder cur;
    private int rows;
    private int cols;
    private BoggleBoard board;
    private BagInt[][] adjs;

    public BoggleSolver(String[] dictionary) {
        trie = new TrieSET26();
        for (String word : dictionary) {
            trie.add(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard b) {
        board = b;
        rows = board.rows();
        cols = board.cols();
        marked = new boolean[rows][cols];
        cur = new StringBuilder();
        adjs = new BagInt[rows][cols];
        precomputeAdj();
        SET<String> result = new SET<String>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dfs(i, j, result, trie.root());
            }
        }

        return result;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int score = 0;
        if (trie.contains(word)) {
            int l = word.length();
            if (8 <= l) {
                score = 11;
            } else if (7 <= l) {
                score = 5;
            } else if (5 <= l) {
                score = l - 3;
            } else if (3 <= l) {
                score = 1;
            }
        }
        return score;
    }


    private void dfs(int r, int c, SET<String> result, TrieSET26.Node node) {
        marked[r][c] = true;
        char ch = board.getLetter(r, c);
        cur.append(ch);
        TrieSET26.Node next = trie.get(ch, node);
        if (ch == 'Q') {
            cur.append('U');
            if (next != null) {
                next = trie.get('U', next);
            }
        }
        String scur = cur.toString();

        if (next != null) {
            if (2 < scur.length() && next.isString() && !result.contains(scur)) {
                result.add(scur);
            }
            for (int i : adjs[r][c]) {
                int[] adj = index(r, c, i);
                int r2 = adj[0], c2 = adj[1];
                if (!marked[r2][c2]) {
                    dfs(r2, c2, result, next);
                }
            }
        }
        marked[r][c] = false;
        cur.deleteCharAt(cur.length() - 1);
        if (ch == 'Q') {
            cur.deleteCharAt(cur.length() - 1);
        }
    }

    private void dfs(int r, int c, SET<String> result) {
        marked[r][c] = true;
        char ch = board.getLetter(r, c);
        cur.append(ch);
        if (ch == 'Q') {
            cur.append('U');
        }
        String scur = cur.toString();
        if (trie.hasKeysWithPrefix(scur)) {
            if (2 < scur.length() && !result.contains(scur) && trie.contains(scur)) {
                result.add(scur);
            }
            for (int i : adjs[r][c]) {
                int[] adj = index(r, c, i);
                int r2 = adj[0], c2 = adj[1];
                if (!marked[r2][c2]) {
                    dfs(r2, c2, result);
                }
            }
        }
        marked[r][c] = false;
        cur.deleteCharAt(cur.length() - 1);
        if (ch == 'Q') {
            cur.deleteCharAt(cur.length() - 1);
        }
    }

    private boolean valid(int i, int j) {
        return 0 <= i && i < rows && 0 <= j && j < cols;
    }

    private class BagInt implements Iterable<Integer> {
        private Bag<Integer> bag;
        private BagInt() {
            bag = new Bag<Integer>();
        }

        private void add(int i) {
            bag.add(i);
        }

        public Iterator<Integer> iterator() {
            return bag.iterator();
        }
    }

    private void precomputeAdj() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                BagInt b = new BagInt();
                for (int k = 0; k < 9; k++) {
                    int[] adj = index(i, j, k);
                    if (k != 4 && valid(adj[0], adj[1])) {
                        b.add(k);
                    }
                }
                adjs[i][j] = b;
            }
        }
    }

    private int[] index(int i, int j, int k) {
        return new int[] {i - 1 + k / 3, j - 1 + k % 3};
    }



    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}

public class TrieSET26 {
    private static final int R = 26;

    private Node root;      // root of trie
    private int n;          // number of keys in trie

    // R-way trie node
    public static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
        public boolean isString() {
            return isString;
        }
    }

    /**
     * Initializes an empty set of strings.
     */
    public TrieSET26() {
        root = new Node();
    }

    public boolean contains(String key) {
        Node x = get(key);
        return x != null && x.isString;
    }

    public void add(String key) {
        Node x = root;
        for (int d = 0; d < key.length(); d++) {
            int c = key.charAt(d) - 'A';
            if (x.next[c] == null) {
                x.next[c] = new Node();
            }
            x = x.next[c];
        }
        if (!x.isString) {
            n++;
            x.isString = true;
        }
    }

    public boolean hasKeysWithPrefix(String prefix) {
        return get(prefix) != null;
    }

    public Node root() {
        return root;
    }

    public Node get(char c, Node x) {
        return x.next[c - 'A'];
    }


    // 在 x 中寻找 key[d] 指向的 Node
    private Node get(String key) {
        Node x = root;
        for (int d = 0; d < key.length() && x != null; d++) {
            x = get(key.charAt(d), x);
        }
        return x;
    }
}

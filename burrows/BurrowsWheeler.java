import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Bag;

public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        int n = s.length();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < n; i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(s.charAt((csa.index(i)+n-1) % n));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt();
        // construct t[]
        Bag<Character> b = new Bag<Character>();
        while (!BinaryStdIn.isEmpty()) {
            b.add(BinaryStdIn.readChar());
        }
        int r = 256;
        int[] count = new int[r+1];
        int n = b.size();
        char[] t = new char[n];
        for (char c : b) {
            t[--n] = c;
            count[c+1]++;
        }
        n = b.size();

        for (int i = 0; i < r; i++) {
            count[i+1] += count[i];
        }

        char[] h = new char[n];
        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            char c = t[i];
            h[count[c]] = c;
            next[count[c]++] = i;
        }
        // writing
        StringBuilder sb = new StringBuilder();
        while (0 < n--) {
            sb.append(h[first]);
            first = next[first];
        }
        BinaryStdOut.write(sb.toString());
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}

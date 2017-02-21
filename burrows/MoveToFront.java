import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.List;
// import java.util.ArrayList;
import java.util.LinkedList;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        int r = 256;
        char[] list = new char[r];
        for (char i = 0; i < r; i++) {
            list[i] = i;
        }

        String s = BinaryStdIn.readString();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i), j = 0;
            while (j < r && list[j] != c) {
                j++;
            }
            BinaryStdOut.write(j);
            System.arraycopy(list, 0, list, 1, j);
            list[0] = c;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        int r = 256;
        char[] list = new char[r];
        for (char i = 0; i < r; i++) {
            list[i] = i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char i = BinaryStdIn.readChar();
            char c = list[i];
            BinaryStdOut.write(c);
            System.arraycopy(list, 0, list, 1, i);
            list[0] = c;
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}

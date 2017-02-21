public class CircularSuffixArray {
    private int[] indices;

    public CircularSuffixArray(String s) {  // circular suffix array of s
        if (s == null) {
            throw new NullPointerException();
        }
        indices = new int[s.length()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }

        sort(s, 0, indices.length-1, 0);
    }

    public int length() {                   // length of s
        return indices.length;
    }

    public int index(int i) {               // returns index of ith sorted suffix
        if (i < 0 || length() <= i) {
            throw new IndexOutOfBoundsException();
        }
        return indices[i];
    }

    private void sort(String s, int lo, int hi, int d) {
        int n = indices.length;
        if (lo < hi && d < n) {
            char v = s.charAt((indices[lo]+d) % n);
            int lt = lo, gt = hi;
            int i = lo + 1;
            while (i <= gt) {
                char t = s.charAt((indices[i]+d) % n);
                if (t < v) {
                    int temp = indices[i];
                    indices[i++] = indices[lt];
                    indices[lt++] = temp;
                } else if (t == v) {
                    i++;
                } else {
                    int temp = indices[i];
                    indices[i] = indices[gt];
                    indices[gt--] = temp;
                }
            }

            sort(s, lo, lt-1, d);
            sort(s, lt, gt, d+1);
            sort(s, i, hi, d);
        }
    }

    public static void main(String[] args) {

    }

}

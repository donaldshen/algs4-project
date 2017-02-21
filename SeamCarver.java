import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int w;
    private int h;
    private double[][] energyMatrix;
    private int[][] colorMatrix;
    private boolean transposed;

    public SeamCarver(Picture picture) {               // create a seam carver object based on the given picture
        if (picture == null) {
            throw new NullPointerException();
        }
        w = picture.width();
        h = picture.height();

        buildM(picture);

        transposed = false;
    }

    private void buildM(Picture p) {
        energyMatrix = new double[w][h];
        colorMatrix = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                // caution: this is a transposed matrix
                Color c = p.get(i, j);
                colorMatrix[i][j] = c.getRed() + c.getGreen() * 256 + c.getBlue() * 65536;
            }
        }
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                energyMatrix[i][j] = calculateEnergy(i, j);
            }
        }
    }

    private double calculateEnergy(int i, int j) {
        if (i == 0 || i == w-1 || j == 0 || j == h-1) {
            return 1000;
        } else {
            double deltax = difference(colorMatrix[i-1][j], colorMatrix[i+1][j]);
            double deltay = difference(colorMatrix[i][j-1], colorMatrix[i][j+1]);
            return Math.sqrt(deltax + deltay);
        }
    }

    private double difference(int c1, int c2) {
        int[] rgb1 = rgb(c1);
        int[] rgb2 = rgb(c2);
        double dr = Math.pow((double) rgb1[0] - rgb2[0], 2);
        double dg = Math.pow((double) rgb1[1] - rgb2[1], 2);
        double db = Math.pow((double) rgb1[2] - rgb2[2], 2);
        return dr + dg + db;
    }

    private int[] rgb(int c) {
        int[] rgb = new int[3];
        rgb[2] = c / 65536;
        c %= 65536;
        rgb[1] = c / 256;
        rgb[0] = c % 256;
        return rgb;
    }

    private void transpose() {
        int t = w;
        w = h;
        h = t;
        double[][] energyT = new double[w][h];
        int[][] colorT = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                energyT[i][j] = energyMatrix[j][i];
                colorT[i][j] = colorMatrix[j][i];
            }
        }
        energyMatrix = energyT;
        colorMatrix = colorT;

        transposed = !transposed;
    }

    public Picture picture() {                         // current picture
        if (transposed) {
            transpose();
        }
        Picture p = new Picture(w, h);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int[] rgb = rgb(colorMatrix[i][j]);
                p.set(i, j, new Color(rgb[0], rgb[1], rgb[2]));
            }
        }
        return p;
    }

    public int width() {                           // width of current picture
        if (transposed) {
            return h;
        } else {
            return w;
        }
    }

    public int height() {                          // height of current picture
        if (transposed) {
            return w;
        } else {
            return h;
        }
    }

    public double energy(int x, int y) {              // energy of pixel at column x and row y
        if (x < 0 || width() <= x || y < 0 || height() <= y) {
            throw new IndexOutOfBoundsException();
        }
        if (transposed) {
            return energyMatrix[y][x];
        } else {
            return energyMatrix[x][y];
        }
    }

    public int[] findHorizontalSeam() {              // sequence of indices for horizontal seam
        if (transposed) {
            transpose();
        }
        return findHSeam();
    }

    private int[] findHSeam() {
        int[] result = new int[w];
        if (2 < h) {
            double[][] distTo = buildDistTo();
            int[][] edgeTo = new int[w][h];
            // only change distTo from (0,1) to (w-1,h-2)
            for (int i = 0; i < w-1; i++) {
                for (int j = 1; j < h-1; j++) {
                    relax(i, j, distTo, edgeTo);
                }
            }
            int ry = 1;
            for (int j = 1; j < h-1; j++) {
                if (distTo[w-1][j] < distTo[w-1][ry]) {
                    ry = j;
                }
            }

            for (int i = w-1; 0 <= i; i--) {
                result[i] = ry;
                ry = edgeTo[i][ry];
            }
        }

        return result;
    }

    private void relax(int x, int y, double[][] distTo, int[][] edgeTo) {
        for (int j = y-1; j <= y+1; j++) {
            double d = distTo[x][y] + energyMatrix[x+1][j];
            if (d < distTo[x+1][j]) {
                distTo[x+1][j] = d;
                edgeTo[x+1][j] = y;
            }
        }
    }

    public int[] findVerticalSeam() {                // sequence of indices for vertical seam
        if (!transposed) {
            transpose();
        }
        return findHSeam();
    }

    private double[][] buildDistTo() {
        double[][] distTo = new double[w][h];
        for (int i = 1; i < w; i++) {
            for (int j = 1; j < h; j++) {
                distTo[i][j] = Double.MAX_VALUE;
            }
        }
        return distTo;
    }

    private void validate(int[] seam) {
        if (seam == null) {
            throw new NullPointerException();
        }
        if (seam.length != w || h < 2) {
            throw new IllegalArgumentException();
        }
        int pre = seam[0];
        for (int s : seam) {
            if (s < 0 || h <= s || 1 < Math.abs(s-pre)) {
                throw new IllegalArgumentException();
            } else {
                pre = s;
            }
        }
    }


    public void removeHorizontalSeam(int[] seam) {  // remove horizontal seam from current picture
        if (transposed) {
            transpose();
        }
        removeHSeam(seam);
    }

    private void removeHSeam(int[] seam) {
        validate(seam);
        h--;
        for (int i = 0; i < w; i++) {
            System.arraycopy(energyMatrix[i], seam[i]+1, energyMatrix[i], seam[i], h-seam[i]);
            System.arraycopy(colorMatrix[i], seam[i]+1, colorMatrix[i], seam[i], h-seam[i]);
        }
        for (int i = 1; i < w-1; i++) {
            if (0 < seam[i]) {
                energyMatrix[i][seam[i]-1] = calculateEnergy(i, seam[i]-1);
            }
            if (seam[i] < h) {
                energyMatrix[i][seam[i]] = calculateEnergy(i, seam[i]);
            }
        }
    }

    public void removeVerticalSeam(int[] seam) {    // remove vertical seam from current picture
        if (!transposed) {
            transpose();
        }
        removeHSeam(seam);
    }

    public static void main(String[] args) {
        Picture p = new Picture(args[0]);
        SeamCarver carver = new SeamCarver(p);
        int[] vs = new int[carver.height()];
        for (int i = 0; i < vs.length; i++) {
            vs[i] = -1;
        }
        carver.removeVerticalSeam(vs);
    }
}

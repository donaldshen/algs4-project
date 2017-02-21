import edu.princeton.cs.algs4.Bag;

public class Board {
  private int n;
  private int[] tiles;

  public Board(int[][] blocks) {
    n = blocks.length;
    tiles = new int[n*n];
    for (int i = 0; i < tiles.length; i++) {
      tiles[i] = blocks[row(i)][col(i)];
    }
  }

  public int dimension() {
    return n;
  }


  public int hamming() {
    int count = 0;
    for (int i = 0; i < tiles.length; i++) {
      if (tiles[i] != 0 && tiles[i] != i + 1) {
        count++;
      }
    }
    return count;
  }


  public int manhattan() {
    int dist = 0;
    for (int i = 0; i < tiles.length; i++) {
      if (tiles[i] != 0) {
        int distR = Math.abs(row(i) - row(tiles[i] - 1));
        int distC = Math.abs(col(i) - col(tiles[i] - 1));
        dist += distR + distC;
      }
    }
    return dist;
  }


  public boolean isGoal() {
    for (int i = 0; i < tiles.length - 1; i++) {
      if (tiles[i] != i + 1) {
        return false;
      }
    }
    return true;
  }

  // a board that is obtained by exchanging any pair of blocks
  public Board twin() {
    int[][] blocks = buildBlocks();
    // n is guaranteed >= 2
    if (tiles[0] == 0 || tiles[1] == 0) {
      int temp = blocks[1][0];
      blocks[1][0] = blocks[1][1];
      blocks[1][1] = temp;
    } else {
      int temp = blocks[0][0];
      blocks[0][0] = blocks[0][1];
      blocks[0][1] = temp;
    }

    return new Board(blocks);
  }


  public boolean equals(Object other) {
    if (other == this) return true;
    if (other == null) return false;
    if (other.getClass() != this.getClass()) return false;

    Board that = (Board) other;
    if (this.n != that.n) {
      return false;
    } else {
      for (int i = 0; i < tiles.length; i++) {
        if (tiles[i] != that.tiles[i]) {
          return false;
        }
      }
      return true;
    }
  }


  public Iterable<Board> neighbors() {
    int r = 0, c = 0;
    for (int i = 0; i < tiles.length; i++) {
      if (tiles[i] == 0) {
        r = row(i);
        c = col(i);
        break;
      }
    }

    Bag<Board> bag = new Bag<Board>();
    if (0 < r) {
      int[][] blocks = buildBlocks();
      blocks[r][c] = blocks[r-1][c];
      blocks[r-1][c] = 0;
      bag.add(new Board(blocks));
    }
    if (r < n-1) {
      int[][] blocks = buildBlocks();
      blocks[r][c] = blocks[r+1][c];
      blocks[r+1][c] = 0;
      bag.add(new Board(blocks));
    }
    if (0 < c) {
      int[][] blocks = buildBlocks();
      blocks[r][c] = blocks[r][c-1];
      blocks[r][c-1] = 0;
      bag.add(new Board(blocks));
    }
    if (c < n-1) {
      int[][] blocks = buildBlocks();
      blocks[r][c] = blocks[r][c+1];
      blocks[r][c+1] = 0;
      bag.add(new Board(blocks));
    }
    return bag;
  }


  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(n + "\n");
    for (int i = 0; i < tiles.length; i++) {
      s.append(String.format("%2d ", tiles[i]));
      if (col(i) == n-1) {
        s.append("\n");
      }
    }
    return s.toString();
  }


  private int row(int i) {
    return i / n;
  }

  private int col(int i) {
    return i % n;
  }

  private int index(int r, int c) {
    return r * n + c;
  }

  private int[][] buildBlocks() {
    int[][] blocks = new int[n][n];
    for (int i = 0; i < tiles.length; i++) {
      blocks[row(i)][col(i)] = tiles[i];
    }
    return blocks;
  }

}

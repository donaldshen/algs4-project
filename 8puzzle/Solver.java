import java.util.Comparator;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class Solver {
  private SearchNode lastNode;

  public Solver(Board initial) {
    if (initial == null) {
      throw new NullPointerException();
    } else {
      MinPQ<SearchNode> pq = new MinPQ<SearchNode>(new ManhattanPriority());
      MinPQ<SearchNode> pq2 = new MinPQ<SearchNode>(new ManhattanPriority());
      pq.insert(new SearchNode(initial, 0, null));
      pq2.insert(new SearchNode(initial.twin(), 0, null));

      while (!pq.min().board.isGoal() && !pq2.min().board.isGoal()) {
        SearchNode sn = pq.delMin();
        for (Board b : sn.board.neighbors()) {
          if (sn.previous == null || !sn.previous.board.equals(b)) {
            pq.insert(new SearchNode(b, sn.moves+1, sn));
          }
        }

        SearchNode sn2 = pq2.delMin();
        for (Board b : sn2.board.neighbors()) {
          if (sn2.previous == null || !b.equals(sn2.previous.board)) {
            pq2.insert(new SearchNode(b, sn2.moves+1, sn2));
          }
        }
      }

      if (pq.min().board.isGoal()) {
        lastNode = pq.min();
      } else {
        lastNode = null;
      }
    }
  }

  public boolean isSolvable() {
    return lastNode != null;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    if (isSolvable()) {
      return lastNode.moves;
    } else {
      return -1;
    }
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    if (isSolvable()) {
      Bag<Board> bag = new Bag<Board>();
      SearchNode node = lastNode;
      while (node != null) {
        bag.add(node.board);
        node = node.previous;
      }
      return bag;
    } else {
      return null;
    }
  }

  private class SearchNode {
    private Board board;
    private int moves;
    private SearchNode previous;

    public SearchNode(Board b, int m, SearchNode p) {
      board = b;
      moves = m;
      previous = p;
    }
  }

  private class HammingPriority implements Comparator<SearchNode> {
    public int compare(SearchNode n1, SearchNode n2) {
      int p1 = n1.board.hamming() + n1.moves;
      int p2 = n2.board.hamming() + n2.moves;
      if (p1 < p2) {
        return -1;
      } else if (p1 == p2) {
        return 0;
      } else {
        return 1;
      }
    }
  }

  private class ManhattanPriority implements Comparator<SearchNode> {
    public int compare(SearchNode n1, SearchNode n2) {
      int p1 = n1.board.manhattan() + n1.moves;
      int p2 = n2.board.manhattan() + n2.moves;
      if (p1 < p2) {
        return -1;
      } else if (p1 == p2) {
        return 0;
      } else {
        return 1;
      }
    }
  }

  public static void main(String[] args) {
    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] blocks = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        blocks[i][j] = in.readInt();
      }
    }

    Board initial = new Board(blocks);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
    StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution())
      StdOut.println(board);
    }
  }

}

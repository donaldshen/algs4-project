import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;

public class KdTree {
  private static class Node {
    private Point2D p;      // the point node
    private Node lb;        // the left/bottom subtree
    private Node rt;        // the right/top subtree

    public Node(Point2D p) {
      this.p = p;
    }
  }

  private static final boolean LR = true;
  private static final boolean BT = false;

  private Node root;
  private int n;

  public KdTree() {
    n = 0;
  }

  public boolean isEmpty() {
    return root == null;
  }

  public int size() {
    return n;
  }


  public void insert(Point2D p) {
    if (p == null) {
      throw new NullPointerException();
    } else {
      if (root == null) {
        root = new Node(p);
        n++;
      } else {
        insert(root, p, LR);
      }
    }
  }

  private void insert(Node x, Point2D p, boolean orientation) {
    if (!x.p.equals(p)) {
      int cmp;
      if (orientation == LR) {
        cmp = Double.compare(p.x(), x.p.x());
      } else {
        cmp = Double.compare(p.y(), x.p.y());
      }

      if (cmp < 0) {
        if (x.lb == null) {
          x.lb = new Node(p);
          n++;
        } else {
          insert(x.lb, p, !orientation);
        }
      } else {
        // 0 <= cmp
        if (x.rt == null) {
          x.rt = new Node(p);
          n++;
        } else {
          insert(x.rt, p, !orientation);
        }
      }
    }
  }


  public boolean contains(Point2D p) {
    if (p == null) {
      throw new NullPointerException();
    } else {
      return contains(root, p, LR);
    }
  }

  private boolean contains(Node x, Point2D p, boolean orientation) {
    if (x == null) {
      return false;
    } else if (x.p.equals(p)) {
      return true;
    } else {
      if (compare(x.p, p, orientation) <= 0) {
        return contains(x.rt, p, !orientation);
      } else {
        return contains(x.lb, p, !orientation);
      }
    }
  }

  private int compare(Point2D p1, Point2D p2, boolean orientation) {
    if (orientation == LR) {
      return Double.compare(p1.x(), p2.x());
    } else {
      return Double.compare(p1.y(), p2.y());
    }
  }



  public void draw() {
    draw(root, LR, new RectHV(0.0, 0.0, 1.0, 1.0));
  }

  private void draw(Node x, boolean orientation, RectHV rect) {
    if (x != null) {
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.setPenRadius(0.01);
      x.p.draw();

      StdDraw.setPenRadius();
      if (orientation == LR) {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.line(x.p.x(), rect.ymin(), x.p.x(), rect.ymax());
      } else {
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.line(rect.xmin(), x.p.y(), rect.xmax(), x.p.y());
      }
      draw(x.lb, !orientation, halfRect(rect, x.p, orientation, -1));
      draw(x.rt, !orientation, halfRect(rect, x.p, orientation, 1));
    }
  }


  private RectHV halfRect(RectHV rect, Point2D p, boolean orientation, int cmp) {
    if (cmp < 0) {
      if (orientation == LR) {
        return new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
      } else {
        return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
      }
    } else {
      if (orientation == LR) {
        return new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
      } else {
        return new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
      }
    }
  }

  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new NullPointerException();
    } else {
      Bag<Point2D> result = new Bag<Point2D>();
      range(root, rect, LR, result);
      return result;
    }
  }

  private void range(Node x, RectHV rect, boolean orientation, Bag<Point2D> result) {
    if (x != null) {
      if (rect.contains(x.p)) {
        result.add(x.p);
      }
      
      boolean cmp1, cmp2;
      if (orientation == LR) {
        cmp1 = x.p.x() < rect.xmin();
        cmp2 = rect.xmax() < x.p.x();
      } else {
        cmp1 = x.p.y() < rect.ymin();
        cmp2 = rect.ymax() < x.p.y();
      }

      if (cmp1) {
        range(x.rt, rect, !orientation, result);
      } else if (cmp2) {
        range(x.lb, rect, !orientation, result);
      } else {
        range(x.rt, rect, !orientation, result);
        range(x.lb, rect, !orientation, result);
      }
    }
  }

  public Point2D nearest(Point2D p) {
    if (p == null) {
      throw new NullPointerException();
    } else {
      return nearest(root, p, LR);
    }
  }

  private Point2D nearest(Node x, Point2D p, boolean orientation) {
    if (x == null) {
      return null;
    } else {
      Point2D result = x.p;
      Node first, second;
      if (compare(x.p, p, orientation) <= 0) {
        first = x.rt;
        second = x.lb;
      } else {
        first = x.lb;
        second = x.rt;
      }

      Point2D r2 = nearest(first, p, !orientation);
      if (r2 != null && r2.distanceTo(p) < result.distanceTo(p)) {
        result = r2;
      }

      double dist;
      if (orientation == LR) {
        dist = Math.abs(p.x() - x.p.x());
      } else {
        dist = Math.abs(p.y() - x.p.y());
      }

      if (dist < result.distanceTo(p)) {
        r2 = nearest(second, p, !orientation);
        if (r2 != null && r2.distanceTo(p) < result.distanceTo(p)) {
          result = r2;
        }
      }

      return result;
    }
  }

  public static void main(String[] args) {
    String filename = args[0];
    In in = new In(filename);

    StdDraw.enableDoubleBuffering();
    KdTree kdtree = new KdTree();
    while (!in.isEmpty()) {
      double x = in.readDouble();
      double y = in.readDouble();
      Point2D p = new Point2D(x, y);
      kdtree.insert(p);
    }
    if (kdtree.contains(new Point2D(0.7, 0.5))) {
      System.out.println(kdtree.size());
    }

    kdtree.draw();
    StdDraw.show();
  }
}

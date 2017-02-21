import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

public class PointSET {
  private SET<Point2D> pset;

  public PointSET() {
    pset = new SET<Point2D>();
  }

  public boolean isEmpty() {
    return pset.isEmpty();
  }

  public int size() {
    return pset.size();
  }


  public void insert(Point2D p) {
    if (!contains(p)) {
      pset.add(p);
    }
  }

  public boolean contains(Point2D p) {
    return pset.contains(p);
  }

  public void draw() {
    for (Point2D p : pset) {
      p.draw();
    }
  }

  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new NullPointerException();
    } else {
      SET<Point2D> result = new SET<Point2D>();
      for (Point2D p : pset) {
        if (rect.contains(p)) {
          result.add(p);
        }
      }
      return result;
    }
  }

  public Point2D nearest(Point2D p) {
    if (p == null) {
      throw new NullPointerException();
    } else if (pset.isEmpty()) {
      return null;
    } else {
      Point2D result = pset.min();
      for (Point2D p2 : pset) {
        if (p.distanceTo(p2) < p.distanceTo(result)) {
          result = p2;
        }
      }
      return result;
    }
  }

  public static void main(String[] args) {
    String filename = args[0];
    In in = new In(filename);

    StdDraw.enableDoubleBuffering();
    PointSET pset = new PointSET();
    while (!in.isEmpty()) {
      double x = in.readDouble();
      double y = in.readDouble();
      Point2D p = new Point2D(x, y);
      pset.insert(p);
    }
    System.out.println(pset.size());
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);
    pset.draw();
    StdDraw.show();
  }
}

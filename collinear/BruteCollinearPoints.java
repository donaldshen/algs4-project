import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Bag;

public class BruteCollinearPoints {
  private LineSegment[] segmentsWithin4Points;
  private int n;

  public BruteCollinearPoints(Point[] points) {
    validate(points);

    Bag<LineSegment> bag = new Bag<LineSegment>();
    int len = points.length;
    for (int i = 0; i < len-3; i++) {
      Point p1 = points[i];
      for (int j = i+1; j < len-2; j++) {
        Point p2 = points[j];
        double s1 = p1.slopeTo(p2);
        for (int k = j+1; k < len-1; k++) {
          Point p3 = points[k];
          if (s1 == p1.slopeTo(p3)) {
            for (int l = k+1; l < len; l++) {
              Point p4 = points[l];
              if (s1 == p1.slopeTo(p4)) {
                Point[] ps = {p1, p2, p3, p4};
                bag.add(createSeg(ps));
              }
            }
          }
        }
      }
    }

    n = 0;
    segmentsWithin4Points = new LineSegment[bag.size()];
    for (LineSegment seg : bag) {
      segmentsWithin4Points[n++] = seg;
    }
  }

  private void validate(Point[] points) {
    if (points == null) {
      throw new NullPointerException();
    }
    int len = points.length;

    for (int i = 0; i < len; i++) {
      if (points[i] == null) {
        throw new NullPointerException();
      }
    }

    for (int i = 0; i < len; i++) {
      for (int j = i+1; j < len; j++) {
        if (points[i].compareTo(points[j]) == 0) {
          throw new IllegalArgumentException();
        }
      }
    }
  }

  private LineSegment createSeg(Point[] ps) {
    Point min = ps[0], max = ps[0];
    for (Point p : ps) {
      if (p.compareTo(min) < 0) {
        min = p;
      } else if (p.compareTo(max) > 0) {
        max = p;
      }
    }
    return new LineSegment(min, max);
  }

  public int numberOfSegments() {
    return n;
  }

  public LineSegment[] segments() {
    LineSegment[] segs = new LineSegment[n];
    System.arraycopy(segmentsWithin4Points, 0, segs, 0, n);
    return segs;
  }

  public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    LineSegment[] segments = collinear.segments();
    for (LineSegment segment : segments) {
      StdOut.println(segment);
      segment.draw();
    }
    StdOut.println(segments.length);
    StdDraw.show();
  }
}

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Bag;
import java.util.Arrays;

public class FastCollinearPoints {
  private Bag<LineSegment> collinears;

  public FastCollinearPoints(Point[] points) {
    validate(points);

    collinears = new Bag<LineSegment>();

    int len = points.length;
    Point[] sortedPs = Arrays.copyOf(points, len);

    // for each point
    for (Point origin : points) {
      // index at len is exclusive
      int j = 0;
      Arrays.sort(sortedPs, origin.slopeOrder());
      // check if there are 3 or more points have same slope
      while (j < len-2) {
        double slope = origin.slopeTo(sortedPs[j++]);
        int count = 2;
        while (j < len && slope == origin.slopeTo(sortedPs[j])) {
          count++;
          j++;
        }
        if (count >= 4) {
          Point[] collinearPs = new Point[count];
          collinearPs[0] = origin;
          for (int k = 1; k < count; k++) {
            collinearPs[k] = sortedPs[j-k];
          }
          LineSegment seg = createCollinear(collinearPs);
          if (seg != null) {
            collinears.add(seg);
          }
        }
      }
    }
  }

  private LineSegment createCollinear(Point[] ps) {
    int i = 1;
    Point start = ps[0];
    Point end = ps[0];

    if (ps.length % 2 == 0) {
      if (ps[0].compareTo(ps[1]) < 0) {
        end = ps[1];
      } else {
        start = ps[1];
      }
      i = 2;
    }

    while (i < ps.length) {
      Point b, s;
      if (ps[i].compareTo(ps[i+1]) < 0) {
        s = ps[i];
        b = ps[i+1];
      } else {
        b = ps[i];
        s = ps[i+1];
      }
      if (s.compareTo(start) < 0) {
        start = s;
      }
      if (end.compareTo(b) < 0) {
        end = b;
      }
      i += 2;
    }
    if (ps[0].compareTo(start) == 0) {
      return new LineSegment(start, end);
    } else {
      return null;
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

  public int numberOfSegments() {
    return collinears.size();
  }

  public LineSegment[] segments() {
    LineSegment[] segs = new LineSegment[numberOfSegments()];
    int i = 0;
    for (LineSegment seg : collinears) {
      segs[i++] = seg;
    }
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
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    LineSegment[] segments = collinear.segments();

    for (LineSegment segment : segments) {
      StdOut.println(segment);
      segment.draw();
    }
    StdOut.println(segments.length);
    StdDraw.show();
  }
}

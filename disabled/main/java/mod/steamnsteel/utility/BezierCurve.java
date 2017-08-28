package mod.steamnsteel.utility;

import java.awt.geom.Point2D;

public class BezierCurve
{
    public static Point2D.Double GetPointOnCurve(Point2D.Double p0, Point2D.Double p1, Point2D.Double p2, Point2D.Double p3, double t)
    {
        double u = 1 - t;
        double tt = t * t;
        double uu = u * u;
        double uuu = uu * u;
        double ttt = tt * t;

        Point2D.Double p = new Point2D.Double(p0.x, p0.y);
        p.x *= uuu;
        p.y *= uuu;

        p.x += 3 * uu * t * p1.x;
        p.y += 3 * uu * t * p1.y;

        p.x += 3 * u * tt * p2.x;
        p.y += 3 * u * tt * p2.y;

        p.x += ttt * p3.x;
        p.y += ttt * p3.y;

        return p;
    }
}

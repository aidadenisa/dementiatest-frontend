package blog.aida.dementiatest_frontend.main.models;

import android.graphics.Point;

/**
 * Created by aida on 16-May-18.
 */

public class Line {

    private Point a;
    private Point b;
    private static int HEIGHT = 150;

    public Line (Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    public Point getA() {
        return a;
    }

    public void setA(Point a) {
        this.a = a;
    }

    public Point getB() {
        return b;
    }

    public void setB(Point b) {
        this.b = b;
    }
}

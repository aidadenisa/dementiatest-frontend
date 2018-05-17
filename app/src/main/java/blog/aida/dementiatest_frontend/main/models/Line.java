package blog.aida.dementiatest_frontend.main.models;

import android.graphics.Point;

/**
 * Created by aida on 16-May-18.
 */

public class Line {

    private Point a;
    private Point b;
    private float p;
    private float m;

    private static int HEIGHT = 150;

    public Line (Point a, Point b) {
        this.a = a;
        this.b = b;

        if(a.x == b.x) {
            m = -1; //undefined
        } else {
            if(a.y == b.y) {
                m = 0;
            } else {
                m =  ( (float) (b.y - a.y)  / (float) (b.x - a.x)) ;
            }
        }



        p = a.y - m * a.x;
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

    public boolean pointBelongsToLine(float x, float y) {

        if(m > -1) {
            if(
                ( y < m * x + p + 15 )
                && ( y > m * x + p - 15 )
                && x > Math.min(a.x,b.x)
                && x < Math.max(a.x,b.x)
            ) {
                return true;
            }
        } else {
            if(
                (x < a.x + 15)
                && (x > a.x - 15)
                && y > Math.min(a.y,b.y)
                && y < Math.max(a.y,b.y)
            ) {
                return true;
            }
        }


        return false;
    }
}

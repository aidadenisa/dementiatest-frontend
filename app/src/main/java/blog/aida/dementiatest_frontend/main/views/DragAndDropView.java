package blog.aida.dementiatest_frontend.main.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import blog.aida.dementiatest_frontend.main.models.ConnectPoints;
import blog.aida.dementiatest_frontend.main.models.Line;

/**
 * Created by aida on 16-May-18.
 */

public class DragAndDropView extends View {

    private List<Line> lines;
    private Paint linePaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;

    private float offsetX;
    private float offsetY;
    private Point initialPointA;
    private Point initialPointB;
    private Line movingLine;
    private int movingLineIndex;


    public DragAndDropView(Context context) {
        super(context);

        lines = new ArrayList<>();
        mCanvas = new Canvas();

        populateLinesList();
    }

    private void populateLinesList() {

        Line square1_1 = new Line(new Point(75, 460), new Point(75 + 150, 460));
        Line square1_2 = new Line(new Point(250, 490), new Point(250, 490+150));
        Line square1_3 = new Line(new Point(75, 660), new Point(75 + 150, 660));
        Line square1_4 = new Line(new Point(40, 490), new Point(40, 490+150));

        Line square2_1 = new Line(new Point(280, 250), new Point(280+150, 250));
        Line square2_2 = new Line(new Point(450, 290), new Point(450, 290 + 150));
        Line square2_3 = new Line(new Point(280, 460), new Point(280 + 150, 460));
        Line square2_4 = new Line(new Point(250, 290), new Point(250, 290 + 150));

        Line triangle1_1 = new Line(new Point(250, 210), new Point(330, 80));
        Line triangle1_2 = new Line(new Point(380, 80), new Point(450, 210));

        Line triangle2_1 = new Line(new Point(500, 250), new Point(620, 330));
        Line triangle2_2 = new Line(new Point(500, 450), new Point(620, 380));

        lines.add(square1_1);
        lines.add(square1_2);
        lines.add(square1_3);
        lines.add(square1_4);
        lines.add(square2_1);
        lines.add(square2_2);
        lines.add(square2_3);
        lines.add(square2_4);
        lines.add(triangle1_1);
        lines.add(triangle1_2);
        lines.add(triangle2_1);
        lines.add(triangle2_2);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.DKGRAY);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.MITER);
        linePaint.setStrokeWidth(4f);

        mCanvas = new Canvas();

        setDrawingCacheEnabled( true );

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 0, 0, linePaint);

        if(lines == null) {
            return;
        }

        for(int i = 0; i< lines.size(); i++ ) {

            Line line = lines.get(i);
            canvas.drawLine(line.getA().x, line.getA().y, line.getB().x, line.getB().y, linePaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Point mid = new Point();
        float d = 0f;

        Line newRotatedLine = new Line(new Point(), new Point());

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                invalidateOldDragAndDropData();

                for(int i=0; i < lines.size(); i++ ) {
                    if (lines.get(i).pointBelongsToLine(event.getX(), event.getY())) {

                        Line line = lines.get(i);

                        initialPointA = new Point(line.getA());
                        initialPointB = new Point(line.getB());

                        movingLine = line;
                        movingLineIndex = i;

                        offsetX = event.getX();
                        offsetY = event.getY();

                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

//                midPoint(mid, event);
//                d = rotation(event);
//                break;

                newRotatedLine = new Line(new Point( (int) event.getX(0), (int) event.getY(0)),
                        new Point ( ));

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if(movingLine != null && movingLineIndex > -1 && initialPointA != null && initialPointB != null) {

                    if( event.getPointerCount() == 2 ) {

//                        movingLine.setM(newRotatedLine.getM());
//                        movingLine.setP(newRotatedLine.getP());
                        float newRot = rotation(event);
//                        float r = newRot - d;
//
//                        double raza = Math.sqrt(Math.pow(movingLine.getB().x - movingLine.getA().x, 2) + Math.pow(movingLine.getB().y -  movingLine.getA().y,2));
//
//                        double AnewX = raza * Math.cos((double)newRot);
//                        double AnewY = raza * Math.sin((double)newRot);
//
//                        movingLine.getA().x = (int) mid.x + (int) AnewX;
//                        movingLine.getA().y = (int) mid.y - (int) AnewY;


                    } else {

                        movingLine.getA().x =  (int)(initialPointA.x + event.getX() - offsetX );
                        movingLine.getB().x =  (int)(initialPointB.x + event.getX() - offsetX );

                        movingLine.getA().y =  (int)(initialPointA.y + event.getY() - offsetY );
                        movingLine.getB().y =  (int)(initialPointB.y + event.getY() - offsetY );

                        lines.set(movingLineIndex,movingLine);

                        invalidate();

                    }

                }
                break;
            default:
                break;
        }

//                Toast.makeText(getContext(),"Am dat click pe o linie", Toast.LENGTH_SHORT).show();

        return (true);
    }

    private void invalidateOldDragAndDropData() {
        movingLine = null;
        movingLineIndex = -1;
        offsetX = 0;
        offsetY = 0;

        initialPointA = null;
        initialPointB = null;
    }

    //calculate the degree of the rotation
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(Point point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set((int)x / 2, (int)y / 2);
    }
}

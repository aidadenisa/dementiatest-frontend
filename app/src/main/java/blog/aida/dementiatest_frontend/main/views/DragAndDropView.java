package blog.aida.dementiatest_frontend.main.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.media.ThumbnailUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.interfaces.CanvasBasedView;
import blog.aida.dementiatest_frontend.main.models.Line;

/**
 * Created by aida on 16-May-18.
 */

public class DragAndDropView extends View implements CanvasBasedView{

    private List<Line> lines;
    private Paint linePaint;
    private Paint movedLinesPaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;

    private float offsetX;
    private float offsetY;
    private Point initialPointA;
    private Point initialPointB;
    private Line movingLine;
    private int movingLineIndex;
    private Point movingLineMiddle;
    private double movingLineRay;

    private List<Line> movedLines;

    private static boolean ROTATE_EVENT = false;


    public DragAndDropView(Context context) {
        super(context);

        lines = new ArrayList<>();
        movedLines = new ArrayList<>();
        mCanvas = new Canvas();

        populateLinesList();

        adaptCanvasSizesToDifferentDisplayDensities();
    }

    private void adaptCanvasSizesToDifferentDisplayDensities() {
        for( int i=0; i<lines.size(); i++ ) {

            if (getResources().getDisplayMetrics().density == 1) {
                lines.get(i).getA().x = lines.get(i).getA().x / 4 * 3;
                lines.get(i).getA().y = lines.get(i).getA().y / 4 * 3;
                lines.get(i).getB().x = lines.get(i).getB().x / 4 * 3;
                lines.get(i).getB().y = lines.get(i).getB().y / 4 * 3;

            }

            if (getResources().getDisplayMetrics().density < 1) {
                lines.get(i).getA().x = lines.get(i).getA().x / 2;
                lines.get(i).getA().y = lines.get(i).getA().y / 2;
                lines.get(i).getB().x = lines.get(i).getB().x / 2;
                lines.get(i).getB().y = lines.get(i).getB().y / 2;

            }

            if(getResources().getDisplayMetrics().density > 1) {
                lines.get(i).getA().x = lines.get(i).getA().x * 2;
                lines.get(i).getA().y = lines.get(i).getA().y * 2;
                lines.get(i).getB().x = lines.get(i).getB().x * 2;
                lines.get(i).getB().y = lines.get(i).getB().y * 2;

            }

            lines.get(i).calculateLineFunctionParameters();
        }

        if (getResources().getDisplayMetrics().density == 1) {
            linePaint.setStrokeWidth(4f);
        }

        if (getResources().getDisplayMetrics().density < 1) {
            linePaint.setStrokeWidth(2f);
        }

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
        linePaint.setStrokeWidth(8f);

        movedLinesPaint = new Paint(linePaint);
        movedLinesPaint.setColor(getResources().getColor(R.color.colorAccent));

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
            if(movedLines.indexOf(line) > -1 ) {
                canvas.drawLine(line.getA().x, line.getA().y, line.getB().x, line.getB().y, movedLinesPaint);
            } else {
                canvas.drawLine(line.getA().x, line.getA().y, line.getB().x, line.getB().y, linePaint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                invalidateOldDragAndDropData();

                for(int i=0; i < lines.size(); i++ ) {
                    if (lines.get(i).pointBelongsToLine(event.getX(), event.getY())) {

                        Line line = lines.get(i);

                        if(movedLines.indexOf(line) == -1 ) {
                            if(movedLines.size() <= 3) {
                                movedLines.add(line);
                            } else {
                                Toast.makeText(
                                        getContext(),
                                        "You are allowed to move only 4 lines! Please move the colored ones",
                                        Toast.LENGTH_LONG
                                ).show();
                                return true;
                            }
                        }

                        initialPointA = new Point(line.getA());
                        initialPointB = new Point(line.getB());

                        movingLine = line;
                        movingLineIndex = i;

                        offsetX = event.getX();
                        offsetY = event.getY();

                        movingLineMiddle = getMiddlePointOfLine(movingLine);
                        movingLineRay = Math.sqrt(
                                Math.pow(movingLine.getA().x - movingLineMiddle.x,2)
                                        + Math.pow(movingLine.getA().y - movingLineMiddle.y,2)
                        );

                        break;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if(movingLine != null && movingLineIndex > -1 && initialPointA != null && initialPointB != null) {

                    if( event.getPointerCount() == 2 ) {

                        ROTATE_EVENT = true;

                        double rotationAngle = rotation(event);

                        if(movingLine.getA().x < movingLine.getB().x) {
                            Point aux = new Point(movingLine.getA());
                            movingLine.setA(new Point(movingLine.getB()));
                            movingLine.setB(aux);
                        }

                        double delta_x = Math.abs(movingLineRay * Math.cos(rotationAngle));
                        double delta_y = Math.abs(movingLineRay * Math.sin(rotationAngle));

                        if(rotationAngle > 1.4) {
                            delta_x = 0;
                        }

                        if(rotationAngle < 0.15) {
                            delta_y = 0;
                        }

                        calculateNewCoordinatesAfterRotation(event,delta_x,delta_y);

                        movingLine.calculateLineFunctionParameters();

                        lines.set(movingLineIndex,movingLine);

                        invalidate();

                    } else {

                        if( !ROTATE_EVENT ) {
                            movingLine.getA().x =  (int)(initialPointA.x + event.getX() - offsetX );
                            movingLine.getB().x =  (int)(initialPointB.x + event.getX() - offsetX );

                            movingLine.getA().y =  (int)(initialPointA.y + event.getY() - offsetY );
                            movingLine.getB().y =  (int)(initialPointB.y + event.getY() - offsetY );

                            movingLine.calculateLineFunctionParameters();

                            lines.set(movingLineIndex,movingLine);

                            invalidate();
                        }

                    }

                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if(ROTATE_EVENT) {
                    ROTATE_EVENT = false;
                }

                if(movingLine != null ) {

                    movingLine.calculateLineFunctionParameters();

                    lines.set(movingLineIndex,movingLine);

                }

                invalidate();
                break;

            default:
                break;
        }

//                Toast.makeText(getContext(),"Am dat click pe o linie", Toast.LENGTH_SHORT).show();

        return (true);
    }

    private void calculateNewCoordinatesAfterRotation(MotionEvent event, double delta_x, double delta_y) {

        movingLine.getA().x = (int) (movingLineMiddle.x + delta_x);
        movingLine.getB().x = (int) (movingLineMiddle.x - delta_x);

        if(event.getX(0) >= movingLineMiddle.x) {
            if( event.getY(0) <= movingLineMiddle.y ) {
                movingLine.getA().y = (int) (movingLineMiddle.y - delta_y);
            } else {
                movingLine.getA().y = (int) (movingLineMiddle.y + delta_y);
            }
        } else {
            if( event.getY(0) <= movingLineMiddle.y ) {
                movingLine.getB().y = (int) (movingLineMiddle.y - delta_y);
            } else {
                movingLine.getB().y = (int) (movingLineMiddle.y + delta_y);
            }
        }

        if(event.getX(1) >= movingLineMiddle.x) {
            if( event.getY(1) <= movingLineMiddle.y ) {
                movingLine.getA().y = (int) (movingLineMiddle.y - delta_y);
            } else {
                movingLine.getA().y = (int) (movingLineMiddle.y + delta_y);
            }
        } else {
            if( event.getY(1) <= movingLineMiddle.y ) {
                movingLine.getB().y = (int) (movingLineMiddle.y - delta_y);
            } else {
                movingLine.getB().y = (int) (movingLineMiddle.y + delta_y);
            }
        }
    }

    private Point getMiddlePointOfLine(Line movingLine) {

        int middleX = (int) (movingLine.getA().x + movingLine.getB().x) / 2;
        int middleY = (int) (movingLine.getA().y + movingLine.getB().y) / 2;
        return new Point(middleX, middleY);
    }

    private void invalidateOldDragAndDropData() {
        movingLine = null;
        movingLineIndex = -1;
        offsetX = 0;
        offsetY = 0;

        initialPointA = null;
        initialPointB = null;

        movingLineMiddle = new Point();
        movingLineRay = 0;
        ROTATE_EVENT = false;

    }

    //calculate the degree of the rotation
    private double rotation(MotionEvent event) {
        double delta_x = Math.abs(event.getX(0) - event.getX(1));
        double delta_y = Math.abs(event.getY(0) - event.getY(1));
        return Math.atan2(delta_y, delta_x);
    }

    public byte[] getDrawing()
    {
        Bitmap whatTheUserDrewBitmap = getDrawingCache();
        // don't forget to clear it (see above) or you just get duplicates

        // almost always you will want to reduce res from the very high screen res
        whatTheUserDrewBitmap = ThumbnailUtils.extractThumbnail(whatTheUserDrewBitmap, 256, 256);
        // NOTE that's an incredibly useful trick for cropping/resizing squares
        // while handling all memory problems etc
        // http://stackoverflow.com/a/17733530/294884

        // you can now save the bitmap to a file, or display it in an ImageView:

        // these days you often need a "byte array". for example,
        // to save to parse.com or other cloud services
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        whatTheUserDrewBitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);

        return baos.toByteArray();
    }

    @Override
    public void startOver() {
        lines.clear();
        movedLines.clear();

        populateLinesList();

        adaptCanvasSizesToDifferentDisplayDensities();

        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
        invalidate();
    }
}
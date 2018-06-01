package blog.aida.dementiatest_frontend.main.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import blog.aida.dementiatest_frontend.main.interfaces.CanvasBasedView;
import blog.aida.dementiatest_frontend.main.models.ConnectPoints;

/**
 * Created by aida on 07-May-18.
 */

public class ConnectPointsView extends View implements CanvasBasedView {


    private Bitmap mBitmap;
    private Canvas  mCanvas;
    private Path mPath;
    private Path circlePath;
    private Paint paint;

    private Paint pointSelectedPaint;
    private Paint pointSelectedTextPaint;
    private Paint circlePaint;

    private List<ConnectPoints> points;

    private List<ConnectPoints> connectedPoints;

    private static int POINTS_RADIUS = 30;

    public ConnectPointsView(Context context) {
        super(context);
        mPath = new Path();
        paint = new Paint();
        paint.setStrokeWidth(8);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.DKGRAY);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);

        pointSelectedPaint = new Paint();
        pointSelectedPaint.setAntiAlias(true);
        pointSelectedPaint.setColor(Color.DKGRAY);

        pointSelectedTextPaint = new Paint();
        pointSelectedTextPaint.setTextSize(40);
        pointSelectedTextPaint.setAntiAlias(true);
        pointSelectedTextPaint.setTextAlign(Paint.Align.CENTER);
        pointSelectedTextPaint.setColor(Color.WHITE);

        mCanvas = new Canvas();

        connectedPoints = new ArrayList<>();

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
//        canvas.drawColor(0xFFAAAAAA);
        // canvas.drawLine(mX, mY, Mx1, My1, mPaint);
        // canvas.drawLine(mX, mY, x, y, mPaint);
        canvas.drawBitmap(mBitmap, 0, 0, paint);
//        canvas.drawPath(mPath, paint);
//        canvas.drawPath( circlePath,  circlePaint);

        if(points == null) {
            return;
        }

        for(int i = 0; i< points.size(); i++ ) {

            ConnectPoints point = points.get(i);
            if(!connectedPoints.contains(point)) {
                canvas.drawCircle(point.getX(), point.getY(), POINTS_RADIUS, circlePaint);
                canvas.drawText(point.getCode(), point.getX(), point.getY() + (POINTS_RADIUS/3), paint);
            }
        }

    }

    public void setPoints(List<ConnectPoints> points) {

        this.points = new ArrayList<>(points.size());
        for(int i = 0; i< points.size(); i++) {
            try {
                this.points.add((ConnectPoints)points.get(i).clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        adaptCanvasSizesToDifferentDisplayDensities();
    }

    private void adaptCanvasSizesToDifferentDisplayDensities() {

        for(int i = 0; i< points.size(); i++ ) {

            ConnectPoints point = points.get(i);

            if (getResources().getDisplayMetrics().density == 1) {

                point.setX((int)point.getX()/2);
                point.setY((int)point.getY()/2);

            }

            if (getResources().getDisplayMetrics().density < 1) {

                point.setX((int)point.getX()/4);
                point.setY((int)point.getY()/4);

            }

        }

        if (getResources().getDisplayMetrics().density == 1) {
            POINTS_RADIUS = 14;
            paint.setStrokeWidth(4);
            paint.setTextSize(18);
            pointSelectedTextPaint.setTextSize(18);
            circlePaint.setStrokeWidth(2f);
        }

        if (getResources().getDisplayMetrics().density < 1) {

            POINTS_RADIUS = 8;
            paint.setStrokeWidth(2);
            paint.setTextSize(8);
            pointSelectedTextPaint.setTextSize(8);
            circlePaint.setStrokeWidth(1f);

        }

    }

//    public void drawCircle(ConnectPoints point) {
////        canvas.drawCircle(x, y, radius, paint);
//        mCanvas.drawCircle(point.getX(), point.getY(), 10, paint);
//    }

    @Override
    public boolean onTouchEvent( MotionEvent event) {

        if(points != null  ) {
            for(int i=0; i< points.size(); i++ ){
                if(points.get(i).getX() + POINTS_RADIUS > event.getX()
                        && points.get(i).getX() - POINTS_RADIUS < event.getX()
                        && points.get(i).getY() + POINTS_RADIUS > event.getY()
                        && points.get(i).getY() - POINTS_RADIUS < event.getY()) {

                    if(!connectedPoints.contains(points.get(i))) {
                        connectedPoints.add(points.get(i));
                        mCanvas.drawCircle(points.get(i).getX(), points.get(i).getY(), POINTS_RADIUS, pointSelectedPaint);
                        mCanvas.drawText(
                                points.get(i).getCode(),
                                points.get(i).getX(),
                                points.get(i).getY() + (POINTS_RADIUS/3),
                                pointSelectedTextPaint
                        );

                    }

                    break;
                }
            }
        }

        if(connectedPoints.size() > 1) {
            for(int j = 0; j < connectedPoints.size() - 1 ; j++ ) {
                mCanvas.drawLine(
                        connectedPoints.get(j).getX(),
                        connectedPoints.get(j).getY(),
                        connectedPoints.get(j+1).getX(),
                        connectedPoints.get(j+1).getY(),
                        paint
                );

                mCanvas.drawText(
                        connectedPoints.get(j).getCode(),
                        connectedPoints.get(j).getX(),
                        connectedPoints.get(j).getY() + (POINTS_RADIUS/3),
                        pointSelectedTextPaint
                );
            }

            mCanvas.drawText(
                    connectedPoints.get(connectedPoints.size() - 1).getCode(),
                    connectedPoints.get(connectedPoints.size() - 1).getX(),
                    connectedPoints.get(connectedPoints.size() - 1).getY() + (POINTS_RADIUS/3),
                    pointSelectedTextPaint
            );
        }
        this.invalidate();
        return true;
    }

    public String getOrderOfConnectedPointsToString() {
        String result = new Gson().toJson(connectedPoints);
        return result;
    }

    @Override
    public void startOver() {
        connectedPoints.clear();

        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
        invalidate();
    }
}

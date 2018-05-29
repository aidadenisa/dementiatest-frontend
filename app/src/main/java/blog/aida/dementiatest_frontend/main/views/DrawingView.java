package blog.aida.dementiatest_frontend.main.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;

/**
 * Created by aida on 05-May-18.
 */

public class DrawingView extends View {

    public int width;
    public  int height;
    private Bitmap bitmap;
    private Canvas canvas;
    private Path path;
    private Paint bitmapPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;

    private Paint paint;

    private float x;
    private float y;
    private static final float TOUCH_TOLERANCE = 4;

    public DrawingView(Context context, Paint paint) {
        super(context);
        this.context=context;
        path = new Path();
        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.DKGRAY);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);

        this.paint = paint;

        // in the class where you set up the view, add this:
        setDrawingCacheEnabled( true );
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w , h , oldw, oldh);

//        this.height = h;
//        this.width = w;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;


        bitmap = Bitmap.createBitmap((int)(width - (20*width/100)) , (int)(height/3 ), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;

//        canvas.clipRect(0,0,width - (20*width/100),height/3);
        canvas.drawBitmap( bitmap, 0, 0, bitmapPaint);
        canvas.drawPath( path,  paint);
        canvas.drawPath( circlePath,  circlePaint);
    }

    private void touch_start(float x, float y) {
        path.reset();
        path.moveTo(x, y);
        this.x = x;
        this.y = y;
    }

    private void touch_move(float x, float y) {

        Rect clippingBounds = canvas.getClipBounds();

        if(x > clippingBounds.right || y > clippingBounds.bottom) {
            return;
        }

        float dx = Math.abs(x - this.x);
        float dy = Math.abs(y - this.y);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(this.x, this.y, (x + this.x)/2, (y + this.y)/2);
            this.x = x;
            this.y = y;

            circlePath.reset();
            circlePath.addCircle(this.x, this.y, 30, Path.Direction.CW);
        }
    }

    private void touch_up() {
        path.lineTo(this.x, this.y);
        circlePath.reset();
        // commit the path to our offscreen
        canvas.drawPath(path,  paint);
        // kill this so we don't double draw
        path.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    public void clearDrawing()
    {
        setDrawingCacheEnabled(false);
        // don't forget that one and the match below,
        // or you just keep getting a duplicate when you save.

        onSizeChanged(width, height, width, height);
        invalidate();

        setDrawingCacheEnabled(true);
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
}

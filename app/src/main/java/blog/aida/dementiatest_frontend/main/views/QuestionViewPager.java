package blog.aida.dementiatest_frontend.main.views;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.jar.Attributes;

/**
 * Created by aida on 05-May-18.
 */

public class QuestionViewPager extends ViewPager {
    public QuestionViewPager(Context context) {
        super(context);
    }

    public QuestionViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (MotionEventCompat.getActionMasked(ev) == MotionEvent.ACTION_MOVE) {
            // ignore move action
        } else {
            if (super.onInterceptTouchEvent(ev)) {
                super.onTouchEvent(ev);
            }
        }
        return false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return MotionEventCompat.getActionMasked(ev) != MotionEvent.ACTION_MOVE && super.onTouchEvent(ev);

    }
}

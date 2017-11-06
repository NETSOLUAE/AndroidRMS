package rmsllcoman.com.Util;

import android.app.Dialog;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by macmini on 6/20/17.
 */

public class ClaimDialog extends Dialog {
    private final View.OnTouchListener gestureListener;
    GestureDetector gestureDetector;

    public ClaimDialog (Context context, int theme) {
        super(context, theme);

        MyGestureDetector gestDetec = new MyGestureDetector();    //inital setup
        gestureDetector = new GestureDetector(gestDetec);
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
            return true;
        else
            return false;
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            System.out.println( "Help im being touched!" );
            return false;
        }
    }
}

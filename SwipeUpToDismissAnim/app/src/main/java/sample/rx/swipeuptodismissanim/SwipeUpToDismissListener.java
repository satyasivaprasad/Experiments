package sample.rx.swipeuptodismissanim;

import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class SwipeUpToDismissListener implements View.OnTouchListener {

    private final View swipeView;
    private float mDownX;
    private int mViewWidth = 1;
    SwipeUpToDismissListener(View swipeView) {
        this.swipeView = swipeView;
    }

    private boolean tracking = false;
    private float startY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mViewWidth < 2) {
            mViewWidth = swipeView.getWidth();
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Rect hitRect = new Rect();
                swipeView.getHitRect(hitRect);
                if (hitRect.contains((int) event.getX(), (int) event.getY())) {
                    tracking = true;
                }
                startY = event.getY();
                mDownX = event.getRawX();
                swipeView.setAlpha(1f);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                tracking = false;
                animateSwipeView(v.getHeight());
                return true;
            case MotionEvent.ACTION_MOVE:
                float finalY = event.getY();
                if (startY > finalY && tracking) {
                    swipeView.setTranslationY(event.getY() - startY);
                    float deltaX = event.getRawX() - mDownX;
                    swipeView.setAlpha(Math.max(0f, Math.min(1f,
                            1f - 2f * Math.abs(deltaX) / mViewWidth)));
                }
                return true;
        }
        return false;
    }


    /**
     * Using the current translation of swipeView decide if it has moved
     * to the point where we want to remove it.
     */
    private void animateSwipeView(int parentHeight) {
        int quarterHeight = parentHeight / 4;
        float currentPosition = swipeView.getTranslationY();
        float animateTo = 0.0f;
        if (currentPosition < -quarterHeight) {
            animateTo = -parentHeight;
        } else if (currentPosition > quarterHeight) {
            animateTo = parentHeight;
        }
        if (animateTo == 0.0f) {
            swipeView.setAlpha(1f);
            swipeView.invalidate();
        }
        ObjectAnimator.ofFloat(swipeView, "translationY", currentPosition, animateTo)
                .setDuration(200)
                .start();
    }
}

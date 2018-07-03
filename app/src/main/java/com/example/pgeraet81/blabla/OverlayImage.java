package com.example.pgeraet81.blabla;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class OverlayImage extends ScaleGestureDetector.SimpleOnScaleGestureListener implements View.OnTouchListener {

    private ScaleGestureDetector scaleGestureDetector;

    private ImageView item;

    private int minX = 0;
    private int minY = 0;
    private int maxX = 0;
    private int maxY = 0;
    private float scaleFactor = 0.7f;
    private boolean scaleDone = true;

    public OverlayImage(Context context, ImageView image, int windowwidth, int windowheight, int minX, int minY) {
        this.item = image;
        this.maxX = windowwidth;
        this.maxY = windowheight;
        this.minX = minX;
        this.minY = minY;

        if (scaleDone)
            item.setOnTouchListener(this);

        scaleGestureDetector = new ScaleGestureDetector(context, this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (motionEvent.getPointerCount() == 1) {
                    float x = motionEvent.getRawX();
                    float y = motionEvent.getRawY();

                    if (x > maxX) {
                        x = maxX;
                    }
                    if (y > maxY) {
                        y = maxY;
                    }
                    if (x < minX) {
                        x = minX;
                    }
                    if (y < minY) {
                        y = minY;
                    }
                    drag(x, y);
                } else {
                    scaleGestureDetector.onTouchEvent(motionEvent);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (item.getX() < 120 && item.getY() > maxY - 600) {
                    item.setImageResource(R.drawable.overlaynone);
                    item.setX(100);
                    item.setY(100);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void drag(float x, float y) {
        item.setX(x - item.getWidth() / 2);
        item.setY(y - item.getHeight() / 2);

        if (item.getX() < 120 && item.getY() > maxY - 600) {
            item.setColorFilter(R.color.colorAccent);
        } else {
            item.setColorFilter(null);
        }
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();

        // Don't let the object get too small or too large.
        scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));

        item.setScaleX(scaleFactor);
        item.setScaleY(scaleFactor);
        // prevent image from jumping to a hanging finger
        scaleDone = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                scaleDone = true;
            }
        }, 500);
        return true;
    }

    public void setImageResource(int resource) {
        this.item.setImageResource(resource);
        item.setColorFilter(null);
    }

    public ImageView getImage() {
        return this.item;
    }

    public float getX() {
        return item.getX();
    }

    public float getY() {
        return item.getY();
    }
}

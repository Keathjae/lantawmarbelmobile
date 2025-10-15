package com.example.lantawmarbelmobileapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class ScannerOverlayView extends View {

    private Paint borderPaint;
    private Paint cornerPaint;
    private Rect frameRect;

    public ScannerOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Border style
        borderPaint = new Paint();
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(4f);
        borderPaint.setAlpha(180);

        // Corner highlights
        cornerPaint = new Paint();
        cornerPaint.setColor(Color.parseColor("#00FF00")); // bright green
        cornerPaint.setStrokeWidth(8f);
        cornerPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Calculate the central frame (square)
        int frameSize = (int) (Math.min(width, height) * 0.6);
        int left = (width - frameSize) / 2;
        int top = (height - frameSize) / 2;
        int right = left + frameSize;
        int bottom = top + frameSize;
        frameRect = new Rect(left, top, right, bottom);

        // Draw transparent background dim
        canvas.drawColor(Color.parseColor("#66000000")); // semi-transparent dark overlay

        // Clear the scan area (make it visible)
        canvas.save();
        canvas.clipRect(frameRect);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.restore();

        // Draw white border
        canvas.drawRect(frameRect, borderPaint);

        // Draw corners (optional)
        int cornerLen = 60;
        // Top-left
        canvas.drawLine(left, top, left + cornerLen, top, cornerPaint);
        canvas.drawLine(left, top, left, top + cornerLen, cornerPaint);
        // Top-right
        canvas.drawLine(right, top, right - cornerLen, top, cornerPaint);
        canvas.drawLine(right, top, right, top + cornerLen, cornerPaint);
        // Bottom-left
        canvas.drawLine(left, bottom, left + cornerLen, bottom, cornerPaint);
        canvas.drawLine(left, bottom, left, bottom - cornerLen, cornerPaint);
        // Bottom-right
        canvas.drawLine(right, bottom, right - cornerLen, bottom, cornerPaint);
        canvas.drawLine(right, bottom, right, bottom - cornerLen, cornerPaint);
    }
}

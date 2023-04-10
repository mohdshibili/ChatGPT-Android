package com.codeware.chatgpt.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.style.LineBackgroundSpan;

public class CodeBlockSpan implements LineBackgroundSpan {
    Drawable drawable;
    int right;

    public CodeBlockSpan(int backgroundColor, boolean isStart, boolean isEnd, int right) {
        this.right = right;

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(backgroundColor);
        gd.setShape(GradientDrawable.RECTANGLE);

        if (isStart && !isEnd) {
            gd.setCornerRadii(new float[]{10, 10, 10, 10, 0, 0, 0, 0});
        } else if (!isStart && isEnd) {
            gd.setCornerRadii(new float[]{0, 0, 0, 0, 10, 10, 10, 10});
        }

        this.drawable = gd;
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        // Adjust the left and right coordinates to cover the full line width
        int adjustedLeft = 0;
        int adjustedRight = c.getWidth();

        drawable.setBounds(adjustedLeft, top, right, bottom);
        drawable.draw(c);
    }
}

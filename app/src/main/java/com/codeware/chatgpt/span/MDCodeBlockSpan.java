package com.codeware.chatgpt.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.text.style.LineBackgroundSpan;

/**
 * code syntax span
 * <p>
 * Created by yuyidong on 16/5/17.
 */
public class MDCodeBlockSpan implements LineBackgroundSpan
{
    private final int mColor;
    private Drawable mDrawable;
    private String mText;
	boolean isBeginning = false;
	int mPadding = 20;
    /**
     * Constructor
     *
     * @param color the color
     */
    public MDCodeBlockSpan(int color)
	{
        mColor = color;
    }

    /**
     * Constructor
     *
     * @param color       color
     * @param isBeginning whether it's the beginning line of the code
     * @param isEnding    whether it's the ending line of the code
     * @param text        the begin or end line content
     */
    public MDCodeBlockSpan(int color, boolean isBeginning, boolean isEnding, String text)
	{
		this.isBeginning = isBeginning;
		
        mColor = color;
        if (isBeginning || isEnding)
		{
            GradientDrawable d = new GradientDrawable();
            d.setColor(mColor);

            if (isBeginning && !isEnding)
			{
                d.setCornerRadii(new float[]{10, 10, 10, 10, 0, 0, 0, 0});
            }
			else if (!isBeginning && isEnding)
			{
                d.setCornerRadii(new float[]{0, 0, 0, 0, 10, 10, 10, 10});
            }
			else
			{
                d.setCornerRadius(10);
            }
            mDrawable = d;
            mText = text;
        }
    }


    @Override
    public void drawBackground(Canvas c, Paint p,
                               int left, int right, int top, int baseline, int bottom,
                               CharSequence text, int start, int end, int lnum)
	{
        if (mDrawable != null && !TextUtils.isEmpty(mText))
		{
            mDrawable.setBounds(left, top - mPadding  , right, bottom);
            mDrawable.draw(c);
        }
		else
		{
            Paint.Style style = p.getStyle();
            int color = p.getColor();
            p.setStyle(Paint.Style.FILL);
            p.setColor(mColor);
            c.drawRect(left, top, right, bottom, p);
            p.setStyle(style);
            p.setColor(color);
        }
    }

}

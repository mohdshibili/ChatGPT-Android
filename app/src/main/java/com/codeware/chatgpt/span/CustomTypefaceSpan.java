package com.codeware.chatgpt.span;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class CustomTypefaceSpan extends MetricAffectingSpan
{
	private Typeface typeface;

	public CustomTypefaceSpan(Typeface typeface)
	{
		this.typeface = typeface;
	}

	@Override
	public void updateMeasureState(TextPaint textPaint)
	{
		apply(textPaint, typeface);
	}

	@Override
	public void updateDrawState(TextPaint textPaint)
	{
		apply(textPaint, typeface);
	}

    private void apply(Paint paint, Typeface typeface)
	{
        int oldStyle;
        Typeface old = paint.getTypeface();
        oldStyle = old != null ? old.getStyle() : 0;
        int fake = oldStyle & ~typeface.getStyle();

        if ((fake & Typeface.BOLD) != 0)
			paint.setFakeBoldText(true);

        if ((fake & Typeface.ITALIC) != 0)
			paint.setTextSkewX(-0.25f);

        paint.setTypeface(typeface);
    }

}

package com.codeware.chatgpt.highlight.syntax;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class EmphasisSpan extends MetricAffectingSpan
{
	@Override
	public void updateMeasureState(TextPaint textPaint)
	{
		textPaint.setTextSkewX(-0.25F);
	}

	@Override
	public void updateDrawState(TextPaint textPaint)
	{

	}
}

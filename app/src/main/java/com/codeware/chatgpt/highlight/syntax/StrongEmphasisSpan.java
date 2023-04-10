package com.codeware.chatgpt.highlight.syntax;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class StrongEmphasisSpan extends MetricAffectingSpan {
    @Override
    public void updateMeasureState(TextPaint textPaint) {
        textPaint.setFakeBoldText(true);
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        textPaint.setFakeBoldText(true);
    }
}

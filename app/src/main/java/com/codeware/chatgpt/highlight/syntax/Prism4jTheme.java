package com.codeware.chatgpt.highlight.syntax;

import android.text.SpannableStringBuilder;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.codeware.chatgpt.highlight.prism4j.Prism4j;

public interface Prism4jTheme {

    @ColorInt
    int background();

    @ColorInt
    int textColor();

    void apply(
            @NonNull String language,
            @NonNull Prism4j.Syntax syntax,
            @NonNull SpannableStringBuilder builder,
            int start,
            int end
    );
}

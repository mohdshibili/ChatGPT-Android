package com.codeware.chatgpt.highlight.syntax;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Prism4jThemeDefault extends Prism4jThemeBase {

    @NonNull
    public static Prism4jThemeDefault create() {
        return new Prism4jThemeDefault(0xFFf5f2f0);
    }

    /**
     * @since 3.0.0
     */
    @NonNull
    public static Prism4jThemeDefault create(@ColorInt int background) {
        return new Prism4jThemeDefault(background);
    }

    private final int background;

    public Prism4jThemeDefault(@ColorInt int background) {
        this.background = background;
    }

    @Override
    public int background() {
        return background;
    }

    @Override
    public int textColor() {
        return 0xdd000000;
    }

    @NonNull
    @Override
    protected ColorHashMap init() {
        return new ColorHashMap()
                .add(0xFF708090, "comment", "prolog", "doctype", "cdata")
                .add(0xFF999999, "punctuation")
                .add(0xFF990055, "property", "tag", "boolean", "number", "constant", "symbol", "deleted")
                .add(0xFF669900, "selector", "attr-name", "string", "char", "builtin", "inserted")
                .add(0xFF9a6e3a, "operator", "entity", "url")
                .add(0xFF0077aa, "atrule", "attr-value", "keyword")
                .add(0xFFDD4A68, "function", "class-name")
                .add(0xFFee9900, "regex", "important", "variable");
    }

    @Override
    protected void applyColor(
            @NonNull String language,
            @NonNull String type,
            @Nullable String alias,
            @ColorInt int color,
            @NonNull SpannableStringBuilder builder,
            int start,
            int end) {

        if ("css".equals(language) && isOfType("string", type, alias)) {
            super.applyColor(language, type, alias, 0xFF9a6e3a, builder, start, end);
            builder.setSpan(new BackgroundColorSpan(0x80ffffff), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return;
        }

        if (isOfType("namespace", type, alias)) {
            color = applyAlpha(.7F, color);
        }

        super.applyColor(language, type, alias, color, builder, start, end);

        if (isOfType("important", type, alias)
                || isOfType("bold", type, alias)) {
            builder.setSpan(new StrongEmphasisSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (isOfType("italic", type, alias)) {
            builder.setSpan(new EmphasisSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}

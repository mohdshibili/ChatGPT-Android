package com.codeware.chatgpt.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.StyleSpan;
import android.util.Pair;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.codeware.chatgpt.R;
import com.codeware.chatgpt.highlight.prism4j.Prism4j;
import com.codeware.chatgpt.highlight.syntax.Prism4jGrammarLocator;
import com.codeware.chatgpt.highlight.syntax.Prism4jSyntaxHighlight;
import com.codeware.chatgpt.highlight.syntax.Prism4jThemeDarkula;
import com.codeware.chatgpt.span.ClickCopySpan;
import com.codeware.chatgpt.span.CustomTypefaceSpan;
import com.codeware.chatgpt.span.MDCodeBlockSpan;
import com.codeware.chatgpt.span.VerticalImageSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpanUtils {
    public static void setBoldSpan(Editable editable) {
        //\*\*(.*?)\*\*

        List<String> bolds = new ArrayList<>();

        Matcher matcher = Pattern.compile("(\\*\\*)(.*?)(\\*\\*)", Pattern.MULTILINE).matcher(editable);
        while (matcher.find()) {
            if (matcher.group(2).length() > 1)
                bolds.add(matcher.group());
        }

        for (String mention : bolds) {
            String textContent = editable.toString();

            int startIndex = textContent.indexOf(mention, 0);

            editable.replace(startIndex, startIndex + mention.length(), mention.substring(2, mention.length() - 2));
            editable.setSpan(new StyleSpan(Typeface.BOLD), startIndex, startIndex + mention.length() - 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            editable.setSpan(new ForegroundColorSpan(Color.WHITE), startIndex, startIndex + mention.length() - 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public static String adjustCodeBlocks(String editable) {
        Matcher matcher = Pattern.compile("(^```[\\w]+[\\n]+)((?:.|\\n)*?)(^```(?:$|\\n))", Pattern.MULTILINE).matcher(editable);
        while (matcher.find()) {
            if (Objects.requireNonNull(matcher.group(2)).length() > 1) {
                editable.replace(matcher.group(), addSpace(matcher.group()));
            }
        }

        return editable;
    }

    public static void setCodeBlockSpanText(TextView textView, Editable editable, int iconSize) {
        List<String> codeBlocks = new ArrayList<>();

        Matcher matcher = Pattern.compile("(^```[\\w]+[\\n]+)((?:.|\\n)*?)(^```(?:$|\\n))", Pattern.MULTILINE).matcher(editable);
        while (matcher.find()) {
            if (Objects.requireNonNull(matcher.group(2)).length() > 1) {
                codeBlocks.add(matcher.group());
            }
        }

        for (String mention : codeBlocks) {
            String textContent = editable.toString();

            int startIndex = textContent.indexOf(mention);
            int endIndex = startIndex + mention.length();

            editable.replace(startIndex, startIndex + 3, "   ");
            editable.replace(endIndex - 3, endIndex, "   ");

            removeSpan(editable, StyleSpan.class, startIndex, endIndex);
            removeSpan(editable, ForegroundColorSpan.class, startIndex, endIndex);

            editable.setSpan(new ForegroundColorSpan(Color.WHITE), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            String codeLang = getCodeLang(mention);

            Prism4jSyntaxHighlight highlight = Prism4jSyntaxHighlight.create(new Prism4j(new Prism4jGrammarLocator()), Prism4jThemeDarkula.create(), "java");
            CharSequence highlighted = highlight.highlight(codeLang, editable.subSequence(startIndex + 3 + codeLang.length(), startIndex + mention.length()).toString());

            editable.replace(startIndex + 3 + codeLang.length(), startIndex + mention.length(), highlighted);

            List<Pair<Integer, Integer>> list = getLineIndexes(editable.subSequence(startIndex, endIndex).toString());

            Pair<Integer, Integer> firstItem = list.get(0);

            int start = firstItem.first;
            int end = firstItem.second;

            editable.setSpan(new MDCodeBlockSpan(
                            Color.parseColor("#343541"),
                            true,
                            false,
                            mention),
                    startIndex + start, startIndex + end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            Pair<Integer, Integer> secondItem = list.get(1);
            Pair<Integer, Integer> lastSecondItem = list.get(list.size() - 2);

            start = secondItem.first;
            end = lastSecondItem.second;

            editable.setSpan(new MDCodeBlockSpan(
                            Color.parseColor("#000000"),
                            false,
                            false,
                            mention),
                    startIndex + start, startIndex + end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            Pair<Integer, Integer> lastItem = list.get(list.size() - 1);

            start = lastItem.first;
            end = lastItem.second;

            editable.setSpan(new MDCodeBlockSpan(
                            Color.parseColor("#000000"),
                            false,
                            true,
                            mention),
                    startIndex + start, startIndex + end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            Drawable copyIcon = ContextCompat.getDrawable(textView.getContext(), R.drawable.ic_copy);
            assert copyIcon != null;
            copyIcon.setBounds(0, 0, iconSize, iconSize);

            String codeBlock = mention.substring(3 + getCodeLang(mention).length(), mention.length() - 3);

            editable.setSpan(new VerticalImageSpan(copyIcon), startIndex, startIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            editable.setSpan(new ClickCopySpan(codeBlock), startIndex, startIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            Typeface typeface = Typeface.create(ResourcesCompat.getFont(textView.getContext(), R.font.fira_code_regular), Typeface.NORMAL);
            editable.setSpan(new CustomTypefaceSpan(typeface), startIndex + 3 + codeLang.length(), startIndex + mention.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            int fontSize = textView.getContext().getResources().getDimensionPixelSize(R.dimen.code_font_size);
            editable.setSpan(new AbsoluteSizeSpan(fontSize), startIndex, startIndex + mention.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            editable.setSpan(new LeadingMarginSpan.Standard(20), startIndex, startIndex + mention.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public static String getCodeLang(String code) {
        Matcher matcher = Pattern.compile("(^```[\\w]+[\\n]+)((?:.|\\n)*?)(^```(?:$|\\n))", Pattern.MULTILINE).matcher(code);
        if (matcher.find()) {
            return matcher.group(1).replace("```", "").trim();
        }

        return "java";
    }

    public static List<Pair<Integer, Integer>> getLineIndexes(String content) {
        List<Pair<Integer, Integer>> list = new ArrayList<>();

        String[] lines = content.split("\n");

        int start;
        int end = 0;

        for (String line : lines) {
            start = end;

            if (line.length() == 0 || line.length() == 1) {
                list.add(new Pair<Integer, Integer>(start, start + 5));
            } else {
                list.add(new Pair<Integer, Integer>(start, start + line.length() - 1));
            }

            end = start + line.length() + 1;
        }
        return list;
    }

    public static String addSpace(String content) {
        StringBuilder sb = new StringBuilder();
        String[] lines = content.split("\n");

        for (String line : lines) {
            if (line.trim().length() < 5) {
                line = line + ">>>>>";
            }

            sb.append(line).append("\n");
        }

        return sb.toString();
    }

    public static <T> void removeSpan(Editable editable, Class<T> clazz) {
        int start = 0;
        int end = editable.length();

        T[] spansToRemove = editable.getSpans(start, end, clazz);

        for (T t : spansToRemove) {
            editable.removeSpan(t);
        }
    }

    public static <T> void removeSpan(Editable editable, Class<T> clazz, int start, int end) {
        T[] spansToRemove = editable.getSpans(start, end, clazz);

        for (T t : spansToRemove) {
            editable.removeSpan(t);
        }
    }
}

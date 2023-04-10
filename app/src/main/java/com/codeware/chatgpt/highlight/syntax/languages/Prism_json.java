package com.codeware.chatgpt.highlight.syntax.languages;


import static com.codeware.chatgpt.highlight.prism4j.Prism4j.pattern;
import static com.codeware.chatgpt.highlight.prism4j.Prism4j.token;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

import androidx.annotation.NonNull;

import com.codeware.chatgpt.highlight.prism4j.Prism4j;

@SuppressWarnings("unused")

public class Prism_json {

    @NonNull
    public static Prism4j.Grammar create(@NonNull Prism4j prism4j) {
        return Prism4j.grammar(
                "json",
                token("property", pattern(compile("\"(?:\\\\.|[^\\\\\"\\r\\n])*\"(?=\\s*:)", CASE_INSENSITIVE))),
                token("string", pattern(compile("\"(?:\\\\.|[^\\\\\"\\r\\n])*\"(?!\\s*:)"), false, true)),
                token("number", pattern(compile("\\b0x[\\dA-Fa-f]+\\b|(?:\\b\\d+\\.?\\d*|\\B\\.\\d+)(?:[Ee][+-]?\\d+)?"))),
                token("punctuation", pattern(compile("[{}\\[\\]);,]"))),
                // not sure about this one...
                token("operator", pattern(compile(":"))),
                token("boolean", pattern(compile("\\b(?:true|false)\\b", CASE_INSENSITIVE))),
                token("null", pattern(compile("\\bnull\\b", CASE_INSENSITIVE)))
        );
    }
}

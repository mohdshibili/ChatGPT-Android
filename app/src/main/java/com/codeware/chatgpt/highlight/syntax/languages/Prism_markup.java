package com.codeware.chatgpt.highlight.syntax.languages;


import static com.codeware.chatgpt.highlight.prism4j.Prism4j.pattern;
import static com.codeware.chatgpt.highlight.prism4j.Prism4j.token;
import static java.util.regex.Pattern.compile;

import androidx.annotation.NonNull;

import com.codeware.chatgpt.highlight.prism4j.Prism4j;

import java.util.regex.Pattern;

@SuppressWarnings("unused")
public abstract class Prism_markup {

    @NonNull
    public static Prism4j.Grammar create(@NonNull Prism4j prism4j) {
        final Prism4j.Token entity = token("entity", pattern(compile("&#?[\\da-z]{1,8};", Pattern.CASE_INSENSITIVE)));
        return Prism4j.grammar(
                "markup",
                token("comment", pattern(compile("<!--[\\s\\S]*?-->"))),
                token("prolog", pattern(compile("<\\?[\\s\\S]+?\\?>"))),
                token("doctype", pattern(compile("<!DOCTYPE[\\s\\S]+?>", Pattern.CASE_INSENSITIVE))),
                token("cdata", pattern(compile("<!\\[CDATA\\[[\\s\\S]*?]]>", Pattern.CASE_INSENSITIVE))),
                token(
                        "tag",
                        pattern(
                                compile("<\\/?(?!\\d)[^\\s>\\/=$<%]+(?:\\s+[^\\s>\\/=]+(?:=(?:(\"|')(?:\\\\[\\s\\S]|(?!\\1)[^\\\\])*\\1|[^\\s'\">=]+))?)*\\s*\\/?>", Pattern.CASE_INSENSITIVE),
                                false,
                                true,
                                null,
                                Prism4j.grammar(
                                        "inside",
                                        token(
                                                "tag",
                                                pattern(
                                                        compile("^<\\/?[^\\s>\\/]+", Pattern.CASE_INSENSITIVE),
                                                        false,
                                                        false,
                                                        null,
                                                        Prism4j.grammar(
                                                                "inside",
                                                                token("punctuation", pattern(compile("^<\\/?"))),
                                                                token("namespace", pattern(compile("^[^\\s>\\/:]+:")))
                                                        )
                                                )
                                        ),
                                        token(
                                                "attr-value",
                                                pattern(
                                                        compile("=(?:(\"|')(?:\\\\[\\s\\S]|(?!\\1)[^\\\\])*\\1|[^\\s'\">=]+)", Pattern.CASE_INSENSITIVE),
                                                        false,
                                                        false,
                                                        null,
                                                        Prism4j.grammar(
                                                                "inside",
                                                                token(
                                                                        "punctuation",
                                                                        pattern(compile("^=")),
                                                                        pattern(compile("(^|[^\\\\])[\"']"), true)
                                                                ),
                                                                entity
                                                        )
                                                )
                                        ),
                                        token("punctuation", pattern(compile("\\/?>"))),
                                        token(
                                                "attr-name",
                                                pattern(
                                                        compile("[^\\s>\\/]+"),
                                                        false,
                                                        false,
                                                        null,
                                                        Prism4j.grammar(
                                                                "inside",
                                                                token("namespace", pattern(compile("^[^\\s>\\/:]+:")))
                                                        )
                                                )
                                        )
                                )
                        )
                ),
                entity
        );
    }

    private Prism_markup() {
    }
}

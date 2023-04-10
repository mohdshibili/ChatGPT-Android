package com.codeware.chatgpt.highlight.syntax.languages;

import static com.codeware.chatgpt.highlight.prism4j.Prism4j.pattern;
import static com.codeware.chatgpt.highlight.prism4j.Prism4j.token;
import static java.util.regex.Pattern.compile;

import androidx.annotation.NonNull;

import com.codeware.chatgpt.highlight.prism4j.Prism4j;

import java.util.regex.Pattern;


public abstract class Prism_clike {

    @NonNull
    public static Prism4j.Grammar create(@NonNull Prism4j prism4j) {
        return Prism4j.grammar(
                "clike",
                token(
                        "comment",
                        pattern(compile("(^|[^\\\\])\\/\\*[\\s\\S]*?(?:\\*\\/|$)"), true),
                        pattern(compile("(^|[^\\\\:])\\/\\/.*"), true, true)
                ),
                token(
                        "string",
                        pattern(compile("([\"'])(?:\\\\(?:\\r\\n|[\\s\\S])|(?!\\1)[^\\\\\\r\\n])*\\1"), false, true)
                ),
                token(
                        "class-name",
                        pattern(
                                compile("((?:\\b(?:class|interface|extends|implements|trait|instanceof|new)\\s+)|(?:catch\\s+\\())[\\w.\\\\]+"),
                                true,
                                false,
                                null,
                                Prism4j.grammar("inside", token("punctuation", pattern(compile("[.\\\\]"))))
                        )
                ),
                token(
                        "keyword",
                        pattern(compile("\\b(?:if|else|while|do|for|return|in|instanceof|function|new|try|throw|catch|finally|null|break|continue)\\b"))
                ),
                token("boolean", pattern(compile("\\b(?:true|false)\\b"))),
                token("function", pattern(compile("[a-z0-9_]+(?=\\()", Pattern.CASE_INSENSITIVE))),
                token(
                        "number",
                        pattern(compile("\\b0x[\\da-f]+\\b|(?:\\b\\d+\\.?\\d*|\\B\\.\\d+)(?:e[+-]?\\d+)?", Pattern.CASE_INSENSITIVE))
                ),
                token("operator", pattern(compile("--?|\\+\\+?|!=?=?|<=?|>=?|==?=?|&&?|\\|\\|?|\\?|\\*|\\/|~|\\^|%"))),
                token("punctuation", pattern(compile("[{}\\[\\];(),.:]")))
        );
    }

    private Prism_clike() {
    }
}

package com.codeware.chatgpt.highlight.syntax.languages;


import static com.codeware.chatgpt.highlight.prism4j.Prism4j.pattern;
import static com.codeware.chatgpt.highlight.prism4j.Prism4j.token;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

import androidx.annotation.NonNull;

import com.codeware.chatgpt.highlight.prism4j.GrammarUtils;
import com.codeware.chatgpt.highlight.prism4j.Prism4j;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")

public class Prism_javascript {

    @NonNull
    public static Prism4j.Grammar create(@NonNull Prism4j prism4j) {

        final Prism4j.Grammar js = GrammarUtils.extend(GrammarUtils.require(prism4j, "clike"), "javascript",
                token("keyword", pattern(compile("\\b(?:as|async|await|break|case|catch|class|const|continue|debugger|default|delete|do|else|enum|export|extends|finally|for|from|function|get|if|implements|import|in|instanceof|interface|let|new|null|of|package|private|protected|public|return|set|static|super|switch|this|throw|try|typeof|var|void|while|with|yield)\\b"))),
                token("number", pattern(compile("\\b(?:0[xX][\\dA-Fa-f]+|0[bB][01]+|0[oO][0-7]+|NaN|Infinity)\\b|(?:\\b\\d+\\.?\\d*|\\B\\.\\d+)(?:[Ee][+-]?\\d+)?"))),
                token("function", pattern(compile("[_$a-z\\xA0-\\uFFFF][$\\w\\xA0-\\uFFFF]*(?=\\s*\\()", CASE_INSENSITIVE))),
                token("operator", pattern(compile("-[-=]?|\\+[+=]?|!=?=?|<<?=?|>>?>?=?|=(?:==?|>)?|&[&=]?|\\|[|=]?|\\*\\*?=?|\\/=?|~|\\^=?|%=?|\\?|\\.{3}")))
        );

        GrammarUtils.insertBeforeToken(js, "keyword",
                token("regex", pattern(
                        compile("((?:^|[^$\\w\\xA0-\\uFFFF.\"'\\])\\s])\\s*)\\/(\\[[^\\]\\r\\n]+]|\\\\.|[^/\\\\\\[\\r\\n])+\\/[gimyu]{0,5}(?=\\s*($|[\\r\\n,.;})\\]]))"),
                        true,
                        true
                )),
                token(
                        "function-variable",
                        pattern(
                                compile("[_$a-z\\xA0-\\uFFFF][$\\w\\xA0-\\uFFFF]*(?=\\s*=\\s*(?:function\\b|(?:\\([^()]*\\)|[_$a-z\\xA0-\\uFFFF][$\\w\\xA0-\\uFFFF]*)\\s*=>))", CASE_INSENSITIVE),
                                false,
                                false,
                                "function"
                        )
                ),
                token("constant", pattern(compile("\\b[A-Z][A-Z\\d_]*\\b")))
        );

        final Prism4j.Token interpolation = token("interpolation");

        GrammarUtils.insertBeforeToken(js, "string",
                token(
                        "template-string",
                        pattern(
                                compile("`(?:\\\\[\\s\\S]|\\$\\{[^}]+\\}|[^\\\\`])*`"),
                                false,
                                true,
                                null,
                                Prism4j.grammar(
                                        "inside",
                                        interpolation,
                                        token("string", pattern(compile("[\\s\\S]+")))
                                )
                        )
                )
        );

        final Prism4j.Grammar insideInterpolation;
        {
            final List<Prism4j.Token> tokens = new ArrayList<>(js.tokens().size() + 1);
            tokens.add(token(
                    "interpolation-punctuation",
                    pattern(compile("^\\$\\{|\\}$"), false, false, "punctuation")
            ));
            tokens.addAll(js.tokens());
            insideInterpolation = Prism4j.grammar("inside", tokens);
        }

        interpolation.patterns().add(pattern(
                compile("\\$\\{[^}]+\\}"),
                false,
                false,
                null,
                insideInterpolation
        ));

        final Prism4j.Grammar markup = prism4j.grammar("markup");
        if (markup != null) {
            GrammarUtils.insertBeforeToken(markup, "tag",
                    token(
                            "script", pattern(
                                    compile("(<script[\\s\\S]*?>)[\\s\\S]*?(?=<\\/script>)", CASE_INSENSITIVE),
                                    true,
                                    true,
                                    "language-javascript",
                                    js
                            )
                    )
            );
        }

        return js;
    }
}

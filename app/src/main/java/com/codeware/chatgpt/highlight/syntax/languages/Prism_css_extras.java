package com.codeware.chatgpt.highlight.syntax.languages;


import static com.codeware.chatgpt.highlight.prism4j.Prism4j.pattern;
import static com.codeware.chatgpt.highlight.prism4j.Prism4j.token;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codeware.chatgpt.highlight.prism4j.GrammarUtils;
import com.codeware.chatgpt.highlight.prism4j.Prism4j;

@SuppressWarnings("unused")
public class Prism_css_extras {

    @Nullable
    public static Prism4j.Grammar create(@NonNull Prism4j prism4j) {

        final Prism4j.Grammar css = prism4j.grammar("css");

        if (css != null) {

            final Prism4j.Token selector = GrammarUtils.findToken(css, "selector");
            if (selector != null) {
                final Prism4j.Pattern pattern = pattern(
                        compile("[^{}\\s][^{}]*(?=\\s*\\{)"),
                        false,
                        false,
                        null,
                        Prism4j.grammar("inside",
                                token("pseudo-element", pattern(compile(":(?:after|before|first-letter|first-line|selection)|::[-\\w]+"))),
                                token("pseudo-class", pattern(compile(":[-\\w]+(?:\\(.*\\))?"))),
                                token("class", pattern(compile("\\.[-:.\\w]+"))),
                                token("id", pattern(compile("#[-:.\\w]+"))),
                                token("attribute", pattern(compile("\\[[^\\]]+\\]")))
                        )
                );
                selector.patterns().clear();
                selector.patterns().add(pattern);
            }

            GrammarUtils.insertBeforeToken(css, "function",
                    token("hexcode", pattern(compile("#[\\da-f]{3,8}", CASE_INSENSITIVE))),
                    token("entity", pattern(compile("\\\\[\\da-f]{1,8}", CASE_INSENSITIVE))),
                    token("number", pattern(compile("[\\d%.]+")))
            );
        }
        return null;
    }
}

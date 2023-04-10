package com.codeware.chatgpt.highlight.syntax.languages;


import static com.codeware.chatgpt.highlight.prism4j.Prism4j.pattern;
import static com.codeware.chatgpt.highlight.prism4j.Prism4j.token;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;

import androidx.annotation.NonNull;

import com.codeware.chatgpt.highlight.prism4j.Prism4j;

@SuppressWarnings("unused")
public class Prism_git {

    @NonNull
    public static Prism4j.Grammar create(@NonNull Prism4j prism4j) {
        return Prism4j.grammar("git",
                token("comment", pattern(compile("^#.*", MULTILINE))),
                token("deleted", pattern(compile("^[-–].*", MULTILINE))),
                token("inserted", pattern(compile("^\\+.*", MULTILINE))),
                token("string", pattern(compile("(\"|')(?:\\\\.|(?!\\1)[^\\\\\\r\\n])*\\1", MULTILINE))),
                token("command", pattern(
                        compile("^.*\\$ git .*$", MULTILINE),
                        false,
                        false,
                        null,
                        Prism4j.grammar("inside",
                                token("parameter", pattern(compile("\\s--?\\w+", MULTILINE)))
                        )
                )),
                token("coord", pattern(compile("^@@.*@@$", MULTILINE))),
                token("commit_sha1", pattern(compile("^commit \\w{40}$", MULTILINE)))
        );
    }
}

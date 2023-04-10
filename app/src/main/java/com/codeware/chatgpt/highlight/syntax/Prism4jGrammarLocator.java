package com.codeware.chatgpt.highlight.syntax;


import androidx.annotation.NonNull;

import com.codeware.chatgpt.highlight.prism4j.GrammarLocator;
import com.codeware.chatgpt.highlight.prism4j.Prism4j;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_c;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_clike;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_clojure;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_cpp;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_csharp;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_css;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_dart;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_git;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_go;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_groovy;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_java;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_javascript;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_json;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_kotlin;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_latex;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_makefile;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_markdown;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_python;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_scala;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_sql;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_swift;
import com.codeware.chatgpt.highlight.syntax.languages.Prism_yaml;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Prism4jGrammarLocator implements GrammarLocator {
    @Override
    public Prism4j.Grammar grammar(@NonNull Prism4j prism4j, String language) {
        switch (language) {
            case "java":
            case "markup":
            case "html":
            case "svg":
            case "xml":
            case "mathml":
                return Prism_java.create(prism4j);
            case "clike":
                return Prism_clike.create(prism4j);
            case "kotlin":
                return Prism_kotlin.create(prism4j);
            case "c":
                return Prism_c.create(prism4j);
            case "clojure":
                return Prism_clojure.create(prism4j);
            case "cpp":
                return Prism_cpp.create(prism4j);
            case "csharp":
            case "dotnet":
                return Prism_csharp.create(prism4j);
            case "css":
                return Prism_css.create(prism4j);
            case "dart":
                return Prism_dart.create(prism4j);
            case "git":
                return Prism_git.create(prism4j);
            case "go":
                return Prism_go.create(prism4j);
            case "groovy":
                return Prism_groovy.create(prism4j);
            case "javascript":
                return Prism_javascript.create(prism4j);
            case "json":
            case "jsonp":
                return Prism_json.create(prism4j);
            case "latex":
                return Prism_latex.create(prism4j);
            case "makefile":
                return Prism_makefile.create(prism4j);
            case "markdown":
                return Prism_markdown.create(prism4j);
            case "python":
                return Prism_python.create(prism4j);
            case "scala":
                return Prism_scala.create(prism4j);
            case "sql":
                return Prism_sql.create(prism4j);
            case "swift":
                return Prism_swift.create(prism4j);
            case "yaml":
                return Prism_yaml.create(prism4j);
            default:
                return null;
        }
    }

    @NonNull
    @Override
    public Set<String> languages() {
        return new HashSet<>(Arrays.asList(
                "java",
                "clike",
                "kotlin",
                "c",
                "clojure",
                "cpp",
                "csharp",
                "css",
                "dart",
                "git",
                "go",
                "groovy",
                "javascript",
                "json",
                "jsonp",
                "latex",
                "makefile",
                "markdown",
                "markup",
                "html",
                "svg",
                "xml",
                "mathml",
                "python",
                "scala",
                "sql",
                "swift",
                "yaml"
        ));
    }
}


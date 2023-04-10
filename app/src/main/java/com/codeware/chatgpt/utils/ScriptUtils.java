package com.codeware.chatgpt.utils;


public class ScriptUtils {
    private static final String LINE_BREAK = "\n";
    private static final String SCRIPT_START = "javascript:(function(){\n";
    private static final String SCRIPT_END = "\n})();";

    public static String sendQuestion(String question) {
        return SCRIPT_START +
                "document.querySelector('div#__next textarea').value = `" + question + "`;" +
                LINE_BREAK +
                "document.querySelector('button.absolute').disabled = false;" +
                LINE_BREAK +
                "document.querySelector('button.absolute').click();" +
                SCRIPT_END;
    }

    public static String checkReplaying() {
        return SCRIPT_START +
                "if (document.querySelector('button.absolute > div')){" +
                LINE_BREAK +
                "return true" +
                LINE_BREAK +
                "}else{" +
                LINE_BREAK +
                "return false" +
                LINE_BREAK +
                "}" +
                SCRIPT_END;
    }

    public static String checkLoading() {
        return SCRIPT_START +
                "return document.querySelector('div#__next textarea')" +
                SCRIPT_END;
    }

    public static String checkHasHeader() {
        return SCRIPT_START +
                "return document.head" +
                SCRIPT_END;
    }

    public static String getCurrentResponse() {
        return SCRIPT_START +
                "return document.querySelector('div#__next div.group.w-full:nth-last-child(2)').innerHTML;" +
                SCRIPT_END;
    }

    public static String stopResponse() {
        return SCRIPT_START +
                "document.querySelector('button.relative').click();" +
                SCRIPT_END;
    }

    public static String retryResponse() {
        return SCRIPT_START +
                "document.querySelector('button.relative').click();" +
                SCRIPT_END;
    }

    public static String interceptFetch() {
        return SCRIPT_START +
                "'use strict';" +
                LINE_BREAK +
                "var headElement = (document.head || document.documentElement);" +
                LINE_BREAK +
                "var script = document.createElement('script');" +
                LINE_BREAK +
                "script.innerHTML = `function intercept() {" +
                LINE_BREAK +
                "const { fetch: originalFetch } = window;" +
                LINE_BREAK +
                "window.fetch = function(){" +
                LINE_BREAK +
                "var url = arguments[0]" +
                LINE_BREAK +
                "return originalFetch.apply(this, arguments).then(function(response) {" +
                LINE_BREAK +
                "const contentType = response.headers.get('content-type');" +
                LINE_BREAK +
                "if (contentType && contentType.includes('application/json')) {" +
                LINE_BREAK +
                "response.clone().text().then(function (body) {" +
                LINE_BREAK +
                "window.Android.onFetchResponse(JSON.stringify({'data':body,'url': url}));" +
                LINE_BREAK +
                "});" +
                LINE_BREAK +
                "}" +
                LINE_BREAK +
                "return response;" +
                LINE_BREAK +
                "});" +
                LINE_BREAK +
                "}" +
                LINE_BREAK +
                "}`;" +
                LINE_BREAK +
                "headElement.insertBefore(script, headElement.firstElementChild);" +
                SCRIPT_END +
                LINE_BREAK +
                "intercept()";
    }
}

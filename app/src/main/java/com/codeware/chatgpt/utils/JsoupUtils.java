package com.codeware.chatgpt.utils;

import com.codeware.chatgpt.model.MessageItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class JsoupUtils {
    public static ArrayList<MessageItem> scrapMessage(String response) {
        ArrayList<MessageItem> messageItems = new ArrayList<>();

        Document doc = Jsoup.parse(response);
        Element markdown = doc.select("div.markdown").first();

        if (markdown == null || markdown.childrenSize() == 0) {
            MessageItem messageItem = new MessageItem();
            messageItem.setTAG_TYPE(TAG_TYPE.P);
            messageItem.setMessage("Error occured! May need to restart");
            messageItems.add(messageItem);

            return messageItems;
        }

        for (Element child : markdown.children()) {
            if (child.tagName().equals("p")) {
                MessageItem messageItem = new MessageItem();
                messageItem.setTAG_TYPE(TAG_TYPE.P);
                messageItem.setMessage(child.html().replace("<code>", "**").replace("</code>", "**").replace("&lt;", "<").replace("&gt;", ">"));
                messageItems.add(messageItem);

                continue;
            }

            if (child.tagName().equals("ol")) {
                int index = 1;

                if (child.hasAttr("start")) {
                    index = Integer.parseInt(child.attr("start").trim());
                }

                for (Element li : child.children()) {
                    MessageItem messageItem = new MessageItem();
                    messageItem.setTAG_TYPE(TAG_TYPE.P);
                    messageItem.setMessage(index + ". " + li.text());
                    messageItems.add(messageItem);

                    index++;
                }

                continue;
            }

            if (child.tagName().equals("pre")) {
                String lang = child.select("div > div > span").text();

                MessageItem messageItem = new MessageItem();
                messageItem.setTAG_TYPE(TAG_TYPE.CODE);
                messageItem.setMessage("```" + lang + "\n\n" + child.select("div > div > code").text().replace("\\n", "\n").replace("\\t", "\t")/*.replace("&lt;","<").replace("&gt;",">")*/.trim() + "  \n```");
                messageItem.setCodeLanguage(lang);
                messageItems.add(messageItem);

                continue;
            }

            MessageItem messageItem = new MessageItem();
            messageItem.setTAG_TYPE(TAG_TYPE.P);
            messageItem.setMessage(child.text());
            messageItems.add(messageItem);
        }

        return messageItems;
    }

    public enum TAG_TYPE {
        P,
        CODE;
    }
}

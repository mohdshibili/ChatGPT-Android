package com.codeware.chatgpt.span;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

public class ClickCopySpan extends ClickableSpan {
    private final String content;

    public ClickCopySpan(String content) {
        this.content = content;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(final View widget) {
		((ClipboardManager) widget.getContext().getSystemService(widget.getContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", content.trim()));
        showMessage(widget.getContext(), "Code copied");
    }

    private void showMessage(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

}

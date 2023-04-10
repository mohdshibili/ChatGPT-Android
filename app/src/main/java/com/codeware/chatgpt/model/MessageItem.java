package com.codeware.chatgpt.model;

import com.codeware.chatgpt.utils.JsoupUtils;

public class MessageItem
{
	String message;
	JsoupUtils.TAG_TYPE tAG_TYPE;
	String codeLanguage;
	
	public MessageItem(){
		
	}

	public void setCodeLanguage(String codeLanguage)
	{
		this.codeLanguage = codeLanguage;
	}

	public String getCodeLanguage()
	{
		return codeLanguage;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}

	public void setTAG_TYPE(JsoupUtils.TAG_TYPE tAG_TYPE)
	{
		this.tAG_TYPE = tAG_TYPE;
	}

	public JsoupUtils.TAG_TYPE getTAG_TYPE()
	{
		return tAG_TYPE;
	}
}

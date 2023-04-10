package com.codeware.chatgpt.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.codeware.chatgpt.model.ChatItem;
import com.codeware.chatgpt.model.MessageItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
	public static final String MESSAGE_TABLE_NAME = "messages";

	public static final String MESSAGE_COLUMN_ID = "id";
	public static final String MESSAGE_COLUMN_QUESTION = "question";
	public static final String MESSAGE_COLUMN_ANSWER = "answer";
	public static final String MESSAGE_COLUMN_ANSWER_HTML = "answer_html";
	public static final String MESSAGE_COLUMN_TIME = "time";
	public static final String MESSAGE_COLUMN_RESPONSE_TIME = "response_time";

    public DatabaseHelper(Context context)
	{
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
	{
        // Create the "message" table
        db.execSQL("CREATE TABLE messages (id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, answer TEXT, answer_html TEXT, time INTEGER, response_time INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
        // Drop the "messages" table if it exists
        db.execSQL("DROP TABLE IF EXISTS messages");

        // Recreate the database
        onCreate(db);
    }


	public boolean insertMessage(String question, String answer, String answer_html, long time, long responseTime)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(MESSAGE_COLUMN_QUESTION, question);
		contentValues.put(MESSAGE_COLUMN_ANSWER, answer);
		contentValues.put(MESSAGE_COLUMN_ANSWER_HTML, answer_html);	
		contentValues.put(MESSAGE_COLUMN_TIME, time);
		contentValues.put(MESSAGE_COLUMN_RESPONSE_TIME, responseTime);
		db.insert("messages", null, contentValues);
		return true;
	}

	public Integer deleteMessage(Integer id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete("messages", 
						 "id = ? ", 
						 new String[] { Integer.toString(id) });
	}


	@SuppressLint("Range")
	public ArrayList<ChatItem> getAllMessages()
	{
		ArrayList<ChatItem> messages = new ArrayList<>();

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor =  db.rawQuery("select * from messages", null);

		while (cursor.moveToNext())
		{
			ChatItem chatItem = new ChatItem();
			chatItem.setId(cursor.getInt(cursor.getColumnIndex(MESSAGE_COLUMN_ID)));
			chatItem.setQuestion(cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_QUESTION)));
			chatItem.setAnswerHtml(cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_ANSWER_HTML)));
			chatItem.setTime(cursor.getLong(cursor.getColumnIndex(MESSAGE_COLUMN_TIME)));
			chatItem.setResponseFinishTime(cursor.getLong(cursor.getColumnIndex(MESSAGE_COLUMN_RESPONSE_TIME)));
			chatItem.setTypingFinished(true);
			chatItem.setMessage(new Gson().fromJson(cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_ANSWER)), new TypeToken<ArrayList<MessageItem>>(){}.getType()));

			messages.add(chatItem);
			
		}

		cursor.close();
		db.close();
		return messages;
	}


	@SuppressLint("Range")
	public ArrayList<ChatItem> search(String searchTerm)
	{
		ArrayList<ChatItem> messages = new ArrayList<>();
		
		// Define the SQL query
		String query = "SELECT * FROM messages WHERE question LIKE '%" + searchTerm + "%' OR answer LIKE '%" + searchTerm + "%'";

		// Get a readable database
		SQLiteDatabase db = getReadableDatabase();

		// Execute the query and get the cursorults as a cursor
		Cursor cursor = db.rawQuery(query, null);

		// Iterate through the cursorults
		while (cursor.moveToNext())
		{
			ChatItem chatItem = new ChatItem();
			chatItem.setId(cursor.getInt(cursor.getColumnIndex(MESSAGE_COLUMN_ID)));
			chatItem.setQuestion(cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_QUESTION)));
			chatItem.setAnswerHtml(cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_ANSWER_HTML)));
			chatItem.setTime(cursor.getLong(cursor.getColumnIndex(MESSAGE_COLUMN_TIME)));
			chatItem.setResponseFinishTime(cursor.getLong(cursor.getColumnIndex(MESSAGE_COLUMN_RESPONSE_TIME)));
			chatItem.setTypingFinished(true);
			chatItem.setMessage(new Gson().fromJson(cursor.getString(cursor.getColumnIndex(MESSAGE_COLUMN_ANSWER)), new TypeToken<ArrayList<MessageItem>>(){}.getType()));

			messages.add(chatItem);
		}

		// Close the cursor and database
		cursor.close();
		db.close();

		return messages;
	}
}

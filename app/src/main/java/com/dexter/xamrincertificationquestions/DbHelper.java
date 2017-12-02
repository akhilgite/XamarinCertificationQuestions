package com.dexter.xamrincertificationquestions;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DbHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "xamarin_questions";
	// tasks table name
	private static final String TABLE_QUEST = "quest";
	// tasks Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_QUES = "question";
	private static final String KEY_ANSWER = "answer"; //correct option
	private static final String KEY_OPT_JSON= "opt_json"; //options in json format
	private static final String KEY_QUE_TYPE= "opt_type"; //option type
	private SQLiteDatabase dbase;
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		dbase=db;
		/*String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_QUEST + " ( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUES
				+ " TEXT, " + KEY_ANSWER+ " TEXT, "+KEY_OPTA +" TEXT, "
				+KEY_OPTB +" TEXT, "+KEY_OPTC+" TEXT)";*/

		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_QUEST + " ( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUES
				+ " TEXT, " + KEY_ANSWER+ " TEXT, "+KEY_OPT_JSON +" TEXT, "
				+KEY_QUE_TYPE +" TEXT)";
		db.execSQL(sql);		
		addQuestions();
		//db.close();
	}
	private void addQuestions()
	{
		Question.Options o1=new Question.Options(new String[]{"Jack sParrow","Jasa Programmer","Jasa new"});
		Question.Answers a1=new Question.Answers(new String[]{"Jasa Programmer"});
		Question q1=new Question("What is JP?",Utility.toJson(o1), Constants.QUESTION_TYPE_SINGLE,Utility.toJson(a1));
		this.addQuestion(q1);

		Question.Options o2=new Question.Options(new String[]{"Monas, Jakarta", "Gelondong, Bangun Tapan, bantul", "Gelondong, Bangun Tapan"});
		Question.Answers a2=new Question.Answers(new String[]{"Gelondong, Bangun Tapan, bantul"});
		Question q2=new Question("where the JP place?",Utility.toJson(o2), Constants.QUESTION_TYPE_SINGLE,Utility.toJson(a2));
		this.addQuestion(q2);

		Question.Options o3=new Question.Options(new String[]{"Usman and Jack", "Jack and Rully","Rully and Usman"});
		Question.Answers a3=new Question.Answers(new String[]{"Rully and Usman"});
		Question q3=new Question("who is CEO of the JP?",Utility.toJson(o3), Constants.QUESTION_TYPE_SINGLE,Utility.toJson(a3));
		this.addQuestion(q3);

		Question.Options o4=new Question.Options(new String[]{"JP is programmer home", "JP also realigy home", "all answer is true"});
		Question.Answers a4=new Question.Answers(new String[]{"JP is programmer home","all answer is true"});
		Question q4=new Question("what do you know about JP?",Utility.toJson(o4), Constants.QUESTION_TYPE_MULTIPLE,Utility.toJson(a4));
		this.addQuestion(q4);

		Question.Options o5=new Question.Options(new String[]{"Realigy","Programming","all answer is true","all answer is true"});
		Question.Answers a5=new Question.Answers(new String[]{"all answer is true","all answer is true"});
		Question q5=new Question("what do you learn in JP?",Utility.toJson(o5), Constants.QUESTION_TYPE_MULTIPLE,Utility.toJson(a5));
		this.addQuestion(q5);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
		// Create tables again
		onCreate(db);
	}
	// Adding new question
	public void addQuestion(Question quest) {
		//SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_QUES, quest.getQUESTION()); 
		values.put(KEY_ANSWER, quest.getANSWER());
		values.put(KEY_OPT_JSON, quest.getOPT_JSON());
		values.put(KEY_QUE_TYPE, quest.getQUE_TYPE());
		// Inserting Row
		dbase.insert(TABLE_QUEST, null, values);		
	}
	public List<Question> getAllQuestions() {
		List<Question> quesList = new ArrayList<Question>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
		dbase=this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Question quest = new Question();
				quest.setID(cursor.getInt(0));
				quest.setQUESTION(cursor.getString(1));
				quest.setANSWER(cursor.getString(2));
				quest.setOPT_JSON(cursor.getString(3));
				quest.setQUE_TYPE(cursor.getString(4));
				quesList.add(quest);
			} while (cursor.moveToNext());
		}
		// return quest list
		return quesList;
	}
	public int rowcount()
	{
		int row=0;
		String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		row=cursor.getCount();
		return row;
	}
}

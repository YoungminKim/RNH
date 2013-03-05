package ym.project.game.rnh;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBManager extends SQLiteOpenHelper {

	public DBManager(Context context) {
		super(context, "RnH.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("Create table RnHrank (min1 integer, min2 integer, sec1 integer, sec2 integer, totalTime integer);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table RnHrank;");
		onCreate(db);
	}

}

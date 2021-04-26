package edu.dali.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public void onCreate(SQLiteDatabase db) {
		String sql="create table user(id integer primary key autoincrement,username varchar(20),password varchar(20),shenfen varchar(20),imagU varchar(20))";
		db.execSQL(sql);
	}
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public long select() {
		SQLiteDatabase sqldb = this.getWritableDatabase();
		Cursor cursor = sqldb.rawQuery("select count(1) from person", null);
		// 该查询语句值返回一条语句
		cursor.moveToFirst();
		long result = cursor.getLong(0);
		cursor.close();
		return result;

	}
}

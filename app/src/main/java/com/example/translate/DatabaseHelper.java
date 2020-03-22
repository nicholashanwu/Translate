package com.example.translate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "language.db";
	public static final String TABLE_NAME = "content_table";
	public static final String COL_1 = "id_pk";
	public static final String COL_2 = "phrase";
	public static final String COL_3 = "category";
	public static final String COL_4 = "learned";



	public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}

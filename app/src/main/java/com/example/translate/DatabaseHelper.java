package com.example.translate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "language_.db";

    private static final String TABLE_NAME = "content_table";

    private static final String COL_1 = "id_pk";
    private static final String COL_2 = "phraseEn";
    private static final String COL_3 = "phraseCn";
    private static final String COL_4 = "pinyin";
    private static final String COL_5 = "category";
    private static final String COL_6 = "learned";
    private static final String COL_7 = "saved";

    private static final String A_TABLE_NAME = "achievement_table";
    private static final String ATT_1 = "id_pk";
    private static final String ATT_2 = "name";
    private static final String ATT_3 = "description";
    private static final String ATT_4 = "currentProgress";
    private static final String ATT_5 = "totalProgress";
    private static final String ATT_6 = "complete";

	private static final String SCORE_TABLE_NAME = "score_table";
	private static final String VAR_1 = "id_pk";
	private static final String VAR_2 = "name";
	private static final String VAR_3 = "score";

	public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "PHRASEEN TEXT, " +
                "PHRASECN TEXT, " +
                "PINYIN TEXT, " +
                "CATEGORY TEXT, " +
                "LEARNED BOOLEAN, " +
                "SAVED BOOLEAN)");


        db.execSQL("CREATE TABLE " + A_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT, " +
                "DESCRIPTION TEXT, " +
                "CURRENTPROGRESS TEXT, " +
                "TOTALPROGRESS TEXT, " +
                "COMPLETE BOOLEAN)");

		db.execSQL("CREATE TABLE " + SCORE_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"NAME TEXT, " +
				"SCORE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + A_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String phraseEn, String phraseCn, String pinyin, String category, boolean learned, boolean saved) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, phraseEn);
        contentValues.put(COL_3, phraseCn);
        contentValues.put(COL_4, pinyin);
        contentValues.put(COL_5, category);
        contentValues.put(COL_6, learned);
        contentValues.put(COL_7, saved);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean insertAchievementData(String name, String description, int currentProgress, int totalProgress, boolean complete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ATT_2, name);
        contentValues.put(ATT_3, description);
        contentValues.put(ATT_4, currentProgress);
        contentValues.put(ATT_5, totalProgress);
        contentValues.put(ATT_6, complete);
        long result = db.insert(A_TABLE_NAME, null, contentValues);
        return result != -1;
    }

	public boolean insertScoreData(String name, int count) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(VAR_2, name);
		contentValues.put(VAR_3, count);
		long result = db.insert(SCORE_TABLE_NAME, null, contentValues);
		return result != -1;
	}

	public Cursor getAllData() {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
	}

    public Cursor getAchievements() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + A_TABLE_NAME, null);
    }

	public Cursor getScores() {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("SELECT * FROM " + SCORE_TABLE_NAME, null);
	}

    ////////////////////////////////////////////

    public Cursor getCategory(String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE category = '" + category + "'", null);
    }

    public Cursor getSaved() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE saved = 1", null);
    }

    public Cursor getLearned() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE learned = 1", null);
    }

	public Cursor getSaveStatus(String phraseEn) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE phraseEn = '" + phraseEn + "'", null);
	}

	public Cursor getLearnedStatus(String phraseEn) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE phraseEn = '" + phraseEn + "'", null);
	}

	public void updateSave(String phraseEn, boolean saved) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(COL_7, saved);
		db.update(TABLE_NAME, contentValues, "phraseEn = ?", new String[]{phraseEn});
	}

	public void updateLearned(String phraseEn, boolean learned) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(COL_6, learned);
		db.update(TABLE_NAME, contentValues, "phraseEn = ?", new String[]{phraseEn});
	}

	public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
    }

	////

    public void clearMyList() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME + " WHERE category = 'custom'");
    }

    public void deletePhrase(String phraseEn) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME + " WHERE category = 'custom' AND phraseEn = '" + phraseEn + "' ");
    }

	////

    public boolean progressAchievement(String achievementName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor res = db.rawQuery("SELECT currentProgress, totalProgress, complete FROM " + A_TABLE_NAME + " WHERE name = '" + achievementName + "' ", null);
        res.moveToFirst();
        int curPro = Integer.valueOf(res.getString(0));
        int totPro = Integer.valueOf(res.getString(1));
        String com = res.getString(2);
        res.close();
        if (com.equals("0")) {                   //if has not been achieved, add 1 to progress
            curPro++;
            contentValues.put(ATT_4, curPro);
            db.update(A_TABLE_NAME, contentValues, "name = ?", new String[]{achievementName});

            if (curPro == totPro) {
                contentValues.put(ATT_6, "1");
                db.update(A_TABLE_NAME, contentValues, "name = ?", new String[]{achievementName});
                updateScore(achievementName);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public boolean checkAchievementStatus(String achievementName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT complete FROM " + A_TABLE_NAME + " WHERE name = '" + achievementName + "' ", null);
        res.moveToFirst();
        return res.getString(0).equals("1");
    }

    public Cursor getAchieved() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + A_TABLE_NAME + " WHERE complete = '1'", null);
    }

	////

    public void updateScore(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + SCORE_TABLE_NAME + " SET score = score + 1 WHERE name = '" + name + "'");

	}

}

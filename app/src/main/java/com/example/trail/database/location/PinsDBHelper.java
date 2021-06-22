package com.example.trail.database.location;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


/** Local SQLite database for saving location data
 * PinDTO */
public class PinsDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pins.db";
    public static final String TABLE_NAME = "PINS";
    public static final int DATABASE_VERSION = 1;

    public final String ID = "_id";                 // private key (PK)
    public final String PATH = "path_id";           // foreign key (FK): 속한 경로의 id
    public final String LOG = "log_id";             // (FK)            : 연결된 장소기록(log) id
    public final String TIME = "timestamp";         // format: YEAR.MM.DD.HH.mm.ss
    public final String LAT = "latitude";           // format:
    public final String LONG = "longitude";         // "
    public final String FLAG = "flag";              // todo think..기록 타입?? status??

    public PinsDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                LAT + " TEXT, " + // 실수값으로 저장 할까
                LONG + " TEXT, " +   // 아니면정수
                FLAG + " BOOLEAN, " +
                LOG + " INTEGER, " +
                "FOREIGN KEY(" + PATH + ") REFERENCES " + PathsDBHelper.DATABASE_NAME + "("+ PathsDBHelper.ID +"))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_NAME);
        onCreate(db);
    }
}
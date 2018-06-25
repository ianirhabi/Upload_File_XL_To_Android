package com.example.arnav.androidcsv.demo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

    public class DBController extends SQLiteOpenHelper {
        private static final String LOGCAT = null;

        public DBController(Context applicationcontext) {

            super(applicationcontext, "journal.db", null, 1);  // creating DATABASE

            Log.d(LOGCAT, "Created");

        }



        @Override
        public void onCreate(SQLiteDatabase database) {

            String query;

            query = "CREATE TABLE IF NOT EXISTS tbljournal ( Name TEXT, Issn TEXT PRIMARY KEY, ImpactFactor REAL)";

            database.execSQL(query);

        }


        @Override
        public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
              String query;
            query = "DROP TABLE IF EXISTS tbljournal";
            database.execSQL(query);
                       onCreate(database);
        }



        public ArrayList<HashMap<String, String>> getAllProducts() {

            ArrayList<HashMap<String, String>> journalList;
            journalList = new ArrayList<HashMap<String, String>>();
            String selectQuery = "SELECT  * FROM tbljournal";
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {

                do {
                    //Id, Company,Name,Price
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("a", cursor.getString(0));
                    map.put("b", cursor.getString(1));
                    map.put("c", cursor.getString(2));
                    journalList.add(map);
                    Log.e("dataofList",cursor.getString(0)+","+cursor.getString(1)+","+cursor.getString(2));
                    } while (cursor.moveToNext());
                }
            return journalList;

        }

    }

package com.example.arnav.androidcsv.demo;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity {


    TextView lbl;
    DBController controller;
    Button btnimport;
    ListView lv;
    final Context context = this;
    ListAdapter adapter;

    ArrayList<HashMap<String, String>> myList;
    public static final int requestcode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new DBController(this);
        lbl = (TextView) findViewById(R.id.txtresulttext);
        btnimport = (Button) findViewById(R.id.btnupload);
        lv = getListView();
        btnimport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("gagt/sdf");
                try {
                    startActivityForResult(fileintent, requestcode);
                } catch (ActivityNotFoundException e) {
                    lbl.setText("No activity can handle picking a file. Showing alternatives.");
                }
            }
        });

        myList = controller.getAllProducts();
        if (myList.size() != 0) {
            ListView lv = getListView();
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, myList,
                    R.layout.v, new String[]{"a", "b", "c"}, new int[]{
                    R.id.txtproductcompany, R.id.txtproductname, R.id.txtproductprice});
            setListAdapter(adapter);
            lbl.setText("");

        }
            }

    /** you were wrong here
     * R.id.txtjournalname, R.id.txtjournalissn, R.id.txtjournalif});
     in v.xml its
     R.id.txtproductcompany, R.id.txtproductname, R.id.txtproductprice});
     */

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null)

        return;
               switch (requestCode) {

            case requestcode:

                String filepath = data.getData().getPath();
                controller = new DBController(getApplicationContext());
                SQLiteDatabase db = controller.getWritableDatabase();
                String tableName = "tbljournal";
                db.execSQL("delete from " + tableName);

                try {

                    if (resultCode == RESULT_OK) {

                        try {
                            FileReader file = new FileReader(filepath);
                            BufferedReader buffer = new BufferedReader(file);
                            ContentValues contentValues = new ContentValues();
                            String line = "";
                            db.beginTransaction();

                            while ((line = buffer.readLine()) != null) {


                                String[] str = line.split(",", 3);  // defining 3 columns with null or blank field //values acceptance

                                //Id, Company,Name,Price

                                String name = str[0].toString();
                                String issn = str[1].toString();
                                String imp = str[2].toString();
                                Log.e("data", name);
                                contentValues.put("Name", name);
                                contentValues.put("ISSN", issn);
                                contentValues.put("ImpactFactor", imp);
                                db.insert(tableName, null, contentValues);

                                lbl.setText("Successfully Updated Database.");

                            }
                            db.setTransactionSuccessful();

                            db.endTransaction();

                        }catch (SQLException e)
                        {
                            Log.e("Error",e.getMessage().toString());
                        }
                        catch (IOException e) {

                            if (db.inTransaction())
                            db.endTransaction();
                            Dialog d = new Dialog(this);
                            d.setTitle(e.getMessage().toString() + "first");
                            d.show();
                            // db.endTransaction();
                        }
                    } else {

                        if (db.inTransaction())

                        db.endTransaction();

                        Dialog d = new Dialog(this);

                        d.setTitle("Only CSV files allowed");

                        d.show();
                                            }
                                  } catch (Exception ex) {

                    if (db.inTransaction())

                    db.endTransaction();


                    Dialog d = new Dialog(this);

                    d.setTitle(ex.getMessage().toString() + "second");

                    d.show();

                    // db.endTransaction();

                }

        }

        myList = controller.getAllProducts();

        if (myList.size() != 0) {

            ListView lv = getListView();

            ListAdapter adapter = new SimpleAdapter(MainActivity.this, myList,

                    R.layout.v, new String[]{"a", "b", "c"}, new int[]{
                    R.id.txtproductcompany, R.id.txtproductname, R.id.txtproductprice});


            setListAdapter(adapter);

            lbl.setText("Data Imported");

        }    }
}
package com.example.divin_a.ssample2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Divin_A on 7/7/2016.
 */

public class ContactDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName2.db";
    public static final String CONTACTS_TABLE_NAME = "contacts2";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    private HashMap hp;

    public ContactDBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table contacts2 " +
                        "(id integer primary key, name text,phone text)"
        );

        db.execSQL(
                "create table Settings " +
                        "(id integer primary key, name text,value text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts2");
        db.execSQL("DROP TABLE IF EXISTS Settings");
        onCreate(db);
    }

    public boolean insertContact  (String name, String phone)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        db.insert("contacts2", null, contentValues);
        return true;
    }

    public void AddDevice(String device)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "BTDevice");
        contentValues.put("value", device);
        db.insert("Settings", null, contentValues);
        return ;
    }
    public  String GetDevice()
    {
        String frvuvr = "";
        try {
            String columnName="BTDevice";
            SQLiteDatabase db = this.getReadableDatabase();
            String[] columns ={"value"};
            // Cursor res = db.rawQuery("select * from Settings where name="+columnName+"", null);
            Cursor res = db.query("Settings", columns, "name=?", new String[] { columnName }, null, null, null);
            res.moveToFirst();


            while (res.isAfterLast() == false) {
                // String name = res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME));
                //String num = res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHONE));
                frvuvr = res.getString(res.getColumnIndex("value"));
                res.moveToNext();
            }
            res.close();
        }
        catch ( Exception ex )
        {
           // frvuvr = ex.toString();
           // Toast.makeText(, ex.toString(), Toast.LENGTH_LONG).show();
        }

        return frvuvr;
    }

    public Integer DeleteDevice (String device)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Settings",
                "name=?",
                new String[] {"BTDevice"});
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts2 where id="+id+"", null );
        return res;
    }

    public int numberOfRows()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        db.update("contacts2", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (String phone)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts2",
                "phone = ? ",
                new String[] {phone});
    }

    public ArrayList<ContactInfo> getAllCotacts()
    {
        ArrayList<ContactInfo> array_list = new ArrayList<ContactInfo>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts2", null );
        res.moveToFirst();

        while(res.isAfterLast() == false)
        {
            String name = res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME));
            String num = res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHONE));
            ContactInfo ctn = new ContactInfo( name, num );
            array_list.add(ctn);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
}
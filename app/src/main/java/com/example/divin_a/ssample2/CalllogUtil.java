package com.example.divin_a.ssample2;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.provider.CallLog;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
// import android.database.sqlite;
class ContactInfo
{
    public String mName;
    public String mNumber;
    public  String mDate = "";
    public  String mLabel = "";

    public ContactInfo(String Name, String Number )
    {
        mName = Name;
        mNumber = Number;
    }
    public ContactInfo(String Name, String Number, String strDate )
    {
        mName = Name;
        mNumber = Number;
        mDate = strDate;
    }

    public ContactInfo(String Name, String Number, String strDate,String strLabel )
    {
        mName = Name;
        mNumber = Number;
        mDate = strDate;
        mLabel = strLabel;
    }

}


/**
 * Created by Divin_A on 7/6/2016.
 */
public class CalllogUtil
{
    static  String[] PROJECTION = { CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL,CallLog.Calls.DURATION, CallLog.Calls.NUMBER, CallLog.Calls.DATE };
    static String ORDERBY = android.provider.CallLog.Calls.DATE + " DESC";

    private static CalllogUtil inst;
    private ArrayList<ContactInfo> mMissed = new ArrayList<>();
    private ArrayList<ContactInfo> mIncom= new ArrayList<>();
    private ArrayList<ContactInfo> mOut= new ArrayList<>();

    private ArrayList<ContactInfo> mQuick= new ArrayList<>();

    private boolean mQuickInit = false;
    private boolean mMissedInit = false;
    private boolean mOutInit = false;
    private boolean mIncomInit = false;

    public void ResetData()
    {
        mMissedInit = false;
        mOutInit = false;
        mIncomInit = false;
    }
    public static CalllogUtil Instance()
    {
        if( null == inst )
        {
            inst = new CalllogUtil();
        }
        return inst;
    }

    private void UpdateIncomListFromDB( Activity context_i )
    {
        HashMap<String, String> objCache = new HashMap<String, String>();
        mIncom.clear();
        String where = CallLog.Calls.TYPE+"="+CallLog.Calls.INCOMING_TYPE;
        Cursor cursor = null;
        try {

            if ( ContextCompat.checkSelfPermission(context_i, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED )
            {

                cursor = context_i.getContentResolver().query(CallLog.Calls.CONTENT_URI, PROJECTION, where, null, ORDERBY);
                //ContextCompat.requestPermissions( context_i, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                //        LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
            }
            else
            {
                // cursor = context_i.managedQuery(CallLog.Calls.CONTENT_URI, PROJECTION, where, null, ORDERBY);
                cursor = context_i.getContentResolver().query(CallLog.Calls.CONTENT_URI, PROJECTION, where, null, ORDERBY);
            }

            // c.moveToFirst();
            Log.d("CALL  count", ""+cursor.getCount()); //do some other operation

            if (cursor.moveToFirst())
            {
                do {

                    ContactInfo  info = FillInfo( cursor );

                    if( null != info )
                    {
                        if(objCache.containsKey(info.mNumber))
                        {

                        }
                        else
                        {
                            mIncom.add(info);
                            objCache.put(info.mNumber, "fdgsdf");
                        }
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        catch ( Exception  ex)
        {}
        mIncomInit = true;
    }
    private void UpdateOutFromDB( Activity context_i )
    {
        mOut.clear();
        HashMap<String, String> objCache = new HashMap<String, String>();
        String where = CallLog.Calls.TYPE+"="+CallLog.Calls.OUTGOING_TYPE;
        Cursor cursor = null;
        try {

            if ( ContextCompat.checkSelfPermission(context_i, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED )
            {

                cursor = context_i.getContentResolver().query(CallLog.Calls.CONTENT_URI, PROJECTION, where, null, ORDERBY);
                //ContextCompat.requestPermissions( context_i, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                //        LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
            }
            else
            {
                // cursor = context_i.managedQuery(CallLog.Calls.CONTENT_URI, PROJECTION, where, null, ORDERBY);
                cursor = context_i.getContentResolver().query(CallLog.Calls.CONTENT_URI, PROJECTION, where, null, ORDERBY);
            }

            // c.moveToFirst();
            Log.d("CALL  count", ""+cursor.getCount()); //do some other operation

            if (cursor.moveToFirst())
            {
                do {

                    ContactInfo  info = FillInfo( cursor );
                    if( null != info )
                    {
                        if(objCache.containsKey(info.mNumber))
                        {

                        }
                        else
                        {
                            mOut.add( info );
                            objCache.put(info.mNumber, "fdgsdf");
                        }
                    }

                } while (cursor.moveToNext());
                cursor.close();
            }

            // c = context_i.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, where, null, null);
            // c = context_i.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, where, null, null);
        }
        catch ( Exception  ex)
        {}
        mOutInit = true;
    }
    private void UpdateMissedListFromDB( Activity context_i )
    {
        mMissed.clear();
        HashMap<String, String> objCache = new HashMap<String, String>();
        String where = CallLog.Calls.TYPE+"="+CallLog.Calls.MISSED_TYPE;
        Cursor cursor = null;
        try {

            if ( ContextCompat.checkSelfPermission(context_i, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED )
            {
               cursor = context_i.getContentResolver().query(CallLog.Calls.CONTENT_URI, PROJECTION, where, null, ORDERBY);
            }
            else
            {
               // cursor = context_i.managedQuery(CallLog.Calls.CONTENT_URI, PROJECTION, where, null, ORDERBY);
                cursor = context_i.getContentResolver().query(CallLog.Calls.CONTENT_URI, PROJECTION, where, null, ORDERBY);
            }

            if (cursor.moveToFirst())
            {
                do
                {
                    ContactInfo  info = FillInfo( cursor );
                    if( null != info )
                    {
                        if(objCache.containsKey(info.mNumber))
                        {

                        }
                        else
                        {
                            mMissed.add( info );
                            objCache.put(info.mNumber, "fdgsdf");
                        }
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        catch ( Exception  ex)
        {}
        mMissedInit = true;
    }

    private void UpdateQuickFromDB( Activity context_i )
    {
        mQuick.clear();
        try
        {
            String dbName = Environment.getExternalStorageDirectory()
                    + File.separator + "QuickContacts";

            SQLiteDatabase mydatabase = SQLiteDatabase.openOrCreateDatabase(dbName,null);
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS QuickContactInfo(Username VARCHAR,number VARCHAR);");

            // mydatabase.execSQL("INSERT INTO TutorialsPoint VALUES('admin','admin');");
          //  mydatabase.execSQL("INSERT INTO QuickContactInfo VALUES('Janet','8431583661');");
          //  mydatabase.execSQL("INSERT INTO QuickContactInfo VALUES('Home','04842505948');");
          //  mydatabase.execSQL("INSERT INTO QuickContactInfo VALUES('Appachan','09497678644');");
          //  mydatabase.execSQL("INSERT INTO QuickContactInfo VALUES('Dell bridge','180030103101');");




            Cursor resultSet = mydatabase.rawQuery("Select * from QuickContactInfo",null);
            if( resultSet.moveToFirst())
            {
                do {

                    ContactInfo  info = new ContactInfo(resultSet.getString(1),resultSet.getString(2));
                    if( null != info )
                    {
                        mQuick.add( info );
                    }
                } while (resultSet.moveToNext());
            }
        }
        catch ( Exception ex ){
            String str = ex.toString();
        }

        // SQLiteDatabase.ope
        mQuickInit = true;
    }

    public  ArrayList<ContactInfo> GetOutCalls( Activity context_i )
    {
        if( mOutInit )
        {
            return mOut;
        }
        //   ArrayList<ContactInfo> list = new ArrayList<ContactInfo>();
        UpdateOutFromDB(context_i);
        //  cursor.moveToFirst();
        return mOut;
    }
    public  ArrayList<ContactInfo> GetIncomCalls( Activity context_i )
    {
        if( mIncomInit )
        {
            return mIncom;
        }
        //   ArrayList<ContactInfo> list = new ArrayList<ContactInfo>();
        UpdateIncomListFromDB(context_i);
        //  cursor.moveToFirst();
        return mIncom;
    }
    public  ArrayList<ContactInfo> GetQuickContacts( Context context_i )
    {
        try {
            if (mQuickInit) {
                return mQuick;
            }
            mQuick.clear();

            // Toast.makeText(context_i, "Querying from DB", Toast.LENGTH_LONG).show();
            //   ArrayList<ContactInfo> list = new ArrayList<ContactInfo>();
            // UpdateQuickFromDB(context_i);
            ContactDBHelper obj = new ContactDBHelper(context_i);
            // obj.insertContact( "janet", "8431583661" );
            // obj.insertContact( "Home","04842505948");
            // obj.insertContact( "Appachan", "09497678644");
            // obj.insertContact( "Dell Bridge","180030103101");

            mQuickInit = true;
            mQuick = obj.getAllCotacts();
        }
        catch ( Exception ex )
        {
            Toast.makeText(context_i, ex.toString(), Toast.LENGTH_LONG).show();
        }
        //  cursor.moveToFirst();
        return mQuick;
    }
    public boolean insertContact (Context context_i, String name, String phone)
    {
        try {
            mQuick.clear();
            ContactDBHelper obj = new ContactDBHelper(context_i);
            obj.deleteContact( phone );
            obj.insertContact(name, phone);
            mQuick = obj.getAllCotacts();
        }
        catch ( Exception ex )
        {
            Toast.makeText(context_i, ex.toString(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public boolean deleteContact (Context context_i, String phone)
    {

        try {
            mQuick.clear();
            ContactDBHelper obj = new ContactDBHelper(context_i);
            obj.deleteContact(phone);
            mQuick = obj.getAllCotacts();
        }
        catch ( Exception ex )
        {
            Toast.makeText(context_i, ex.toString(), Toast.LENGTH_LONG).show();
        }
        return true;
    }


    public  ArrayList<ContactInfo> GetMissedCalls( Activity context_i )
    {
        if( mMissedInit )
        {
            return mMissed;
        }
        //   ArrayList<ContactInfo> list = new ArrayList<ContactInfo>();
        UpdateMissedListFromDB(context_i);
        //  cursor.moveToFirst();
        return mMissed;
    }


    private ContactInfo FillInfo( Cursor cursor)
    {
        ContactInfo info = null;
        try {
            String strCachedName = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String strNumLabel = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NUMBER_LABEL));
            String strNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            String strDuration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION ));
            String strDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE ));


            if( strCachedName != null )
            {
                if( strCachedName.isEmpty() )
                {
                    info =  new ContactInfo(strNumber,strNumber, strDate);
                }
                else
                {
                    if( null == strNumLabel)
                    {
                        info = new ContactInfo(strCachedName, strNumber, strDate);
                    }
                    else
                    {
                        info = new ContactInfo(strCachedName, strNumber, strDate, strNumLabel );
                    }
                }
            }
            else
            {
                info =  new ContactInfo(strNumber,"", strDate);
            }
        }
        catch (Exception ex)
        {
        }
        return  info;
    }
}

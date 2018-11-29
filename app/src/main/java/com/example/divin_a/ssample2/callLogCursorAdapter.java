package com.example.divin_a.ssample2;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Divin_A on 7/6/2016.
 */
public class callLogCursorAdapter extends CursorAdapter {

    public callLogCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.contact_dial_row, parent, false);

        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views

       // TextView textViewPersonName = (TextView) view.findViewById(R.id.Name);
        // textViewPersonName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
 //       textViewPersonName.setText(cursor.getString(cursor.getColumnIndex( CallLog.Calls.DURATION )));

       // TextView textViewPersonPIN = (TextView) view.findViewById(R.id.Number);
        // textViewPersonPIN.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
//        textViewPersonPIN.setText(cursor.getString(cursor.getColumnIndex( CallLog.Calls.NUMBER )));

 //       String callDate = cursor.getString(cursor.getColumnIndex( CallLog.Calls.DATE ));
       // Date callDayTime = new Date(Long.valueOf(callDate));


    }
}

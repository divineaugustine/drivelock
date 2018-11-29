package com.example.divin_a.ssample2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Divin_A on 5/24/2016.
 */
        import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;
import android.widget.Toast;

import static android.content.Intent.ACTION_CALL;

public class LazyContactAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<ContactInfo> data;
    private static LayoutInflater inflater=null;
    private  int meColor = Color.RED;
    // public ImageLoader imageLoader;

    private boolean mbCallType=true;
    public LazyContactAdapter(Activity a, ArrayList<ContactInfo> d, int col, boolean callType )
    {
        activity = a;
        data=d;
        meColor=col;
        mbCallType = callType;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /// imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return 0;
      /*  ContactInfo item = data.get(position);
        if( item.mDate != null && !item.mDate.isEmpty())
        {
            return 0;
        }
        else
        {
            return 1;
        }*/
        // return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    public static class ViewHolder
    {
        //public TextView Message;
       // public TextView ContactNum;
        public TextView Name;
        public TextView Date;
        public TextView Label;

        public Button btCall;
       // public ImageView img;
    }


    public static class ButttonTag
    {
        public String strNum;
        public View parentView;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ContactInfo item = data.get(position);

        boolean bQuickContact = false;
        if( item.mDate != null && !item.mDate.isEmpty())
        {
            bQuickContact = false;
        }



        View vi=convertView;
        ViewHolder holder;
        if(convertView==null)
        {
            if( !mbCallType )
            {
                vi = inflater.inflate(R.layout.quickcontactsmainrow, null);
            }
            else if( bQuickContact )
            {
                vi = inflater.inflate(R.layout.singlerowcontact, null);

            }
            else
            {
                vi = inflater.inflate(R.layout.contact_dial_row, null);
            }


          //  ((MainActivity)activity).IncrCounter(position);
            holder = new ViewHolder();
            holder.Name = (TextView) vi.findViewById(R.id.Name);
           // holder.ContactNum = (TextView) vi.findViewById(R.id.Number);

            if( !mbCallType )
            {
                holder.Date = (TextView) vi.findViewById(R.id.date);
            }
            else if( !bQuickContact )
            {
                holder.Label = (TextView) vi.findViewById(R.id.Label);
                holder.Date = (TextView) vi.findViewById(R.id.date);
            }
            holder.btCall = (Button)vi.findViewById(R.id.btCall);
            if( !mbCallType)
            {
                // holder.btCall.setBackgroundResource( android.R.drawable.ic_menu_delete);
                holder.btCall.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_delete, 0, 0, 0);
            }
           // holder.img = (ImageView)vi.findViewById(R.id.list_image);
          //  holder.Person = (TextView) vi.findViewById(R.id.Person);
            vi.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }


       // holder.img.setBackgroundColor( meColor);
        //  ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

       // ContactInfo item = data.get(position);

        // Setting all values in listview
        //holder.Message.setText( item.Message);
        if( null != item.mName )
        {
            holder.Name.setText( item.mName);
        }

       // if( null != item.mNumber )
        {
        // holder.ContactNum.setText(item.mNumber);
         //   holder.ContactNum.setText("");
        }

        if( !mbCallType )
        {
            holder.Date.setText(item.mNumber);
        }
        else if( !bQuickContact)
        {
            if (null != item.mDate) {
                if (item.mDate.equals("")) {
                    holder.Date.setVisibility(View.GONE);
                    holder.Date.setText("");
                } else {

                    try {
                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(Long.parseLong(item.mDate));
                        String date = DateFormat.format("EEE, d MMM yyyy HH:mm:ss", cal).toString();
                        holder.Date.setText(date);
                        holder.Date.setTextColor(meColor);
                        holder.Date.setVisibility(View.VISIBLE);
                    } catch (Exception ex) {
                    }
                }
            } else {
                holder.Date.setVisibility(View.GONE);
            }

            // vi.setBackgroundColor(meColor);
            if (null != item.mLabel) {
                holder.Label.setVisibility(View.VISIBLE);
                holder.Label.setText(item.mLabel);
                //holder.Label.setTextColor(meColor);
            } else {
                holder.Label.setVisibility(View.GONE);
            }
        }
        // holder.btCall.setBackgroundResource( android.);
        ButttonTag tag = new ButttonTag();
        tag.strNum = item.mNumber;
        tag.parentView = vi;

        holder.btCall.setTag( tag );
        holder.btCall.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ButttonTag) v.getTag()).parentView.setBackgroundResource(R.drawable.blueclick2);
                    // change color
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((ButttonTag) v.getTag()).parentView.setBackgroundResource(R.drawable.grey);
                    // set to normal color
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    ((ButttonTag) v.getTag()).parentView.setBackgroundResource(R.drawable.grey);
                    // set to normal color
                }
                return false;
            }
        });

        holder.btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strNum = ((ButttonTag) v.getTag()).strNum;
                if (strNum != null) {
                    if (mbCallType) {
                        DialNumber(v.getContext(), "tel:" + strNum);
                    } else {
                        new AlertDialog.Builder(LazyContactAdapter.this.activity)
                                .setTitle("Delete")
                                .setMessage("Do you really want to delete?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Toast.makeText(LazyContactAdapter.this.activity, "Yaay", Toast.LENGTH_SHORT).show();
                                        CalllogUtil.Instance().deleteContact(LazyContactAdapter.this.activity, strNum);
                                        data = CalllogUtil.Instance().GetQuickContacts(LazyContactAdapter.this.activity);
                                        LazyContactAdapter.this.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                }

                // Your code that you want to execute on this button click
            }

        });

       /* vi.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.test);
                    // change color
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.grey);
                    // set to normal color
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    v.setBackgroundResource(R.drawable.grey);
                    // set to normal color
                }
                return false;
            }
        });*/

        // imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);

/*
        vi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                // switch (event.getAction()) {
                // Toast.makeText(activity, "onTouch " + String.valueOf(event.getActionMasked()), Toast.LENGTH_SHORT).show();
                 ((MainActivity)activity).IncrCounter(event.getActionMasked());
                //((MainActivity)activity).PrintMessage(event.getActionMasked());

                switch (event.getActionMasked()) {



                    case MotionEvent.ACTION_DOWN:
                        ((MainActivity)activity).historicX = event.getX();
                        ((MainActivity)activity).historicY = event.getY();
                        return true;
                        // break;

                    case MotionEvent.ACTION_UP:
                        // TextView item = (TextView) v.findViewById(R.id.Name);
                        LazyContactAdapter.ViewHolder viewHolder = (LazyContactAdapter.ViewHolder) v.getTag();

                        String strName = "Error ";
                        if (viewHolder != null)
                        {
                            strName = viewHolder.Name.getText().toString();//item.getText().toString();
                        }
                        if (event.getX() - ((MainActivity)activity).historicX < -((MainActivity)activity).DELTA) {
                            // FunctionDeleteRowWhenSlidingLeft();
                            Toast.makeText(activity, "Swipe left " + strName, Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (event.getX() - ((MainActivity)activity).historicX > ((MainActivity)activity).DELTA) {
                            // FunctionDeleteRowWhenSlidingRight();
                            Toast.makeText(activity, "Swipe right " + strName, Toast.LENGTH_SHORT).show();

                            if (viewHolder != null)
                            {
                                TextView item = viewHolder.ContactNum;
                                ;// ((TextView)view).getText().toString();
                                String text = item.getText().toString();
                                ((MainActivity)activity).DialNumber("tel:" + text);
                            }
                            return true;
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        return true;
                    default:
                       // ((MainActivity)activity).historicX = 0;// event.getX();
                       // ((MainActivity)activity).historicY = 0;//event.getY();
                        return false;
                }
                return false;
            }
        });
*/
        return vi;
    }


    public static void DialNumber( Context ctx, String snumber )
    {
        if (ContextCompat.checkSelfPermission(ctx,
                Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            // Uri number = Uri.parse("tel:8431583661");
            Uri number = Uri.parse(snumber);
            Intent callIntent = new Intent(ACTION_CALL, number);
            ctx.startActivity(callIntent);
        }
    }
}
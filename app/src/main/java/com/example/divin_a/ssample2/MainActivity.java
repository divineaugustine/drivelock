package com.example.divin_a.ssample2;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Intent.ACTION_CALL;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // XML node keys
    static final String KEY_SONG = "song"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_ARTIST = "artist";
    static final String KEY_DURATION = "duration";
    static final String KEY_THUMB_URL = "thumb_url";
    // Declare
    static final int PICK_CONTACT=1;

    private int mnLoopCounter = 0;

    private ArrayList<String> mdevices;
    private ListPopupWindow listPopupWindow;
    ListView list;
    LazyContactAdapter mContactAdapter;
    // Map<Integer,String> myMap = new HashMap<Integer,String>();
    HashMap<String, String> ContactCache = new HashMap<String, String>();


    float historicX = Float.NaN, historicY = Float.NaN;
    static  int DELTA = 50;
    enum Direction {LEFT, RIGHT;}

    MainActivity objTHis;

    EditText edtName;
    EditText edtNum;
    Button mbtAdd;
    Button mbtCancel;
    LinearLayout mliLay;


    private void ShowButtons( boolean bShow )
    {
        if(bShow)
        {
            edtNum.setVisibility( View.VISIBLE );
            edtName.setVisibility(View.VISIBLE );
            mbtAdd.setVisibility(View.VISIBLE );
            mliLay.setVisibility( View.VISIBLE);
            mbtCancel.setVisibility( View.VISIBLE );
        }
        else
        {
            edtNum.setVisibility( View.GONE );
            edtName.setVisibility( View.GONE );
            mbtAdd.setVisibility( View.GONE );
            mbtCancel.setVisibility( View.GONE );
            mliLay.setVisibility( View.GONE);
        }

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        DELTA = (int)((double)list.getWidth() * .3);

       // System.out.println("Width:" + listview.getWidth());
      //  System.out.println("Height:" + listview.getHeight());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addcontacts)
        {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, PICK_CONTACT);
            return true;
        }
        else if (id == R.id.action_addnum)
        {
            ShowButtons( true );
            return true;
        }
        else if( id == R.id.action_bluetooth_current )
        {
            ContactDBHelper obj = new ContactDBHelper(MainActivity.this);
            String dv = obj.GetDevice();
            Toast.makeText(MainActivity.this, dv, Toast.LENGTH_LONG).show();
        }
        else if( id == R.id.StartService )
        {
            Intent i = new Intent(this, ScreenONService.class);
            i.putExtra(ScreenBroadcastReceiver.IntentTypeInfo, ScreenBroadcastReceiver.BR_ALWAYS_ON_SCREENON );
            i.putExtra("start", true);
            this.startService(i);
        }
        else if( id == R.id.StopService )
        {
            Intent i = new Intent(this, ScreenONService.class);
            i.putExtra(ScreenBroadcastReceiver.IntentTypeInfo, ScreenBroadcastReceiver.BR_ALWAYS_ON_SCREENON );
            i.putExtra("start", false);
            this.startService(i);
        }


        else if ( id == R.id.action_bluetooth )
        {
            mdevices = BuletoothHelper.Instance().GetAllPairedDevices( this );

            String[] products={"Camera", "Laptop", "Watch","Smartphone",
                    "Television"};

            listPopupWindow = new ListPopupWindow(
                    MainActivity.this);
            listPopupWindow.setAdapter(new ArrayAdapter(
                    MainActivity.this,
                    android.R.layout.simple_list_item_1, mdevices));

            listPopupWindow.setAnchorView(MainActivity.this.mliLay);
            listPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
            listPopupWindow.setHeight(ListPopupWindow.MATCH_PARENT);//MainActivity.this.list.getHeight() / 2);

            String strCurrent = BuletoothHelper.Instance().GetConfiguredDevice();
            int nPosition = mdevices.indexOf(strCurrent);

            listPopupWindow.setModal(true);
            listPopupWindow.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                            String dv = MainActivity.this.mdevices.get(position).toString();
                            Toast.makeText(MainActivity.this, dv, Toast.LENGTH_LONG).show();

                            ContactDBHelper obj = new ContactDBHelper(MainActivity.this);
                            obj.DeleteDevice(dv);
                            obj.AddDevice(dv);
                            BuletoothHelper.Instance().SetConfiguredDevice(dv);

                            listPopupWindow.dismiss();
                        }
                    });
            listPopupWindow.show();
            listPopupWindow.setSelection(nPosition);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK)
                {

                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if ( c!= null && c.moveToFirst())
                    {

                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        String cNumber = c.getString(c.getColumnIndex("data1"));

                        if (hasPhone.equalsIgnoreCase("1"))
                        {
                            edtName.setText(name);
                            edtNum.setText(cNumber);

                            ShowButtons(true);
                           //     CalllogUtil.Instance().insertContact(objTHis, name, cNumber);
                           //     Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_LONG).show();

                          //      ArrayList<ContactInfo> Contacts = CalllogUtil.Instance().GetQuickContacts(objTHis);
                          //      mContactAdapter = new LazyContactAdapter(objTHis, Contacts, Color.GRAY);
                          //      list.setAdapter(mContactAdapter);
                        }
                    }
                }
                break;
        }
    }
    public void handleUncaughtException (Thread thread, Throwable e)
    {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically

        Intent intent = new Intent ();
        intent.setAction ("com.mydomain.SEND_LOG"); // see step 5.
        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity (intent);

        System.exit(1); // kill off the crashed app
    }


    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    final private  int PERMISSION_REQUEST_CODE = 1;
    final private  int PERMISSION_REQUEST_CODE2 = 2;


private void Permission( String strperm, int requestCode )
{
    int hasContactPermission =ActivityCompat.checkSelfPermission(MainActivity.this, strperm );
    if(hasContactPermission != PackageManager.PERMISSION_GRANTED )
    {
       // Toast.makeText(MainActivity.this, strperm + "Permission is not granted already", Toast.LENGTH_LONG).show();
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{strperm}, requestCode );
    }
    else
    {
        //Toast.makeText(MainActivity.this, strperm+"Permission is already granted", Toast.LENGTH_LONG).show();
    }
}



    private void requestContactPermission()
    {
        if(Build.VERSION.SDK_INT < 23)
        {
            Toast.makeText(MainActivity.this, "Old code base", Toast.LENGTH_LONG).show();

            return;
            //your code here
        }


        Permission( Manifest.permission.READ_CALL_LOG, PERMISSION_REQUEST_CODE+1 );
        Permission( Manifest.permission.READ_CONTACTS, PERMISSION_REQUEST_CODE );
        Permission( Manifest.permission.READ_PHONE_STATE, PERMISSION_REQUEST_CODE+2 );
        Permission( Manifest.permission.BLUETOOTH, PERMISSION_REQUEST_CODE+3 );
        Permission( Manifest.permission.CALL_PHONE, PERMISSION_REQUEST_CODE+4 );
        Permission( Manifest.permission.MODIFY_AUDIO_SETTINGS, PERMISSION_REQUEST_CODE+5 );
        Permission( Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE+6 );
        Permission( Manifest.permission.RECEIVE_BOOT_COMPLETED, PERMISSION_REQUEST_CODE+7 );
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[]       permissions, int[] grantResults) {
        //Toast.makeText(MainActivity.this, "onRequestPermissionsResult - " + Integer.toString(requestCode), Toast.LENGTH_LONG).show();

        if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Log.i("Permission", "Contact permission has now been granted. Showing result.");
            //Toast.makeText(this,"Contact Permission is Granted", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("Permission", "Contact permission was NOT granted.");
           // Toast.makeText(MainActivity.this, "Contact permission was NOT granted.", Toast.LENGTH_LONG).show();

        }

       /* switch (requestCode) {

            case PERMISSION_REQUEST_CODE:
                // Check if the only required permission has been granted
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i("Permission", "sms permission has now been granted. Showing result.");
                    Toast.makeText(this,"sms Permission is Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("Permission", "sms permission was NOT granted.");
                    Toast.makeText(MainActivity.this, "sms permission was NOT granted.", Toast.LENGTH_LONG).show();

                }

                break;
            case PERMISSION_REQUEST_CODE2:
                // Check if the only required permission has been granted
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i("Permission", "Contact permission has now been granted. Showing result.");
                    Toast.makeText(this,"Contact Permission is Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("Permission", "Contact permission was NOT granted.");
                    Toast.makeText(MainActivity.this, "Contact permission was NOT granted.", Toast.LENGTH_LONG).show();

                }

                break;
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        objTHis = this;
        super.onCreate(savedInstanceState);


        requestContactPermission();


        //   Thread.setDefaultUncaughtExceptionHandler(new MyApplication(this));

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.e("Alert", "Lets See if it Works !!!");
            }
        });


        // Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_main);

      //  Button b1 = (Button) findViewById(R.id.btNotification);
     //   b1.setOnClickListener(this);
        list=(ListView)findViewById(R.id.listContact);


        ArrayList<ContactInfo> Contacts = CalllogUtil.Instance().GetQuickContacts( this );
        //new ArrayList<ContactInfo>();

     /*   Contacts.add( new ContactInfo("Janet", "8431583661"));
        Contacts.add( new ContactInfo("Home", "04842505948"));
        Contacts.add( new ContactInfo("Appachan", "09497678644"));
        Contacts.add( new ContactInfo("Dell Bridge", "180030103101"));
        Contacts.add( new ContactInfo("Janet", "8431583661"));
        Contacts.add( new ContactInfo("Home", "04842505948"));
        Contacts.add( new ContactInfo("Appachan", "09497678644"));
        Contacts.add( new ContactInfo("Dell Bridge", "180030103101"));
        Contacts.add( new ContactInfo("Janet", "8431583661"));
        Contacts.add( new ContactInfo("Home", "04842505948"));
        Contacts.add( new ContactInfo("Appachan", "09497678644"));
        Contacts.add( new ContactInfo("Dell Bridge", "180030103101"));
        Contacts.add( new ContactInfo("Janet", "8431583661"));
        Contacts.add( new ContactInfo("Home", "04842505948"));
        Contacts.add( new ContactInfo("Appachan", "09497678644"));
        Contacts.add( new ContactInfo("Dell Bridge", "180030103101"));

*/
        ContactDBHelper obj = new ContactDBHelper(this);
        String device = obj.GetDevice();
        BuletoothHelper.Instance().SetConfiguredDevice( device );

        edtName = (EditText)findViewById(R.id.txtContactName);
        edtNum = (EditText)findViewById(R.id.txtPhoneNUm);
        mbtAdd = (Button)findViewById(R.id.btAdd);
        mbtCancel = (Button)findViewById( R.id.btCancel );
        mliLay = (LinearLayout)findViewById(R.id.linLayout );

        ShowButtons( false );


        mbtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = edtName.getText().toString();
                String strNum = edtNum.getText().toString();

                if (!strName.isEmpty()) {
                    if (!strNum.isEmpty()) {
                        // ContactDBHelper obj = new ContactDBHelper(objTHis);
                        // obj.insertContact( strName, strNum );
//                        CalllogUtil.Instance().deleteContact ( objTHis, strNum );
                        CalllogUtil.Instance().insertContact(objTHis, strName, strNum);

                        Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_LONG).show();

                        ArrayList<ContactInfo> Contacts = CalllogUtil.Instance().GetQuickContacts(objTHis);
                        mContactAdapter = new LazyContactAdapter(objTHis, Contacts, Color.GRAY, false);
                        list.setAdapter(mContactAdapter);


                        ShowButtons(false);
                    }
                }


                // Your code that you want to execute on this button click
            }

        });
        mbtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtName.setText("");
                edtNum.setText("");
                ShowButtons( false );
                // Your code that you want to execute on this button click
            }
        });
        mContactAdapter=new LazyContactAdapter(this, Contacts, Color.GRAY, false);
        list.setAdapter(mContactAdapter);


        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list.setMultiChoiceModeListener(new ModeCallback());

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
              //  TextView item = (TextView) view.findViewById(R.id.Number);
                ;// ((TextView)view).getText().toString();
               // String text = item.getText().toString();
               // DialNumber("tel:" + text);
            }
        });



       /* list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                // switch (event.getAction()) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        historicX = event.getX();
                        historicY = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        // TextView item = (TextView) v.findViewById(R.id.Name);
                        LazyContactAdapter.ViewHolder viewHolder = (LazyContactAdapter.ViewHolder) v.getTag();

                        String strName = "Error ";
                        if( viewHolder != null )
                        {
                            strName =  viewHolder.Name.getText().toString();//item.getText().toString();
                        }
                        if (event.getX() - historicX < -DELTA)
                        {
                            // FunctionDeleteRowWhenSlidingLeft();
                            Toast.makeText(getBaseContext(), "Swipe left " + strName, Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (event.getX() - historicX > DELTA)
                        {
                            // FunctionDeleteRowWhenSlidingRight();
                            Toast.makeText(getBaseContext(), "Swipe right " +strName, Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        break;
                    default:
                        historicX =0;// event.getX();
                        historicY = 0;//event.getY();
                        return false;
                }
                return false;
            }
        });*/


        try
        {
            Intent serviceIntent = new Intent(this,ScreenONService.class);
            this.startService(serviceIntent);


            /*Intent i0 = new Intent();
            i0.setAction("com.example.divin_a.ssample2.ScreenONService");
            startService(i0);*/
        }
        catch (Exception ex)
        {
            Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
        }

        /*ArrayList<String> list = BuletoothHelper.Instance().GetAllPairedDevices();
        for (String device : list)
        {
            Toast.makeText(MainActivity.this, device, Toast.LENGTH_SHORT).show();
        }*/

    }

    private class ModeCallback implements ListView.MultiChoiceModeListener {

        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
           //  MenuInflater inflater = getMenuInflater();
            // inflater.inflate(R.menu.list_select_menu, menu);
            mode.setTitle("Select Items");
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
           // switch (item.getItemId()) {
              //  case R.id.share:
               //     Toast.makeText(MainActivity.this, "Shared " + getListView().getCheckedItemCount() +
               //             " items", Toast.LENGTH_SHORT).show();
               //     mode.finish();
               //     break;
               // default:
                //    Toast.makeText(MainActivity.this, "Clicked " + item.getTitle(),
                //            Toast.LENGTH_SHORT).show();
                //    break;
           // }
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode,
                                              int position, long id, boolean checked) {
            final int checkedCount = list.getCheckedItemCount();
            switch (checkedCount) {
                case 0:
                    mode.setSubtitle(null);
                    break;
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + checkedCount + " items selected");
                    break;
            }
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            /*   case R.id.btNotification:
                // Toast.makeText(MainActivity.this, "YOUR MESSAGE", Toast.LENGTH_LONG).show();
                AudioManager mAudioManager = (AudioManager) objTHis.getSystemService(Context.AUDIO_SERVICE);
                mAudioManager.dispatchMediaKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY));
                break;
         case R.id.btDial:
                DialNumber("tel:8431583661");
                // do your code
                break;



            case R.id.btHome:
                // do your code
                DialNumber("tel:04842505948");
                break;

            case R.id.btCallAppachan:
                // do your code
                DialNumber("tel:09497678644");
                break;

            case R.id.btInbox:
                // ShowInbox();
                ArrayList<MySms> AllSms = GetAllSms();//  new ArrayList<MySms>();


                adapter=new LazyAdapter(this, AllSms);
                list.setAdapter(adapter);
try
{
    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id)
        {
            TextView item = (TextView) view.findViewById(R.id.Body);
            ;// ((TextView)view).getText().toString();
            String text = item.getText().toString();

            Pattern p = Pattern.compile("(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\\\d\\\\d");
            Matcher m = p.matcher(text);
            while (m.find())
            { // Find each match in turn; String can't do this.
                String name = m.group(1); // Access a submatch group; String can't do this.
                Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();

            }


        }
    });
}
catch ( Exception ex)
{
    Toast.makeText(getBaseContext(), ex.toString(), Toast.LENGTH_LONG).show();
}

                break;
*/
            default:
                break;
        }
    }

    private String GetContactNameFromID( String nContactID )
    {
        return nContactID;
        /*
        String Displayname  = "Error Name";

        if( nContactID == null || nContactID.isEmpty())
        {
            return null;
        }

        if(ContactCache.containsKey(nContactID))//here item is key;item=Entertainment English
        {

            Displayname = ContactCache.get(nContactID);
            return  Displayname;
        }

        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                //ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                //        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                //        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                new String[]{nContactID},
                null);

        if (cursorPhone.moveToFirst())
        {
            Displayname = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            ContactCache.put( nContactID, Displayname );
        }
        cursorPhone.close();
        return  Displayname;*/
    }
    public void DialNumber( String snumber )
    {
        if (ContextCompat.checkSelfPermission(objTHis,
                Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            // Uri number = Uri.parse("tel:8431583661");
            Uri number = Uri.parse(snumber);
            Intent callIntent = new Intent(ACTION_CALL, number);
            startActivity(callIntent);
        }
    }

  //  public void onAttachedToWindow() {
  //      Window window = getWindow();
 //       window.addFlags(/*WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
  //              | */WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//
 //               | WindowManager.LayoutParams.FLAG_DIM_BEHIND
 //              /* | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
 //               | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD*/);
 //   }
}

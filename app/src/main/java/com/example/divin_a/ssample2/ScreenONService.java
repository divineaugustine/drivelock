package com.example.divin_a.ssample2;

/**
 * Created by Divin_A on 5/28/2016.
 */

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
        import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
        import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.ArrayList;


public class ScreenONService extends Service
{
    BroadcastReceiver mReceiver=null;
    private BuletoothHelper mbtHelper;



    private boolean mbAlwaysStartonScreenON = false;
    @Override
    public void onCreate()
    {
        super.onCreate();

        mbtHelper = BuletoothHelper.Instance();
        // Toast.makeText(getBaseContext(), "Service on create", Toast.LENGTH_SHORT).show();

        // Register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
      //  filter.addAction(Intent.ACTION_SCREEN_OFF);

      /*  filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_OFF);*/
        mReceiver = new ScreenBroadcastReceiver();
        registerReceiver(mReceiver, filter);

        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        // IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
       // IntentFilter filter4 = new IntentFilter(Intent.ACTION_CALL); // ?


        registerReceiver(mReceiver, filter1);
        // registerReceiver(mReceiver, filter2);
        registerReceiver(mReceiver, filter3);
        //registerReceiver(mReceiver, filter4);
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
       // boolean screenOn = false;
        if( intent == null )
        {
            return;
        }

      /*  screenOn = intent.getBooleanExtra("screenon", false);
        if( screenOn  )
        {
            if( BuletoothHelper.Instance().IsBluetoothEnabled()) {
                Intent dialogIntent = new Intent(this, MainActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            }
        }*/

        switch ( intent.getIntExtra(ScreenBroadcastReceiver.IntentTypeInfo, 0) )
        {
            case ScreenBroadcastReceiver.BR_ALWAYS_ON_SCREENON:
            {
                boolean screenOn = false;
                screenOn = intent.getBooleanExtra("start", false);
                mbAlwaysStartonScreenON = screenOn;
            }
            break;

            case ScreenBroadcastReceiver.BR_ScreenON:
            {

         //       Log.i("DDDDDDDDD-SR", "ACTION_SCREEN_ON" ) ;

                boolean screenOn = false;
                screenOn = intent.getBooleanExtra("screenon", false);
                // If call is on, no need to show the UI
                if( screenOn && !BuletoothHelper.Instance().GetCallOn())
                {
                    if( mbAlwaysStartonScreenON )
                    {
                        ShowLockScreen();
                    }
                    else if( BuletoothHelper.Instance().IsBluetoothEnabled())
                    {
                        ShowLockScreen();
                    }
                }
            }
            break;
            case ScreenBroadcastReceiver.BR_Bluetooth_Connected:
            {
                String strDeviceName;
                strDeviceName = intent.getStringExtra("Device");
                // BuletoothHelper.Instance().Connect( strDeviceName );


/*
                Below code will resume player automatically when the device is connected.
                Need change to make so that if a music player was paused already
                or the device connected is a music player... something of that sort
                Toast.makeText(getApplicationContext(), "BR_Bluetooth_Connected " + strDeviceName , Toast.LENGTH_LONG).show();
                // Device connected,, just send a VLC resume if its not on call
                // VLC will resume only if it was paused.
                if( false == BuletoothHelper.Instance().GetCallOn() && BuletoothHelper.Instance().IsConfiguredDevice( getApplicationContext(),strDeviceName))
                {
                    Toast.makeText(getApplicationContext(), "Resuming music", Toast.LENGTH_LONG).show();

                    AudioManager mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.dispatchMediaKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY));
                }
*/

                //IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
                //registerReceiver(mReceiver, filter);

         //       Toast.makeText(getApplicationContext(), "BR_Bluetooth_Connected", Toast.LENGTH_LONG).show();
            }
            break;
            case ScreenBroadcastReceiver.BR_Bluetooth_DisConnected:
            {
                String strDeviceName;
                strDeviceName = intent.getStringExtra("Device");
                // BuletoothHelper.Instance().DisConnect(strDeviceName);

               // IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
               // unregisterReceiver(mReceiver);


                Toast.makeText(getApplicationContext(), "BR_Bluetooth_DisConnected " + strDeviceName , Toast.LENGTH_LONG).show();

                if( BuletoothHelper.Instance().GetCallOn() && BuletoothHelper.Instance().IsConfiguredDevice(getApplicationContext(), strDeviceName ))
                {
                    TurnOnSpeakerMode();
                }
            }
            break;
            case ScreenBroadcastReceiver.BR_CallMode_ring:
            {
                BuletoothHelper.Instance().SetCalling(true);
            }
            break;
            case ScreenBroadcastReceiver.BR_CallModeTalk:
            {
                BuletoothHelper.Instance().SetCalling(true);
            }
            break;
            case ScreenBroadcastReceiver.BR_CallModeOff:
            {
                BuletoothHelper.Instance().SetCalling( false );
                CalllogUtil.Instance().ResetData();
            }
            break;
            default:
            break;
        }
          /*boolean screenOn = false;

        try
        {
            // Get ON/OFF values sent from receiver ( AEScreenOnOffReceiver.java )
            screenOn = intent.getBooleanExtra("screenon", false);

        }
        catch(Exception e)
        {}

        //  Toast.makeText(getBaseContext(), "Service on start :"+screenOn,
        //Toast.LENGTH_SHORT).show();

      if (screenOn)
        {
            if( BuletoothHelper.Instance().IsDevicePaired( "dfaf"))
            {
                // your code here
                // Some time required to start any service
                Toast.makeText(getBaseContext(), "Screen on, ", Toast.LENGTH_SHORT).show();
                Intent dialogIntent = new Intent(this, MainActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            }
        }*/

         /*else {

            // your code here
            // Some time required to stop any service to save battery consumption
            Toast.makeText(getBaseContext(), "Screen off,", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {

      //  Toast.makeText(getBaseContext(), "Screen onDestroy", Toast.LENGTH_LONG).show();

     //   Log.i("DDDDDDDDD-SR", "onDestroy" ) ;

     //   Log.i("ScreenOnOff", "Service  distroy");
        if(mReceiver!=null)
           unregisterReceiver(mReceiver);

    }

    private void ShowLockScreen()
    {
        Intent dialogIntent = new Intent(this, LockDisplay.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
    }

    public void TurnOnSpeakerMode()
    {
      //  Toast.makeText(getApplicationContext(), "TurnOnSpeakerMode", Toast.LENGTH_LONG).show();

        AudioManager manager = (AudioManager)(getApplicationContext().getSystemService(Context.AUDIO_SERVICE));
        manager.setSpeakerphoneOn(true);
        /*if(manager.getMode()== AudioManager.MODE_IN_CALL)
        {
        }*/
    }
    public void TurnOFFSpeakerMode()
    {
        AudioManager manager = (AudioManager)(getApplicationContext().getSystemService(Context.AUDIO_SERVICE));
        manager.setSpeakerphoneOn(false);
        /*if(manager.getMode()== AudioManager.MODE_IN_CALL)
        {
        }*/
    }
}
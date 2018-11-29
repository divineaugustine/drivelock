package com.example.divin_a.ssample2;

/**
 * Created by Divin_A on 5/28/2016.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ScreenBroadcastReceiver extends BroadcastReceiver
{

    public static String IntentTypeInfo = "IntentType";

    public static final int BR_ScreenON  = 1;
    public static final int BR_Bluetooth_Connected = 2;
    public static final int BR_Bluetooth_DisConnected = 3;
    public static final int BR_CallMode_ring = 4;
    public static final int BR_CallModeOff = 5;
    public static final int BR_CallModeTalk = 6;
    public static final int BR_ALWAYS_ON_SCREENON = 7;


    //private boolean screenOff;

    @Override
    public void onReceive(Context context, Intent intent)
    {

        //Toast.makeText(context, "BroadcastReceiver", Toast.LENGTH_SHORT).show();

        if( null == intent )
        {
            return;
        }

       /* if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

            screenOff = true;

        } else*/

        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
        {

        //    Log.i("DDDDDDDDD-BR", "ACTION_SCREEN_ON" ) ;

            Intent i = new Intent(context, ScreenONService.class);
            i.putExtra(IntentTypeInfo, BR_ScreenON );
            i.putExtra("screenon", true);
            context.startService(i);

            // Toast.makeText(context, "ACTION_SCREEN_ON", Toast.LENGTH_LONG).show();
        }
        else if(intent.getAction().equals(BluetoothDevice.ACTION_ACL_CONNECTED))
        {
            Intent i = new Intent(context, ScreenONService.class);
            i.putExtra(IntentTypeInfo, BR_Bluetooth_Connected );
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            //BuletoothHelper.Instance().Connect( device.getName() );
            i.putExtra("Device", device.getName());
            context.startService(i);

          //  Toast.makeText(context, device.getName(), Toast.LENGTH_SHORT).show();
        }
        else if(intent.getAction().equals(BluetoothDevice.ACTION_ACL_DISCONNECTED))
        {

            Intent i = new Intent(context, ScreenONService.class);
            i.putExtra(IntentTypeInfo, BR_Bluetooth_DisConnected );
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            //BuletoothHelper.Instance().DisConnect( device.getName() );
            i.putExtra("Device", device.getName());
            context.startService(i);

           // Toast.makeText(context, device.getName(), Toast.LENGTH_SHORT).show();
/*
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            Toast.makeText(context, device.getName(), Toast.LENGTH_SHORT).show();


            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                Toast.makeText(context, "BluetoothDevice.ACTION_FOUND", Toast.LENGTH_SHORT).show();
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action))
            {
                Toast.makeText(context, "BluetoothDevice.ACTION_ACL_CONNECTED", Toast.LENGTH_SHORT).show();
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                Toast.makeText(context, "BluetoothAdapter.ACTION_DISCOVERY_FINISHED", Toast.LENGTH_SHORT).show();
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action))
            {
                Toast.makeText(context, "BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED", Toast.LENGTH_SHORT).show();
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action))
            {
                Toast.makeText(context, "BluetoothDevice.ACTION_ACL_DISCONNECTED", Toast.LENGTH_SHORT).show();
            }*/
        }
      //  Toast.makeText(context, "Screen on_off,", Toast.LENGTH_LONG).show();
     // Toast.makeText(context, "BroadcastReceiver :"+screenOff, Toast.LENGTH_SHORT).show();
     // Send Current screen ON/OFF value to service


    }//public void onReceive(Context context, Intent intent)

}
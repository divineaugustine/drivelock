package com.example.divin_a.ssample2;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import java.util.ArrayList;
import java.util.Set;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.os.Bundle;


/**
 * Created by Divin_A on 5/28/2016.
 */
public class BuletoothHelper
{
    private static  BuletoothHelper mInst = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private int REQUEST_ENABLE_BT = 123;

    private boolean mbCall = false;
    public void SetCalling( boolean bCall )
    {
        mbCall = bCall;
    }

   // private SharedPreferences msettings;
    public boolean GetCallOn()
    {
        return mbCall;
    }

    /*private ArrayList<String> m_connecteddevices = new ArrayList<String>();

    public void Connect( String strDevice_i )
    {
        if(!m_connecteddevices.contains(strDevice_i))
        {
            m_connecteddevices.add(strDevice_i);
        }
    }

    public void DisConnect( String strDevice_i )
    {
        m_connecteddevices.remove( strDevice_i );
    }

    public boolean IsDevicePaired( String strDevice )
    {
       // return mBluetoothAdapter.isEnabled();
        return m_connecteddevices.contains( strDevice );
    }*/



    private  String mConfiguredDevice = "";
    public void SetConfiguredDevice( String strDevuce )
    {
        mConfiguredDevice = strDevuce;
    }

    public String GetConfiguredDevice( )
    {
        return mConfiguredDevice;
    }

    public boolean IsConfiguredDevice( Context cont, String strDevice)
    {
        if( mConfiguredDevice.isEmpty())
        {
            ContactDBHelper obj = new ContactDBHelper(cont);
            mConfiguredDevice = obj.GetDevice();
        }

        if( mConfiguredDevice.equals( strDevice ))
        {
            return  true;
        }
        return false;
    }

    private BuletoothHelper()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null)
        {
            // Device does not support Bluetooth
        }
        else
        {
            if (!mBluetoothAdapter.isEnabled())
            {
                //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
               // startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }




       // msettings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }


   /* private void EnableBluetooth()
    {
        if (!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }*/


    //@Override
   // protected void onActivityResult(int requestCode, int resultCode, Intent data)
   // {
        // Check which request we're responding to
      //  if (requestCode == REQUEST_ENABLE_BT)
       // {
            // Make sure the request was successful
           // if (resultCode == RESULT_OK)
           // {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
          //  }
       // }
   // }

    public static BuletoothHelper Instance()
    {
        if( mInst == null )
        {
            mInst = new BuletoothHelper();
        }
        return mInst;
    }

    public ArrayList<String> GetAllPairedDevices( AppCompatActivity ctn)
    {
        if (!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ctn.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        ArrayList<String> devices = new ArrayList<String>();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0)
        {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices)
            {
                // Add the name and address to an array adapter to show in a ListView
                // mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                String str = device.getName();
                devices.add(str);
            }
        }
        return  devices;
    }


    public boolean IsBluetoothEnabled()
    {
        return mBluetoothAdapter.isEnabled();
    }
}

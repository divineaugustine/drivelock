package com.example.divin_a.ssample2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by Divin_A on 7/5/2016.
 */


public class MyCallStateListener extends BroadcastReceiver {                                    // 1

    @Override
    public void onReceive(Context context, Intent intent) {                                         // 2
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);                         // 3
        String msg = "Phone state changed to " + state;

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state))
        {                                   // 4
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);  // 5
            msg += ". Incoming number is " + incomingNumber;

            Intent i = new Intent(context, ScreenONService.class);
            i.putExtra(ScreenBroadcastReceiver.IntentTypeInfo, ScreenBroadcastReceiver.BR_CallMode_ring );
            context.startService(i);

           // BuletoothHelper.Instance().SetCalling( true );
        }
        else  if (TelephonyManager.EXTRA_STATE_IDLE.equals(state))
        {
            Intent i = new Intent(context, ScreenONService.class);
            i.putExtra(ScreenBroadcastReceiver.IntentTypeInfo, ScreenBroadcastReceiver.BR_CallModeOff );
            context.startService(i);

          //  BuletoothHelper.Instance().SetCalling( false );
        }
        else  if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state))
        {
            Intent i = new Intent(context, ScreenONService.class);
            i.putExtra(ScreenBroadcastReceiver.IntentTypeInfo, ScreenBroadcastReceiver.BR_CallModeTalk );
            context.startService(i);

            //  BuletoothHelper.Instance().SetCalling( false );
        }
        // Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

    }

}
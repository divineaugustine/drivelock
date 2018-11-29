package com.example.divin_a.ssample2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Divin_A on 8/15/2016.
 */

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction()))
        {
            try
            {
                Intent serviceIntent = new Intent(context,ScreenONService.class);
                context.startService(serviceIntent);
            }
            catch (Exception ex)
            {
                Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
package com.example.divin_a.ssample2;

import android.app.Application;
import android.app.PendingIntent;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

/**
 * Created by Divin_A on 5/29/2016.
 */

/*
public class MyApplication implements
        java.lang.Thread.UncaughtExceptionHandler {
    private final Activity myContext;
    private final String LINE_SEPARATOR = "\n";

    public MyApplication(Activity context)
    {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);

        //Intent intent = new Intent(myContext, AnotherActivity.class);
       // intent.putExtra("error", errorReport.toString());
       // myContext.startActivity(intent);
        Toast.makeText(myContext, errorReport.toString(), Toast.LENGTH_LONG).show();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

}*/

public class MyApplication extends Application
{
    private CalllogUtil minst;
    private BuletoothHelper mobjHekper;
    public void onCreate()
    {
        super.onCreate();

        mobjHekper = BuletoothHelper.Instance();
        minst = CalllogUtil.Instance();
        minst.GetQuickContacts(getApplicationContext());
        // Initialize the singletons so their instances
        // are bound to the application process.
       // initSingletons();
    }

    // uncaught exception handler variable
    private Thread.UncaughtExceptionHandler defaultUEH;

    // handler listener
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
            new Thread.UncaughtExceptionHandler()
            {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {

                    // here I do logging of exception to a db

                    // re-throw critical exception further to the os (important)
                    defaultUEH.uncaughtException(thread, ex);
                }
            };

    public MyApplication() {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

        // setup handler for uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    }
}


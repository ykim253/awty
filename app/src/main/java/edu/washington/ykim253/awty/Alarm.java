package edu.washington.ykim253.awty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            String text = intent.getStringExtra("text");
            String phoneNumber = intent.getStringExtra("phoneNum");
            Toast.makeText(context, phoneNumber + ": " + text, Toast.LENGTH_SHORT).show();
        }
    }
}

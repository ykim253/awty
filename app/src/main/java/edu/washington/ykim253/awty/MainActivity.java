package edu.washington.ykim253.awty;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private Button btnStart;
    private EditText editText;
    private EditText editPhone;
    private EditText editTime;
    private Intent service;
    private AlarmManager aM;
    private PendingIntent pendingIntent;
    private String text;
    private String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.start);
        editText = (EditText) findViewById(R.id.messageEdit);
        editPhone = (EditText) findViewById(R.id.phoneEdit);
        editTime = (EditText) findViewById(R.id.timeEdit);

        BroadcastReceiver alarm = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try{
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, text, null, null);
                    Toast.makeText(context, "Text sent", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(context, "Text failed" , Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };
        aM = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        registerReceiver(alarm, new IntentFilter("edu.washington.ykim253.alarm"));

        Intent forAlarm = new Intent();
        forAlarm.setAction(("edu.washington.ykim253.alarm"));
        pendingIntent = PendingIntent.getBroadcast(this, 0, forAlarm, 0);
    }

    public void onClick(View v) {
        if (editText.getText().toString() == null || editTime.getText().toString() == null || editPhone.getText() == null) {
            Toast.makeText(this, "Have all information filled please", Toast.LENGTH_SHORT).show();
        } else {

            phoneNumber = editPhone.getText().toString();

            //which service is it on? (start or stop)
            if(btnStart.getText().toString().equals("Start")) {
                //validate text
                if(!editText.getText().toString().equals("")) {
                    //validate phone
                    if(phoneNumber.matches("\\d{10}") || phoneNumber.matches("\\d{3}[-\\s]\\d{3}[-\\s]\\d{4}")) {
                        //validate time
                        String time = editTime.getText().toString();

                        if(time.equals("")) {
                            Toast.makeText(this, "Enter a valid time", Toast.LENGTH_SHORT).show();
                        } else {
                            int interval = Integer.parseInt(time);
                            if(interval > 0) {
                                btnStart.setText("Stop");
                                text = editText.getText().toString();
                                aM.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                                        System.currentTimeMillis(),
                                        interval*60000, pendingIntent);
                                Toast.makeText(this, "Success: Getting to You Now" , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Enter a value greater than 0", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(this, "Enter a valid phone number in valid format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Enter a message", Toast.LENGTH_SHORT).show();
                }
            } else {
                btnStart.setText("Start");
                aM.cancel(pendingIntent);
                Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void onDestroy() {
        super.onDestroy();
        pendingIntent.cancel();
        aM.cancel(pendingIntent);
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

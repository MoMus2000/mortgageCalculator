package com.example.mcalcpro;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import ca.roumani.i2c.MPro;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener,SensorEventListener {

    private TextToSpeech tts;
    public final int LIMIT_1 = 6;
    public final int LIMIT_2 = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tts =new TextToSpeech(this,this);
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

    }
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {

    }
    public void onSensorChanged(SensorEvent event)
    {
        double ax = event.values[0];
        double ay = event.values[1];
        double az = event.values[2];
        double a = Math.sqrt(ax*ax + ay*ay + az*az);
        if(a > 20)
        {
            ((EditText) findViewById(R.id.Principle)).setText("");
            ((EditText) findViewById(R.id.Amort)).setText("");
            ((EditText) findViewById(R.id.Int)).setText("");
            ((TextView) findViewById(R.id.Output)).setText("");
        }
    }

    public void onInit(int initStatus)
    {
        this.tts.setLanguage(Locale.US);

    }


    public void buttonClicked(View v)
    {
        EditText PrinView = (EditText) findViewById(R.id.Principle);
        String z = PrinView.getText().toString();
        EditText AmortView = (EditText) findViewById(R.id.Amort);
        String x = AmortView.getText().toString();
        EditText iView = (EditText) findViewById(R.id.Int);
        String c = iView.getText().toString();

        try
        {
            MPro mp = new MPro();
            mp.setPrinciple(z);
            mp.setAmortization(x);
            mp.setInterest(c);
            String toSpeak = "Monthly Payment = " + mp.computePayment("%.2f");

            String s = "Monthly Payment = " + mp.computePayment("%,.2f");
            s += "\n\n";
            s += "By making this payments monthly for  " + x + " years, the mortgage will be paid in full" +
                    ". But if you terminate the mortgage on its nth anniversary, the balance still " +
                    " owing " +
                    "depends on n as shown below";
            s += "\n\n";
            s += String.format("%8s", "n") + String.format("%16s", "Balance");
            s += "\n\n";
            int q = Integer.parseInt(x);
            int i;

            for( i = 0; i <= q; i++)
            {
                s += String.format("%8d",i) + mp.outstandingAfter(i,"%,16.0f");
                s += "\n\n";



            }
            s += String.format("%8d",q) + mp.outstandingAfter(q,"%,16.0f");
            s += "\n\n";

            ((TextView) findViewById(R.id.Output)).setText(s);
            tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);


        }
        catch(Exception e)
        {
            Toast label = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            label.show();


        }














    }
}

package pramod.fi.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressWarnings("Deprecated")
public class MainActivity extends Activity implements SensorEventListener{

    SensorManager sensorManager;
    float accX, accY, accZ;
    float shake;
    Vibrator vibrator;
    Camera camera;
    Parameters params;
    boolean lightOn;
    int count;
    TextView textView;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        vibrator = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        camera = Camera.open();
        lightOn = false;
        count = 0;
        textView = (TextView) findViewById(R.id.textview);
        layout = (RelativeLayout) findViewById(R.id.layout);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accX = event.values[0];
            accY = event.values[1];
            accZ = event.values[2];

            shake = (float) Math.sqrt((accX*accX)+(accY*accY)+(accZ*accZ));
            Log.d("Shake",""+shake);

            if(shake>12){
                params = camera.getParameters();
                if(!lightOn) {
                    vibrator.vibrate(100);
                    params.setFlashMode(Parameters.FLASH_MODE_TORCH);
                    lightOn=true;
                    textView.setText("Torch is on");
                    textView.setTextColor(Color.BLACK);
                    layout.setBackgroundColor(Color.WHITE);
                }else{
                    params.setFlashMode(Parameters.FLASH_MODE_OFF);
                    lightOn=false;
                    textView.setText("Torch is off");
                    textView.setTextColor(Color.WHITE);
                    layout.setBackgroundColor(Color.BLACK);
                }
                camera.setParameters(params);
                camera.startPreview();
                vibrator.vibrate(100);
            }

        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        params = camera.getParameters();
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}

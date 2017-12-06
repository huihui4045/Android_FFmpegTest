package android_ffmpegtest.android_ffmpegtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HelloWorldActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);


        textView = ((TextView) findViewById(R.id.information));


    }

    public void getFFmpegInformation(View view){

        textView.setText(getFFmpegInformation());
    }

    public native String getFFmpegInformation();
}

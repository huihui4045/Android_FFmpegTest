package android_ffmpegtest.android_ffmpegtest;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;

public class Mp42YuvActivity extends AppCompatActivity {

    private String TAG=this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp42_yuv);
    }

    public void load(View view) {
        String input = new File(Environment.getExternalStorageDirectory(), "input.mp4").getAbsolutePath();
        String  output= new File(Environment.getExternalStorageDirectory(), "output.yuv").getAbsolutePath();


        File file=new File(input);

        Log.e(TAG, "fileName: "+file.getName()+" "+file.length());


        Log.e("MainActivity","input:"+input);
        open(input, output);
    }

    public native void open(String inputStr, String outStr);
}

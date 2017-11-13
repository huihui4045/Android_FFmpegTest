package android_ffmpegtest.android_ffmpegtest;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by molu_ on 2017/11/12.
 */

public class VideoView extends SurfaceView {
    public VideoView(Context context) {
        super(context);
    }

    public VideoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }



    private void init() {

        SurfaceHolder holder=getHolder();

        holder.setFormat(PixelFormat.RGBA_8888);
    }

    public void player(final String input){

        new Thread(new Runnable() {
            @Override
            public void run() {

                render(input,VideoView.this.getHolder().getSurface());
            }
        }).start();
    }

    private native void render(String input, Surface surface);


}

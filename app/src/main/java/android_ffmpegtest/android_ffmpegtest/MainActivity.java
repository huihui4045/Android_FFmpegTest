package android_ffmpegtest.android_ffmpegtest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private static final String TAG="MainActivity";


   /* native-lib
    avcodec-56
    avdevice-56
    avformat-56
    avutil-54
    postproc-53
    swresample-1
    swscale-3*/

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("avcodec-56");
        System.loadLibrary("avdevice-56");
        System.loadLibrary("avfilter-5");
        System.loadLibrary("avformat-56");
        System.loadLibrary("avutil-54");
        System.loadLibrary("postproc-53");
        System.loadLibrary("swresample-1");
        System.loadLibrary("swscale-3");
        System.loadLibrary("native-lib");
    }

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_demo);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);

        mListView = ((ListView) findViewById(R.id.listView));

        mListView.setOnItemClickListener(this);

        mListView.setAdapter(new DemoListAdapter());

    }







    private static final DemoInfo[] DEMOS = {
            new DemoInfo("ffmpge  HelloWorld", "ffmpge  HelloWorld demo", HelloWorldActivity.class),
            new DemoInfo("ffmpge实现mp4转YUV", "ffmpge实现mp4转YUV demo", Mp42YuvActivity.class),
            new DemoInfo("ffmpge实现视频播放器", "ffmpge实现视频播放器 demo", VideoActivity.class),

            //new DemoInfo("自定义View", "自定义View 使用demo", CustomActivity.class),
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        Class<? extends Activity> demoClass = DEMOS[position].demoClass;

        intent = new Intent(MainActivity.this, demoClass);


        this.startActivity(intent);
    }

    private class DemoListAdapter extends BaseAdapter {
        public DemoListAdapter() {
            super();
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            convertView = View.inflate(MainActivity.this, R.layout.demo_info_item, null);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            //TextView desc = (TextView) convertView.findViewById(R.id.desc);
            title.setText(DEMOS[index].title);
            //desc.setText(DEMOS[index].desc);
            if (index >= 25) {
                title.setTextColor(Color.YELLOW);
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return DEMOS.length;
        }

        @Override
        public Object getItem(int index) {
            return DEMOS[index];
        }

        @Override
        public long getItemId(int id) {
            return id;
        }
    }

    private static class DemoInfo {
        private final String title;
        private final String desc;
        private final Class<? extends Activity> demoClass;

        public DemoInfo(String title, String desc, Class<? extends Activity> demoClass) {
            this.title = title;
            this.desc = desc;
            this.demoClass = demoClass;
        }
    }


   // Error:error: linker command failed with exit code 1 (use -v to see invocation)
}

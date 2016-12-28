package news.zxzq.com.videonews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import news.zxzq.com.videoplayer.part.SimpleVideoPlayer;

public class TestActivity extends AppCompatActivity {

    Button bweweww;
    SimpleVideoPlayer simpleVideoPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Log.e("MainActivity","MainActivity...........");
        simpleVideoPlayer = (SimpleVideoPlayer) findViewById(R.id.sssss);
        simpleVideoPlayer.setVideoPath(VideoUrlRes.getTestUrl1());

    }
    @Override
    protected void onResume() {
        super.onResume();
        simpleVideoPlayer.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        simpleVideoPlayer.onPause();
    }
}

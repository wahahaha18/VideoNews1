package news.zxzq.com.videoplayer.full;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;
import news.zxzq.com.videoplayer.R;

public class VideoViewActivity extends AppCompatActivity {
    public static final String KEY_VIDEO_PATH = "video_path";
    public static void open(Context context,String video_path){
        Intent intent = new Intent(context,VideoViewActivity.class);
        intent.putExtra(KEY_VIDEO_PATH,video_path);
        context.startActivity(intent);
    }

    private VideoView videoView;
    private TextView tv_BufferInfo;
    private ImageView iv_Loading;
    private MediaPlayer mediaPlayer;
    private int downSpeed;//下载速度
    private int bufferPercent;//缓冲百分比
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawableResource(android.R.color.black);
        setContentView(R.layout.activity_video_view);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        init();
    }

    private void init() {
        initBufferView();
        initVideoView();
    }



    private void initBufferView() {
        tv_BufferInfo = (TextView) findViewById(R.id.tv_BufferInfo);
        iv_Loading = (ImageView) findViewById(R.id.iv_Loading);
        tv_BufferInfo.setVisibility(View.INVISIBLE);
        iv_Loading.setVisibility(View.INVISIBLE);
    }
    private void initVideoView() {
        Vitamio.isInitialized(this);
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setMediaController(new CustomControllerView(this));
        videoView.setKeepScreenOn(true);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer = mp;
                //设置缓冲大小
                mediaPlayer.setBufferSize(512*1024);
            }
        });
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what){
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        showBufferView();
                        if (videoView.isPlaying()){
                            videoView.pause();
                        }
                        return true;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        hideBufferView();
                        videoView.start();
                        return true;
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        downSpeed = extra;
                        updateBufferView();
                        return true;

                }
                return false;
            }
        });

        videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                bufferPercent = percent;
            }
        });
    }

    private void updateBufferView() {
        String info = bufferPercent + "%" + "  " + downSpeed + "KB/s";
        tv_BufferInfo.setText(info);
    }

    private void hideBufferView() {
        tv_BufferInfo.setVisibility(View.INVISIBLE);
        iv_Loading.setVisibility(View.INVISIBLE);

    }

    private void showBufferView() {
        tv_BufferInfo.setVisibility(View.VISIBLE);
        iv_Loading.setVisibility(View.VISIBLE);
        downSpeed = 0;
        bufferPercent = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.setVideoPath(getIntent().getStringExtra(KEY_VIDEO_PATH));
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.stopPlayback();
    }
}

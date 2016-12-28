package news.zxzq.com.videoplayer.part;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import news.zxzq.com.videoplayer.R;
import news.zxzq.com.videoplayer.full.VideoViewActivity;

/**
 * Created by Administrator on 2016/12/16.
 */

public class SimpleVideoPlayer extends FrameLayout {
    private static final int PROGRESS_Max = 1000;
    private ProgressBar pb_part_Video;
    private ImageButton ib_start,ib_all;
    private ImageView iv_pre;
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean isPreparing;
    private boolean isPlaying;
    private String videoPath;
    public SimpleVideoPlayer(Context context) {
        this(context,null);
    }

    public SimpleVideoPlayer(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public SimpleVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Vitamio.isInitialized(getContext());
        LayoutInflater.from(getContext()).inflate(R.layout.layout_simple_video_player,this,true);
        //初始化SurfaceView
        initSurfaceView();
        //初始化ControllerView
        initControllerView();

    }



    private void initSurfaceView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        Log.e("SimpleVideoPlayer","surfaceView:"+surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setFormat(PixelFormat.RGBA_8888);
    }
    private void initControllerView() {
        iv_pre = (ImageView) findViewById(R.id.ivPreview);
        ib_start = (ImageButton) findViewById(R.id.btnToggle);
        ib_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    pauseMediaPlayer();
                }else if (isPreparing){
                    startMediaplayer();
                }else {
                    Toast.makeText(getContext(), "Can't play now！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pb_part_Video = (ProgressBar) findViewById(R.id.progressBar);
        pb_part_Video.setMax(PROGRESS_Max);

        ib_all = (ImageButton) findViewById(R.id.btnFullScreen);
        ib_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoViewActivity.open(getContext(),videoPath);
            }
        });
    }

    public void onResume(){
        initMediaPlayer();
        preMdiaplayer();
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer(getContext());
        Log.e("SimpleVideoPlayer","mediaPlayer:"+mediaPlayer);
        mediaPlayer.setDisplay(surfaceHolder);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPreparing = true;
                startMediaplayer();
            }
        });
        //对audio进行处理
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_FILE_OPEN_OK){
                    mediaPlayer.audioInitedOk(mediaPlayer.audioTrackInit());
                    return true;
                }
                return false;
            }
        });
        //视频大小改变的监听
        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                int surface_Widh = surfaceView.getWidth();
                int surface_height = surface_Widh * height / width;
                ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
                params.width = surface_Widh;
                params.height = surface_height;
                surfaceView.setLayoutParams(params);
            }
        });

    }



    //准备MediaPlayer
    private void preMdiaplayer() {

        try {
            //重置MediaPlayer
            mediaPlayer.reset();
            //给MediaPlayer添加视频路径
            mediaPlayer.setDataSource(videoPath);
            //循环播放
            mediaPlayer.setLooping(true);
            //异步准备
            mediaPlayer.prepareAsync();
            iv_pre.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //开启MediaPlayer
    private void startMediaplayer() {
        iv_pre.setVisibility(View.INVISIBLE);
        ib_start.setImageResource(R.drawable.ic_pause);
        mediaPlayer.start();
        isPlaying = true;
        handler.sendEmptyMessage(0);
    }

    public void onPause(){
        pauseMediaPlayer();
        relaseMediaPlayer();
    }

    private void pauseMediaPlayer() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        ib_start.setImageResource(R.drawable.ic_play_arrow);
        isPlaying = false;
        handler.removeMessages(0);

    }

    private void relaseMediaPlayer() {
        mediaPlayer.release();
        mediaPlayer = null;
        isPreparing = false;
        pb_part_Video.setProgress(0);
    }


    public void setVideoPath(String videoPath){
        this.videoPath = videoPath;
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isPlaying){
                //每0.2秒更新一次播放进度
                int progresses = (int) (mediaPlayer.getCurrentPosition() * PROGRESS_Max / mediaPlayer.getDuration());
                pb_part_Video.setProgress(progresses);
                //发送一个空的延迟消息，不断地调用本身，实现对进度条的更新
                handler.sendEmptyMessageDelayed(0,200);
            }
        }
    };
}

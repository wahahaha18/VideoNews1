package news.zxzq.com.videoplayer.full;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import io.vov.vitamio.widget.MediaController;
import news.zxzq.com.videoplayer.R;

/**
 * Created by Administrator on 2016/12/19.
 */

public class CustomControllerView extends MediaController {
    private MediaPlayerControl mediaPlayerControl;//视频播放控制接口，用于管理视频进度
    private AudioManager audioManager;//音频管理
    private Window window;//用于视频亮度管理
    private int maxVolume;//最大音量
    private int currentVolum;//当前音量
    private float currentBrightness;//当前亮度
    public CustomControllerView(Context context) {
        super(context);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        window = ((Activity)context).getWindow();
    }

    //获取自定义layout
    @Override
    protected View makeControllerView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_custom_video_controller,this);
        initView();
        return view;
    }

    //获取自定义视频控制器， //视频播放控制接口，用于管理视频进度
    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        super.setMediaPlayer(player);
        mediaPlayerControl = player;
    }

    //初始化视图设置一些监听
    private void initView() {

        //快进按钮
        ImageButton btnFastForward = (ImageButton) findViewById(R.id.btnFastForward);
        btnFastForward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取当前播放的位置
                long position = mediaPlayerControl.getCurrentPosition();
                //快进10秒，默认的毫秒计算
                position += 10000;
                //如果快进10秒后，大于等于总的视频长度，则到头
                if (position >= mediaPlayerControl.getDuration()){
                    position = mediaPlayerControl.getDuration();
                }
                //否则移动到快进的位置
                mediaPlayerControl.seekTo(position);
            }
        });

        //快退按钮
        ImageButton btnFastRewind = (ImageButton) findViewById(R.id.btnFastRewind);
        btnFastRewind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                long position = mediaPlayerControl.getCurrentPosition();
                position -= 10000;
                if (position < 0){
                    position = 0;
                }
                mediaPlayerControl.seekTo(position);
            }
        });

        //调整视图（左边调整亮度，右边调整音量）
        final View adjustView = (View) findViewById(R.id.adjustView);
        //依赖GestureDetector（手势识别类）,来进行划屏调整音量和亮度的手势处理
        final GestureDetector gestureDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float startX = e1.getX();//开始的X轴坐标
                float startY = e1.getY();//开始的y轴坐标
                float endX = e2.getX();//结束的X轴坐标
                float endY = e2.getY();//结束的X轴坐标
                float width = adjustView.getWidth();//调整视图的宽
                float height = adjustView.getHeight();//调整视图的高
                float percent = (startY - endY) / height;//高度滑动的百分比

                //开始的X轴坐标，在屏幕的前1/5
                if (startX < width / 5){
                    adjustBrightness(percent);
                }
                //开始的X轴坐标，在屏幕的倒数1/5
                else if (startX > width * 4/5){
                    adjustVolume(percent);
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });

        //对adjustView（调整视图）进行touch监听
        //在onTouch方法中，只需要将event交给gestureDetector去做就行了
        adjustView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //当用户摁下的时候
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN){
                    //获取当前音量
                    currentVolum = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    //获取当前亮度
                    currentBrightness = window.getAttributes().screenBrightness;
                }
                //交给手势类gestureDetector去做
                gestureDetector.onTouchEvent(event);
                //在调整过程中，一直显示
                show();
                return true;
            }
        });

    }

    //调整音量的方法
    ////最终音量 = 最大音量 * 改变的百分比 + 当前音量
    private void adjustVolume(float percent) {
        int volume = (int) (maxVolume * percent + currentVolum);
        //如果最终音量大于最大音量，结果为最大音量
        volume = volume >= maxVolume ? maxVolume : volume;
        volume = volume < 0 ? 0 : volume;

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volume,AudioManager.FLAG_SHOW_UI);

    }

    //调整亮度的方法（最小亮度 = 0 ，最大亮度 = 1.0f）
    //最终亮度 = percentage + 当前亮度
    private void adjustBrightness(float percent) {

        float brightness = percent * 1.0f + currentBrightness;

        brightness = brightness >= 1.0f ? 1.0f : brightness;
        brightness = brightness < 0 ? 0 :  brightness;
        //设置亮度
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.screenBrightness = brightness;
        window.setAttributes(layoutParams);
    }
}

package news.zxzq.com.videoplayer.list;

import android.content.Context;
import android.view.Surface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

/**
 * Created by Administrator on 2016/12/20.
 */

public class MediaPlayerManager {
    private static MediaPlayerManager mediaPlayerManager;
    private List<OnPlaybackListener> onPlayBackListeners;
    private Context context;
    private MediaPlayer mediaPlayer;
    private boolean needRelease = false;
    private String videoId;

    public static MediaPlayerManager getsInstance(Context context){
        if (mediaPlayerManager == null){
            mediaPlayerManager = new MediaPlayerManager(context);
        }
        return mediaPlayerManager;
    }

    public MediaPlayerManager(Context context) {
        this.context = context;
        onPlayBackListeners = new ArrayList<OnPlaybackListener>(){};
        Vitamio.isInitialized(context);
    }

    public String getVideoId() {
        return videoId;
    }

    public void onResume(){

        mediaPlayer = new MediaPlayer(context);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.setBufferSize(512 * 1024);
                mediaPlayer.start();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlayer();
            }
        });
        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                if (width == 0 || height == 0){
                    return;
                }
                changeVideoSize(width,height);
            }
        });
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what){
                    case MediaPlayer.MEDIA_INFO_FILE_OPEN_OK:
                        mediaPlayer.audioInitedOk(mediaPlayer.audioTrackInit());
                        return true;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        startBufferring();
                        return true;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        endBufferring();
                        return true;

                }
                return false;
            }
        });
    }
    public void onPause(){
        stopPlayer();
        if (needRelease){
            mediaPlayer.release();
        }
        mediaPlayer = null;

    }

    private long startTime;

    public void startPlayer(Surface surface, String path, String videoId){
        if (System.currentTimeMillis() - startTime < 300){
            return;
        }
        startTime = System.currentTimeMillis();
        if (this.videoId != null){
            stopPlayer();
        }

        this.videoId = videoId;
        for (OnPlaybackListener listener : onPlayBackListeners){
            listener.onStartPlay(videoId);
        }

        try {
            mediaPlayer.setDataSource(path);
            needRelease = true;
            mediaPlayer.setSurface(surface);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void stopPlayer(){

        if (videoId == null){
            return;
        }
        for (OnPlaybackListener listener : onPlayBackListeners){
            listener.onStopPlay(videoId);
        }
        this.videoId = null;
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.reset();

    }
    public void addOnPlaybackListener(OnPlaybackListener listener){

        onPlayBackListeners.add(listener);
    }
    public void removeOnPlaybackListener(){

        onPlayBackListeners.clear();
    }
    private void changeVideoSize(int width,int height){

        for (OnPlaybackListener listener : onPlayBackListeners){
            listener.onSizeMeasured(videoId,width,height);
        }
    }
    private void startBufferring(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        for (OnPlaybackListener listener : onPlayBackListeners){
            listener.onStartBuffering(videoId);
        }

    }
    private void endBufferring(){

        mediaPlayer.start();
        for (OnPlaybackListener listener : onPlayBackListeners){
            listener.onStopBuffering(videoId);
        }
    }


    // 视图接口
    // 在视频播放模块完成播放处理, 视图层来实现此接口, 完成视图层UI更新
    public interface OnPlaybackListener {

        void onStartBuffering(String videoId); // 视频缓冲开始

        void onStopBuffering(String videoId); // 视频缓冲结束

        void onStartPlay(String videoId); // 开始播放

        void onStopPlay(String videoId);// 停止播放

        void onSizeMeasured(String videoId, int width, int height);// 大小更改
    }
}

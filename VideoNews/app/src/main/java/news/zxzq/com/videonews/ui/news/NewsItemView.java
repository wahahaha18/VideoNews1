package news.zxzq.com.videonews.ui.news;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import news.zxzq.com.videonews.R;
import news.zxzq.com.videonews.commons.CommonUtils;
import news.zxzq.com.videonews.entity.NewsEntity;
import news.zxzq.com.videonews.ui.base.BaseItemView;
import news.zxzq.com.videonews.ui.news.commets.CommentsActivity;
import news.zxzq.com.videoplayer.list.MediaPlayerManager;

/**
 * Created by Administrator on 2016/12/26.
 */

public class NewsItemView extends BaseItemView<NewsEntity> implements TextureView.SurfaceTextureListener, MediaPlayerManager.OnPlaybackListener {
    @BindView(R.id.textureView)
    TextureView textureView; // 用来展现视频的TextureView
    @BindView(R.id.ivPreview)
    ImageView ivPreview;
    @BindView(R.id.tvNewsTitle)
    TextView tvNewsTitle;
    @BindView(R.id.tvCreatedAt)
    TextView tvCreatedAt;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ivPlay)
    ImageView ivPlay;
    private Surface surface;
    private MediaPlayerManager mediaPlayerManager;
    private NewsEntity newsEntity;
    public NewsItemView(Context context) {
        super(context);
    }

    @Override
    public void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_news,this,true);
        ButterKnife.bind(this);
        textureView.setSurfaceTextureListener(this);
        mediaPlayerManager = MediaPlayerManager.getsInstance(getContext());
        mediaPlayerManager.addOnPlaybackListener(this);

    }

    @Override
    public void bindModel(NewsEntity newsEntity) {

        this.newsEntity = newsEntity;
        tvNewsTitle.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        ivPreview.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.VISIBLE);
        tvNewsTitle.setText(newsEntity.getNewsTitle());
        tvCreatedAt.setText(CommonUtils.format(newsEntity.getCreatedAt()));
        String url = CommonUtils.encodeUrl(newsEntity.getPreviewUrl());
        Picasso.with(getContext()).load(url).into(ivPreview);
    }

    @OnClick(R.id.tvCreatedAt)
    public void toComments(){
        //跳转到评论页面
        CommentsActivity.open(getContext(),newsEntity);
    }
    @OnClick(R.id.textureView)
    public void stopPlayer(){
        mediaPlayerManager.stopPlayer();

    }
    @OnClick(R.id.ivPreview)
    public void startPlayer(){

        if (surface == null){
            return;
        }
        String videoUrl = newsEntity.getVideoUrl();
        String videoId = newsEntity.getObjectId();
        mediaPlayerManager.startPlayer(surface,videoUrl,videoId);
    }
    //判断是否是当前播放的视频
    public boolean isCurrentVideo(String videoId){
        if (videoId == null || newsEntity == null){
            return false;
        }
        return videoId.equals(newsEntity.getObjectId());

    }


    //**********        setSurfaceTextureListener       ***********************
    //textureView -> surface相关监听
    //拿到Surface
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        this.surface = new Surface(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    //当surface销毁时，停止播放
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        this.surface.release();
        this.surface = null;
        if (newsEntity.getObjectId().equals(mediaPlayerManager.getVideoId())){
            mediaPlayerManager.stopPlayer();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    //********************     mediaPlayerManager.addOnPlaybackListener(this);     ************
    @Override
    public void onStartBuffering(String videoId) {

        if (isCurrentVideo(videoId)){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStopBuffering(String videoId) {

        if (isCurrentVideo(videoId)){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStartPlay(String videoId) {

        if (isCurrentVideo(videoId)){
            if (isCurrentVideo(videoId)){
                tvNewsTitle.setVisibility(View.INVISIBLE);
                ivPreview.setVisibility(View.INVISIBLE);
                ivPlay.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onStopPlay(String videoId) {

        if (isCurrentVideo(videoId)){
            tvNewsTitle.setVisibility(View.VISIBLE);
            ivPreview.setVisibility(View.VISIBLE);
            ivPlay.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onSizeMeasured(String videoId, int width, int height) {

        if (isCurrentVideo(videoId)) {
            //无需求，不做处理
        }
    }
}

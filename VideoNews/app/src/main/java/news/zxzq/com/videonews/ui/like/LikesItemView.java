package news.zxzq.com.videonews.ui.like;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import news.zxzq.com.videonews.R;
import news.zxzq.com.videonews.UserManager;
import news.zxzq.com.videonews.commons.CommonUtils;
import news.zxzq.com.videonews.entity.NewsEntity;
import news.zxzq.com.videonews.ui.base.BaseItemView;
import news.zxzq.com.videonews.ui.news.commets.CommentsActivity;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class LikesItemView extends BaseItemView<NewsEntity> {

    public LikesItemView(Context context) {
        super(context);
    }

    @BindView(R.id.ivPreview)
    ImageView ivPreview;
    @BindView(R.id.tvNewsTitle)
    TextView tvNewsTitle;
    @BindView(R.id.tvCreatedAt)
    TextView tvCreatedAt;
    private NewsEntity newsEntity;

    @Override
    public void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_likes,this,true);
        ButterKnife.bind(this);
    }

    @Override
    public void bindModel(NewsEntity newsEntity) {
        this.newsEntity = newsEntity;
        tvNewsTitle.setText(newsEntity.getNewsTitle());
        tvCreatedAt.setText(CommonUtils.format(newsEntity.getCreatedAt()));
        String url = CommonUtils.encodeUrl(newsEntity.getPreviewUrl());
        Picasso.with(getContext()).load(url).into(ivPreview);
    }

    //跳转到评论页面
    @OnClick
    public void navigateToComments() {
        CommentsActivity.open(getContext(), newsEntity);
    }

    //长按删除
    @OnLongClick
    public boolean unCollectNews(){
        listener.onItemLongClick(newsEntity.getObjectId(), UserManager.getInstance().getObjectId());
        return true;
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(String newsId, String userId);
    }

    private OnItemLongClickListener listener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
            this.listener = listener;
    }

}

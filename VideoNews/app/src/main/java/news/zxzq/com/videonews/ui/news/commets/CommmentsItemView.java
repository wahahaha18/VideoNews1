package news.zxzq.com.videonews.ui.news.commets;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import news.zxzq.com.videonews.R;
import news.zxzq.com.videonews.commons.CommonUtils;
import news.zxzq.com.videonews.entity.AuthorEntity;
import news.zxzq.com.videonews.entity.CommentsEntity;
import news.zxzq.com.videonews.ui.base.BaseItemView;

/**
 * Created by Administrator on 2016/12/27.
 */

public class CommmentsItemView extends BaseItemView<CommentsEntity> {
    @BindView(R.id.tvContent)
    TextView tvContent; // 评论内容
    @BindView(R.id.tvAuthor)
    TextView tvAuthor; // 评论作者
    @BindView(R.id.tvCreatedAt)
    TextView tvCreatedAt; // 评论时间

    public CommmentsItemView(Context context) {
        super(context);
    }

    @Override
    public void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_comments,this,true);
        ButterKnife.bind(this);
    }

    @Override
    public void bindModel(CommentsEntity commentsEntity) {
        //数据绑定
        String content = commentsEntity.getContent();//评论内容
        Date createdAt = commentsEntity.getCreatedAt();// 评论时间
        AuthorEntity authorEntity = commentsEntity.getAuthor();
        String username = authorEntity.getUsername(); // 评论作者
        tvContent.setText(content);
        tvAuthor.setText(username);
        tvCreatedAt.setText(CommonUtils.format(createdAt));
    }
}

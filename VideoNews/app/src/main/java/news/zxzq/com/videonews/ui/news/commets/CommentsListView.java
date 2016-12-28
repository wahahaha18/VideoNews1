package news.zxzq.com.videonews.ui.news.commets;

import android.content.Context;
import android.util.AttributeSet;

import news.zxzq.com.videonews.bombapi.BombConst;
import news.zxzq.com.videonews.entity.CommentsEntity;
import news.zxzq.com.videonews.entity.InQuery;
import news.zxzq.com.videonews.entity.QueryResult;
import news.zxzq.com.videonews.ui.base.BaseResorceView;
import retrofit2.Call;

/**
 * Created by Administrator on 2016/12/27.
 */

public class CommentsListView extends BaseResorceView<CommentsEntity,CommmentsItemView > {
    public CommentsListView(Context context) {
        super(context);
    }

    public CommentsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String newsId;

    public void setNewsId(String newsId){
        this.newsId = newsId;
    }

    @Override
    protected Call<QueryResult<CommentsEntity>> queryData(int limit, int skip) {
        InQuery where = new InQuery(BombConst.FIELD_NEWS,BombConst.TABLE_NEWS,newsId);
        return newApi.getComments(limit,skip,where);
    }

    @Override
    protected int getLimit() {
        return 20;
    }

    @Override
    protected CommmentsItemView createView() {
        return new CommmentsItemView(getContext());
    }
}

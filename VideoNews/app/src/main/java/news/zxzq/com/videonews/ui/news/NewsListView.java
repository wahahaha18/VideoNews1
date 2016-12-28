package news.zxzq.com.videonews.ui.news;

import android.content.Context;
import android.util.AttributeSet;

import news.zxzq.com.videonews.entity.NewsEntity;
import news.zxzq.com.videonews.ui.base.BaseResorceView;
import retrofit2.Call;

/**
 * Created by Administrator on 2016/12/26.
 */

public class NewsListView extends BaseResorceView<NewsEntity,NewsItemView>{
    public NewsListView(Context context) {
        super(context);
    }

    public NewsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected Call queryData(int limit, int skip) {
        return newApi.getVideoList(limit, skip);
    }
    @Override
    protected int getLimit() {
        return 5;
    }

    @Override
    protected NewsItemView createView() {
        return new NewsItemView(getContext());
    }
}

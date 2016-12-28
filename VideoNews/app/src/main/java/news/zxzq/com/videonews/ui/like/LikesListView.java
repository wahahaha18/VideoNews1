package news.zxzq.com.videonews.ui.like;

import android.content.Context;
import android.util.AttributeSet;

import news.zxzq.com.videonews.UserManager;
import news.zxzq.com.videonews.bombapi.BombClient;
import news.zxzq.com.videonews.bombapi.BombConst;
import news.zxzq.com.videonews.bombapi.NewApi;
import news.zxzq.com.videonews.commons.ToastUtils;
import news.zxzq.com.videonews.entity.CollectResult;
import news.zxzq.com.videonews.entity.InQuery;
import news.zxzq.com.videonews.entity.NewsEntity;
import news.zxzq.com.videonews.entity.QueryResult;
import news.zxzq.com.videonews.ui.base.BaseResorceView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的收藏列表视图
 */

public class LikesListView extends BaseResorceView<NewsEntity, LikesItemView> {
    public LikesListView(Context context) {
        super(context);
    }

    public LikesListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LikesListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected int getLimit() {
        return 15;
    }

    @Override
    protected Call<QueryResult<NewsEntity>> queryData(int limit, int skip) {
        String userId = UserManager.getInstance().getObjectId();
        InQuery where = new InQuery(BombConst.FIELD_LIKES, BombConst.TABLE_USER, userId);
        return newApi.getLikedList(limit, skip, where);
    }

    @Override
    protected LikesItemView createView() {
        //给itemView设置长按监听
        LikesItemView likesItemView = new LikesItemView(getContext());
        likesItemView.setOnItemLongClickListener(new LikesItemView.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(String newsId, String userId) {
                cancel(newsId, userId);
            }
        });
        return likesItemView;
    }



    //退出登录时要清空收藏列表
    public void clear() {
        modelAdapter.clearData();
    }

    //给itemView设置长按监听
    private void cancel(String newsId, String userId) {
        //新接口用Retrofit_cloud生成的接口实现类newsApi_cloud（因为根路径不同）
        NewApi newsApi_cloud = BombClient.getInstance().getNewsApi_cloud();
        Call<CollectResult> call = newsApi_cloud.unCollectNews(
                newsId,
                userId);
        call.enqueue(new Callback<CollectResult>() {
            @Override
            public void onResponse(Call<CollectResult> call, Response<CollectResult> response) {
                CollectResult collectResult = response.body();
                if (collectResult.isSuccess()) {
                    ToastUtils.showShort("取消收藏成功");
                    //取消收藏成功后，自动刷新一下。
                    autoRefresh();
                } else {
                    ToastUtils.showShort("取消收藏失败：" + collectResult.getError());
                }
            }

            @Override
            public void onFailure(Call<CollectResult> call, Throwable t) {
                ToastUtils.showShort(t.getMessage());
            }
        });
    }
}

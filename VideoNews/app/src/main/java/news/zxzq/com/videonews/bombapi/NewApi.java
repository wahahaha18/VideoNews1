package news.zxzq.com.videonews.bombapi;

import news.zxzq.com.videonews.entity.CollectResult;
import news.zxzq.com.videonews.entity.CommentsEntity;
import news.zxzq.com.videonews.entity.CommentsResult;
import news.zxzq.com.videonews.entity.InQuery;
import news.zxzq.com.videonews.entity.NewsEntity;
import news.zxzq.com.videonews.entity.PublishEntity;
import news.zxzq.com.videonews.entity.QueryResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/12/26.
 */

public interface NewApi {
    //获取新闻列表,排序方式，接时间新到旧排序
    @GET("1/classes/News?order=-createdAt")
    Call<QueryResult<NewsEntity>> getVideoList(@Query("limit") int limit, @Query("skip") int skip);

    //获取新闻的所有评论,接时间新到旧排序
    //注意，我们希望评论作者不仅返回objectId，还返回username，可以使用URL编码参数include=author。
    @GET("1/classes/Comments?include=author&order=-createdAt")
    Call<QueryResult<CommentsEntity>> getComments(
            @Query("limit") int limit,
            @Query("skip") int skip,
            @Query("where") InQuery where);
    //收藏新闻
    @GET("bef74a37a08d3205/changeLike?action=like")
    Call<CollectResult> collectNews(
            @Query("newsId") String newsId,
            @Query("userId") String userId
    );

    //取消收藏新闻
    @GET("bef74a37a08d3205/changeLike?action=dislike")
    Call<CollectResult> unCollectNews(
            @Query("newsId") String newsId,
            @Query("userId") String userId
    );

    //发表评论
    @POST("1/classes/Comments")
    Call<CommentsResult> postComments(@Body PublishEntity publishEntity);

    //获取收藏列表
    @GET("1/classes/News?order=-createdAt")
    Call<QueryResult<NewsEntity>> getLikedList(
            @Query("limit") int limit,
            @Query("skip") int skip,
            @Query("where") InQuery where
    );
}

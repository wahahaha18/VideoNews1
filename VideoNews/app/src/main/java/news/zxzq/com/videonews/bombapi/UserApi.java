package news.zxzq.com.videonews.bombapi;

import news.zxzq.com.videonews.entity.SuccessfulResponseResult;
import news.zxzq.com.videonews.entity.UserEntity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/12/22.
 */

public interface UserApi {
    //用户注册的相对路径
//    @Headers({
//            "Accept: application/vnd.github.v3.full+json",
//            "User-Agent: Retrofit-Sample-App"
//    })
    @POST("1/users")
    Call<SuccessfulResponseResult> register(@Body UserEntity userEntity);

    //用户登录的相对路径
    @GET("1/login")
    Call<SuccessfulResponseResult> login(@Query("username") String username, @Query("password") String password);
}

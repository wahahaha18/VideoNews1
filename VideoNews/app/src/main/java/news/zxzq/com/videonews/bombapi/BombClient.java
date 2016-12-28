package news.zxzq.com.videonews.bombapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import news.zxzq.com.videonews.entity.User;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/12/21.
 */

public class BombClient {
    public static BombClient bombClient;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private UserApi userApi;
    private NewApi newApi;
    private NewApi newsApi_cloud;//用于新接口
    private Retrofit retrofit_cloud;//用于新接口

    public static BombClient getInstance(){
        if (bombClient == null){
            bombClient = new BombClient();
        }
        return bombClient;
    }
    private BombClient(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        BombInterceptor bombInterceptor = new BombInterceptor();
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(bombInterceptor)//自定义添加请求头的拦截器
                .addInterceptor(httpLoggingInterceptor)//日志拦截器
                .build();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.bmob.cn/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        //构建retrofit_cloud
        retrofit_cloud = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://cloud.bmob.cn/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public UserApi getUserApi(){

         if (userApi == null){
             userApi = retrofit.create(UserApi.class);
         }
        return userApi;
    }
    public NewApi getNewApi(){

        if (newApi == null){
            newApi = retrofit.create(NewApi.class);
        }
        return newApi;
    }
    //获取newsApi_cloud
    public NewApi getNewsApi_cloud(){
        if (newsApi_cloud == null){
            newsApi_cloud = retrofit_cloud.create(NewApi.class);
        }
        return newsApi_cloud;
    }


    /**
     * //设置注册时发送的请求
     * @param username
     * @param password
     * @return Call对象，用于处理返回的响应
     */
    public Call register(String username, String password){

        User user = new User(username,password);
        Gson gson = new Gson();
        String json = gson.toJson(user);
        RequestBody requestBody = RequestBody.create(null,json);
        Request request = new Request.Builder()
                .url("https://api.bmob.cn/1/users")
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }
}

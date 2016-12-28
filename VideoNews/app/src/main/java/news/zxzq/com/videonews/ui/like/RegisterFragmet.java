package news.zxzq.com.videonews.ui.like;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import news.zxzq.com.videonews.R;
import news.zxzq.com.videonews.bombapi.BombClient;
import news.zxzq.com.videonews.commons.ToastUtils;
import news.zxzq.com.videonews.entity.ErrorResponseResult;
import news.zxzq.com.videonews.entity.SuccessfulResponseResult;
import news.zxzq.com.videonews.entity.UserEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/12/21.
 */

public class RegisterFragmet extends DialogFragment {
    private View view;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_register,container,false);
        ButterKnife.bind(this,view);
        return view;
    }
    @OnClick(R.id.btnRegister)
    public void onRegister(View view){
        final String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            ToastUtils.showShort(R.string.username_or_password_can_not_be_null);
            return;
        }
        btnRegister.setVisibility(View.GONE);


        UserEntity userEntity = new UserEntity(username,password);
//        Gson gson = new Gson();
//        String json = gson.toJson(userEntity);
//        Log.e("RegisterFragmet","json");
//        RequestBody requestBody = RequestBody.create(null, json);
        //网络模块
//        Call call = BombClient.getInstance().register(username, password);
        Call<SuccessfulResponseResult> call = BombClient.getInstance().getUserApi().register(userEntity);
       call.enqueue(new Callback<SuccessfulResponseResult>() {
           @Override
           public void onResponse(Call<SuccessfulResponseResult> call, Response<SuccessfulResponseResult> response) {
               if (!response.isSuccessful()){
                   btnRegister.setVisibility(View.VISIBLE);
                   Gson gson = new Gson();
                   try {
                       String error = response.errorBody().string();
                       ErrorResponseResult errorResponseResult = gson.fromJson(error, ErrorResponseResult.class);
                       ToastUtils.showShort(errorResponseResult.getError());
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

               }else {

                   btnRegister.setVisibility(View.VISIBLE);
                   SuccessfulResponseResult body = response.body();
                   Log.e("aaa","...."+body.toString());
                   ToastUtils.showShort(R.string.register_success);
                   onRegisterSuccessListener.registerSuccess(username,body.getObjectId());
               }

           }

           @Override
           public void onFailure(Call<SuccessfulResponseResult> call, Throwable t) {

               ToastUtils.showShort("网络请求超时....");
           }
       });
        /*call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                ToastUtils.showShort("网络请求超时");
            }

            @Override
            public void onResponseUI(Call call, String content) {

                ToastUtils.showShort("注册成功！");
            }
        });*/

    }

    public interface OnRegisterSuccessListener{
        void registerSuccess(String username,String objectId);
    }
    private OnRegisterSuccessListener onRegisterSuccessListener;
    public void setOnRegisterSuccessListener(OnRegisterSuccessListener onRegisterSuccessListener){
        this.onRegisterSuccessListener = onRegisterSuccessListener;
    }
}

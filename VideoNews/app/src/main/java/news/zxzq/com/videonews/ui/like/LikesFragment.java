package news.zxzq.com.videonews.ui.like;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import news.zxzq.com.videonews.R;
import news.zxzq.com.videonews.UserManager;

/**
 * Created by Administrator on 2016/12/21.
 */

public class LikesFragment extends Fragment implements RegisterFragmet.OnRegisterSuccessListener, LoginFragment.OnLoginSuccessListener {
    private View view;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.mView)
    View mView;
    @BindView(R.id.likesListView)
    LikesListView likesListView;
    private RegisterFragmet registerFragmet;
    private LoginFragment mLoginFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.layout_frag_likes,container,false);
            ButterKnife.bind(this,view);
            //判断用户登录状态，更新UI
            UserManager userManager = UserManager.getInstance();
            if (!userManager.isOffline()){
                userOnLine(userManager.getUsername(),userManager.getObjectId());
            }
        }
        return view;
    }
    @OnClick({R.id.btnLogout,R.id.btnLogin,R.id.btnRegister})
    public void show(View view){
        switch (view.getId()){
            case R.id.btnLogout:

                userOffline();
                break;
            case R.id.btnLogin:

                if ( mLoginFragment== null){
                    mLoginFragment = new LoginFragment();
                    mLoginFragment.setListener(this);
                }
                mLoginFragment.show(getChildFragmentManager(),"Login Dialog");
                break;
            case R.id.btnRegister:
                if (registerFragmet == null){
                    registerFragmet = new RegisterFragmet();
//                    TO添加注册成功的监听
                }
                registerFragmet.show(getChildFragmentManager(),"Register Dialog");
                registerFragmet.setOnRegisterSuccessListener(this);

                break;

        }
    }

    @Override
    public void registerSuccess(String username, String objectId) {
        //关闭对话框
        registerFragmet.dismiss();
        btnLogin.setVisibility(View.INVISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);
        mView.setVisibility(View.INVISIBLE);
        btnLogout.setVisibility(View.VISIBLE);
        tvUsername.setText(username);

        // 存储用户信息
        UserManager.getInstance().setUsername(username);
        UserManager.getInstance().setObjectId(objectId);
        //刷新收藏列表
        likesListView.autoRefresh();
    }

    @Override
    public void loginSuccess(String username, String objectId) {

        mLoginFragment.dismiss();
        //用户上线
        userOnLine(username,objectId);
    }

    //用户上线
    private void userOnLine(String username,String objectId){
        //更新UI
        btnLogin.setVisibility(View.INVISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);
        btnLogout.setVisibility(View.VISIBLE);
        mView.setVisibility(View.INVISIBLE);
        tvUsername.setText(username);
        // 存储用户信息
        UserManager.getInstance().setUsername(username);
        UserManager.getInstance().setObjectId(objectId);
        //  刷新收藏列表
        likesListView.autoRefresh();
    }
    //用户下线
    private void userOffline(){
        //清除用户相关信息
        UserManager.getInstance().clear();
        //更新UI
        btnLogin.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.INVISIBLE);
        mView.setVisibility(View.VISIBLE);
        tvUsername.setText(R.string.tourist);
        //  清空收藏列表
        likesListView.clear();
    }

}

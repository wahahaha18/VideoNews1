package news.zxzq.com.videonews.ui.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import news.zxzq.com.videonews.R;
import news.zxzq.com.videoplayer.list.MediaPlayerManager;

/**
 * Created by Administrator on 2016/12/21.
 */

public class NewsFragment extends Fragment {
    @BindView(R.id.news_ListView)
    NewsListView news_ListView;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            Log.e("NewsFragment..","View:"+view);
            view = inflater.inflate(R.layout.layout_frag_newsvideo,container,false);

        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        // 首次进来，自动刷新
        news_ListView.post(new Runnable() {
            @Override
            public void run() {
                news_ListView.autoRefresh();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MediaPlayerManager.getsInstance(getContext()).onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaPlayerManager.getsInstance(getContext()).onPause();
    }
    //移除View
    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        移除View
        ((ViewGroup)view.getParent()).removeView(view);
    }

    //清除所有监听（不再需要Ui交互）
    @Override
    public void onDestroy() {
        super.onDestroy();
        //清除所有监听（不再需要Ui交互）
        MediaPlayerManager.getsInstance(getContext()).removeOnPlaybackListener();
    }
}

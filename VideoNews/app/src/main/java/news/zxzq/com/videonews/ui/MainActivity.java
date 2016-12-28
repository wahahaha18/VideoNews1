package news.zxzq.com.videonews.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import news.zxzq.com.videonews.R;
import news.zxzq.com.videonews.ui.like.LikesFragment;
import news.zxzq.com.videonews.ui.local.LocalVideoFragment;
import news.zxzq.com.videonews.ui.news.NewsFragment;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindViews({R.id.btnNews, R.id.btnLocal, R.id.btnLikes})
    List<Button> buttons;
    private Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


    }

    private void init() {
        unbinder = ButterKnife.bind(this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        //首次进入默认选中在线新闻btn
        buttons.get(0).setSelected(true);
    }

    private FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new NewsFragment();
                case 1:
                    return new LocalVideoFragment();
                case 2:
                    return new LikesFragment();
                default:
                    throw new RuntimeException("ViewPager的适配器出现异常");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //Button，UI改变
            buttons.get(0).setSelected(position == 0);
            buttons.get(1).setSelected(position == 1);
            buttons.get(2).setSelected(position == 2);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @OnClick({R.id.btnNews, R.id.btnLocal, R.id.btnLikes})
    public void showFragment(View view){
        switch (view.getId()){
            case R.id.btnNews:
                viewPager.setCurrentItem(0);
                break;
            case R.id.btnLocal:
                viewPager.setCurrentItem(1);
                break;
            case R.id.btnLikes:
                viewPager.setCurrentItem(2);
                break;
            default:
                throw new RuntimeException("点击事件的适配器出现异常");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}

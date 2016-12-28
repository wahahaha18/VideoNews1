package news.zxzq.com.videonews.ui.base;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import news.zxzq.com.videonews.R;
import news.zxzq.com.videonews.bombapi.BombClient;
import news.zxzq.com.videonews.bombapi.NewApi;
import news.zxzq.com.videonews.commons.ToastUtils;
import news.zxzq.com.videonews.entity.QueryResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/12/23.
 */

public abstract class BaseResorceView<Model,ItemView extends BaseItemView<Model>> extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener ,MugenCallbacks{

    public BaseResorceView(Context context) {
        this(context,null);
    }

    public BaseResorceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseResorceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    // 适配器
    protected ModelAdapter modelAdapter;
    private int skip = 0;
    protected NewApi newApi;
    private void init() {
        newApi = BombClient.getInstance().getNewApi();
        LayoutInflater.from(getContext()).inflate(R.layout.layout_base_resorceview,this,true);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 适配器
        modelAdapter = new ModelAdapter();
        recyclerView.setAdapter(modelAdapter);
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(this);
        //下拉刷新的颜色样式
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        //上拉加载
        Mugen.with(recyclerView,this).start();
    }


    protected abstract int getLimit();
    protected abstract Call<QueryResult<Model>> queryData(int limit, int skip);
    private boolean loadAll;
    protected abstract ItemView createView();

    public void autoRefresh(){
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }
    @Override
    public void onRefresh() {
        //  queryData()
        Call<QueryResult<Model>> call = queryData(getLimit(), 0);
        if (call == null){
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        call.enqueue(new Callback<QueryResult<Model>>() {
            @Override
            public void onResponse(Call<QueryResult<Model>> call, Response<QueryResult<Model>> response) {

                swipeRefreshLayout.setRefreshing(false);
                ArrayList<Model> results = response.body().getResults();
                skip = results.size();
                loadAll = results.size() < getLimit();
                modelAdapter.clearData();
                modelAdapter.addData(results);

            }

            @Override
            public void onFailure(Call<QueryResult<Model>> call, Throwable t) {

                swipeRefreshLayout.setRefreshing(false);//下拉刷新停止
                ToastUtils.showShort("onFailure:" + t.getMessage());
            }
        });

    }

    @Override
    public void onLoadMore() {
        Log.e("onLoadMore........","..................");

        Call<QueryResult<Model>> call = queryData(getLimit(), skip);
        if (call == null) {
            ToastUtils.showShort("查询条件异常");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<QueryResult<Model>>() {
            @Override
            public void onResponse(Call<QueryResult<Model>> call, Response<QueryResult<Model>> response) {
                Log.e("onLoadMore........","results:"+response.body().getResults().toString());
                progressBar.setVisibility(View.GONE);
                ArrayList<Model> results = response.body().getResults();
                Log.e("onLoadMore........","results:"+results.size());
                skip += results.size();
                Log.e("onLoadMore........","skip:"+skip);
                loadAll = results.size() < getLimit();
                modelAdapter.addData(results);
            }

            @Override
            public void onFailure(Call<QueryResult<Model>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);//隐藏上拉视图
                ToastUtils.showShort("onFailure:" + t.getMessage());
            }
        });

    }

    @Override
    public boolean isLoading() {
        return progressBar.getVisibility() == View.VISIBLE;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return loadAll;
    }

    protected class ModelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private ArrayList<Model> arrayList = new ArrayList<>();
        //清除适配器中的数据
        public void clearData(){
            arrayList.clear();
            notifyDataSetChanged();
        }
        //向适配器中添加数据
        public void addData(ArrayList<Model> arrayList){
            this.arrayList = arrayList;
            notifyDataSetChanged();
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemView itemView = createView();

            itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            return new RecyclerView.ViewHolder(itemView) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            //当前项的数据
            Model model = arrayList.get(position);
            //当前项的视图
            ItemView itemView = (ItemView) holder.itemView;
            //将当前项的数据设置到当前视图中
            itemView.bindModel(model);
            Log.e("RecyclerView.ViewHolder","arrayList.size():"+arrayList.size());
        }

        @Override
        public int getItemCount() {

            return arrayList.size();
        }
    }
}

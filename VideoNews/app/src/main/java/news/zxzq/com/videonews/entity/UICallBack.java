package news.zxzq.com.videonews.entity;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/21.
 */

public abstract class UICallBack implements Callback {
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    public void onFailure(final Call call, final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onFailureUI(call,e);
            }
        });

    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        try {
            if (!response.isSuccessful()){
                throw new IOException ("error code:"+response.code());
            }
            final String content = response.body().string();
        handler.post(new Runnable() {
            @Override
            public void run() {

                onResponseUI(call, content);
            }

        });
        } catch (IOException e) {
                e.printStackTrace();
            }



    }
    public abstract void onFailureUI(Call call, IOException e);
    public abstract void onResponseUI(Call call, String content);

}

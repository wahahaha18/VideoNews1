package news.zxzq.com.videonews.ui.base;

import android.content.Context;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/12/23.
 */

public abstract class BaseItemView<Model> extends FrameLayout {
    public BaseItemView(Context context) {
        super(context);
        initView();
    }
    public abstract void initView();
    public abstract void bindModel(Model model);
}

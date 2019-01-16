package a.cool.huanxin;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import a.cool.huanxin.utils.ToastHelper;

public class CCApplication extends Application {

    private static final String TAG = CCApplication.class.getSimpleName();

    private static CCApplication INSTANCE;

    public static CCApplication getInstance() {
        return INSTANCE;
    }

    public CCApplication() {
        INSTANCE = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        Utils.init(this);
        ToastHelper.init(this);
        //初始化环信SDK
        initSDK();
    }

    //初始化环信SDK
    public void initSDK() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        options.setAutoAcceptGroupInvitation(false);
        options.setAutoLogin(true);
        //初始化
        EMClient.getInstance().init(getApplicationContext(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }
}

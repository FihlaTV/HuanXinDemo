package a.cool.huanxin.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.NetUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import a.cool.huanxin.CCApplication;
import a.cool.huanxin.base.ICallback;
import a.cool.huanxin.ben.CurrentUser;
import a.cool.huanxin.ben.MessageBean;
import a.cool.huanxin.manager.CurrentUserManager;


public class HuanXinServer extends Service {

    private static HuanXinConnectionListener mHuanXinConnectionListener;


    public static final String ACTION_NETWORK_STATE_CHANGED = "networkStateChanged";

    private static final Object lock = new Object();
    private static HuanXinServer INSTANCE;
    private boolean mNetworkConnected = false;

    public static void startService() {
        Context context = CCApplication.getInstance();
        if (context != null) {
            Intent startIntent = new Intent(context, HuanXinServer.class);
            try {
                context.startService(startIntent);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public static void stopService() {
        Context context = CCApplication.getInstance();
        if (context != null) {
            Intent startIntent = new Intent(context, HuanXinServer.class);
            context.stopService(startIntent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("HuanXinServer", "onStartCommand()");
        if (INSTANCE == null) {
            synchronized (lock) {
                if (INSTANCE == null) {
                    INSTANCE = this;
                    registerNetReceiver();
                }
            }
        }
        startSocket();
        return super.onStartCommand(intent, flags, startId);
    }

    private void registerNetReceiver() {
        mNetworkConnected = NetWorkManager.getInstance().isNetConnect();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(mNetStateReceiver, filter);
    }

    private BroadcastReceiver mNetStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean networkIsOn = NetWorkManager.getInstance().isNetConnect();
            if (networkIsOn == mNetworkConnected) {
                return;
            }
            mNetworkConnected = networkIsOn;
            if (networkIsOn) {
                if (NetWorkManager.getInstance().isIsNoInternet()) {
//                    ReConnectSuccessEvent.post();
                }
                NetWorkManager.getInstance().setIsNoInternet(false);
                startSocket();
            } else {
                if (!NetWorkManager.getInstance().isIsNoInternet()) {
//                    NoInternetEvent.post();
                }
                NetWorkManager.getInstance().setIsNoInternet(true);
                stopSocket();
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSocket();
        synchronized (lock) {
            if (INSTANCE != null) {
                unregisterReceiver(mNetStateReceiver);
                INSTANCE = null;
            }
        }
    }

    public static void keepAlive() {
        if (INSTANCE == null) {
            startService();
        } else {
            INSTANCE.startSocket();
        }
    }

    public static boolean isConnected() {
        if (INSTANCE == null) {
            return false;
        } else {
            return EMClient.getInstance().isConnected();
        }
    }

    private void startSocket() {
        synchronized (lock) {
            if (isConnected()) {return;}
            if (EMClient.getInstance().isLoggedInBefore()) {
                mHuanXinConnectionListener = new HuanXinConnectionListener();
                EMClient.getInstance().addConnectionListener(mHuanXinConnectionListener);
                EMClient.getInstance().chatManager().addMessageListener(msgListener);
            } else {
                loginHuanXin();
            }
        }
    }

    private void stopSocket() {
        synchronized (lock) {
            EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        }
    }

    //实现ConnectionListener接口
    public static class HuanXinConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            Log.d("HuanXinServer", ",onConnected ");
        }

        @Override
        public void onDisconnected(final int error) {
            if (error == EMError.USER_REMOVED) {
                Log.d("HuanXinServer", ",显示帐号已经被移除 ");
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                Log.d("HuanXinServer", ",显示帐号在其他设备登录 ");
            } else {
                if (NetUtils.hasNetwork(CCApplication.getInstance())) {
                    Log.d("HuanXinServer", ",连接不到聊天服务器 ");
                    loginHuanXin();
                } else {
                    Log.d("HuanXinServer", ",请检查网络设置 ");
                }
            }
        }
    }

    public static EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            Log.d("HuanXinServer", "msgListener onMessageReceived  收到消息 messages = ");
            for (EMMessage message : messages) {
                Log.d("HuanXinServer", "msgListener onMessageReceived  收到消息 messages = " + message);
                MessageBean messageBean = new MessageBean();
                messageBean.setMessage(((EMTextMessageBody) message.getBody()).getMessage());
                messageBean.setReceiceUserName(message.getTo());
                messageBean.setSendUserName(message.getFrom());
                messageBean.setCreateAt(message.getMsgTime());
                EventBus.getDefault().post(messageBean);

            }

//            ToastHelper.showShortMessage("收到消息 messages = " + messages);
//            MessageBean message = ;
//            message.conversation(message.getData());
//            EventBus.getDefault().post(message);
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            Log.d("HuanXinServer", "msgListener 收到透传消息  onCmdMessageReceived  messages = " + messages);
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            Log.d("HuanXinServer", "msgListener 收到已读回执  onMessageRead  messages = " + messages);
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
            Log.d("HuanXinServer", "msgListener 收到已送达回执  onMessageDelivered  messages = " + message);
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {

        }


        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            Log.d("HuanXinServer", "msgListener 消息状态变动  onMessageChanged  messages = " + message);
        }
    };

    public static void sendMessage(String content, String userName, final ICallback callback) {
        EMMessage message = EMMessage.createTxtSendMessage(content, userName);
        message.setChatType(EMMessage.ChatType.Chat);
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.d("HuanXinServer", "发送成功");
                if (callback != null) {
                    callback.onResult(1);
                }
            }

            @Override
            public void onError(int code, String error) {
                Log.d("HuanXinServer", "发送失败" + code + "," + error);
                if (code == 201) {
                    loginHuanXin();
                }
                if (callback != null) {
                    callback.onStringError(code, error);
                }
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d("HuanXinServer", "正在发送中" + progress + "," + status);

            }
        });
    }

    public static void loginHuanXin() {
        CurrentUser currentUser = CurrentUserManager.getInstance().getCurrentUser();
        if (currentUser == null) {return;}
        EMClient.getInstance().login(currentUser.getUserName(), currentUser.getPwd(), new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
                keepAlive();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
            }
        });
    }
}

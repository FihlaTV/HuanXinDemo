package a.cool.huanxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发送文本消息
 * //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
 * EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
 * //如果是群聊，设置chattype，默认是单聊
 * if (chatType == CHATTYPE_GROUP)
 * message.setChatType(ChatType.GroupChat);
 * //发送消息
 * EMClient.getInstance().chatManager().sendMessage(message);
 * 发送语音消息
 * //filePath为语音文件路径，length为录音时间(秒)
 * EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
 * //如果是群聊，设置chattype，默认是单聊
 * if (chatType == CHATTYPE_GROUP)
 * message.setChatType(ChatType.GroupChat);
 * EMClient.getInstance().chatManager().sendMessage(message);
 * 发送视频消息
 * //videoPath为视频本地路径，thumbPath为视频预览图路径，videoLength为视频时间长度
 * EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
 * //如果是群聊，设置chattype，默认是单聊
 * if (chatType == CHATTYPE_GROUP)
 * message.setChatType(ChatType.GroupChat);
 * EMClient.getInstance().chatManager().sendMessage(message);
 * 发送图片消息
 * //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
 * EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
 * //如果是群聊，设置chattype，默认是单聊
 * if (chatType == CHATTYPE_GROUP)
 * message.setChatType(ChatType.GroupChat);
 * EMClient.getInstance().chatManager().sendMessage(message);
 * 发送地理位置消息
 * //latitude为纬度，longitude为经度，locationAddress为具体位置内容
 * EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
 * //如果是群聊，设置chattype，默认是单聊
 * if (chatType == CHATTYPE_GROUP)
 * message.setChatType(ChatType.GroupChat);
 * EMClient.getInstance().chatManager().sendMessage(message);
 * 发送文件消息
 * EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
 * // 如果是群聊，设置chattype，默认是单聊
 * if (chatType == CHATTYPE_GROUP)
 * message.setChatType(ChatType.GroupChat);
 * EMClient.getInstance().chatManager().sendMessage(message);
 * 发送透传消息
 * 透传消息能做什么：头像、昵称的更新等。可以把透传消息理解为一条指令，通过发送这条指令给对方，告诉对方要做的 action，收到消息可以自定义处理的一种消息。（透传消息不会存入本地数据库中，所以在 UI 上是不会显示的）。另，以“em_”和“easemob::”开头的action为内部保留字段，注意不要使用
 * <p>
 * EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
 * <p>
 * //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
 * cmdMsg.setChatType(ChatType.GroupChat)
 * String action="action1";//action可以自定义
 * EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
 * String toUsername = "test1";//发送给某个人
 * cmdMsg.setTo(toUsername);
 * cmdMsg.addBody(cmdBody);
 * EMClient.getInstance().chatManager().sendMessage(cmdMsg);
 * 发送扩展消息
 * 当 SDK 提供的消息类型不满足需求时，开发者可以通过扩展自 SDK 提供的文本、语音、图片、位置等消息类型，从而生成自己需要的消息类型。
 * <p>
 * 这里是扩展自文本消息，如果这个自定义的消息需要用到语音或者图片等，可以扩展自语音、图片消息，亦或是位置消息。
 * <p>
 * EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
 * <p>
 * // 增加自己特定的属性
 * message.setAttribute("attribute1", "value");
 * message.setAttribute("attribute2", true);
 * ...
 * EMClient.getInstance().chatManager().sendMessage(message);
 * <p>
 * //接收消息的时候获取到扩展属性
 * //获取自定义的属性，第2个参数为没有此定义的属性时返回的默认值
 * message.getStringAttribute("attribute1",null);
 * message.getBooleanAttribute("attribute2", false);
 * ...
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edt_main_activity) EditText edtMainActivity;
    @BindView(R.id.btn_send) Button btnSend;
    @BindView(R.id.edt_user_name_main_activity) EditText edtUserNameMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        Log.i("MainActivity", "onDisconnected  显示帐号已经被移除");
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        Log.i("MainActivity", "onDisconnected  显示帐号在其他设备登录");
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)) {
                            //连接不到聊天服务器
                            Log.i("MainActivity", "onDisconnected  连接不到聊天服务器");
                        } else {
                            //当前网络不可用，请检查网络设置
                            Log.i("MainActivity", "onDisconnected  请检查网络设置");
                        }
                    }
                }
            });
        }
    }

    /**
     *
     */

    @OnClick(R.id.btn_send)
    public void toSendMessage() {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(edtMainActivity.getText().toString(), edtUserNameMainActivity.getText().toString());
        //如果是群聊，设置chattype，默认是单聊
//        if (chatType == CHATTYPE_GROUP) {
//            message.setChatType(EMMessage.ChatType.GroupChat);
//        }
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("MainActivity", "setMessageStatusCallback onSuccess ");
            }

            @Override
            public void onError(int code, String error) {
                Log.i("MainActivity", "setMessageStatusCallback onError  error = " + error);
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    @OnClick(R.id.btn_getmessage_history)
    public void getHistoryMessage() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation("1234");
        //获取此会话的所有消息
        List<EMMessage> messages = conversation.getAllMessages();
        Log.i("MainActivity", "getHistoryMessage messages  messages = " + messages);
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
//        List<EMMessage> messages2 = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
    }

    @OnClick(R.id.btn_get_unread_message)
    public void getUnReadMessage() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation("1234");
        //获取此会话的所有消息
        List<EMMessage> messages = conversation.getAllMessages();

        Log.i("MainActivity", "getHistoryMessage messages  messages = " + messages);
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
//        List<EMMessage> messages2 = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
    }

    @OnClick(R.id.btn_east_ui)
    public void toEasyUI() {
        startActivity(new Intent(this, EasyUIActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 记得在不需要的时候移除listener，如在activity的onDestroy()时
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            Log.i("MainActivity", "msgListener onMessageReceived  收到消息 messages = " + messages);

            ToastHelper.showShortMessage("收到消息 messages = " + messages);
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            Log.i("MainActivity", "msgListener 收到透传消息  onCmdMessageReceived  messages = " + messages);
            ToastHelper.showShortMessage("收到透传消息 messages = " + messages);
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
            Log.i("MainActivity", "msgListener 收到已读回执  onMessageRead  messages = " + messages);
            ToastHelper.showShortMessage("收到已读回执 messages = " + messages);
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
            Log.i("MainActivity", "msgListener 收到已送达回执  onMessageDelivered  messages = " + message);
            ToastHelper.showShortMessage("收到已送达回执 messages = " + message);
        }


        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            Log.i("MainActivity", "msgListener 消息状态变动  onMessageChanged  messages = " + message);
            ToastHelper.showShortMessage("消息状态变动 messages = " + message);
        }
    };


}

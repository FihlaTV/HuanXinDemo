package a.cool.huanxin.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import a.cool.huanxin.R;
import a.cool.huanxin.adapter.MessageAdapter;
import a.cool.huanxin.base.BaseActivity;
import a.cool.huanxin.base.ICallback;
import a.cool.huanxin.ben.ChatMessage;
import a.cool.huanxin.ben.CurrentUser;
import a.cool.huanxin.ben.MessageBean;
import a.cool.huanxin.constants.AppConstant;
import a.cool.huanxin.manager.CurrentUserManager;
import a.cool.huanxin.service.HuanXinServer;
import a.cool.huanxin.utils.ResourceUtil;
import a.cool.huanxin.utils.ToastHelper;
import butterknife.BindView;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity {

    @BindView(R.id.iv_back_chat_activity) ImageView ivBackChatActivity;
    @BindView(R.id.tv_user_name_chat_activity) TextView tvUserNameChatActivity;
    @BindView(R.id.ll_title_chat_activity) RelativeLayout llTitleChatActivity;
    @BindView(R.id.rlv_chat_activity) RecyclerView mRecyclerView;
    @BindView(R.id.edt_chat_activity) EditText mInputEdtChat;
    @BindView(R.id.rl_send_message) RelativeLayout rlSendMessage;
    @BindView(R.id.ll_input_chat_activity) LinearLayout llInputChatActivity;

    @BindView(R.id.srl_chat_messages) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_input_line_chat_activity) TextView mInputLine;
    private ChatMessage mChatMessage;
    private int mUserId;
    private boolean isSendMessing;
    private MessageAdapter messageAdapter;
    private List<MessageBean> mMessageBeanList = new ArrayList<>();
    private CurrentUser mCurrentUser;
    private String mCurrentUserName;
    private String mFriendUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mCurrentUser = CurrentUserManager.getInstance().getCurrentUser();
        mChatMessage = getIntent().getParcelableExtra(AppConstant.IntentKey.EXTRA_DATA);
        if (mCurrentUser == null || mChatMessage == null) { onBackPressed(); }
        tvUserNameChatActivity.setText(mChatMessage.getUserName());
        mCurrentUserName = mCurrentUser.getUserName();
        mFriendUserName = mChatMessage.getUserName();
        ImmersionBar.with(this).keyboardEnable(true).init();
        initRecyclerView();
        initSwipeRefreshLayout();
        getTextMessageData();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @OnClick(R.id.rl_send_message)
    public void sendMessageClicked() {
        if (mInputEdtChat == null || isSendMessing) { return;}
        isSendMessing = true;
        final String content = mInputEdtChat.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastHelper.showShortMessage("输入内容后再点击发送");
            isSendMessing = false;
            return;
        }
        if (mChatMessage == null) {
            ToastHelper.showShortMessage("查无此用户");
            isSendMessing = false;
            return;
        }
        final String receiverUserName = mChatMessage.getPhoneNumber();
        if (TextUtils.isEmpty(receiverUserName)) {
            ToastHelper.showShortMessage("此用户未注册环信");
            isSendMessing = false;
            return;
        }
        LogUtils.d("HuanXinServer", " receiverUserName = " + receiverUserName);
        HuanXinServer.sendMessage(content, receiverUserName, new ICallback() {
            @Override
            public void onResult(Object o) {
                Log.i("MainActivity", "setMessageStatusCallback onSuccess ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MessageBean messageBean = new MessageBean();
                        messageBean.setCreateAt(System.currentTimeMillis());
                        messageBean.setSendUserName(mCurrentUserName);
                        messageBean.setReceiceUserName(mFriendUserName);
                        messageBean.setMessage(content);
                        mMessageBeanList.add(messageBean);
//                        MessageBeanUtils.getInstance().insertOneData(messageBean);
                        ToastHelper.showShortMessage("发送消息成功");
                        refreshRecyclerView(mMessageBeanList);
                        if (mInputEdtChat != null) {
                            mInputEdtChat.setText("");
                        }
                        isSendMessing = false;
                    }
                });
            }

            @Override
            public void onError(final Throwable error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MainActivity", "setMessageStatusCallback onError  error = " + error);
                        ToastHelper.showShortMessage("发送消息失败:" + error);
                        isSendMessing = false;
                    }
                });
            }

            @Override
            public void onStringError(int code, String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isSendMessing = false;
                    }
                });
            }
        });
    }

    @OnClick(R.id.iv_back_chat_activity)
    public void backClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.exit_stop_original_place, R.anim.exit_to_right);
    }

    public void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(mMessageBeanList, this, mCurrentUserName);
        mRecyclerView.setAdapter(messageAdapter);
    }

    public void getTextMessageData() {
//        List<MessageBean> messageBeanArrayList = MessageBeanUtils.getInstance().queryAllData();
//        if (messageBeanArrayList != null && messageBeanArrayList.size() > 0) {
//            for (MessageBean messageBean : messageBeanArrayList) {
//                if (isCurrentMessage(messageBean)) {
//                    mMessageBeanList.add(messageBean);
//                }
//            }
//        }
        refreshRecyclerView(mMessageBeanList);
    }

    public boolean isCurrentMessage(MessageBean messageBean) {
        if (messageBean == null) {return false;}
        return ((messageBean.getSendUserName().equals(mCurrentUserName) && messageBean.getReceiceUserName().equals(mFriendUserName)) || ((messageBean.getSendUserName().equals(mFriendUserName)
                && messageBean.getReceiceUserName().equals(mCurrentUserName))));
    }

    public void initSwipeRefreshLayout() {
//        logger.d("initSwipeRefreshLayout()");
        if (mSwipeRefreshLayout != null && mChatMessage != null) {
            mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(ResourceUtil.getColor(R.color.transparent));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.text_chat_progress_color);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
//                    IMChatService.getInstance().loadMessages(mConversation, mTextChatMessageAdapter.getItemCount(), 50, new ICallback<List<DBMessage>>() {
//                        @Override
//                        public void onResult(List<DBMessage> dbMessages) {
//                            if (isRefreshingMessage) {
//                                return;
//                            }
//                            isRefreshingMessage = true;
//                            if (dbMessages != null) {
//                                int index = dbMessages.size();
//                                insertRefreshRecyclerView(dbMessages, index);
//                            }
//                            if (mSwipeRefreshLayout != null) {
//                                mSwipeRefreshLayout.setRefreshing(false);
//                            }
//                            isRefreshingMessage = false;
//                        }
//
//                        @Override
//                        public void onError(Throwable error) {
//                            logger.d("initSwipeRefreshLayout() getTextMessageData() Failed error = " + error);
//                            if (mSwipeRefreshLayout != null) {
//                                mSwipeRefreshLayout.setRefreshing(false);
//                            }
//                            isRefreshingMessage = false;
//                        }
//                    });
                }
            });
        }
    }

    protected void refreshRecyclerView(final List<MessageBean> dbMessages) {
        if (messageAdapter == null) {return;}
        messageAdapter.setData(dbMessages);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveTextChatMessage(MessageBean messageBean) {
        LogUtils.d("HuanXinServer onReceiveTextChatMessage messageBean = " + messageBean);
        if (messageBean.getReceiceUserName().equals(mCurrentUserName) && messageBean.getSendUserName().equals(mFriendUserName)) {
            mMessageBeanList.add(messageBean);
            refreshRecyclerView(mMessageBeanList);
        }
    }
}

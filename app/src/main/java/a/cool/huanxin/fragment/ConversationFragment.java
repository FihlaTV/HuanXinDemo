package a.cool.huanxin.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.blankj.utilcode.util.LogUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import a.cool.huanxin.R;
import a.cool.huanxin.adapter.ChatMessageAdapter;
import a.cool.huanxin.base.BaseFragment;
import a.cool.huanxin.ben.ChatMessage;
import a.cool.huanxin.constants.AppConstant;
import a.cool.huanxin.utils.ActivityUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ConversationFragment extends BaseFragment implements View.OnTouchListener {

    @BindView(R.id.rl_conversation_list) RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ChatMessageAdapter mChatMessageAdapter;
    private List<ChatMessage> chatMessagesList = new ArrayList<>();

    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(this);
        getFriendList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getFriendList() {
        if (EMClient.getInstance().isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                        LogUtils.d("ConversationFragment onViewCreated usernames = " + usernames);
                        for (String username : usernames) {
                            ChatMessage chatMessage = new ChatMessage();
                            chatMessage.setUserName(username);
                            chatMessagesList.add(chatMessage);
                        }
                        initRecyclerView(chatMessagesList);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        LogUtils.d("ConversationFragment getContacts error HyphenateException : " + e);
                    }
                }
            }).start();

        } else {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtils.d("ConversationFragment getContacts e mRecyclerView.postDelayed(new Runnable()");

                    getFriendList();
                }
            }, 1000);
        }
    }

    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarColor(R.color.btn3)
                .init();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


    public void initRecyclerView(final List<ChatMessage> userList) {
        if (mRecyclerView == null) {return;}
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (userList != null && userList.size() > 0 && mRecyclerView != null) {
//            mTips.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (mLinearLayoutManager == null) {
                        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        mRecyclerView.setLayoutManager(mLinearLayoutManager);
                    }
                    if (mChatMessageAdapter == null) {
                        mChatMessageAdapter = new ChatMessageAdapter();
                        mChatMessageAdapter.setDataSilently(userList);
                        mChatMessageAdapter.setOnItemClickListener(mMessageItemClickListener);
                        mRecyclerView.setAdapter(mChatMessageAdapter);
                    } else {
                        mChatMessageAdapter.setData(userList);
                    }
                } else if (mRecyclerView != null) {
                    mRecyclerView.setVisibility(View.GONE);
//            mTips.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private AdapterView.OnItemClickListener mMessageItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
            if (mChatMessageAdapter == null) { return; }
            ChatMessage chatMessage = mChatMessageAdapter.getItem(position);
            if (chatMessage == null) {return;}
            ActivityUtil.startChatActivity(ConversationFragment.this, chatMessage, AppConstant.ActivityRequestCode.GO_TO_TEXT_CHAT_ACTIVITY);
        }
    };

    /**
     * refresh ui
     */
    public void refresh(ChatMessage chatMessage) {
        LogUtils.d("ConversationFragment refresh chatMessage = " + chatMessage);
        chatMessagesList.add(chatMessage);
        initRecyclerView(chatMessagesList);
    }
}

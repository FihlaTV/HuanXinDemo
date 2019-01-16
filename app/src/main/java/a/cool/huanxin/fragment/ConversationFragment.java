package a.cool.huanxin.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gyf.barlibrary.ImmersionBar;

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

    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


    public void initRecyclerView(List<ChatMessage> userList) {
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
    public void refresh() {
//        if(!handler.hasMessages(MSG_REFRESH)){
//            handler.sendEmptyMessage(MSG_REFRESH);
//        }
    }


}

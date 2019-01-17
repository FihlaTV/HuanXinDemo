package a.cool.huanxin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import a.cool.huanxin.R;
import a.cool.huanxin.activity.AddContactActivity;
import a.cool.huanxin.adapter.AddFriendAdapter;
import a.cool.huanxin.base.BaseFragment;
import a.cool.huanxin.ben.AddFriendBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ContactsFragment extends BaseFragment implements View.OnTouchListener {

    @BindView(R.id.iv_add_friend) ImageView ivAddFriend;
    @BindView(R.id.btn_notification) Button btnNotification;
    @BindView(R.id.btn_group_chat) Button btnGroupChat;
    @BindView(R.id.btn_chat_room) Button btnChatRoom;
    @BindView(R.id.btn_video_chat_room) Button btnVideoChatRoom;
    @BindView(R.id.rl_contacts_list) RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private AddFriendAdapter mAddFriendAdapter;
    private List<AddFriendBean> mAddFriendBeanList = new ArrayList<>();
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(this);
        initRecyclerView(mAddFriendBeanList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).statusBarColorTransformEnable(false)
                .keyboardEnable(false)
                .navigationBarColor(R.color.colorPrimary)
                .init();
    }

    /**
     * refresh ui
     */
    public void refresh(AddFriendBean addFriendBean) {
        mAddFriendBeanList.add(addFriendBean);
        initRecyclerView(mAddFriendBeanList);

//        if(!handler.hasMessages(MSG_REFRESH)){
//            handler.sendEmptyMessage(MSG_REFRESH);
//        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @OnClick(R.id.iv_add_friend)
    public void addFriend() {
        startActivity(new Intent(getActivity(), AddContactActivity.class));
    }

    @OnClick(R.id.btn_notification)
    public void notiificationClicked() {

    }

    @OnClick(R.id.btn_group_chat)
    public void groupChatClicked() {

    }

    @OnClick(R.id.btn_video_chat_room)
    public void meetingClicked() {

    }

    public void initRecyclerView(List<AddFriendBean> userList) {
        if (userList != null && userList.size() > 0 && mRecyclerView != null) {
//            mTips.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            if (mLinearLayoutManager == null) {
                mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
            }
            if (mAddFriendAdapter == null) {
                mAddFriendAdapter = new AddFriendAdapter();
                mAddFriendAdapter.setDataSilently(userList);
                mRecyclerView.setAdapter(mAddFriendAdapter);
            } else {
                mAddFriendAdapter.setData(userList);
            }
        } else if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.GONE);
//            mTips.setVisibility(View.VISIBLE);
        }
    }
}

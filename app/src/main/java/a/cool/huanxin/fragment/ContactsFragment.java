package a.cool.huanxin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;

import a.cool.huanxin.R;
import a.cool.huanxin.activity.AddContactActivity;
import a.cool.huanxin.base.BaseFragment;
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
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
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
    public void refresh() {
//        if(!handler.hasMessages(MSG_REFRESH)){
//            handler.sendEmptyMessage(MSG_REFRESH);
//        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
}

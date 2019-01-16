package a.cool.huanxin.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import a.cool.huanxin.R;
import a.cool.huanxin.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewFriendsMsgActivity extends BaseActivity {

    @BindView(R.id.iv_back_chat_activity) ImageView ivBackChatActivity;
    @BindView(R.id.ll_title_chat_activity) RelativeLayout llTitleChatActivity;
    @BindView(R.id.list) ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends_msg);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_back_chat_activity)
    public void back() {
        finish();
    }
}

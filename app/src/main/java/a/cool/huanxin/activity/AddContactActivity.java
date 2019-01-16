package a.cool.huanxin.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.hyphenate.chat.EMClient;

import a.cool.huanxin.R;
import a.cool.huanxin.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddContactActivity extends BaseActivity {

    @BindView(R.id.iv_back_chat_activity) ImageView ivBackChatActivity;
    @BindView(R.id.tv_user_name_chat_activity) TextView tvUserNameChatActivity;
    @BindView(R.id.ll_title_chat_activity) RelativeLayout llTitleChatActivity;
    @BindView(R.id.edit_note) EditText editNote;
    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.name) TextView nameTextView;
    @BindView(R.id.indicator) Button indicator;
    @BindView(R.id.ll_user) RelativeLayout llUser;
    @BindView(R.id.btn_search) Button btnSearch;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_search})
    public void search() {
         String name = editNote.getText().toString();
        if (TextUtils.isEmpty(name)) {return;}
        //show the userame and add button if user exist
        // TODO you can search the user from your app server here.
        llUser.setVisibility(View.VISIBLE);
        nameTextView.setText(name);
    }

    @OnClick({R.id.indicator})
    public void addFriend() {
        if (EMClient.getInstance().getCurrentUser().equals(nameTextView.getText().toString())) {
            ToastUtils.showShort("输入不能为空");
            return;
        }
        if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(nameTextView.getText().toString())) {
            ToastUtils.showShort("已经是好友");
            return;
        }
        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //demo use a hardcode reason here, you need let user to input if you like
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager().addContact(nameTextView.getText().toString(), s);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    @OnClick(R.id.iv_back_chat_activity)
    public void back() {
        finish();
    }
}

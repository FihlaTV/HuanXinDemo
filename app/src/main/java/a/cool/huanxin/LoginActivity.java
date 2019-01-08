package a.cool.huanxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edt_user_name) EditText edtUserName;
    @BindView(R.id.edt_pwd_name) EditText edtPwdName;
    @BindView(R.id.btn_login) Button btnLogin;
    @BindView(R.id.btn_to_register) Button mToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void registerClicke() {
        String userName = edtUserName.getText().toString();
        String pwd = edtPwdName.getText().toString();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {return;}
        if ((userName.equals(SharedPrefUtils.getInstance().getString("UserName"))) &&
                pwd.equals(SharedPrefUtils.getInstance().getString("Pwd"))) {
            SharedPrefUtils.getInstance().putBoolean("HashLogin", true);
            EMClient.getInstance().login(userName, pwd, new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    Log.d("main", "登录聊天服务器成功！");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
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

    @OnClick(R.id.btn_to_register)
    public void toRegisterClicke() {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}

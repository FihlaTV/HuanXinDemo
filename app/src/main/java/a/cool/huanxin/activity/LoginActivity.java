package a.cool.huanxin.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import a.cool.huanxin.R;
import a.cool.huanxin.base.BaseActivity;
import a.cool.huanxin.ben.CurrentUser;
import a.cool.huanxin.manager.CurrentUserManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edt_user_name) EditText edtUserName;
    @BindView(R.id.edt_pwd_name) EditText edtPwdName;
    @BindView(R.id.btn_login) Button btnLogin;
    @BindView(R.id.btn_to_register) Button mToRegister;

    private boolean progressShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void registerClicke() {
        final String userName = edtUserName.getText().toString();
        final String pwd = edtPwdName.getText().toString();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {return;}

        progressShow = true;
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d("LoginActivity", "EMClient.getInstance().onCancel");
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();
        EMClient.getInstance().login(userName, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }
                CurrentUser currentUser = new CurrentUser();
                currentUser.setUserName(userName);
                currentUser.setPwd(pwd);
                CurrentUserManager.getInstance().saveCurrentUser(currentUser);
                Log.d("main", "登录聊天服务器成功！");
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, final String message) {
                Log.d("main", "登录聊天服务器失败！");
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @OnClick(R.id.btn_to_register)
    public void toRegisterClicke() {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}

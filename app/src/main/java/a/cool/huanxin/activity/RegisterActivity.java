package a.cool.huanxin.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import a.cool.huanxin.R;
import a.cool.huanxin.base.BaseActivity;
import a.cool.huanxin.ben.CurrentUser;
import a.cool.huanxin.manager.CurrentUserManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.edt_user_name) EditText edtUserName;
    @BindView(R.id.edt_pwd_name) EditText edtPwdName;
    @BindView(R.id.btn_register) Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_register)
    public void registerClicke() {
        final String userName = edtUserName.getText().toString();
        final String pwd = edtPwdName.getText().toString();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {return;}
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.Is_the_registered));
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // call method in SDK
                    EMClient.getInstance().createAccount(userName, pwd);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!RegisterActivity.this.isFinishing()) {
                                pd.dismiss();
                                CurrentUser currentUser = new CurrentUser();
                                currentUser.setUserName(userName);
                                currentUser.setPwd(pwd);
                                CurrentUserManager.getInstance().saveCurrentUser(currentUser);
                                ToastUtils.showShort("注册成功");
                                pd.dismiss();
                                finish();
                            }
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!RegisterActivity.this.isFinishing()) {
                                pd.dismiss();
                                int errorCode = e.getErrorCode();
                                if (errorCode == EMError.NETWORK_ERROR) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.EXCEED_SERVICE_LIMIT) {
                                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.register_exceed_service_limit), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        }).start();
    }
}

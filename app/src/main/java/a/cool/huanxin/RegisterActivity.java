package a.cool.huanxin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

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
        try {
            final String userName = edtUserName.getText().toString();
            final String pwd = edtPwdName.getText().toString();
            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {return;}
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
                                    SharedPrefUtils.getInstance().putString("UserName", userName);
                                }
                                SharedPrefUtils.getInstance().putString("Pwd", pwd);
                                finish();
                            }
                        });
                    } catch (final HyphenateException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!RegisterActivity.this.isFinishing()) {
                                }
                                int errorCode = e.getErrorCode();
                                if (errorCode == EMError.NETWORK_ERROR) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).start();


            //注册失败会抛出HyphenateException
            EMClient.getInstance().createAccount(userName, pwd);
            SharedPrefUtils.getInstance().putString("UserName", userName);
            SharedPrefUtils.getInstance().putString("Pwd", pwd);
            finish();
        } catch (final HyphenateException e) {

            int errorCode = e.getErrorCode();
            if (errorCode == EMError.NETWORK_ERROR) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
            } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
            } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
            } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
//                    } else if (errorCode == EMError.EXCEED_SERVICE_LIMIT) {
//                        Toast.makeText(RegisterActivity.this, getResources().getString(R.string.register_exceed_service_limit), Toast.LENGTH_SHORT).show();
//                    } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
            }
        }

    }
}

package a.cool.huanxin.activity;

import android.content.Intent;
import android.os.Bundle;

import com.hyphenate.chat.EMClient;

import a.cool.huanxin.R;
import a.cool.huanxin.base.BaseActivity;
import a.cool.huanxin.ben.CurrentUser;
import a.cool.huanxin.manager.CurrentUserManager;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        CurrentUser currentUser = CurrentUserManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            EMClient.getInstance().chatManager().loadAllConversations();
            EMClient.getInstance().groupManager().loadAllGroups();
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}

package a.cool.huanxin.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;

import a.cool.huanxin.R;
import a.cool.huanxin.base.BaseActivity;
import a.cool.huanxin.ben.CurrentUser;
import a.cool.huanxin.fragment.ContactsFragment;
import a.cool.huanxin.fragment.ConversationFragment;
import a.cool.huanxin.fragment.DiscoverFragment;
import a.cool.huanxin.fragment.MeFragment;
import a.cool.huanxin.manager.CurrentUserManager;
import a.cool.huanxin.service.HuanXinServer;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.ll_category)
    LinearLayout llCategory;
    @BindView(R.id.ll_service)
    LinearLayout llService;
    @BindView(R.id.ll_mine)
    LinearLayout llMine;

    private ConversationFragment mConversationFragment;
    private ContactsFragment mContactsFragment;
    private DiscoverFragment mDiscoverFragment;
    private MeFragment mMeFragment;
    private CurrentUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        HuanXinServer.keepAlive();
        selectedFragment(0);
        tabSelected(llHome);
        llHome.setOnClickListener(this);
        llCategory.setOnClickListener(this);
        llService.setOnClickListener(this);
        llMine.setOnClickListener(this);
        mCurrentUser = CurrentUserManager.getInstance().getCurrentUser();
        LogUtils.d("MainActivity  Current = " + mCurrentUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                selectedFragment(0);
                tabSelected(llHome);
                break;
            case R.id.ll_category:
                selectedFragment(1);
                tabSelected(llCategory);
                break;
            case R.id.ll_service:
                selectedFragment(2);
                tabSelected(llService);
                break;
            case R.id.ll_mine:
                selectedFragment(3);
                tabSelected(llMine);
                break;
            default:
                break;
        }
    }

    private void selectedFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:
                if (mConversationFragment == null) {
                    mConversationFragment = new ConversationFragment();
                    transaction.add(R.id.content, mConversationFragment);
                } else {
                    transaction.show(mConversationFragment);
                }
                break;
            case 1:
                if (mContactsFragment == null) {
                    mContactsFragment = new ContactsFragment();
                    transaction.add(R.id.content, mContactsFragment);
                } else {
                    transaction.show(mContactsFragment);
                }
                break;
            case 2:
                if (mDiscoverFragment == null) {
                    mDiscoverFragment = new DiscoverFragment();
                    transaction.add(R.id.content, mDiscoverFragment);
                } else {
                    transaction.show(mDiscoverFragment);
                }
                break;
            case 3:
                if (mMeFragment == null) {
                    mMeFragment = new MeFragment();
                    transaction.add(R.id.content, mMeFragment);
                } else {
                    transaction.show(mMeFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mConversationFragment != null) {
            transaction.hide(mConversationFragment);
        }
        if (mContactsFragment != null) {
            transaction.hide(mContactsFragment);
        }
        if (mDiscoverFragment != null) {
            transaction.hide(mDiscoverFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }
    }

    private void tabSelected(LinearLayout linearLayout) {
        llHome.setSelected(false);
        llCategory.setSelected(false);
        llService.setSelected(false);
        llMine.setSelected(false);
        linearLayout.setSelected(true);
    }
}

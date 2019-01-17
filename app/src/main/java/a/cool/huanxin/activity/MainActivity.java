package a.cool.huanxin.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.hyphenate.EMClientListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMultiDeviceListener;
import com.hyphenate.chat.EMClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import a.cool.huanxin.R;
import a.cool.huanxin.base.BaseActivity;
import a.cool.huanxin.ben.AddFriendBean;
import a.cool.huanxin.ben.ChatMessage;
import a.cool.huanxin.ben.CurrentUser;
import a.cool.huanxin.constants.Constant;
import a.cool.huanxin.fragment.ContactsFragment;
import a.cool.huanxin.fragment.ConversationFragment;
import a.cool.huanxin.fragment.DiscoverFragment;
import a.cool.huanxin.fragment.MeFragment;
import a.cool.huanxin.manager.CurrentUserManager;
import a.cool.huanxin.service.HuanXinServer;
import a.cool.huanxin.utils.ActivityUtil;
import a.cool.huanxin.utils.ThreadExecutor;
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

    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private int currentTabIndex;
    private BroadcastReceiver internalDebugReceiver;

    private LocationManager mLocationManager;
    private String mLocationProvider;

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

        registerBroadcastReceiver();

        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        EMClient.getInstance().addClientListener(clientListener);
        EMClient.getInstance().addMultiDeviceListener(new MyMultiDeviceListener());
        //debug purpose only
        registerInternalDebugReceiver();
        getPermission();
    }

    /**
     * debug purpose only, you can ignore this
     */
    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
//                DemoHelper.getInstance().logout(false, new EMCallBack() {
//                    @Override
//                    public void onSuccess() {
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                finish();
//                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onProgress(int progress, String status) {}
//
//                    @Override
//                    public void onError(int code, String message) {}
//                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
            LogUtils.d("MyContactListener onContactAdded = " + username);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUserName(username);
            if (mConversationFragment != null) {
                mConversationFragment.refresh(chatMessage);
            }
        }

        @Override
        public void onContactDeleted(final String username) {
            LogUtils.d("MyContactListener onContactDeleted = " + username);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

//
//                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
//                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
//                        String st10 = getResources().getString(R.string.have_you_removed);
//                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
//                                .show();
//                        ChatActivity.activityInstance.finish();
//                    }
                }
            });
            updateUnreadAddressLable();
        }

        @Override
        public void onContactInvited(String username, String reason) {
            LogUtils.d("MyContactListener onContactInvited = " + username);
            //当有好友添加时 这里监听
            AddFriendBean addFriendBean = new AddFriendBean();
            addFriendBean.setUserName(username);
            if (mContactsFragment != null) {
                mContactsFragment.refresh(addFriendBean);
            }
        }

        @Override
        public void onFriendRequestAccepted(String username) {
            LogUtils.d("MyContactListener onFriendRequestAccepted = " + username);
        }

        @Override
        public void onFriendRequestDeclined(String username) {
            LogUtils.d("MyContactListener onFriendRequestDeclined = " + username);
        }
    }

    EMClientListener clientListener = new EMClientListener() {
        @Override
        public void onMigrate2x(boolean success) {
            Toast.makeText(MainActivity.this, "onUpgradeFrom 2.x to 3.x " + (success ? "success" : "fail"), Toast.LENGTH_LONG).show();
            if (success) {
                refreshUIWithMessage();
            }
        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                if (currentTabIndex == 0) {
                    // refresh conversation list
                    if (mContactsFragment != null) {
//                        mContactsFragment.refresh();
                    }
                }
            }
        });
    }


    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
                if (currentTabIndex == 0) {
                    // refresh conversation list
                    if (mConversationFragment != null) {
//                        mConversationFragment.refresh();
                    }
                } else if (currentTabIndex == 1) {
                    if (mContactsFragment != null) {
//                        mContactsFragment.refresh();
                    }
                }
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
//                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyMultiDeviceListener implements EMMultiDeviceListener {

        @Override
        public void onContactEvent(int event, String target, String ext) {

        }

        @Override
        public void onGroupEvent(int event, String target, final List<String> username) {
            switch (event) {
                case EMMultiDeviceListener.GROUP_LEAVE:
//                    ChatActivity.activityInstance.finish();
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
//            unreadLabel.setText(String.valueOf(count));
//            unreadLabel.setVisibility(View.VISIBLE);
        } else {
//            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                int count = getUnreadAddressCountTotal();
//                if (count > 0) {
//                    unreadAddressLable.setVisibility(View.VISIBLE);
//                } else {
//                    unreadAddressLable.setVisibility(View.INVISIBLE);
//                }
            }
        });

    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMessageCount();
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
        currentTabIndex = position;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HuanXinServer.stopService();
        unregisterBroadcastReceiver();
        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    public void getPermission() {
        PermissionUtils.permission(PermissionConstants.LOCATION)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        shouldRequest.again(true);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        refreshLocation();
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {
                            PermissionUtils.launchAppDetailsSettings();
                        }
                    }
                }).request();
    }

    @SuppressLint("MissingPermission")
    public void refreshLocation() {
        LogUtils.d("getAddress refreshLocation(0");
        ThreadExecutor.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (isViewClose()) { return; }
                LogUtils.d("getAddress refreshLocation()  000");
                mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                List<String> providers = mLocationManager.getProviders(true);
                if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                    mLocationProvider = LocationManager.NETWORK_PROVIDER;
                    LogUtils.d("getAddress refreshLocation()  111");
                } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
                    mLocationProvider = LocationManager.GPS_PROVIDER;
                    LogUtils.d("getAddress refreshLocation()  2222");
                } else {
                    return;
                }
                Location location = mLocationManager.getLastKnownLocation(mLocationProvider);
                if (location != null) {
                    LogUtils.d("getAddress refreshLocation()  3333 = " + location.getLatitude() + " location.getLongitude() = " + location.getLongitude());
                    getAddress(location.getLatitude(), location.getLongitude());
                }
            }
        }, 2000);
    }

    public void getAddress(double latitude, double longitude) {
        if (isViewClose()) { return; }
        LogUtils.d("getAddress()  2222");
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                if (address != null) {
                    String data = address.toString();
                    if (TextUtils.isEmpty(data)) {
                        LogUtils.d("getAddress()  (TextUtils.isEmpty(data)");
//                        updateUserInfo(latitude, longitude, null);
                    } else {
                        int startCity = data.indexOf("admin=") + "admin=".length();
                        int endCity = data.indexOf(",", startCity);
                        int startPlace = data.indexOf("thoroughfare=") + "thoroughfare=".length();
                        int endPlace = data.indexOf(",", startPlace);
                        int startCountry = data.indexOf("countryName=") + "countryName=".length();
                        int endCountry = data.indexOf(",", startCountry);
                        String country = data.substring(startCountry, endCountry);
                        String city = data.substring(startCity, endCity);
                        String place = data.substring(startPlace, endPlace);
//                        updateUserInfo(latitude, longitude, country + "\n" + city + "\n" + place);

                        LogUtils.d("getAddress()  (TextUtils.isEmpty(data)"+latitude, longitude, country + "\n" + city + "\n" + place);
                    }
                } else {
//                    updateUserInfo(latitude, longitude, null);
                }
            } else {
//                updateUserInfo(latitude, longitude, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
//            updateUserInfo(latitude, longitude, null);
        }
    }

    public boolean isViewClose() {
        return (ActivityUtil.isFinishing(this));
    }
}

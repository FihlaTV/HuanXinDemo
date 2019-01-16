package a.cool.huanxin.manager;

import android.text.TextUtils;

import a.cool.huanxin.ben.CurrentUser;
import a.cool.huanxin.constants.AppConstant;
import a.cool.huanxin.utils.GsonConverter;
import a.cool.huanxin.utils.SharedPrefUtils;

public class CurrentUserManager {

    private static CurrentUserManager currentUserManager;

    private CurrentUser mUser;

    public static CurrentUserManager getInstance() {
        if (currentUserManager == null) {
            synchronized (CurrentUserManager.class) {
                if (currentUserManager == null) {
                    currentUserManager = new CurrentUserManager();
                }
            }
        }
        return currentUserManager;
    }


    public boolean hasLogin() {
        return getCurrentUser() != null;
    }

    public CurrentUser getCurrentUser() {
        if (mUser == null) {
            synchronized (this) {
                if (mUser == null) {
                    String json = SharedPrefUtils.getInstance().getString(AppConstant.SharedPreferenceKey.CURRENT_USER);
                    CurrentUser currentUser = !TextUtils.isEmpty(json) ? GsonConverter.fromJson(json, CurrentUser.class) : null;
                    mUser = currentUser;
                }
            }
        }
        return mUser;
    }

    public void updateCurrentUser(CurrentUser currentUser) {
        CurrentUser old = mUser;
        mUser = currentUser;
        if (currentUser != null) {
            SharedPrefUtils.getInstance().putString(AppConstant.SharedPreferenceKey.CURRENT_USER, GsonConverter.toJson(currentUser));
        } else {
            SharedPrefUtils.getInstance().removeValue(AppConstant.SharedPreferenceKey.CURRENT_USER);
        }
    }

    public void saveCurrentUser(CurrentUser currentUser) {
        if (currentUser == null) {return;}
        mUser = currentUser;
        SharedPrefUtils.getInstance().putString(AppConstant.SharedPreferenceKey.CURRENT_USER, GsonConverter.toJson(currentUser));
    }
}

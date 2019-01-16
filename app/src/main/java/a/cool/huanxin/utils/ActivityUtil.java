package a.cool.huanxin.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import a.cool.huanxin.R;
import a.cool.huanxin.activity.ChatActivity;
import a.cool.huanxin.activity.MainActivity;
import a.cool.huanxin.ben.ChatMessage;
import a.cool.huanxin.constants.AppConstant;


public class ActivityUtil {

    public static boolean isFinishing(Activity activity) {
        return (activity == null || activity.isFinishing());
    }


    public static void startActivity(Activity activity, Class targetClass) {
        Intent intent = new Intent(activity, targetClass);
        activity.startActivity(intent);
    }

    public static void startMainActivity(Activity activity) {
        try {
            if (activity == null || activity.isFinishing()) {
                return;
            }
            startActivity(activity, MainActivity.class);
            activity.finish();
        } catch (Exception e) {

        }
    }

    public static void startChatActivity(Fragment fragment, int userId, int requestCode) {
        if (fragment == null) { return; }
        Intent intent = new Intent(fragment.getContext(), ChatActivity.class);
        intent.putExtra(AppConstant.IntentKey.EXTRA_ID, userId);
        fragment.startActivityForResult(intent, requestCode);
        fragment.getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.slide_in_from_middle_to_middle);
    }

    public static void startChatActivity(Fragment fragment, ChatMessage chatMessage, int requestCode) {
        if (fragment == null) { return; }
        Intent intent = new Intent(fragment.getContext(), ChatActivity.class);
        intent.putExtra(AppConstant.IntentKey.EXTRA_DATA, chatMessage);
        fragment.startActivityForResult(intent, requestCode);
        fragment.getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.slide_in_from_middle_to_middle);
    }

    public static void startChatActivity(Activity activity, int userId, int requestCode) {
        if (activity == null) { return; }
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(AppConstant.IntentKey.EXTRA_ID, userId);
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.enter_from_right, R.anim.slide_in_from_middle_to_middle);
    }

}

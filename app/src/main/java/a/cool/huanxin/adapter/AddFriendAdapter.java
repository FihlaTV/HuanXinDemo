package a.cool.huanxin.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import a.cool.huanxin.R;
import a.cool.huanxin.base.BaseRVAdapter;
import a.cool.huanxin.base.IViewHolder;
import a.cool.huanxin.ben.AddFriendBean;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddFriendAdapter extends BaseRVAdapter<AddFriendBean, AddFriendAdapter.AddFriendAdapterHolder> {

    @Override
    protected AddFriendAdapterHolder doCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddFriendAdapterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_friend_adapter, parent, false));
    }

    @Override
    protected void bindItemData(AddFriendAdapterHolder viewHolder, AddFriendBean chatMessage, int position) {
        viewHolder.bindView(chatMessage, position);
    }

    public class AddFriendAdapterHolder extends RecyclerView.ViewHolder implements IViewHolder<AddFriendBean> {

        @BindView(R.id.riv_item_chat_message_adapter)
        ImageView mAvatar;
        @BindView(R.id.tv_name_item_chat_message_adapter)
        TextView mUserName;
        @BindView(R.id.tv_bio_item_chat_message_adapter)
        TextView mBio;
        @BindView(R.id.btn_add_friend_add_friend_adapter)
        Button maddFriendBtn;
        @BindView(R.id.ll_all_riv_item_chat_message_adapter)
        LinearLayout mAllRivItem;

        public AddFriendAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView(final AddFriendBean addFriendBean, int position) {
            mUserName.setText(addFriendBean.getUserName());
            maddFriendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        EMClient.getInstance().contactManager().acceptInvitation((addFriendBean.getUserName()));
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}

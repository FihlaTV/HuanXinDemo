package a.cool.huanxin.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import a.cool.huanxin.R;
import a.cool.huanxin.base.BaseRVAdapter;
import a.cool.huanxin.base.IViewHolder;
import a.cool.huanxin.ben.ChatMessage;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ChatMessageAdapter extends BaseRVAdapter<ChatMessage, ChatMessageAdapter.ChatMessageAdapterHolder> {

    @Override
    protected ChatMessageAdapterHolder doCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatMessageAdapterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_adapter, parent, false));
    }

    @Override
    protected void bindItemData(ChatMessageAdapterHolder viewHolder, ChatMessage chatMessage, int position) {
        viewHolder.bindView(chatMessage, position);
    }

    public class ChatMessageAdapterHolder extends RecyclerView.ViewHolder implements IViewHolder<ChatMessage> {

        @BindView(R.id.riv_item_chat_message_adapter)
        ImageView mAvatar;
        @BindView(R.id.tv_name_item_chat_message_adapter)
        TextView mUserName;
        @BindView(R.id.tv_bio_item_chat_message_adapter)
        TextView mBio;
        @BindView(R.id.tv_time_item_chat_message_adapter)
        TextView mTime;
        @BindView(R.id.ll_all_riv_item_chat_message_adapter)
        LinearLayout mAllRivItem;

        public ChatMessageAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindView(ChatMessage chatMessage, int position) {
            String bio = chatMessage.getUserBio();
            mTime.setText(chatMessage.getMessageTime());
            mUserName.setText(chatMessage.getUserName());
            mBio.setText(chatMessage.getUserBio());
            if (TextUtils.isEmpty(bio)) {
                mBio.setVisibility(View.GONE);
            } else {
                mBio.setVisibility(View.VISIBLE);
            }
        }
    }
}

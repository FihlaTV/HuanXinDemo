package a.cool.huanxin.ben;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatMessage implements Parcelable {

    private int userId;
    private String userAvatar;
    private String userName;
    private String userBio;
    private String messageTime;
    private String phoneNumber;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "userId=" + userId +
                ", userAvatar='" + userAvatar + '\'' +
                ", userName='" + userName + '\'' +
                ", userBio='" + userBio + '\'' +
                ", messageTime='" + messageTime + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.userAvatar);
        dest.writeString(this.userName);
        dest.writeString(this.userBio);
        dest.writeString(this.messageTime);
        dest.writeString(this.phoneNumber);
    }

    public ChatMessage() {}

    protected ChatMessage(Parcel in) {
        this.userId = in.readInt();
        this.userAvatar = in.readString();
        this.userName = in.readString();
        this.userBio = in.readString();
        this.messageTime = in.readString();
        this.phoneNumber = in.readString();
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel source) {return new ChatMessage(source);}

        @Override
        public ChatMessage[] newArray(int size) {return new ChatMessage[size];}
    };
}

package a.cool.huanxin.ben;

public class AddFriendBean {

    private String userName;
    private boolean isFriend;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    @Override
    public String toString() {
        return "AddFriendBean{" +
                "userName='" + userName + '\'' +
                ", isFriend=" + isFriend +
                '}';
    }
}

package a.cool.huanxin.ben;

public class MessageBean {

    private String receiceUserName;
    private String message;
    private long createAt;
    private String sendUserName;
    public boolean isRead;

    public String getReceiceUserName() {
        return receiceUserName;
    }

    public void setReceiceUserName(String receiceUserName) {
        this.receiceUserName = receiceUserName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "receiceUserName='" + receiceUserName + '\'' +
                ", message='" + message + '\'' +
                ", createAt=" + createAt +
                ", sendUserName='" + sendUserName + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
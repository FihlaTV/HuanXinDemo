package a.cool.huanxin.ben;

public class MessageBean {

    private String receiceUserName;
    private String message;
    private long createAt;
    private String sendUserName;
    public boolean isRead;
    private String messageId;

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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "receiceUserName='" + receiceUserName + '\'' +
                ", message='" + message + '\'' +
                ", createAt=" + createAt +
                ", sendUserName='" + sendUserName + '\'' +
                ", isRead=" + isRead +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}
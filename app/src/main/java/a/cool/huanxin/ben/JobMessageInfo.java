package a.cool.huanxin.ben;

import android.os.Parcel;
import android.os.Parcelable;

public class JobMessageInfo implements Parcelable {

    private int userId;
    private String JobName;
    private String userName;
    private String bio;
    private String gender;
    private String farAway;
    private String jobAvatar;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getJobName() {
        return JobName;
    }

    public void setJobName(String jobName) {
        JobName = jobName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFarAway() {
        return farAway;
    }

    public void setFarAway(String farAway) {
        this.farAway = farAway;
    }

    public String getJobAvatar() {
        return jobAvatar;
    }

    public void setJobAvatar(String jobAvatar) {
        this.jobAvatar = jobAvatar;
    }

    @Override
    public String toString() {
        return "JobMessageInfo{" +
                "userId=" + userId +
                ", JobName='" + JobName + '\'' +
                ", userName='" + userName + '\'' +
                ", bio='" + bio + '\'' +
                ", gender='" + gender + '\'' +
                ", farAway='" + farAway + '\'' +
                ", jobAvatar='" + jobAvatar + '\'' +
                '}';
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.JobName);
        dest.writeString(this.userName);
        dest.writeString(this.bio);
        dest.writeString(this.gender);
        dest.writeString(this.farAway);
        dest.writeString(this.jobAvatar);
    }

    public JobMessageInfo() {}

    protected JobMessageInfo(Parcel in) {
        this.userId = in.readInt();
        this.JobName = in.readString();
        this.userName = in.readString();
        this.bio = in.readString();
        this.gender = in.readString();
        this.farAway = in.readString();
        this.jobAvatar = in.readString();
    }

    public static final Creator<JobMessageInfo> CREATOR = new Creator<JobMessageInfo>() {
        @Override
        public JobMessageInfo createFromParcel(Parcel source) {return new JobMessageInfo(source);}

        @Override
        public JobMessageInfo[] newArray(int size) {return new JobMessageInfo[size];}
    };
}

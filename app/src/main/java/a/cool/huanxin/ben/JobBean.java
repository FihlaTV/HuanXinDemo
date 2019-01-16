package a.cool.huanxin.ben;

public class JobBean {

    private String jobAvatar;
    private String jobName;

    public String getJobAvatar() {
        return jobAvatar;
    }

    public void setJobAvatar(String jobAvatar) {
        this.jobAvatar = jobAvatar;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public String toString() {
        return "JobBean{" +
                "jobAvatar='" + jobAvatar + '\'' +
                ", jobName='" + jobName + '\'' +
                '}';
    }
}

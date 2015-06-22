package testme.com.myapplication.pojo;

/**
 * Created by Exam on 4/27/2015.
 */
public class Sender {
    private int mId;
    private String mFirstName;
    private String mLastName;
    private String mAvatar;

    public boolean isUser() {
        return mIsUser;
    }

    public void setUser(boolean isUser) {
        mIsUser = isUser;
    }

    private boolean mIsUser;


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }
}

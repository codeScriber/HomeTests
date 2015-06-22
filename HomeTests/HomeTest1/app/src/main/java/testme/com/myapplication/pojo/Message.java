package testme.com.myapplication.pojo;

/**
 * Created by Exam on 4/27/2015.
 */
public class Message {
    private long mId;
    private long mRecievedTime;
    private Sender mMessageSender;
    private Content mMessageContnet;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getRecievedTime() {
        return mRecievedTime;
    }

    public void setRecievedTime(long recievedTime) {
        mRecievedTime = recievedTime;
    }

    public Sender getMessageSender() {
        return mMessageSender;
    }

    public void setMessageSender(Sender messageSender) {
        mMessageSender = messageSender;
    }

    public Content getMessageContnet() {
        return mMessageContnet;
    }

    public void setMessageContnet(Content messageContnet) {
        mMessageContnet = messageContnet;
    }
}

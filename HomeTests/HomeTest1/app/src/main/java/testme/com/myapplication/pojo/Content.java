package testme.com.myapplication.pojo;

/**
 * Created by Exam on 4/27/2015.
 */
public class Content {
    public enum ContentType {
        TEXT, IMAGE, LOCATION
    }

    public ContentType getType() {
        return mType;
    }

    public void setType(ContentType mType) {
        this.mType = mType;
    }

    private ContentType mType;

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    private String mContent;
}

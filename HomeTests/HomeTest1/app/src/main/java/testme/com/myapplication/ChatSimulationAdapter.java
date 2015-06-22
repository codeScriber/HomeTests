package testme.com.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import testme.com.myapplication.pojo.Content;
import testme.com.myapplication.pojo.Message;
import testme.com.myapplication.ui.ChatViewGenerator;

/**
 * Created by Exam on 4/27/2015.
 */
public class ChatSimulationAdapter extends BaseAdapter {

    private Context mContext;
    private List<Message> mMessages;
    private ImageLoader mImageLoader;
    private StringBuilder mStringBuilder;
    private DateFormat mDateFormat;
    private ChatViewGenerator mChatviewGen;

    public ChatSimulationAdapter(Context context, ImageLoader imageLoader){
        this.mContext = context;
        this.mImageLoader = imageLoader;
        this.mStringBuilder = new StringBuilder();
        mDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z");
        mChatviewGen = new ChatViewGenerator((LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE ));
    }


    public void setMessages(List<Message> messages){
        mMessages = messages;
    }

    @Override
    public int getCount() {
        return mMessages == null ? 0 : mMessages.size();
    }

    @Override
    public Message getItem(int position) {
        return mMessages == null ? null : mMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMessages == null ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = mMessages.get(position);
        RelativeLayout mainView = (RelativeLayout)convertView;
        if(mainView == null ){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            mainView = (RelativeLayout)inflater.inflate(R.layout.chat_item, parent, false);
            ViewHolder viewholder = new ViewHolder();
            viewholder.mAvarImage = (NetworkImageView)mainView.findViewById(R.id.avatar_image);
            viewholder.mHeaderText = (TextView)mainView.findViewById(R.id.header_text);
            viewholder.mContentContainer = (ViewGroup)mainView.findViewById(R.id.header_and_data_container);
            viewholder.mContentType = null;
            mainView.setTag(viewholder);
        }
        ViewHolder viewHolder = (ViewHolder)mainView.getTag();
        adjustChatItem(mainView, message.getMessageSender().isUser());
        viewHolder.mAvarImage.setImageUrl(message.getMessageSender().getAvatar(), mImageLoader);
        viewHolder.mHeaderText.setText(getFormattedHeaderText(message));
        if( viewHolder.mContentType == null || viewHolder.mContentType != message.getMessageContnet().getType()){
            mChatviewGen.generattChatViewforType(viewHolder.mContentContainer, message, mImageLoader);
            viewHolder.mContentType = message.getMessageContnet().getType();
        }else{
            View contentView = viewHolder.mContentContainer.getChildAt(0);
            if( contentView != null ) {
                mChatviewGen.setViewData(parent, message, mImageLoader);
            }
        }
        return mainView;
    }

    private String getFormattedHeaderText(Message message){
        mStringBuilder.setLength(0);
        mStringBuilder.append(message.getMessageSender().getFirstName());
        try {
            Date messageDate = mDateFormat.parse(""+message.getRecievedTime());
            mStringBuilder.append("\n").append(mDateFormat.format(messageDate));
        } catch ( ParseException ex ) {
            ex.getStackTrace();
        }
        return mStringBuilder.toString();
    }

    private void adjustChatItem(RelativeLayout chatItemLayout, boolean isUserChatWindow){
        LayoutViewHolder lvh = (LayoutViewHolder)chatItemLayout.getTag();
        if( lvh == null ){
            lvh = new LayoutViewHolder();
            lvh.avatarView = chatItemLayout.findViewById(R.id.avatar_image);
            lvh.containerView = chatItemLayout.findViewById(R.id.header_and_data_container);
            lvh.isUserLayout = true;
        }
        //only if it's not the same change it.
        if( lvh.isUserLayout != isUserChatWindow ){
            lvh.isUserLayout = isUserChatWindow;
            RelativeLayout.LayoutParams oldLayout = (RelativeLayout.LayoutParams)
                    lvh.avatarView.getLayoutParams();
            RelativeLayout.LayoutParams newParams = new
                    RelativeLayout.LayoutParams(oldLayout.height, oldLayout.width);
            newParams.addRule(isUserChatWindow ? RelativeLayout.ALIGN_PARENT_RIGHT :
                    RelativeLayout.ALIGN_PARENT_LEFT);
            newParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lvh.avatarView.setLayoutParams(newParams);

            oldLayout = (RelativeLayout.LayoutParams)
                    lvh.containerView.getLayoutParams();
            newParams = new
                    RelativeLayout.LayoutParams(oldLayout.height, oldLayout.width);
            newParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            int rule = isUserChatWindow ? RelativeLayout.LEFT_OF :
                    RelativeLayout.RIGHT_OF;
            newParams.addRule(rule, lvh.avatarView.getId());
            lvh.containerView.setLayoutParams(newParams);
        }
    }


    private static class ViewHolder{
        public NetworkImageView mAvarImage;
        public TextView mHeaderText;
        public ViewGroup mContentContainer;
        public Content.ContentType mContentType;
    }

    private static class LayoutViewHolder {
        public View avatarView;
        public View containerView;
        public boolean isUserLayout;
    }
}

package com.codepath.apps.tweettrove.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweettrove.R;
import com.codepath.apps.tweettrove.models.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Hari on 8/14/2016.
 */
public class MessagesAdapter
        extends ArrayAdapter<Message>
{


    public MessagesAdapter(Context context, List<Message> objects) {
        super(context, R.layout.item_message, objects);
    }

    public class ViewHolder
    {
        @BindView(R.id.ivMsgProfileImage)
        public ImageView ivMsgProfileImage;

        @BindView(R.id.tvText)
        public TextView tvText;

        @BindView(R.id.tvTime)
        public TextView tvTime;

        @BindView(R.id.tvUserName)
        public TextView tvUserName;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        Message message = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvText.setText(message.text);
        viewHolder.tvUserName.setText(message.senderScreenName);


        Picasso.with(getContext())
                .load(message.profileImageUrlHttps)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(viewHolder.ivMsgProfileImage);

        return convertView;

    }


}

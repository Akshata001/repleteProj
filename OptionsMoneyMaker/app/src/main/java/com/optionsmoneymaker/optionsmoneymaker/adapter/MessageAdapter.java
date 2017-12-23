package com.optionsmoneymaker.optionsmoneymaker.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.optionsmoneymaker.optionsmoneymaker.MessageDetailActivity;
import com.optionsmoneymaker.optionsmoneymaker.R;
import com.optionsmoneymaker.optionsmoneymaker.fragment.MessageActionDialogFragment;
import com.optionsmoneymaker.optionsmoneymaker.model.MessageData;
import com.optionsmoneymaker.optionsmoneymaker.utils.Constants;
import com.optionsmoneymaker.optionsmoneymaker.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Sagar on 10/13/2016.
 */
public class MessageAdapter extends ArrayAdapter<MessageData> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MessageData> msgList;

    public MessageAdapter(Context context, ArrayList<MessageData> list) {
        super(context, R.layout.row_message_item, list);
        this.context = context;
        inflater = LayoutInflater.from(context);
        msgList = new ArrayList<>();
        msgList = list;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.row_message_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        try {
            SimpleDateFormat serverDateFormat = new SimpleDateFormat(Constants.SERVER_DATETIME_FORMAT, Locale.US);
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT, Locale.US);
            //Converting the String back to java.util.Date
            Date date = serverDateFormat.parse(getItem(position).getDateTime());

            holder.txtDate.setText(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtProName.setText(getItem(position).getProductName());
        holder.txtMessage.setText(Html.fromHtml(getItem(position).getTitle()));

        if (getItem(position).getIsRead().equalsIgnoreCase("1")) {
            //convertView.setBackground(context.getResources().getDrawable(R.drawable.btn_dark_grey));
            //convertView.setPadding(10, 10, 10, 10);
            holder.txtProName.setTextColor(context.getResources().getColor(R.color.app_date_color));
            holder.txtDate.setTextColor(context.getResources().getColor(R.color.app_date_color));
            holder.txtMessage.setTextColor(context.getResources().getColor(R.color.app_date_color));
            holder.txtProName.setTypeface(Typeface.DEFAULT);
            holder.txtDate.setTypeface(Typeface.DEFAULT);
            holder.txtMessage.setTypeface(Typeface.DEFAULT);
        } else {
            //convertView.setBackground(context.getResources().getDrawable(R.drawable.btn_light_grey));
            //convertView.setPadding(10, 10, 10, 10);
            holder.txtMessage.setTextColor(context.getResources().getColor(R.color.black_font));
            holder.txtProName.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtDate.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtMessage.setTypeface(Typeface.DEFAULT_BOLD);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getItem(position).setIsRead("1");
                    msgList.get(position).setIsRead("1");
                    // create a new Gson instance
                    Gson gson = new Gson();
                    // convert your list to json
                    String jsonMsgList = gson.toJson(msgList);
                    JSONArray jsonArray = new JSONArray(jsonMsgList);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("status", 1);
                    jsonObject.put("data", jsonArray);

                    SessionManager session = new SessionManager(context);
                    session.setLatestMessage(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(context, MessageDetailActivity.class);
                intent.putExtra(Constants.TYPE, "list");
                intent.putExtra(Constants.ID, getItem(position).getId());
                intent.putExtra(Constants.MESSAGE, getItem(position).getMessage());
                intent.putExtra(Constants.PRODUCT, getItem(position).getProductName());
                intent.putExtra(Constants.DATE, getItem(position).getDateTime());
                intent.putExtra(Constants.TITLE, getItem(position).getTitle());
                context.startActivity(intent);
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(MessageAdapter.class.getSimpleName(), "onLongClick: " + position);
                MessageActionDialogFragment newFragment =
                        MessageActionDialogFragment.newInstance(msgList.get(position).getId(),
                                msgList.get(position).getIsRead());

                FragmentManager manager = ((Activity) context).getFragmentManager();
                newFragment.show(manager, "MessageAction");
                return true;
            }
        });
        return convertView;
    }

    public class ViewHolder {
        @InjectView(R.id.txt_product_name)
        TextView txtProName;
        @InjectView(R.id.txt_message)
        TextView txtMessage;
        @InjectView(R.id.txt_date)
        TextView txtDate;

        ViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }
}

package com.optionsmoneymaker.optionsmoneymaker.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.optionsmoneymaker.optionsmoneymaker.R;
import com.optionsmoneymaker.optionsmoneymaker.adapter.MessageAdapter;
import com.optionsmoneymaker.optionsmoneymaker.model.MessageData;
import com.optionsmoneymaker.optionsmoneymaker.model.MessageEvent;
import com.optionsmoneymaker.optionsmoneymaker.model.NotificationResult;
import com.optionsmoneymaker.optionsmoneymaker.pulltorefresh.PullToRefreshBase;
import com.optionsmoneymaker.optionsmoneymaker.pulltorefresh.PullToRefreshView;
import com.optionsmoneymaker.optionsmoneymaker.rest.RestClient;
import com.optionsmoneymaker.optionsmoneymaker.utils.Constants;
import com.optionsmoneymaker.optionsmoneymaker.utils.SessionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by Sagar on 10/1/2016.
 */
public class HomeFragment extends BaseFragment {

    static PullToRefreshView mPullRefreshListView;
    static ListView listMessage;
    public static boolean active = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, rootView);

        mPullRefreshListView = (PullToRefreshView) rootView.findViewById(R.id.list_message);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listMessage = mPullRefreshListView.getRefreshableView();
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();

                mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void reload() {
        if (cd.isConnectingToInternet()) {
            hideKeyboard();
            showProgressbar("Latest Message");
            RestClient.getMoneyMaker().lastMessage(session.getUserID(), new Callback<Response>() {
                @Override
                public void success(Response result, Response response) {
                    dismiss();
                    try {
                        JSONObject main = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                        if ((int) main.getInt("status") == 1) {
                            session.setFirstTime(false);
                            session.setLatestMessage(main.toString());

                            ArrayList<MessageData> list = new ArrayList<MessageData>();
                            list = session.getLatestMessage().getData();
                            MessageAdapter messageAdapter = new MessageAdapter(getActivity(), list);
                            listMessage.setAdapter(messageAdapter);
                        } else if ((int) main.getInt("status") == 0) {
                            toast("No Data Found");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Home", "API failure " + error);
                    dismiss();
                }
            });
        } else {
            toast(getResources().getString(R.string.no_internet));
        }

        mPullRefreshListView.onRefreshComplete();
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
        EventBus.getDefault().unregister(this);
    }

    @Subscribe()
    public void onMessageEvent(MessageEvent event) {
        /* Do something */
        if (event.getEvent().equals(Constants.REFRESH_MESSAGES)) {
            reload();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (session.getFirstTime()) {
            reload();
        } else {
            ArrayList<MessageData> list = new ArrayList<MessageData>();
            list = session.getLatestMessage().getData();
            MessageAdapter messageAdapter = new MessageAdapter(getActivity(), list);
            listMessage.setAdapter(messageAdapter);

            mPullRefreshListView.onRefreshComplete();
        }
    }

    public static void refreshView(Context context) {
        ArrayList<MessageData> list = new ArrayList<MessageData>();
        SessionManager session = new SessionManager(context);
        list = session.getLatestMessage().getData();
        // TODO: 12/15/2017 replace context by activity context
        MessageAdapter messageAdapter = new MessageAdapter(context, list);
        listMessage.setAdapter(messageAdapter);
        mPullRefreshListView.onRefreshComplete();
    }


}

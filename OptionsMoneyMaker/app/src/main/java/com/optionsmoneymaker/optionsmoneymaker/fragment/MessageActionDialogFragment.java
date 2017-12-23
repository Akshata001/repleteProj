package com.optionsmoneymaker.optionsmoneymaker.fragment;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.optionsmoneymaker.optionsmoneymaker.R;
import com.optionsmoneymaker.optionsmoneymaker.model.MessageData;
import com.optionsmoneymaker.optionsmoneymaker.model.MessageEvent;
import com.optionsmoneymaker.optionsmoneymaker.model.NotificationResult;
import com.optionsmoneymaker.optionsmoneymaker.rest.RestClient;
import com.optionsmoneymaker.optionsmoneymaker.utils.ConnectionDetector;
import com.optionsmoneymaker.optionsmoneymaker.utils.Constants;
import com.optionsmoneymaker.optionsmoneymaker.utils.SessionManager;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Akshata on 12/9/2017.
 */

public class MessageActionDialogFragment extends DialogFragment {


    protected ConnectionDetector cd;
    private String isRead;
    private String messageId;
    private SessionManager session;
    private boolean isCallInProgress = false;

    public static MessageActionDialogFragment newInstance(String messageId, String isMsgRead) {
        MessageActionDialogFragment f = new MessageActionDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("MESSAGE_ID", messageId);
        args.putString("IS_MSG_READ", isMsgRead);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cd = new ConnectionDetector(getActivity());
        isRead = getArguments().getString("IS_MSG_READ");
        messageId = getArguments().getString("MESSAGE_ID");

        session = new SessionManager(getActivity());

        hideKeyboard();
    }


    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View dialog = inflater.inflate(R.layout.dialog_msg_action, container, false);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        TextView textviewMessageReadUnread = (TextView) dialog.findViewById(R.id.textviewMessageReadUnread);
        String callForMessage = "";

        if (isRead.equals("1")) {
            callForMessage = "mark_message_unread";
            textviewMessageReadUnread.setText("Mark as Unread");
        } else {
            callForMessage = "message_read";
            textviewMessageReadUnread.setText("Mark as Read");
        }

        TextView textviewMessageDelete = (TextView) dialog.findViewById(R.id.textviewMessageDelete);

        final String finalCallForMessage = callForMessage;
        textviewMessageReadUnread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call to mark message as read
                if (!isCallInProgress) {
                    isCallInProgress = true;
                    callToMarkMessageAsReadUnread(messageId, finalCallForMessage);
                }
            }
        });

        textviewMessageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call to mark message as read
                if (!isCallInProgress) {
                    isCallInProgress = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to delete this message?")
                            .setPositiveButton(R.string.ok, new
                                    DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dismiss();
                                            callToDeleteMessage(messageId);
                                        }
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dismiss();
                        }
                    }).setTitle("Message");
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();
                }
            }
        });

        Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return dialog;
    }

    private void callToDeleteMessage(String messageId) {
        if (cd.isConnectingToInternet()) {
            hideKeyboard();
            try {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                String strTime = dateFormatter.format(new Date());

                RestClient.getMoneyMaker().messageDelete(messageId, session.getUserID(),
                        strTime, new Callback<NotificationResult>() {
                            @Override
                            public void success(NotificationResult result, Response response) {
//                                toast(getResources().getString(R.string.no_internet));
                                Log.d(HomeFragment.class.getSimpleName(), "success: " + response.toString());
                                refreshScreen();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e("Message Detail", "API failure " + error);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            toast(getResources().getString(R.string.no_internet));
        }
        isCallInProgress = false;
    }

    private void callToMarkMessageAsReadUnread(String messageId, String callForMessage) {
        if (cd.isConnectingToInternet()) {
            hideKeyboard();
            try {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                String strTime = dateFormatter.format(new Date());

                if (callForMessage.equals("message_read")) {
                    RestClient.getMoneyMaker().messageRead(messageId, session.getUserID(),
                            strTime, new Callback<NotificationResult>() {
                                @Override
                                public void success(NotificationResult result, Response response) {
//                                toast(getResources().getString(R.string.no_internet));
                                    Log.d(HomeFragment.class.getSimpleName(), "success: " + response.toString());
                                    refreshScreen();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.e("Message Detail", "API failure " + error);
                                }
                            });
                } else if (callForMessage.equals("mark_message_unread")) {
                    RestClient.getMoneyMaker().messageUnRead(messageId, session.getUserID(),
                            strTime, new Callback<NotificationResult>() {
                                @Override
                                public void success(NotificationResult result, Response response) {
//                                toast(getResources().getString(R.string.no_internet));
                                    Log.d(HomeFragment.class.getSimpleName(), "success: " + response.toString());
                                    refreshScreen();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.e("Message Detail", "API failure " + error);
                                }
                            });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            toast(getResources().getString(R.string.no_internet));
        }

        isCallInProgress = false;
    }

    private void refreshScreen() {
        dismiss();
        EventBus.getDefault().post(new MessageEvent(Constants.REFRESH_MESSAGES));
    }


    public void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


}
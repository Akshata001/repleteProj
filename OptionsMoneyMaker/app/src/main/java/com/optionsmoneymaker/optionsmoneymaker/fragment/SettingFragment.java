package com.optionsmoneymaker.optionsmoneymaker.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OSNotificationAction;
import com.optionsmoneymaker.optionsmoneymaker.MessageDetailActivity;
import com.optionsmoneymaker.optionsmoneymaker.R;
import com.optionsmoneymaker.optionsmoneymaker.model.NotificationResult;
import com.optionsmoneymaker.optionsmoneymaker.rest.RestClient;
import com.optionsmoneymaker.optionsmoneymaker.utils.Constants;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Sagar on 10/1/2016.
 */
public class SettingFragment extends BaseFragment{

    @InjectView(R.id.tv_font_text)
    TextView tvFontText;
    @InjectView(R.id.tv_notification_text)
    TextView tvNotiText;

    private Dialog dialog;
    private int fontIndex;

    private ArrayList<String> notiArray = new ArrayList<>();

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.inject(this, rootView);

        notiArray.add("Instant(Push)");
        notiArray.add("Never");
        notiArray.add("Every 1 minute");
        notiArray.add("Every 5 minutes");
        notiArray.add("Every 10 minutes");
        notiArray.add("Every 20 minutes");
        notiArray.add("Every 30 minutes");
        notiArray.add("Every 1 hour");

        fontIndex = (session.getFontSize()-6)/2;
        tvFontText.setText((fontIndex*10 + 50) + "%");
        tvNotiText.setText(notiArray.get(session.getNotiIndex()));

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @OnClick(R.id.rl_notification)
    public void setPolling(){
        dialog = onCreateDialogSingleChoice();
        dialog.show();
    }

    @OnClick(R.id.rl_font)
    public void setFont(){
        showFontDialog();
    }

    private void showFontDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_font_size);
        dialog.setCancelable(false);

        final TextView tvFontSize = (TextView) dialog.findViewById(R.id.tv_font_size);
        final SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.seekBar1);

        final int[] tmpIndex = new int[1];
        tvFontSize.setText((fontIndex * 10 + 50) + "%");
        seekBar.setProgress(fontIndex);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tmpIndex[0] = i;
                tvFontSize.setText((i*10 + 50) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setFontSize(((2*tmpIndex[0])+6));
                fontIndex = (session.getFontSize()-6)/2;
                tvFontText.setText(tvFontSize.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public Dialog onCreateDialogSingleChoice() {
        final CharSequence[] ar = new CharSequence[notiArray.size()];

        for (int ia = 0; ia < notiArray.size(); ia++) {
            ar[ia] = notiArray.get(ia);
        }

        //Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        int pos = session.getNotiIndex();

        builder.setTitle("Sync Schedule").setSingleChoiceItems(ar, pos, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int id) {
                        if (cd.isConnectingToInternet()) {
                            showProgressbar("Sync Schedule");
                            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                            String strTime = dateFormatter.format(new Date());
                            ListView lw = ((AlertDialog) dialog).getListView();
                            final int p = lw.getCheckedItemPosition();

                            String strID = "" + (p+1);

                            RestClient.getMoneyMaker().syncTime("set_sync_time", strTime, session.getUserID(),
                                    strID, new Callback<NotificationResult>() {
                                        @Override
                                        public void success(NotificationResult result, Response response) {
                                            if ((int) result.getStatus() == 1) {
                                                session.setNotiIndex(p);
                                                tvNotiText.setText(notiArray.get(p));
                                            } else if ((int) result.getStatus() == 0) {
                                                toast("Something went wrong please try again later");
                                            }
                                            dismiss();
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Log.e("Setting", "API failure " + error);
                                            dismiss();
                                        }
                                    });

                        }else {
                            toast(getResources().getString(R.string.no_internet));
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
}

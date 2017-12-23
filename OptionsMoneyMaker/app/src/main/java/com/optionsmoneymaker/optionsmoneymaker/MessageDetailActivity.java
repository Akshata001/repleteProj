package com.optionsmoneymaker.optionsmoneymaker;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.optionsmoneymaker.optionsmoneymaker.model.MessageDetail;
import com.optionsmoneymaker.optionsmoneymaker.model.NotificationResult;
import com.optionsmoneymaker.optionsmoneymaker.rest.RestClient;
import com.optionsmoneymaker.optionsmoneymaker.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Sagar on 10/13/2016.
 */
public class MessageDetailActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.webview)
    WebView webView;
    @InjectView(R.id.txt_product_name)
    TextView txtProName;
    @InjectView(R.id.txt_date)
    TextView txtDate;
    @InjectView(R.id.txt_message)
    TextView txtTitle;

    private final String mimeType = "text/html";
    private final String encoding = "UTF-8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, session.getFontSize());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_layout);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            ((TextView) actionBar.getCustomView().findViewById(R.id.txt_action_title)).setText("HOME");
        }

        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        nMgr.cancelAll();

        if (getIntent().getStringExtra(Constants.TYPE).equalsIgnoreCase("list")) {
            webView.loadDataWithBaseURL("", getIntent().getStringExtra(Constants.MESSAGE), mimeType, encoding, "");
            txtProName.setText(getIntent().getStringExtra(Constants.PRODUCT));
            txtTitle.setText(getIntent().getStringExtra(Constants.TITLE));

            try {
                SimpleDateFormat serverDateFormat = new SimpleDateFormat(Constants.SERVER_DATETIME_FORMAT, Locale.US);
                SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT, Locale.US);
                //Converting the String back to java.util.Date
                Date date = serverDateFormat.parse(getIntent().getStringExtra(Constants.DATE));

                txtDate.setText(dateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (cd.isConnectingToInternet()) {
                hideKeyboard();
                try {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                    String strTime = dateFormatter.format(new Date());

                    RestClient.getMoneyMaker().messageRead(getIntent().getStringExtra(Constants.ID), session.getUserID(),
                            strTime, new Callback<NotificationResult>() {
                                @Override
                                public void success(NotificationResult result, Response response) {
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.e("Message Detail", "API failure " + error);
                                }
                            });
                } catch (Exception e) {

                }
            } else {
                //toast(getResources().getString(R.string.no_internet));
            }
        } else if (getIntent().getStringExtra(Constants.TYPE).equalsIgnoreCase("notification")) {
            if (cd.isConnectingToInternet()) {
                hideKeyboard();
                messageDetail(getIntent().getStringExtra(Constants.ID));
            } else {
                toast(getResources().getString(R.string.no_internet));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void messageDetail(String id) {
        showProgressbar("Message Detail");

        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
            String strTime = dateFormatter.format(new Date());

            RestClient.getMoneyMaker().messageDetail(id, strTime, session.getUserID(), new Callback<MessageDetail>() {
                @Override
                public void success(MessageDetail result, Response response) {

                    if ((int) result.getStatus() == 1) {
                        webView.loadDataWithBaseURL("", result.getMessage(), mimeType, encoding, "");
//                        txtProName.setText(result.getProductName());
                        txtProName.setText("");
                        txtTitle.setText(result.getTitle());

                        try {
                            SimpleDateFormat serverDateFormat = new SimpleDateFormat(Constants.SERVER_DATETIME_FORMAT, Locale.US);
                            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT, Locale.US);
                            //Converting the String back to java.util.Date
                            Date date = serverDateFormat.parse(result.getDateTime());

                            txtDate.setText(dateFormat.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else if ((int) result.getStatus() == 0) {
                        toast("No Data Found");
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Message Detail", "API failure " + error);
                }
            });
        } catch (Exception e) {

        } finally {
            dismiss();
        }
    }
}

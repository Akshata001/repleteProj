package com.optionsmoneymaker.optionsmoneymaker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.optionsmoneymaker.optionsmoneymaker.model.SuccessResult;
import com.optionsmoneymaker.optionsmoneymaker.rest.RestClient;
import com.optionsmoneymaker.optionsmoneymaker.utils.ConnectionDetector;
import com.optionsmoneymaker.optionsmoneymaker.utils.SessionManager;

import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Sagar on 03-10-2016.
 */
public class BaseActivity extends AppCompatActivity {

    protected SessionManager session;
    protected ConnectionDetector cd;
    protected OptionMoneyMaker mCustomApplication;

    @Override
    protected void onResume() {
        super.onResume();
        mCustomApplication.setCurrentActivity(this);
    }

    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences() {
        Activity currActivity = mCustomApplication.getCurrentActivity();
        if (this.equals(currActivity))
            mCustomApplication.setCurrentActivity(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(this);
        cd = new ConnectionDetector(this);
        mCustomApplication = (OptionMoneyMaker) this.getApplicationContext();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private ProgressDialog pDialog;

    public void showProgressbar(String message) {
        pDialog = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pDialog.setTitle("Option Money Maker");
        pDialog.setMessage(Html.fromHtml("<b>Connecting-To-Server</b> for " + message));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void dismiss() {
        if (pDialog != null) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    protected String getText(EditText editText) {
        return editText == null ? "" : editText.getText().toString().trim();
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void toast(int resId) {
        toast(this.getResources().getText(resId).toString());
    }

    protected void startActivity(Class klass) {
        startActivity(new Intent(this, klass));
    }

    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void logout() {
        final ProgressDialog pDialog = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pDialog.setTitle("Option Money Maker");
        pDialog.setMessage(Html.fromHtml("<b>Connecting-To-Server</b> for logout."));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        try {
            RestClient.getMoneyMaker().logout(session.getUserID(), session.getRegisterID(), new Callback<SuccessResult>() {
                @Override
                public void success(SuccessResult result, Response response) {
                    if ((int) result.getStatus() == 1) {
                        session.logoutUser();
                        toast(result.getData());
                    } else if ((int) result.getStatus() == 0) {
                        toast(result.getData());
                    }
                    pDialog.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    //session.logoutUser();
                    pDialog.dismiss();
                }
            });
        } catch (Exception e) {
            //session.logoutUser();
            pDialog.dismiss();
        }
    }
}

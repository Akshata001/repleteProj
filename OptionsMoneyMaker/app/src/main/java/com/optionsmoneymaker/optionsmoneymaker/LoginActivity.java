package com.optionsmoneymaker.optionsmoneymaker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.onesignal.OneSignal;
import com.optionsmoneymaker.optionsmoneymaker.model.SuccessResult;
import com.optionsmoneymaker.optionsmoneymaker.rest.RestClient;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Sagar on 03-10-2016.
 */
public class LoginActivity extends BaseActivity{

  @InjectView(R.id.ll_progressbar)
  LinearLayout llProgressBar;
  @InjectView(R.id.etxt_username)
  EditText eTxtUserName;
  @InjectView(R.id.etxt_password)
  EditText eTxtPassword;
  @InjectView(R.id.btn_login)
  Button btnLogin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (cd.isConnectingToInternet()) {
          if (setValidation()){
            hideKeyboard();
            login_omm();
          }
        }else {
          hideKeyboard();
          Snackbar.make(v, getResources().getString(R.string.no_internet), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }
      }
    });
  }

  @OnClick(R.id.txt_forgot_password)
  public void forgotPassword(){
    Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.optionsmoneymaker.com/forgot-password/"));
    startActivity(viewIntent);
  }

  /**
   * Set validation to check username and password
   * @return
   */
  private boolean setValidation(){

    if (eTxtUserName.getText().toString().trim().length() == 0) {
      eTxtUserName.setError("Enter Email Address.");
      return false;
    }else {
      if (!Patterns.EMAIL_ADDRESS.matcher(eTxtUserName.getText().toString().trim()).matches()) {
        eTxtUserName.setError("Enter valid Email Address.");
        return false;
      }
    }

    if (eTxtPassword.getText().toString().length() == 0){
      eTxtPassword.setError("Enter password");
      return false;
    }

    return true;
  }

  private void login_omm(){
    showProgressbar("Login");

    try {
      OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
        @Override
        public void idsAvailable(String userId, String registrationId) {
          Log.e("debug", "User:" + userId);
          if (registrationId != null)
            Log.e("debug", "registrationId:" + registrationId);

          session.setRegisterID(userId);
        }
      });

      RestClient.getMoneyMaker().login("api_login",eTxtUserName.getText().toString().trim(),eTxtPassword.getText().toString(),session.getRegisterID(), new Callback<SuccessResult>() {
                @Override
                public void success(SuccessResult result, Response response) {
                  if ((int) result.getStatus() == 1) {
                    toast(result.getMessage());
                    session.createLoginSession(result.getEmail(), result.getUserId(), "", result.getFirstName(), result.getLastName(), result.getLevelName(), "");
                    startActivity(MainActivity.class);
                    finish();
                  } else if ((int) result.getStatus() == 0) {
                    toast(result.getData());
                  }
                  dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                  Log.e("Login", "API failure " + error);
                  dismiss();
                }
              });
    }catch (Exception e){
      dismiss();
    }
  }
}

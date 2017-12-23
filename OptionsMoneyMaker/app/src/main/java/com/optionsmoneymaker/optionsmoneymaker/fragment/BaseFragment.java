package com.optionsmoneymaker.optionsmoneymaker.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.optionsmoneymaker.optionsmoneymaker.R;
import com.optionsmoneymaker.optionsmoneymaker.rest.RestClient;
import com.optionsmoneymaker.optionsmoneymaker.utils.ConnectionDetector;
import com.optionsmoneymaker.optionsmoneymaker.utils.SessionManager;

import java.lang.reflect.Method;

/**
 * Created by Sagar on 07-10-2016.
 */
public class BaseFragment extends Fragment{

    protected SessionManager session;
    protected ConnectionDetector cd;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getActivity());
        cd = new ConnectionDetector(getActivity());

        hideKeyboard();
    }

    public void showProgressbar(String message){
        pDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pDialog.setTitle("Option Money Maker");
        pDialog.setMessage(Html.fromHtml("<b>Connecting-To-Server</b> for " + message));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void dismiss(){
        if (pDialog != null) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    protected String getText(EditText editText){
        return editText == null ? "" : editText.getText().toString().trim();
    }

    public void toast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    protected void toast(int resId) {
        toast(getActivity().getResources().getText(resId).toString());
    }

    protected void startActivity(Class klass) {
        startActivity(new Intent(getActivity(), klass));
    }

    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void logout() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pDialog.setTitle("SuperSales");
        pDialog.setMessage(Html.fromHtml("<b>Connecting-To-Server</b>"));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        session.logoutUser();
        pDialog.dismiss();
    }

    protected void setAppFont(ViewGroup mContainer, Typeface mFont) {
        if (mContainer == null || mFont == null) return;

        final int mCount = mContainer.getChildCount();

        // Loop through all of the children.
        for (int i = 0; i < mCount; ++i) {
            final View mChild = mContainer.getChildAt(i);
            if (mChild instanceof TextView) {
                // Set the font if it is a TextView.
                ((TextView) mChild).setTypeface(mFont);
            } else if (mChild instanceof ViewGroup) {
                // Recursively attempt another ViewGroup.
                setAppFont((ViewGroup) mChild, mFont);
            } else {
                try {
                    Method mSetTypeface = mChild.getClass().getMethod("setTypeface", Typeface.class);
                    mSetTypeface.invoke(mChild, mFont);
                } catch (Exception e) {

                }
            }
        }
    }
}

package com.optionsmoneymaker.optionsmoneymaker.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.optionsmoneymaker.optionsmoneymaker.R;
import com.optionsmoneymaker.optionsmoneymaker.model.About;
import com.optionsmoneymaker.optionsmoneymaker.model.ContactUS;
import com.optionsmoneymaker.optionsmoneymaker.rest.RestClient;
import com.optionsmoneymaker.optionsmoneymaker.utils.UIUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Sagar on 10/3/2016.
 */
public class HelpFragment extends BaseFragment {

    @InjectView(R.id.tv_web)
    TextView tvWeb;
    @InjectView(R.id.btn_email)
    Button btnEmail;

    private String strEmail;

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        ButterKnife.inject(this, rootView);

        if (cd.isConnectingToInternet()) {
            hideKeyboard();
            contactUS();
        } else {
            Snackbar.make(container, getResources().getString(R.string.no_internet), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    hideKeyboard();
                    UIUtils.sendMail(getActivity(), "Need Help?", "", strEmail);
                } else {
                    Snackbar.make(v, getResources().getString(R.string.no_internet), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        });

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

    private void contactUS(){
        showProgressbar("Contact US");
        RestClient.getMoneyMaker().contactUS("contact_us", new Callback<ContactUS>() {
            @Override
            public void success(ContactUS result, Response response) {
                dismiss();
                if ((int) result.getStatus() == 1) {
                    tvWeb.setText(Html.fromHtml("<u>" + result.getData().getWebsite() + "</u>"));
                    strEmail = result.getData().getEmail();
                } else if ((int) result.getStatus() == 0) {
                    toast("No Data Found");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Contact US", "API failure " + error);
                dismiss();
            }
        });
    }
}

package com.optionsmoneymaker.optionsmoneymaker.fragment;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.optionsmoneymaker.optionsmoneymaker.MainActivity;
import com.optionsmoneymaker.optionsmoneymaker.R;
import com.optionsmoneymaker.optionsmoneymaker.model.About;
import com.optionsmoneymaker.optionsmoneymaker.model.SuccessResult;
import com.optionsmoneymaker.optionsmoneymaker.rest.RestClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Sagar on 10/1/2016.
 */
public class AboutFragment extends BaseFragment {

    @InjectView(R.id.tv_about)
    TextView tvAbout;
    @InjectView(R.id.tv_version)
    TextView tvVersion;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.inject(this, rootView);

        if (cd.isConnectingToInternet()) {
            hideKeyboard();
            aboutUS();
        } else {
            Snackbar.make(container, getResources().getString(R.string.no_internet), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }

        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;

            tvVersion.setText("Version: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

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

    private void aboutUS(){
        showProgressbar("About US");
        RestClient.getMoneyMaker().aboutUS("about_us", new Callback<About>() {
            @Override
            public void success(About result, Response response) {
                dismiss();
                if ((int)result.getStatus() == 1){
                    tvAbout.setText(result.getData());
                }else if ((int)result.getStatus() == 0){
                    toast(result.getData());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("About US", "API failure " + error);
                dismiss();
            }
        });
    }
}

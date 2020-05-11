package com.edugainnow.edugain.ui.uprofi;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edugainnow.edugain.R;
import com.edugainnow.edugain.util.Apis;
import com.edugainnow.edugain.util.CustomPerference;
import com.edugainnow.edugain.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class UserProfile extends Fragment {

    private UserProfileViewModel mViewModel;

    private TextView txtWallet, txtcrWallet, txtDebitMoney;
    public static UserProfile newInstance() {
        return new UserProfile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_profile_fragment, container, false);

        if(Utils.isNetworkAvailable(getActivity()))
            executeWallet();
         txtWallet =
                root.findViewById(R.id.txt_walletBal);
        txtcrWallet =
                root.findViewById(R.id.txtcrWallet);
        txtDebitMoney =
                root.findViewById(R.id.txtDebitMoney);
                return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        // TODO: Use the ViewModel

    }

    void executeWallet()
    {
        Utils.customProgress(getActivity(),"Please Wait...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Reg_ID", CustomPerference.getString(getActivity(),CustomPerference.USER_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Apis.Wallet, jsonObject,
                response -> {

            Utils.customProgressStop();
                    try {
                        txtWallet.setText("\u20b9 "+response.getString("TotalAvailableBal"));
                        txtDebitMoney.setText("\u20b9 "+response.getString("TotalDebitBal"));
                        txtcrWallet.setText("\u20b9 "+response.getString("TotalCreaditBal"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {
            Utils.customProgressStop();

        });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);


    }
}

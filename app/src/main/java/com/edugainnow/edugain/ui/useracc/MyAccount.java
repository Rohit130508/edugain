package com.edugainnow.edugain.ui.useracc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edugainnow.edugain.R;
import com.edugainnow.edugain.profile.MainProfile;
import com.edugainnow.edugain.ui.auth.MainScreen;
import com.edugainnow.edugain.util.CustomPerference;


public class MyAccount extends Fragment {
    private TextView txtMyProfile, txtMyScore, txtPriceHistory, txtLogout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_my_account, container, false);

        txtMyProfile = root.findViewById(R.id.txtMyProfile);
        txtMyScore = root.findViewById(R.id.txtMyScore);
        txtPriceHistory = root.findViewById(R.id.txtPriceHistory);
        txtLogout = root.findViewById(R.id.txtLogout);

        txtLogout.setOnClickListener(v -> getLogout());
        txtMyProfile.setOnClickListener(v -> startActivity(new Intent(getActivity(), MainProfile.class)));
        txtMyScore.setOnClickListener(v -> startActivity(new Intent(getActivity(), ScoreList.class)));
        txtPriceHistory.setOnClickListener(v -> startActivity(new Intent(getActivity(), PriceHistory.class)));

        return root;
    }

    void getLogout()
    {
        CustomPerference.clearPref(getActivity());
        startActivity(new Intent(getActivity(), MainScreen.class)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

    }
}
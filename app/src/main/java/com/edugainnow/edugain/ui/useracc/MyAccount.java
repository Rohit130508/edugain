package com.edugainnow.edugain.ui.useracc;

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


public class MyAccount extends Fragment {
    private TextView txtMyProfile, txtMyScore, txtPriceHistory;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_my_account, container, false);

        txtMyProfile = root.findViewById(R.id.txtMyProfile);
        txtMyScore = root.findViewById(R.id.txtMyScore);
        txtPriceHistory = root.findViewById(R.id.txtPriceHistory);

        txtMyProfile.setOnClickListener(v -> startActivity(new Intent(getActivity(), MainProfile.class)));
        txtMyScore.setOnClickListener(v -> startActivity(new Intent(getActivity(), ScoreList.class)));
        txtPriceHistory.setOnClickListener(v -> startActivity(new Intent(getActivity(), MainProfile.class)));

        return root;
    }
}
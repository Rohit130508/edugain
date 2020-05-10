package com.edugainnow.edugain.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edugainnow.edugain.R;
import com.edugainnow.edugain.ui.home.activity.NewRegistration;
import com.edugainnow.edugain.ui.home.activity.TodayPackageReg;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.text_home);
        final TextView txtNewRegistration = root.findViewById(R.id.txtNewRegistration);
        final TextView txtRegList = root.findViewById(R.id.txtRegList);

        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));
        homeViewModel.getText1().observe(getViewLifecycleOwner(), s -> txtNewRegistration.setText(s));
        homeViewModel.getText2().observe(getViewLifecycleOwner(), s -> txtRegList.setText(s));

        txtNewRegistration.setOnClickListener(v -> startActivity(new Intent(getActivity(), NewRegistration.class)));
        txtRegList.setOnClickListener(v -> startActivity(new Intent(getActivity(), TodayPackageReg.class)));

        return root;
    }
}

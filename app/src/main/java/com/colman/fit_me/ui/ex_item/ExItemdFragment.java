package com.colman.fit_me.ui.ex_item;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.colman.fit_me.R;

public class ExItemdFragment extends Fragment {

    private ExItemViewModel exItemViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        exItemViewModel =
                ViewModelProviders.of(this).get(ExItemViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
/*        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}

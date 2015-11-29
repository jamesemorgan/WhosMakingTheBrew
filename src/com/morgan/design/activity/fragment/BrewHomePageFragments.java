package com.morgan.design.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.morgan.design.R;
import com.morgan.design.activity.TeaRoundHomeActivity;

public class BrewHomePageFragments extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_run_tea_round, container, false);

        final Button button = (Button) rootView.findViewById(R.id.brew_round_dashboard_enter_brew_round_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent home = new Intent(getActivity(), TeaRoundHomeActivity.class);
                startActivity(home);
            }
        });

        return rootView;
    }
}

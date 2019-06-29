package com.mayhematan.taptheball;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    Button startBtn,backBtn;
    TextView recordTV;
    SharedPreferences preferences;
    public StartFragment() {
        ///// Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ////// Inflate the layout for this fragment
        ////////////////show a start game fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        recordTV = view.findViewById(R.id.RecordTV);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int getscore = preferences.getInt("top score", 0);
        recordTV.setText(""+getResources().getString(R.string.top_score)+ " " + getscore);
        startBtn = view.findViewById(R.id.StartBtn);
        startBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /////////////start the game by broadcast
                Intent intent = new Intent("com.mayhematan.taptheball.GAME_BEGIN");
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                getActivity().getFragmentManager().beginTransaction().remove(StartFragment.this).commit();
            }
        });
        backBtn = view.findViewById ( R.id.back_to_main );
        backBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                /////////////end's the game by broadcast
                Intent intent = new Intent("com.mayhematan.taptheball.BACK_TO_MAIN");
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                getActivity().getFragmentManager().beginTransaction().remove(StartFragment.this).commit();
            }
        } );

        return view;
    }

}

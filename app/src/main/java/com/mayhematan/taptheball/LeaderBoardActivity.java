package com.mayhematan.taptheball;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderBoardActivity extends AppCompatActivity {
    ArrayList<Player> playerInfoList;
    SharedPreferences preference;
    Integer size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.leaderboard_activity );
        RecyclerView recyclerView = findViewById ( R.id.recycler );
        recyclerView.setHasFixedSize ( true );
        recyclerView.setLayoutManager(new LinearLayoutManager (this));
        preference = PreferenceManager.getDefaultSharedPreferences ( LeaderBoardActivity.this );
        playerInfoList = new ArrayList<> (); // initializing all the players ever played the game
        size = preference.getInt ( "size", 0 );
        for (Integer playerInfo = 1; playerInfo <= size; playerInfo++) {
            String tmp = preference.getString ( playerInfo.toString (), "" );
            if (!tmp.equals ( "" )) {
                Player player = new Player ( tmp );
                playerInfoList.add ( player );
            }
        }
        Collections.sort ( playerInfoList, new Comparator<Player> () { // sorting the players by scores to show later by desending order.
            @Override
            public int compare(Player o1, Player o2) { //
                int score1 = o1.getScore ();
                int score2 = o2.getScore ();
                return score2-score1;
            }
        } );
        final PlayerAdapter playerAdapter = new PlayerAdapter (playerInfoList);
        playerAdapter.setListener(new PlayerAdapter.MyPlayerListener() {
            @Override
            public void onPlayerClicked(int position, View view) {
            }

            @Override
            public void onPlayerLongClicked(int position, View view) {
                playerInfoList.remove(position);
                playerAdapter.notifyItemRemoved(position);
                size--;
                preference.edit().putInt("size", size).apply();
            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                playerInfoList.remove(viewHolder.getAdapterPosition());
                playerAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                size--;
                preference.edit().putInt("size", size).apply();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(playerAdapter);
        playerAdapter.notifyItemInserted(playerInfoList.size());
    }
}

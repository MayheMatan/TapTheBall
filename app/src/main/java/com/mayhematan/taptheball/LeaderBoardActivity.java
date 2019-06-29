package com.mayhematan.taptheball;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

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
        playerInfoList = new ArrayList<> ();
        size = preference.getInt ( "size", 0 );
        for (Integer i = 1; i <= size; i++) {
            String tmp = preference.getString ( i.toString (), "" );
            if (!tmp.equals ( "" )) {
                Player player = new Player ( tmp );
                System.out.println (player.getName ());
                if (player.getPhoto () == null) {
                    SharedPreferences lastUser = this.getSharedPreferences ( "lastUser", this.MODE_PRIVATE );
                    Bitmap bitmap = new Player ().StringToBitMap ( lastUser.getString ( "Photo", "-1" ) );
                }
                playerInfoList.add ( player );
            }
        }
        final PlayerAdapter playerAdapter = new PlayerAdapter (playerInfoList);
        playerAdapter.setListener(new PlayerAdapter.MyPlayerListener() {
            @Override
            public void onPlayerClicked(int position, View view) {
                Toast.makeText(LeaderBoardActivity.this, playerInfoList.get(position).getName(), Toast.LENGTH_SHORT).show();
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

                if(direction==ItemTouchHelper.RIGHT)
                    Toast.makeText(LeaderBoardActivity.this, "Right", Toast.LENGTH_SHORT).show();
                else if(direction==ItemTouchHelper.LEFT)
                    Toast.makeText(LeaderBoardActivity.this, "Left", Toast.LENGTH_SHORT).show();

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

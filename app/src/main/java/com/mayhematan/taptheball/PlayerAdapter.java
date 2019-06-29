package com.mayhematan.taptheball;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private List<Player> players;
    private MyPlayerListener listener;

    interface MyPlayerListener {
        void onPlayerClicked(int position,View view);
        void onPlayerLongClicked(int position,View view);
    }

    public void setListener(MyPlayerListener listener) {
        this.listener = listener;
    }

    public PlayerAdapter(List<Player> players) {
        this.players = players;
    }


    public class PlayerViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        TextView creditTv;
        ImageView ballIv;

        public PlayerViewHolder(View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.player_name);
            creditTv = itemView.findViewById(R.id.player_score);
            ballIv = itemView.findViewById(R.id.ball_img);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(listener!=null)
                        listener.onPlayerClicked(getAdapterPosition(),view);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(listener!=null)
                        listener.onPlayerLongClicked(getAdapterPosition(),view);
                    return false;
                }
            });
        }
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cell,parent,false);
        PlayerViewHolder playerViewHolder = new PlayerViewHolder(view);
        return playerViewHolder;
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        Player player = players.get(position);
        holder.nameTv.setText(""+player.getName());
        holder.creditTv.setText(player.getScore ()+"");
        holder.ballIv.setImageBitmap(player.getPhoto());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
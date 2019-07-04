package com.mayhematan.taptheball;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager { // Singleton Pattern Class
    private static MusicManager reference = null;
    private MediaPlayer mediaPlayer;

    private MusicManager() {}

    public static MusicManager getInstance() {
        if (reference == null) {
            reference = new MusicManager ();
        }
        return reference;
    }

    public void initalizeMediaPlayer (Context context, int musicId) {
        mediaPlayer = new MediaPlayer ();
        mediaPlayer.release ();
        mediaPlayer = MediaPlayer.create ( context, musicId );
        mediaPlayer.setLooping ( true );
    }

    public void startMusic() {
        mediaPlayer.start ();
    }

    public void stopMusic() {
        mediaPlayer.stop ();
        mediaPlayer.release ();
    }
}

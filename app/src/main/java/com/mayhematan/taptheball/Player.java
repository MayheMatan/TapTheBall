package com.mayhematan.taptheball;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Player {
    String name;
    Integer score;
    Bitmap photo;

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }



    public Player(Bitmap photo, String name, int score) {
        this.name = name;
        this.score = score;
        this.photo = photo;
    }

    public Player() {
        name = "";
        score = 0;
    }

    public Player(String raw) {
        String[] data = raw.split(";");
        name = new String(data[0]);
        score = Integer.parseInt(data[1]);
        photo = StringToBitMap(data[2]);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String BitMapToString(Bitmap bitmap) {
        if(bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        }
        return "-1";
    }

    public Bitmap StringToBitMap(String encodedString) {
        if (!encodedString.equals("-1")) {
            try {
                byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            } catch (Exception e) {
                e.getMessage();
                return null;
            }
        }
        return null;
    }
    @Override
    public String toString() {
        return name + ";" + score.toString() + ";" + BitMapToString(photo);
    }
}

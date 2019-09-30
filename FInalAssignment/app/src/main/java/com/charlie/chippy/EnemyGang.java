package com.charlie.chippy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;

public class EnemyGang {


    int xPosition;
    int yPosition;
    int direction;
    Bitmap image;
    Enemy enemy;

    private Rect hitBox;
    private int ENEMY_WIDTH = 15;

    ArrayList<Rect> enemies = new ArrayList<Rect>();



    public EnemyGang(Context context, int x, int y) {
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.images);
        this.xPosition = x;
        this.yPosition = y;

        this.hitBox = new Rect(this.xPosition, this.yPosition, this.xPosition + this.image.getWidth(), this.yPosition + this.image.getHeight());
    }

    public void updateEnemyGangPosition() {
        this.xPosition = this.xPosition - 15;

        // update the position of the hitbox
        this.hitBox.left = this.xPosition;
        this.hitBox.right = this.xPosition + this.image.getWidth();
        this.updateEnemyGangHitbox();
    }

    public void updateEnemyGangHitbox() {
        // update the position of the hitbox
        this.hitBox.top = this.yPosition;
        this.hitBox.left = this.xPosition;
        this.hitBox.right = this.xPosition + this.image.getWidth();
        this.hitBox.bottom = this.yPosition + this.image.getHeight();
    }

    public Rect getHitbox() {
        return this.hitBox;
    }


    public void setXPosition(int x) {
        this.xPosition = x;
        this.updateEnemyGangHitbox();
    }

    public void setYPosition(int y) {
        this.yPosition = y;
        this.updateEnemyGangHitbox();
    }

    public int getXPosition() {
        return this.xPosition;
    }

    public int getYPosition() {
        return this.yPosition;
    }

    public Bitmap getBitmap() {
        return this.image;
    }


    public void spawnEnemyGang(){

        Rect enemyGang = new Rect(this.xPosition
                , this.yPosition,this.xPosition+this.image.getWidth()
                , this.yPosition );

        this.enemies.add(enemyGang);

    }

}

package com.charlie.chippy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameEngine extends SurfaceView implements Runnable {


    final static String TAG="TAPPY-SPACESHIP";

    // screen size
    int screenHeight;
    int screenWidth;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;




    // -----------------------------------
    // GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------
    Player player;
    boolean shoot = false;



    Bullets bullet;
    Enemy enemy;
    EnemyGang enemyGang;
    EnemyGang enemyGang1;
    EnemyGang enemyGang2;
    List<EnemyGang> enemyGangList = new ArrayList<EnemyGang>();
    List<Bullets> bulletsList = new ArrayList<Bullets>();

    // ----------------------------
    // ## GAME STATS
    // ----------------------------
    int score = 0;
    int lives = 5;



    int fingerXPosition;
    int fingerYPosition;

    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;


        this.printScreenInfo();

        // @TODO: Add your sprites

        this.spawnPlayer();
        this.spawnEnemies();
        this.spawnEnemyGang();

        this.spawnBullets();


        // @TODO: Any other game setup

    }


    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }


    private void spawnPlayer(){

        this.player = new Player(this.getContext(),190,this.screenHeight/2 + 200);
    }

    private  void spawnEnemies(){

        this.enemy = new Enemy(this.getContext(),370,this.screenHeight/2-400);

    }

    private void spawnEnemyGang(){


        this.enemyGang = new EnemyGang(this.getContext(),enemy.getHitbox().right,this.screenHeight/2-400);
        this.enemyGangList.add(new EnemyGang(this.getContext(),enemy.getHitbox().right ,this.screenHeight/2-315));

        this.enemyGangList.add( new EnemyGang(this.getContext(),enemy.getHitbox().right ,this.screenHeight/2-235));

        this.enemyGangList.add(enemyGang);


    }

    private  void spawnBullets(){

        this.bullet = new Bullets(getContext(),this.player.getHitbox().left ,this.player.getHitbox().top);

        this.bulletsList.add(bullet);
    }
    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------


    int numloops = 0;

    public void updatePositions() {


        // Update position of background


        // @TODO: Update position of player

        numloops = numloops + 1;



        int BULLET_SPEED = 150;



        for(int i = 0; i < this.bulletsList.size(); i++){
             bullet = this.bulletsList.get(i);
            bullet.yPosition = bullet.yPosition - BULLET_SPEED;
            bullet.updateHitbox();

        }

        if(shoot){

            if(numloops % 2 == 0){
                spawnBullets();
            }
        }


        // @TODO: Update position of enemy ships




        // @TODO: Collision detection between enemy and wall


        // @TODO: Collision detection between player and enemy

    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();


            // DRAW THE BACKGROUND




            this.canvas.drawColor(Color.BLACK);
            // configure the drawing tools

            paintbrush.setColor(Color.WHITE);





            //PLAYER AND ITS HITBOX

            paintbrush.setStrokeWidth(5);
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);


            this.canvas.drawBitmap(this.player.getBitmap(),this.player.getXPosition(),this.player.getYPosition(),paintbrush);
            Rect playersHitBox = this.player.getHitbox();
            this.canvas.drawRect(playersHitBox.left,playersHitBox.top,playersHitBox.right,playersHitBox.bottom,paintbrush);

            Rect enemyHitbox = this.enemy.getHitbox();
            this.canvas.drawRect(enemyHitbox.left,enemyHitbox.top,enemyHitbox.right,enemyHitbox.bottom,paintbrush);
            this.canvas.drawBitmap(this.enemy.getBitmap(),this.enemy.getXPosition(),this.enemy.getYPosition(),paintbrush);


//            Rect  enemyGangHitbox = this.enemyGang.getHitbox();
//            this.canvas.drawRect(enemyGangHitbox.left,enemyGangHitbox.top,enemyGangHitbox.right,enemyGangHitbox.bottom,paintbrush);

            for(int i=0; i<enemyGangList.size();i++) {

                Rect enemyGangHitbox = this.enemyGang.getHitbox();

                int x = enemyGangList.get(i).xPosition;
                int y = enemyGangList.get(i).yPosition;
                canvas.drawRect(enemyGangList.get(i).getHitbox().left,enemyGangList.get(i).getHitbox().top,enemyGangList.get(i).getHitbox().right,enemyGangList.get(i).getHitbox().bottom,paintbrush);

            }
            // enemyGangList

            for(int i = 0; i < enemyGangList.size(); i++){

                int x = enemyGangList.get(i).xPosition;
                int y = enemyGangList.get(i).yPosition;

                canvas.drawBitmap(this.enemyGang.getBitmap(),x,y,paintbrush);

            }




            //Draw bullets on the screen

            for(int i = 0; i < this.bulletsList.size(); i++ ){
                bullet = this.bulletsList.get(i);

                canvas.drawRect(bulletsList.get(i).getHitbox().left,bulletsList.get(i).getHitbox().top,bulletsList.get(i).getHitbox().right,bulletsList.get(i).getHitbox().bottom,paintbrush);
                canvas.drawBitmap(bullet.getImage(),this.bullet.xPosition,this.bullet.yPosition,paintbrush);

            }

            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setFPS() {
        try {
            gameThread.sleep(50);
        }
        catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();

        int fingerXPosition = (int)event.getX();
        int fingerYPosition = (int)event.getY();


        if(userAction == MotionEvent.ACTION_DOWN){
            shoot = true;
        }
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_MOVE) {
            this.player.xPosition = fingerXPosition;
            this.player.yPosition = fingerYPosition;
            player.updateHitbox();


        }
        else if (userAction == MotionEvent.ACTION_UP) {
            // move player down
            shoot = false;
        }

        return true;
    }
}



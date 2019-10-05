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


    final static String TAG="CHIPPY";

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
    List<EnemyGang> enemyGangList1 = new ArrayList<EnemyGang>();
    List<EnemyGang> enemyGangList2 = new ArrayList<EnemyGang>();
    List<EnemyGang> enemyGangList3 = new ArrayList<EnemyGang>();
    List<EnemyGang> enemyGangList4 = new ArrayList<EnemyGang>();
    List<Bullets> bulletsList = new ArrayList<Bullets>();

    int BULLET_WIDTH = 35;
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
        this.spawnEnemyGang1();
        this.spawnEnemyGang2();
        this.spawnEnemyGang3();
        this.spawnEnemyGang4();
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

    private void spawnEnemyGang1(){

// Draw Enemy Gang on the right side of larger enemy Beetle
        for(int i = 0; i<=6; i++) {

            this.enemyGangList1.add(new EnemyGang(this.getContext(), enemy.getHitbox().right - 40, this.screenHeight / 2 - 400));
        }

    }
    private void spawnEnemyGang2(){

// Draw Enemy Gang on the bottom side of the larger enemy
        for(int i = 0; i<=11; i++) {

            this.enemyGangList2.add(new EnemyGang(this.getContext(), enemy.getBitmap().getWidth() , this.screenHeight / 2-140));
        }

    }

    // Draw Enemy Gang on the left side of the larger enemy
    private void spawnEnemyGang3(){


        for(int i = 0; i<=6; i++) {

            this.enemyGangList3.add(new EnemyGang(this.getContext(),  enemy.getBitmap().getWidth()/2 - 10 , this.screenHeight / 2 - 400));
        }

    }
    // Draw Enemy Gang on the top side of the larger enemy
    private void spawnEnemyGang4(){


        for(int i = 0; i<=9; i++) {

            this.enemyGangList4.add(new EnemyGang(this.getContext(), enemy.getBitmap().getWidth() +50, this.enemy.getBitmap().getHeight() - 60));
        }

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

    public void distance( double squarex, double squarey, double bulletx, double bullety){
        double a = (squarex - bulletx);
        double b = (squarey - bullety);
        double distance = Math.sqrt((a*a) + (b*b));

        // 2. calculate the "rate" to move
        double xn = (a / distance);
        double yn = (b / distance);

        // 3. move the bullet
       bulletx = bulletx + (int)(xn * 15);
        bullety = bullety + (int)(yn * 15);

        Log.d(TAG,"New bullet (x,y): (" + bulletx + "," + bullety + ")");

    }

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

//
////            Rect  enemyGangHitbox = this.enemyGang.getHitbox();
////            this.canvas.drawRect(enemyGangHitbox.left,enemyGangHitbox.top,enemyGangHitbox.right,enemyGangHitbox.bottom,paintbrush);
//
//            for(int i=0; i<enemyGangList.size();i++) {
//
//                Rect enemyGangHitbox = this.enemyGang.getHitbox();
//
//                int x = enemyGangList.get(i).xPosition;
//                int y = enemyGangList.get(i).yPosition;
//                canvas.drawRect(enemyGangList.get(i).getHitbox().left,enemyGangList.get(i).getHitbox().top,enemyGangList.get(i).getHitbox().right,enemyGangList.get(i).getHitbox().bottom,paintbrush);
//
//            }
//            // enemyGangList
//
//            for(int i = 0; i < enemyGangList.size(); i++){
//
//                int x = enemyGangList.get(i).xPosition;
//                int y = enemyGangList.get(i).yPosition;
//
//                canvas.drawBitmap(this.enemyGang.getBitmap(),x,y,paintbrush);
//
//            }

            paintbrush.setColor(Color.WHITE);

            // Drawing an array of enemyGang1 on the screen

            for(int i = 1; i< enemyGangList1.size(); i++){
                for(int j = 1; j< enemyGangList1.size(); j++){
                    if(i == 1){
                        this.canvas.drawBitmap(this.enemyGangList1.get(j).getBitmap(), this.enemyGangList1.get(j).getXPosition() + BULLET_WIDTH * j +10, this.enemyGangList1.get(j).getYPosition() + 10  , paintbrush);
                    }
                    this.canvas.drawBitmap(this.enemyGangList1.get(j).getBitmap(), this.enemyGangList1.get(j).getXPosition() + BULLET_WIDTH * j + 10, this.enemyGangList1.get(j).getYPosition() + BULLET_WIDTH * i  + 10, paintbrush);

                }
            }


            //Drawing an array of hitboxes to the enemyGangList1 on the screen

            for(int i = 1; i< enemyGangList1.size(); i++){
                for (int j = 1; j<enemyGangList1.size(); j++){
                    if(i == 1){
                        this.canvas.drawRect(this.enemyGangList1.get(i).getHitbox().left + BULLET_WIDTH *j + 10,this.enemyGangList1.get(i).getHitbox().top,this.enemyGangList1.get(i).getHitbox().right + BULLET_WIDTH * j +10,this.enemyGangList1.get(i).getHitbox().bottom,paintbrush);
                    }
                    this.canvas.drawRect(this.enemyGangList1.get(i).getHitbox().left + BULLET_WIDTH *j + 10,this.enemyGangList1.get(i).getHitbox().top+ BULLET_WIDTH *i  + 10,this.enemyGangList1.get(i).getHitbox().right + BULLET_WIDTH * j +10,this.enemyGangList1.get(i).getHitbox().bottom+ BULLET_WIDTH * i + 10,paintbrush);
                }
            }


            // Drawing an array of enemyGang2 on the screen

            for(int i = 1; i< enemyGangList2.size(); i++){
                for(int j = 1; j< enemyGangList2.size(); j++){
                    if(i == 1){
                        this.canvas.drawBitmap(this.enemyGangList2.get(j).getBitmap(), this.enemyGangList2.get(j).getXPosition() + BULLET_WIDTH * j +10, this.enemyGangList2.get(j).getYPosition() + 10  , paintbrush);
                    }
                    this.canvas.drawBitmap(this.enemyGangList2.get(j).getBitmap(), this.enemyGangList2.get(j).getXPosition() + BULLET_WIDTH * j + 10, this.enemyGangList2.get(j).getYPosition() + BULLET_WIDTH * i  + 10, paintbrush);

                }
            }
            // Drawing an array of hitboxes to the enemyGangList2 on the screen
            for(int i = 1; i< enemyGangList2.size(); i++){
                for (int j = 1; j<enemyGangList2.size(); j++){
                    if(i == 1){
                        this.canvas.drawRect(this.enemyGangList2.get(i).getHitbox().left + BULLET_WIDTH *j + 10,this.enemyGangList2.get(i).getHitbox().top,this.enemyGangList2.get(i).getHitbox().right + BULLET_WIDTH * j +10,this.enemyGangList2.get(i).getHitbox().bottom,paintbrush);
                    }
                    this.canvas.drawRect(this.enemyGangList2.get(i).getHitbox().left + BULLET_WIDTH *j + 10,this.enemyGangList2.get(i).getHitbox().top+ BULLET_WIDTH *i  + 10,this.enemyGangList2.get(i).getHitbox().right + BULLET_WIDTH * j +10,this.enemyGangList2.get(i).getHitbox().bottom+ BULLET_WIDTH * i + 10,paintbrush);
                }
            }

            // Drawing an array of enemyGang3 on the screen

            for(int i = 1; i< enemyGangList3.size(); i++){
                for(int j = 1; j< enemyGangList3.size(); j++){
                    if(i == 1){
                        this.canvas.drawBitmap(this.enemyGangList3.get(j).getBitmap(), this.enemyGangList3.get(j).getXPosition() + BULLET_WIDTH * j +10, this.enemyGangList3.get(j).getYPosition() + 10  , paintbrush);
                    }
                    this.canvas.drawBitmap(this.enemyGangList3.get(j).getBitmap(), this.enemyGangList3.get(j).getXPosition() + BULLET_WIDTH * j + 10, this.enemyGangList3.get(j).getYPosition() + BULLET_WIDTH * i  + 10, paintbrush);

                }
            }

            // Drawing an array of hitboxes to the enemyGangList3 on the screen

            for(int i = 1; i< enemyGangList3.size(); i++){
                for (int j = 1; j<enemyGangList3.size(); j++){
                    if(i == 1){
                        this.canvas.drawRect(this.enemyGangList3.get(i).getHitbox().left + BULLET_WIDTH *j + 10,this.enemyGangList3.get(i).getHitbox().top,this.enemyGangList3.get(i).getHitbox().right + BULLET_WIDTH * j +10,this.enemyGangList3.get(i).getHitbox().bottom,paintbrush);
                    }
                    this.canvas.drawRect(this.enemyGangList3.get(i).getHitbox().left + BULLET_WIDTH *j + 10,this.enemyGangList3.get(i).getHitbox().top+ BULLET_WIDTH *i  + 10,this.enemyGangList3.get(i).getHitbox().right + BULLET_WIDTH * j +10,this.enemyGangList3.get(i).getHitbox().bottom+ BULLET_WIDTH * i + 10,paintbrush);
                }
            }

            // Drawing an array of enemyGang4 on the screeen

            for(int i = 1; i< enemyGangList4.size(); i++){
                for(int j = 1; j< enemyGangList4.size(); j++){
                    if(i == 1){
                        this.canvas.drawBitmap(this.enemyGangList4.get(j).getBitmap(), this.enemyGangList4.get(j).getXPosition() + BULLET_WIDTH * j +10, this.enemyGangList4.get(j).getYPosition() + 10  , paintbrush);
                    }
                    this.canvas.drawBitmap(this.enemyGangList4.get(j).getBitmap(), this.enemyGangList4.get(j).getXPosition() + BULLET_WIDTH * j + 10, this.enemyGangList4.get(j).getYPosition() + BULLET_WIDTH * i  + 10, paintbrush);

                }
            }
            // Drawing an array of hitboxes to the enemyGangList4 on the screen

            for(int i = 1; i< enemyGangList4.size(); i++){
                for (int j = 1; j<enemyGangList4.size(); j++){
                    if(i == 1){
                        this.canvas.drawRect(this.enemyGangList4.get(i).getHitbox().left + BULLET_WIDTH *j + 10,this.enemyGangList4.get(i).getHitbox().top,this.enemyGangList4.get(i).getHitbox().right + BULLET_WIDTH * j +10,this.enemyGangList4.get(i).getHitbox().bottom,paintbrush);
                    }
                    this.canvas.drawRect(this.enemyGangList4.get(i).getHitbox().left + BULLET_WIDTH *j + 10,this.enemyGangList4.get(i).getHitbox().top+ BULLET_WIDTH *i  + 10,this.enemyGangList4.get(i).getHitbox().right + BULLET_WIDTH * j +10,this.enemyGangList4.get(i).getHitbox().bottom+ BULLET_WIDTH * i + 10,paintbrush);
                }
            }

            paintbrush.setColor(Color.BLACK);
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



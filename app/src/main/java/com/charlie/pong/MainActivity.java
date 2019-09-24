package com.charlie.pong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import android.graphics.Point;

import android.os.Bundle;
import android.view.Display;

public class MainActivity extends AppCompatActivity {



    Canvas canvas;
    PongView gameView;
    Display display;
    Point screenSize;
    int screenWidth;
    int screenHeight;

    long timeOfLastFrame;
    long fps;


    // gameObjects  -sprites
    Point ballPosition;
    int ballWidth;


    




    GameEngine pongGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get size of the screen



        this.gameView = new PongView(this);
        setContentView(this.gameView);


        this.display = this.getWindowManager().getDefaultDisplay();
        this.screenSize = new Point();
        this.display.getSize(screenSize);
        this.screenHeight = screenSize.y;
        this.screenWidth = screenSize.x;



        //1. size of ball

        this.ballWidth = this.screenWidth/35;


        //3. Initial position of the ball on the screen
        ballPosition = new Point();
        ballPosition.x = this.screenWidth/2;
        ballPosition.y = this.screenHeight/2;


        // Initialize the GameEngine object
        // Pass it the screen size (height & width)

    }

    // Android Lifecycle functions
    // ----------------------------

    // This function gets run when user switches from the game to some other app on the phone
    @Override
    protected void onPause() {
        super.onPause();

        // Pause the game
        pongGame.pauseGame();
    }

    // This function gets run when user comes back to the game
    @Override
    protected void onResume() {
        super.onResume();

        // Start the game
        this.gameView.resumeGame();

    }
    class PongView extends  SurfaceView implements Runnable{

        Thread gameThread = null;
        SurfaceHolder holder;
        boolean gameIsRunning;
        Paint paintbrush;


        public PongView(Context context) {
            super(context);
            this.holder = this.getHolder();
            paintbrush = new Paint();


        }

        @Override
        public void run() {

            while(gameIsRunning == true){
                this.updatePositions();
                this.drawPositions();
                this.setFPS();

            }



        }


        public void updatePositions(){

        }
        public void drawPositions(){

            if(this.holder.getSurface().isValid()){

                canvas = this.holder.lockCanvas();

                canvas.drawColor(Color.BLUE);

                paintbrush.setColor(Color.argb(255,255,255,255));

                paintbrush.setTextSize(45);


                // code to draw the ball

                int left = ballPosition.x;
                int top = ballPosition.y;
                int right = ballPosition.x + ballWidth;
                int bottom = ballPosition.y + ballWidth;

                canvas.drawRect(left, top, right , bottom, paintbrush);

                this.holder.unlockCanvasAndPost(canvas);
            }




        }

        public void setFPS(){


            long timeOfCurrentFrame = (System.currentTimeMillis() - timeOfLastFrame);
            long timeToSleep = 15 - timeOfCurrentFrame;

            if(timeOfCurrentFrame > 0){
                fps = (int) (1000/timeOfCurrentFrame);

            }
            if(timeToSleep > 0){
                try{
                    gameThread.sleep(timeToSleep);

                }catch (InterruptedException e) {

                }


            }
            timeOfLastFrame = System.currentTimeMillis();

        }

        public boolean onTouchEvent(MotionEvent event){
            return  super.onTouchEvent(event);

        }

        public void pauseGame(){

        }

        public void resumeGame(){
            gameIsRunning = true;
            this.gameThread = new Thread(this);
            this.gameThread.start();;

        }
    }
}


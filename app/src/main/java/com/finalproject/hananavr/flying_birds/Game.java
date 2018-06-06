package com.finalproject.hananavr.flying_birds;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game extends View {

    ArrayList<Bird> birds = new ArrayList<>();
    Paint paint;
    InGameMenu igm;
    Bitmap heart, inGameMenu, rightShooter, leftShooter;
    Shooter shooter;
    int pause_flg, lives, fontSize, shooterDirection_flg, score, difficultyChanger;
    float xTouchPos;

    //Variables needed for proper random bird creation logic
    Runnable runnable;
    ScheduledExecutorService service;
    Future<?> future;

    public Game(Context context) {
        super(context);
        lives = 2;
        pause_flg = 0;
        shooterDirection_flg = 0;
        score = 0;
        difficultyChanger = 5;
        xTouchPos = 0;

        shooter = new Shooter(context, Resources.getSystem().getDisplayMetrics().widthPixels/2-90, 280);
        //Setting for font style (used for showing the live left as text)
        fontSize = getResources().getDimensionPixelSize(R.dimen.inGameFontSize);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(fontSize);

        //All the necessery resources for in game menu
        inGameMenu = BitmapFactory.decodeResource(getResources(), R.drawable.menu_button);
        Bitmap igmBackground = BitmapFactory.decodeResource(getResources(), R.drawable.igmbackground);
        Bitmap igmResume = BitmapFactory.decodeResource(getResources(), R.drawable.resumegameoption);
        Bitmap igmMainMenu = BitmapFactory.decodeResource(getResources(), R.drawable.mainmenuoption);
        Bitmap igmExit = BitmapFactory.decodeResource(getResources(), R.drawable.exitgameoption);
        igm = new InGameMenu(context,igmBackground,igmResume,igmMainMenu,igmExit);

        //Heart image to show next to the lives left text
        heart = BitmapFactory.decodeResource(getResources(), R.drawable.heart);

        //Pictures of left & right shooter direction for changing shooter direction animation
        rightShooter = BitmapFactory.decodeResource(getResources(), R.drawable.rightshooter);
        leftShooter = BitmapFactory.decodeResource(getResources(), R.drawable.leftshooter);

        startGame();

    }

    private void startGame() {
        runnable = new Runnable() {
            public void run() {
                boolean rndBool;
                Random r = new Random();
                if(score > 1500)
                    difficultyChanger += 3;
                if(score > 3000)
                    difficultyChanger += 3;
                if(score > 4500)
                    difficultyChanger += 3;
                if(score > 6000)
                    difficultyChanger += 3;
                if(score > 7500)
                    difficultyChanger += 3;

                int rndBirdNum = r.nextInt(5)+1;
                switch (rndBirdNum){
                    case 1:
                        rndBool = r.nextBoolean();
                        if(rndBool == true){
                            RedBird leftRed = new RedBird(getContext(),rndBool);
                            leftRed.setBirdInfo(-260,r.nextInt(600), r.nextInt(difficultyChanger)+difficultyChanger);
                            birds.add(leftRed);
                        }else{
                            RedBird rightRed = new RedBird(getContext(),rndBool);
                            rightRed.setBirdInfo(Resources.getSystem().getDisplayMetrics().widthPixels,r.nextInt(600), r.nextInt(difficultyChanger)+difficultyChanger);
                            birds.add(rightRed);
                        }
                        break;
                    case 2:
                        rndBool = r.nextBoolean();
                        if(rndBool == true){
                            YellowBird leftYellow = new YellowBird(getContext(),rndBool);
                            leftYellow.setBirdInfo(-260,r.nextInt(600),r.nextInt(difficultyChanger)+difficultyChanger);
                            birds.add(leftYellow);
                        }else{
                            YellowBird rightYellow = new YellowBird(getContext(),rndBool);
                            rightYellow.setBirdInfo(Resources.getSystem().getDisplayMetrics().widthPixels,r.nextInt(600),r.nextInt(difficultyChanger)+difficultyChanger);
                            birds.add(rightYellow);
                        }
                        break;
                    case 3:
                        rndBool = r.nextBoolean();
                        if(rndBool == true){
                            GreyBird leftGrey = new GreyBird(getContext(),rndBool);
                            leftGrey.setBirdInfo(-260,r.nextInt(600), r.nextInt(difficultyChanger)+difficultyChanger);
                            birds.add(leftGrey);
                        }else{
                            GreyBird rightGrey = new GreyBird(getContext(),rndBool);
                            rightGrey.setBirdInfo(Resources.getSystem().getDisplayMetrics().widthPixels,r.nextInt(600), r.nextInt(difficultyChanger)+difficultyChanger);
                            birds.add(rightGrey);
                        }
                        break;
                    case 4:
                        rndBool = r.nextBoolean();
                        if(rndBool == true){
                            PoliceBird leftPolice = new PoliceBird(getContext(),rndBool);
                            leftPolice.setBirdInfo(-260,r.nextInt(600),r.nextInt(difficultyChanger)+difficultyChanger);
                            birds.add(leftPolice);
                        }else{
                            PoliceBird rightPolice = new PoliceBird(getContext(),rndBool);
                            rightPolice.setBirdInfo(Resources.getSystem().getDisplayMetrics().widthPixels,r.nextInt(600),r.nextInt(difficultyChanger)+difficultyChanger);
                            birds.add(rightPolice);
                        }
                        break;
                    case 5:
                        rndBool = r.nextBoolean();
                        if(rndBool == true){
                            Eagle leftEagle = new Eagle(getContext(), rndBool);
                            leftEagle.setBirdInfo(-260, r.nextInt(600), r.nextInt(difficultyChanger)+difficultyChanger);
                            birds.add(leftEagle);
                        }else{
                            Eagle rightEagle = new Eagle(getContext(), rndBool);
                            rightEagle.setBirdInfo(Resources.getSystem().getDisplayMetrics().widthPixels, r.nextInt(600), r.nextInt(difficultyChanger)+difficultyChanger);
                            birds.add(rightEagle);
                        }
                        break;
                }

            }
        };
        service = Executors.newSingleThreadScheduledExecutor();
        future = service.scheduleAtFixedRate(runnable, 5, 3, TimeUnit.SECONDS);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ListIterator it = birds.listIterator();
        while (it.hasNext()){
            Bird bird = (Bird) it.next();
            bird.draw(canvas, pause_flg);
        }
        //Drawing lives left as text
        canvas.drawText(String.valueOf(lives),10,fontSize, paint);

        //Drawing heart image
        canvas.drawBitmap(heart,35,4,null);

        //Drawing current score as text
        canvas.drawText("Score: "+String.valueOf(score),95,fontSize, paint);

        //Drawing in game menu image
        canvas.drawBitmap(inGameMenu,Resources.getSystem().getDisplayMetrics().widthPixels-100,4,null);

        checkBirdPassingScreen();

//        if(shooterDirection_flg == 0)
//            canvas.drawBitmap(rightShooter,Resources.getSystem().getDisplayMetrics().widthPixels/2-50,240,null);
//        else
//            canvas.drawBitmap(leftShooter,Resources.getSystem().getDisplayMetrics().widthPixels/2-100,235,null);

        shooter.draw(canvas, xTouchPos);

        if(pause_flg == 1){
            igm.draw(canvas);
        }
        invalidate();
    }

    private void checkBirdPassingScreen() {
        ListIterator it = birds.listIterator();
        while (it.hasNext()){
            Bird bird = (Bird) it.next();
            if (bird.IsRightDirection()){
                if(bird.getX() > Resources.getSystem().getDisplayMetrics().widthPixels && !bird.IsDead()){
                    if(lives > 0)
                        lives--;
                    if (lives == 0){    // Game Over
                        Thread resultThread = new Thread(){
                            public void run(){
                                Intent resultThread = new Intent(getContext(), Result.class);
                                resultThread.putExtra("SCORE",score);
                                getContext().startActivity(resultThread);
                            }
                        };
                        resultThread.start();
                        //getContext().startActivity(new Intent(getContext(),Result.class));
                    }
                    birds.remove(bird);
                    return;
                }
            }
            else{
                if(bird.getX()+bird.getImageWidth() < 0 && !bird.IsDead()) {
                    if(lives > 0)
                        lives--;
                    if (lives == 0){    // Game Over
                        Thread resultThread = new Thread(){
                            public void run(){
                                Intent backToMainMenu = new Intent(getContext(), Result.class);
                                backToMainMenu.putExtra("SCORE",score);
                                getContext().startActivity(backToMainMenu);
                            }
                        };
                        resultThread.start();
                        //getContext().startActivity(new Intent(getContext(),Result.class));
                    }
                    birds.remove(bird);
                    return;
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        if (eventAction == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            xTouchPos = x;


            //Check where the finger pressed to change shooter direction accordingly
            if(x <= Resources.getSystem().getDisplayMetrics().widthPixels/2)
                shooterDirection_flg = 1;
            else
                shooterDirection_flg = 0;

            if(pause_flg == 0){
                //Check for every bird if the clicks count are enough to turn the bird dead
                ListIterator it = birds.listIterator();
                while (it.hasNext()){
                    Bird bird = (Bird) it.next();
                    if(!bird.IsDead()){
                        if(x >= bird.getX() && x < (bird.getX()+bird.getImageWidth()) && y >= bird.getY() && y < (bird.getY()+bird.getImageHeight()))
                        {
                            bird.currentClicksCounter++;
                            if(bird.requiredClicksToKill == bird.currentClicksCounter){
                                score += bird.score;
                                if(bird.getBirdSpeedX() < 10){
                                    bird.setBirdSpeedY(10);
                                    bird.setBirdSpeedX(10);
                                }else{
                                    bird.setBirdSpeedY(bird.getBirdSpeedX());
                                }

                                bird.setDead(true);
                                return true;
                            }
                        }
                    }
                }

                //Check if in game menu icon is pressed
                if(x >= Resources.getSystem().getDisplayMetrics().widthPixels-100 && x < (Resources.getSystem().getDisplayMetrics().widthPixels-100+inGameMenu.getWidth()) && y >= 4 && y < (4+inGameMenu.getHeight())){
                    pause_flg = 1;
                    future.cancel(true);
                    Thread pausedStateThread = new Thread(){
                        public void run(){
                            ListIterator it = birds.listIterator();
                            while(it.hasNext()){
                                Bird bird = (Bird) it.next();
                                bird.pausedState();
                            }
                        }
                    };
                    pausedStateThread.start();
                    return true;
                }
            }

            //If the in game menu is open, only checks this code section
            if(pause_flg == 1){
                //If resume option is pressed
                if(x >= 810 && x < (810+igm.getOptionWidth()) && y >= 265 && y < (265+igm.getOptionHeight()))
                {
                    future = service.scheduleAtFixedRate(runnable, 2, 3, TimeUnit.SECONDS);
                    Thread runningStateThread = new Thread(){
                        public void run(){
                            ListIterator it = birds.listIterator();
                            while(it.hasNext()){
                                Bird bird = (Bird) it.next();
                                bird.runningState();
                            }
                        }
                    };
                    runningStateThread.start();
                    pause_flg = 0;
                }
                //If back to main menu option is pressed
                if(x >= 810 && x < (810+igm.getOptionWidth()) && y >= 350 && y < (350+igm.getOptionHeight())){
                    Thread backToMainMenuThread = new Thread(){
                        public void run(){
                            getContext().startActivity(new Intent(getContext(),MainActivity.class));
                        }
                    };
                    backToMainMenuThread.start();
                    return true;
                }
                //If exit option is pressed
                if(x >= 810 && x < (810+igm.getOptionWidth()) && y >= 435 && y < (435+igm.getOptionHeight())){
                    Thread exitGameThread = new Thread(){
                        public void run(){
                            System.exit(0);
                        }
                    };
                    exitGameThread.start();
                    return true;
                }
            }
        }
        return false;
    }
}


// Toast.makeText(this.getContext().getApplicationContext(), "Text" ,Toast.LENGTH_LONG).show();
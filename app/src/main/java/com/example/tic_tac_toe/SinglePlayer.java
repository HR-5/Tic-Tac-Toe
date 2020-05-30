package com.example.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class SinglePlayer extends AppCompatActivity {
    Singleplayer singleplayer;
    ConstraintLayout cons;
    TextView t;
    String name1, name2;
    int n, player,check,flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);
        cons = (ConstraintLayout) findViewById(R.id.singlec);
        flag = 0;
        singleplayer = new Singleplayer(this);
        final Switch sw = (Switch) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check = 1;
                } else {
                    check = 0;
                }
            }
        });
    }

    public void play(View view) {
        EditText t1 = (EditText) findViewById(R.id.editText2);
        name1 = t1.getText().toString();
        name2 = "BOT";
        if (name1.equals(""))
            Toast.makeText(getApplicationContext(), "Enter Valid Name", Toast.LENGTH_SHORT).show();
        else {
            flag = 1;
            t1.setVisibility(View.INVISIBLE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(t1.getWindowToken(), 0);
            Button b = (Button) findViewById(R.id.button);
            Switch sw = (Switch) findViewById(R.id.switch1);
            sw.setVisibility(View.INVISIBLE);
            b.setVisibility(View.INVISIBLE);
            cons.addView(singleplayer);
        }
    }

    public void home(View view) {
        Intent in = new Intent(SinglePlayer.this, MainActivity.class);
        startActivity(in);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (flag == 2||flag ==1) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                back(null);
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        else {
            home(null);
        }
        return true;
    }

    public void back(View view){
        Intent in = new Intent(SinglePlayer.this, SinglePlayer.class);
        startActivity(in);
    }

    void result() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String s = "";
                if (n >= 9) {
                    flag = 2;
                    s = "Match Draw";
                    MediaPlayer ring = MediaPlayer.create(SinglePlayer.this, R.raw.win2);
                    ring.start();
                } else {
                    if (player == 1) {
                        s = name1 + " Won";
                        MediaPlayer ring = MediaPlayer.create(SinglePlayer.this, R.raw.win0);
                        ring.start();
                    }
                    else if (player == 2) {
                        s = name2 + " Won";
                        MediaPlayer ring = MediaPlayer.create(SinglePlayer.this, R.raw.win1);
                        ring.start();
                    }
                }
                setContentView(R.layout.result);
                Vibrator vibrator;
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= 26) {
                    assert vibrator != null;
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                }
                t = (TextView) findViewById(R.id.restext);
                t.setText(s);
            }
        }, 500);
    }


    public class Singleplayer extends View {
        float[] xc, yc;
        public Bitmap bit;
        public Canvas mcan;
        int sw, sh, o11, o12, w;
        int[][] box;
        public Paint paint, mpaint;

        Singleplayer(Context context) {
            this(context, null);
        }

        public Singleplayer(Context context, AttributeSet attrs) {
            super(context);
            player = 1;
            box = new int[3][3];
            n = 0;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            sw = displayMetrics.widthPixels;
            sh = displayMetrics.heightPixels;
            int mColor = ResourcesCompat.getColor(getResources(), R.color.white, null);
            o11 = ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null);
            o12 = ResourcesCompat.getColor(getResources(), R.color.red, null);
            paint = new Paint();
            mpaint = new Paint();
            paint.setColor(mColor);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(20);

            mpaint.setColor(o11);
            mpaint.setAntiAlias(true);
            mpaint.setStyle(Paint.Style.STROKE);
            mpaint.setStrokeJoin(Paint.Join.ROUND);
            mpaint.setStrokeCap(Paint.Cap.ROUND);
            mpaint.setStrokeWidth(15);
        }

        protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
            super.onSizeChanged(width, height, oldWidth, oldHeight);
            bit = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mcan = new Canvas(bit);
        }

        @SuppressLint("DrawAllocation")
        public void onDraw(final Canvas can) {
            super.onDraw(can);
            if (bit != null)
                can.drawBitmap(bit, 0, 0, null);
            xc = new float[4];
            yc = new float[4];
            w = (sw - 400) / 3;
            int y = (sh - sw + 400) / 2;
            for (int i = 0; i < 4; i++) {
                xc[i] = 200 + i * w;
                yc[i] = y + i * w;
            }
            for (int i = 1; i < 3; i++) {
                can.drawLine(200, y + i * w, 200 + 3 * w, y + i * w, paint);
                can.drawLine(200 + i * w, y, 200 + i * w, y + 3 * w, paint);
            }
            if (n == 0 && check==0) {
                player = 2;
                makeanymove();
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouchEvent(MotionEvent event) {
            float px = event.getX();
            float py = event.getY();
            int x = 10, y = 10;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                for (int i = 0; i < 3; i++) {
                    if (xc[i] < px && px < xc[i + 1]) {
                        x = i;
                        break;
                    }
                }
                for (int i = 0; i < 3; i++) {
                    if (yc[i] < py && py < yc[i + 1]) {
                        y = i;
                        break;
                    }
                }
                if (x < 3 && y < 3)
                    play(x, y);
            }
            return false;
        }

        void play(int x, int y) {

            if (box[x][y] != 1 && box[x][y] != 2) {
                player = 1;
                mpaint.setColor(o11);
                drawplay(x,y);
                box[x][y] = 1;
                n++;
                MediaPlayer ring = MediaPlayer.create(SinglePlayer.this, R.raw.sound);
                ring.start();
                if(win(x,y)){
                    n=0;
                    result();
                }
                if (n != 9) {
                    player = 2;
                    mpaint.setColor(o12);
                    int[][] b = new int[3][3];
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            b[i][j] = box[i][j];
                        }
                    }
                    int[] m = findBestMove(b);
                    int i = m[0];
                    int j = m[1];
                    box[i][j] = 2;
                    drawplay(i,j);
                    ring = MediaPlayer.create(SinglePlayer.this, R.raw.sound);
                    ring.start();
                    if(win(i, j)){
                        n=0;
                        result();
                    }
                    n++;
                }

                if(n==9)
                    result();

                invalidate();
            }
        }

        Boolean isMovesLeft(int b[][]) {
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (b[i][j] == 0)
                        return true;
            return false;
        }

        int evaluate(int b[][]) {
            for (int row = 0; row < 3; row++) {
                if (b[row][0] == b[row][1] &&
                        b[row][1] == b[row][2]) {
                    if (b[row][0] == player)
                        return +10;
                    else if (b[row][0] == player - 1)
                        return -10;
                }
            }

            for (int col = 0; col < 3; col++) {
                if (b[0][col] == b[1][col] &&
                        b[1][col] == b[2][col]) {
                    if (b[0][col] == player)
                        return +10;

                    else if (b[0][col] == player - 1)
                        return -10;
                }
            }

            if (b[0][0] == b[1][1] && b[1][1] == b[2][2]) {
                if (b[0][0] == player)
                    return +10;
                else if (b[0][0] == player - 1)
                    return -10;
            }

            if (b[0][2] == b[1][1] && b[1][1] == b[2][0]) {
                if (b[0][2] == player)
                    return +10;
                else if (b[0][2] == player - 1)
                    return -10;
            }

            return 0;
        }


        int minimax(int b[][], int depth, Boolean isMax) {
            int score = evaluate(b);
            if (score == 10)
                return score;

            if (score == -10)
                return score;

            if (!isMovesLeft(b))
                return 0;

            if (isMax) {
                int best = -1000;


                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (b[i][j] == 0) {
                            b[i][j] = player;
                            best = Math.max(best, minimax(b, depth + 1, !isMax));
                            b[i][j] = 0;
                        }
                    }
                }
                return best;
            } else {
                int best = 1000;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (b[i][j] == 0) {
                            b[i][j] = player - 1;
                            best = Math.min(best, minimax(b, depth + 1, !isMax));
                            b[i][j] = 0;
                        }
                    }
                }
                return best;
            }
        }

        int[] findBestMove(int b[][]) {
            int bestVal = -1000;
            int[] m = {-1, -1};

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (b[i][j] == 0) {
                        b[i][j] = player;
                        int moveVal = minimax(b, 0, false);
                        b[i][j] = 0;
                        if (moveVal > bestVal) {
                            m[0] = i;
                            m[1] = j;
                            bestVal = moveVal;
                        }
                    }
                }
            }

            return m;
        }

        Boolean makeanymove() {
            Random rnd = new Random();
            int i, j;
            i = 10;
            j = 10;
            do {
                i = rnd.nextInt(3);
                j = rnd.nextInt(3);
            } while (box[i][j] != 0);
            if (i != 10 || j != 10) {
                mpaint.setColor(o12);
                drawplay(i,j);
                box[i][j] = 2;
                n++;
                return true;
            }
            return false;
        }

        Boolean win(int x, int y) {
            int f = 0;
            if (box[x][0] == box[x][1] && box[x][1] == box[x][2]) {
                if (box[x][0] == player) {
                    mcan.drawLine((xc[x] + xc[x + 1]) / 2, yc[0], (xc[x] + xc[x + 1]) / 2, yc[3], mpaint);
                    f = 1;
                }

            }

            if (box[0][y] == box[1][y] &&
                    box[1][y] == box[2][y]) {
                if (box[0][y] == player) {
                    mcan.drawLine(xc[0], (yc[y] + yc[y + 1]) / 2, xc[3], (yc[y] + yc[y + 1]) / 2, mpaint);
                    f = 1;
                }
            }


            if (box[0][0] == box[1][1] && box[1][1] == box[2][2]) {
                if (box[0][0] == player) {
                    mcan.drawLine(xc[0], yc[0], xc[3], yc[3], mpaint);
                    f = 1;
                }

            }

            if (box[0][2] == box[1][1] && box[1][1] == box[2][0]) {
                if (box[0][2] == player) {
                    mcan.drawLine(xc[0], yc[3], xc[3], yc[0], mpaint);
                    f = 1;
                }

            }
            if(f==1)
                return true;
            return false;
        }
        public void drawplay(int x,int y){
            if(check == 0){
                if(player==1){
                    mcan.drawCircle((xc[x] + xc[x + 1]) / 2, (yc[y] + yc[y + 1]) / 2, (w / 2) - 50, mpaint);
                }
                else if(player==2){
                    mcan.drawLine(xc[x] + 60, yc[y] + 60, xc[x + 1] - 60, yc[y + 1] - 60, mpaint);
                    mcan.drawLine(xc[x] + 60, yc[y + 1] - 60, xc[x + 1] - 60, yc[y] + 60, mpaint);
                }
            }
            else if(check == 1){
                if(player==2){
                    mcan.drawCircle((xc[x] + xc[x + 1]) / 2, (yc[y] + yc[y + 1]) / 2, (w / 2) - 50, mpaint);
                }
                else if(player==1){
                    mcan.drawLine(xc[x] + 60, yc[y] + 60, xc[x + 1] - 60, yc[y + 1] - 60, mpaint);
                    mcan.drawLine(xc[x] + 60, yc[y + 1] - 60, xc[x + 1] - 60, yc[y] + 60, mpaint);
                }
            }
        }
    }
}
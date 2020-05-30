package com.example.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MultiPlayer extends AppCompatActivity {
    Multiplayer multiPlayer;
    ConstraintLayout cons;
    TextView t;
    String name1,name2;
    ArrayList<String> names = new ArrayList<>();
    int n,player,size,flag;
    ArrayList<Player> pdetail;

    static class Player implements Comparable<Player>{
        String pname;
        int pscore;
        public int getscore(){
            return pscore;
        }

        @Override
        public int compareTo(Player cplayer) {
            return this.pscore - cplayer.pscore;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);
        cons = (ConstraintLayout) findViewById(R.id.multic);
        multiPlayer = new Multiplayer(this);
        pdetail = new ArrayList<Player>();
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        size = prefs.getInt("size",0);
        flag = 0;
        for (int i=0;i<size;i++){
            Player pd = new Player();
            pd.pscore = prefs.getInt("score_"+i,0);
            pd.pname = prefs.getString("names_"+i,"null");
            pdetail.add(pd);
        }

    }

    public void recycle(View view){
        for (int i = 0;i<size;i++){
            String s = pdetail.get(i).pname + " : " + pdetail.get(i).pscore;
            s = s.toUpperCase();
            names.add(s);
        }
        flag = 1;
        setContentView(R.layout.recyclerview);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle);
        RecyclerAdapter adapter = new RecyclerAdapter(names,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void back(View view){
        Intent in = new Intent(MultiPlayer.this, MultiPlayer.class);
        startActivity(in);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (flag == 2||flag == 1 ||flag == 3) {
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

    public void play(View view){
        EditText t1 = (EditText) findViewById(R.id.editText);
        name1 = t1.getText().toString();
        EditText t2 = (EditText) findViewById(R.id.editText2);
        name2 = t2.getText().toString();
        if(name2.equals("") || name1.equals(""))
            Toast.makeText(getApplicationContext(),"Enter Valid Name",Toast.LENGTH_SHORT).show();
        else if(name2.compareTo(name1)==0)
            Toast.makeText(getApplicationContext(),"Enter Different Names",Toast.LENGTH_SHORT).show();
        else {
            flag = 3;
            Button btn = (Button) findViewById(R.id.button2);
            t1.setVisibility(View.INVISIBLE);
            t2.setVisibility(View.INVISIBLE);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(t1.getWindowToken(), 0);
            InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm1.hideSoftInputFromWindow(t2.getWindowToken(), 0);
            Button b = (Button) findViewById(R.id.button);
            b.setVisibility(View.INVISIBLE);
            btn.setVisibility(View.INVISIBLE);
            cons.addView(multiPlayer);
        }
    }
    public void home(View view){
        Intent in = new Intent(MultiPlayer.this, MainActivity.class);
        startActivity(in);
    }
    void result(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                flag =2;
                String s = "";
                if(n==9) {
                    s = "Match Draw";
                    s = s.toUpperCase();
                    MediaPlayer ring = MediaPlayer.create(MultiPlayer.this, R.raw.win2);
                    ring.start();
                }
                else{
                    if (player == 1) {
                        s = name1 + " Won";
                        s = s.toUpperCase();
                        Player pd = new Player();
                        pd.pname = name1;
                        pd.pname = pd.pname.toUpperCase();
                        int ind = -1;
                        for (int i =0;i<size;i++){
                            if(pdetail.get(i).pname.compareTo(pd.pname)==0) {
                                ind = i;
                                break;
                            }
                        }
                        if(ind == -1) {
                            pd.pscore = 1;
                            pdetail.add(pd);
                            size++;
                        }
                        else {
                            pd = pdetail.get(ind);
                            int sc = pd.pscore;
                            sc++;
                            pdetail.remove(ind);
                            pd.pscore = sc;
                            pdetail.add(pd);
                        }
                        Collections.sort(pdetail,Collections.<Player>reverseOrder());
                        MediaPlayer ring = MediaPlayer.create(MultiPlayer.this, R.raw.win0);
                        ring.start();
                    }
                    else if (player == 2) {
                        s = name2 + " Won";
                        s = s.toUpperCase();
                        Player pd = new Player();
                        pd.pname = name2;
                        pd.pname = pd.pname.toUpperCase();
                        int ind = -1;
                        for (int i =0;i<size;i++){
                            if(pdetail.get(i).pname.compareTo(pd.pname)==0) {
                                ind = i;
                                break;
                            }
                        }
                        if(ind == -1) {
                            pdetail.add(pd);
                            size++;
                        }
                        else {
                            pd = pdetail.get(ind);
                            int sc = pd.pscore;
                            sc++;
                            pd.pscore = sc;
                            pdetail.add(ind,pd);
                        }
                        Collections.sort(pdetail,Collections.<Player>reverseOrder());
                        MediaPlayer ring = MediaPlayer.create(MultiPlayer.this, R.raw.win1);
                        ring.start();
                    }
                }
                n=0;
                setContentView(R.layout.result);
                Vibrator vibrator;
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= 26) {
                    assert vibrator != null;
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                }
                t = (TextView) findViewById(R.id.restext);
                t.setText(s);
                share();
            }
        }, 500);

    }
    void share(){
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("size",size);
        for(int i=0;i<size;i++){
            editor.putString("names_"+i,pdetail.get(i).pname);
            editor.putInt("score_"+i,pdetail.get(i).pscore);
            editor.apply();
        }

    }

    public class Multiplayer extends View {
        float[] xc, yc;
        public Bitmap bit;
        public Canvas mcan;
        int sw, sh, o11, o12, w;
        int[][] box;
        public Paint paint, mpaint;

        Multiplayer(Context context) {
            this(context, null);
        }

        public Multiplayer(Context context, AttributeSet attrs) {
            super(context);
            player=1;
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
                int c1,c2,c3,c4;
                if (player == 1) {
                    mpaint.setColor(o11);
                    mcan.drawLine(xc[x] + 60, yc[y] + 60, xc[x + 1] - 60, yc[y + 1] - 60, mpaint);
                    mcan.drawLine(xc[x] + 60, yc[y + 1] - 60, xc[x + 1] - 60, yc[y] + 60, mpaint);
                    box[x][y] = 1;
                    n++;
                    MediaPlayer ring = MediaPlayer.create(MultiPlayer.this, R.raw.sound);
                    ring.start();
                    c1=checkh(x,y);
                    c2=checkv(x,y);
                    c3=checkdl(x,y);
                    c4=checkdr(x,y);
                    if(c1 == 2||c2 == 2||c3 == 2||c4 == 2){
                        result();
                    }
                    else
                        player++;
                } else {
                    mpaint.setColor(o12);
                    mcan.drawCircle((xc[x] + xc[x + 1]) / 2, (yc[y] + yc[y + 1]) / 2, (w / 2) - 50, mpaint);
                    box[x][y] = 2;
                    n++;
                    MediaPlayer ring = MediaPlayer.create(MultiPlayer.this, R.raw.sound);
                    ring.start();
                    c1=checkh(x,y);
                    c2=checkv(x,y);
                    c3=checkdl(x,y);
                    c4=checkdr(x,y);
                    if(c1 == 2||c2 == 2||c3 == 2||c4 == 2)
                    {
                        result();
                    }
                    else
                        player--;
                }
                invalidate();
                if(n==9)
                    result();
            }
        }
        int checkh(int x,int y){
            int c1=0;
            if(x<2){
                int i=x+1;
                do{
                    if(box[i][y]==player){
                        c1++;
                    }
                    i++;
                }while(i<3);
            }
            if(x>0){
                int i=x-1;
                do{
                    if(box[i][y]==player){
                        c1++;
                    }
                    i--;
                }while(i>=0);
            }
            if(c1==2){
                mcan.drawLine(xc[0],(yc[y]+yc[y+1])/2,xc[3],(yc[y]+yc[y+1])/2,mpaint);
            }
            return c1;
        }
        int checkv(int x,int y){
            int c2=0;
            if(y<2){
                int i=y+1;
                do{
                    if(box[x][i]==player){
                        c2++;
                    }
                    i++;
                }while(i<3);
            }
            if(y>0){
                int i=y-1;
                do{
                    if(box[x][i]==player){
                        c2++;
                    }
                    i--;
                }while(i>=0);
            }
            if(c2==2){
                mcan.drawLine((xc[x]+xc[x+1])/2,yc[0],(xc[x]+xc[x+1])/2,yc[3],mpaint);
            }
            return c2;
        }
        int checkdl(int x,int y){
            int c3=0;
            if(x==y) {
                if (x < 2) {
                    int i =x+1;
                    do{
                        if(box[i][i]==player)
                            c3++;
                        i++;
                    }while (i<3);
                }
                if(x>0){
                    int i=x-1;
                    do{
                        if(box[i][i]==player)
                            c3++;
                        i--;
                    }while (i>=0);
                }
                if(c3==2){
                    mcan.drawLine(xc[0],yc[0],xc[3],yc[3],mpaint);
                }
            }
            return c3;
        }
        int checkdr(int x,int y){
            int c4=0;
            if(x+y==2){
                if(x>0){
                    int i =x-1;
                    do{
                        if(box[i][2-i]==player){
                            c4++;
                        }
                        i--;
                    }while (i>=0);
                }
                if(x<2){
                    int i=x+1;
                    do{
                        if(box[i][2-i]==player)
                            c4++;
                        i++;
                    }while (i<3);
                }
                if(c4==2){
                    mcan.drawLine(xc[0],yc[3],xc[3],yc[0],mpaint);
                }
            }
            return c4;
        }
    }
}


package com.effy.whac_a_mole;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private Button start,play_again,menu;
    private GridView field;
    private int score,time,count,b_score;
    private TextView scoreText,timerText,your_score,best_score,your_best_score;
    private List<Integer> fieldsNumbers;
    private SharedPreferences data_app;
    private Timer timer;
    private Random rand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        init();
        load_data();
        startGame();
    }

    public void init(){
        fieldsNumbers  = new ArrayList<>(8);
        timer = new Timer();
        rand = new Random();
        data_app = getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        time = 30;
        count = 0;
    }

    public void load_data(){
        b_score = data_app.getInt("best",0);
    }

    public void save_data(){
        data_app.edit().putInt("best",b_score).apply();
    }


    public void startGame(){
        your_best_score = findViewById(R.id.your_best_score);
        your_best_score.setText("Your best score: "+b_score);
        start = findViewById(R.id.start);
        start.setOnClickListener(v -> {
            setContentView(R.layout.game);//переключаю на игровой экран
            field = (GridView) findViewById(R.id.field);
            scoreText = findViewById(R.id.score);
            timerText = (TextView) findViewById(R.id.timer);
            generateField();//метод генирации игрового поля и установки счётчика
            field.setOnItemClickListener((parent, view, position, id) -> {//если элемент GridView на который нажал игрок содержит единицу (суслика) то наращиваю счётчик очков
                if(fieldsNumbers.get(position) == 1){
                    score++;
                    runOnUiThread(() -> scoreText.setText("SCORE: "+ score));
                }
            });
        });
    }

    public void generateField(){
        timer.scheduleAtFixedRate(new TimerTask() {//каждые пол секунды генерирует новую позицию суслика
        @Override
            public void run() {
                count++;// количество генераций суслика
                if(count%2 == 0){//каждые две генерации (что соотвествует 1 секунде) уменьшаю количество таймера на 1
                    time--;
                }
                fieldsNumbers.clear();
                for(int i = 0;i<9;i++){//перед генерацией заполняю поле нулями
                    fieldsNumbers.add(0);
                }
                fieldsNumbers.set(rand.nextInt(8),1);//в случайно позиции от 0 до 8 генерирую одного суслика
                runOnUiThread(() -> {
                    field.setAdapter(new FieldAdapter(MainActivity.this, fieldsNumbers));
                    timerText.setText(time+"");
                });
            if(time == 0) {
                timer.cancel();
                timer.purge();
                time = 30;
                try {
                    Thread.sleep(1000);//небольшая задержка перед переходом на послеигровой экран
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> afterGame());
            }
            }
        }, 0, 500);
    }

    private void afterGame() {
        setContentView(R.layout.after_game);
        your_score = findViewById(R.id.your_score);
        your_score.setText("YOUR SCORE: "+score);
        if(score>b_score){
            b_score = score;
        }
        best_score = findViewById(R.id.best_score);
        best_score.setText("Best score: "+b_score);
        save_data();
        play_again = findViewById(R.id.play_again);
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(v -> {
            setContentView(R.layout.activity_main);
                startGame();
        });
        play_again.setOnClickListener(v -> {
            setContentView(R.layout.game);
            field = (GridView) findViewById(R.id.field);
            scoreText = findViewById(R.id.score);
            timerText = (TextView) findViewById(R.id.timer);
            generateField();
            field.setOnItemClickListener((parent, view, position, id) -> {
                if(fieldsNumbers.get(position) == 1){
                    score++;
                    runOnUiThread(() -> scoreText.setText("SCORE: "+ score));
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        save_data();
    }
}
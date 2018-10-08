package com.example.prabhatkushwaha.ss;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Quiz_main_window extends AppCompatActivity {
    ViewPager viewPager1;

    Map<String, Map<String, String>> hashMapHashMap;
    int layout[] = {R.layout.page, R.layout.page, R.layout.page, R.layout.page, R.layout.page, R.layout.page, R.layout.page, R.layout.page, R.layout.page, R.layout.page};
    String names[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    Button next, submit;
    Map<String,String>  rightmap;
    Map<String, String> ansMap;

    ArrayList<String> qns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main_window);
        Bundle b = getIntent().getExtras();
        String title = (String) b.get("quiztype");
        hashMapHashMap = (Map<String, Map<String, String>>) b.get("hashmap");
        rightmap=new HashMap<>();
        ansMap = new HashMap<>();
        ImageView imageView = findViewById(R.id.img);
        TextView header = findViewById(R.id.head);
        qns = new ArrayList<>();
        for (String q1 : hashMapHashMap.keySet()) {
            qns.add(q1);
        }


        if (title.equals("mathsQuiz")) {
            header.setText(title);
            Drawable d = getResources().getDrawable(R.drawable.math);
            imageView.setBackground(d);

        }
        if (title.equals("logicalQuiz")) {
            header.setText(title);
            Drawable d = getResources().getDrawable(R.drawable.logical);
            imageView.setBackground(d);

        }

        if (title.equals("biologyQuiz")) {
            header.setText(title);
            Drawable d = getResources().getDrawable(R.drawable.bio);
            imageView.setBackground(d);

        }
        if (title.equals("chemistryQuiz")) {
            header.setText(title);
            Drawable d = getResources().getDrawable(R.drawable.chem);
            imageView.setBackground(d);
        }

        next = findViewById(R.id.nextbutton);
        submit = findViewById(R.id.submitButton);

        viewPager1 = findViewById(R.id.viewpager);
        viewpageadaptar viewpageadaptar = new viewpageadaptar(this, layout, names, hashMapHashMap);
        viewPager1.setAdapter(viewpageadaptar);

        viewPager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < 9) {
                    next.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                } else {

                    next.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();

                bundle.putSerializable("ansMap", (Serializable) ansMap);
                bundle.putSerializable("rightMap", (Serializable) rightmap);

                Intent intent=new Intent(Quiz_main_window.this,Quiz_Result.class);
                intent.putExtra("score",String.valueOf(getResult()));
                intent.putExtras(bundle);

                startActivity(intent);

            }
        });

    }


    public int getResult() {
        int score = 0;
        for (String question : qns) {
            String userAns = ansMap.get(question);
            String actualAns = hashMapHashMap.get(question).get("ans");
            rightmap.put(question,actualAns);
            if (userAns != null && actualAns != null && userAns.equals(actualAns)) {
                score++;
            }
        }
        return score;
    }

    public void saveUserAnswer(String question, String anwser) {
        ansMap.put(question, anwser);

    }


}


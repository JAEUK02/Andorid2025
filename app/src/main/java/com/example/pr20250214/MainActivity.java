package com.example.pr20250214;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int counter=0;
    Handler handle = new Handler(Looper.myLooper());
    TextView[] box;
    // 중복되지 않는 random수 10개 만들기(arr)
    ArrayList<Integer> arr = new ArrayList<Integer>();  // arr = []

    String gtext = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Thread 발생
        Thread thr = new Thread("test") {
            @Override
            public void run() {
                for(;;) {
                    counter++;

                    if(counter == 5) {
                        // main thread에게 명령어를 보낸다.
                        handle.post(new Runnable() {
                            @Override
                            public void run() {
                                // main thread가 실행해야 할 내용
                                for(int i=0; i<box.length; i++) {
                                    box[i].setText("");
                                }
                            }
                        });
                    }


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        };
        thr.start();



        // LinearLayout 사이즈 정하기
        LinearLayout.LayoutParams size = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        size.setMargins(10,10,10,10);



        //0부터 9까지의 중복되지 않는 난수 10개를 만들자
        while(arr.size()<10) {
            int rd = (int)(Math.random()*10);
            boolean which = true;   // which가 true면 arr에 rd가 없다.
            for(int i=0; i<arr.size(); i++) {
                if(arr.get(i) == rd) {
                    which = false;
                }
            }
            if(which) {
                arr.add(rd);
            }
        }


        // 버튼크기 정하기
        LinearLayout.LayoutParams boxsize = new LinearLayout.LayoutParams(150,150);
        boxsize.setMargins(10,10,10,10);


        // 검정버튼 TextView 10개 만들기
        box = new TextView[10];   // box = [ instance, instance, instance .... ]
        for(int i=0; i<10; i++) {
            box[i] = new TextView(this);
            box[i].setText(arr.get(i) + "");
            box[i].setLayoutParams(boxsize);
            box[i].setTextSize(40.0f);
            box[i].setGravity(View.TEXT_ALIGNMENT_GRAVITY);
            box[i].setTextColor(Color.WHITE);
            box[i].setBackgroundColor(Color.BLACK);
            box[i].setId(i);
        }

        // XML에서 최상위 LinearLayout 가져오기
        LinearLayout tlinear = (LinearLayout) findViewById(R.id.ll1);

        for(int k=0; k<10; k++) {
            // LinearLayout 새롭게 만들기
            LinearLayout linear = new LinearLayout(this);
            // linear에 size 적용하기
            linear.setLayoutParams(size);

            int pos = (int) (Math.random() * 5);   // 0부터 5까지(5 포함 안됨)의 난수
            for (int i = 0; i < 5; i++) {
                if (i == pos) {
                    linear.addView(box[k]);
                    continue;
                }

                TextView blank0 = new TextView(this);
                blank0.setLayoutParams(boxsize);
                blank0.setText("");
                blank0.setTextSize(40.0f);
                blank0.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                blank0.setTextColor(Color.WHITE);
                linear.addView(blank0);
            }

            // tlinear에 linear에 삽입
            tlinear.addView(linear);
        }
        boxtouch btc = new boxtouch();
        for(int i = 0; i<box.length;i++){
            box[i].setOnTouchListener(btc);
        }

    }
    class boxtouch implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent ev) {
            TextView tv1 = (TextView) findViewById(R.id.tv1);
            if (ev.getAction() == 0) {
                box[v.getId()].setVisibility(View.INVISIBLE);
                gtext += arr.get(v.getId()) + "";
                tv1.setText(gtext);


                if (gtext.length() == 10) {
                    if (gtext.equals("0123456789")) {
                        tv1.setText("성공");
                    } else {
                        tv1.setText("fail");
                    }
                }
            }
            return true;
        }
    }
}

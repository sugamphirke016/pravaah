package com.example.pravaah.services;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.pravaah.R;

import java.util.logging.Logger;

public class PomodoroFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView progressText;
    private TextView pomodoroName;
    private TextView pomodoroCountDownTimer;
    private CountDownTimer countDownTimer;
    private TextView progressStatus;
    private TextView progressLaps;
    private ImageButton startBtn, resetBtn, exitBtn, completedBtn;
    long totalTimeInMillis = 2*60*1000;
    private int lapsElapsed = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pomodoro, container, false);

        progressBar = view.findViewById(R.id.progress_Bar);
        progressText = view.findViewById(R.id.progress_text);
        pomodoroName = view.findViewById(R.id.pomodoro_name);
        pomodoroCountDownTimer = view.findViewById(R.id.progress_countdown_timer);
        progressStatus = view.findViewById(R.id.progress_status);
        progressLaps = view.findViewById(R.id.progress_laps);
        startBtn = view.findViewById(R.id.startBtn);
        resetBtn = view.findViewById(R.id.resetBtn);
        exitBtn = view.findViewById(R.id.exitBtn);
        completedBtn = view.findViewById(R.id.completedBtn);

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

        Spannable text1 = new SpannableString("PO");
        text1.setSpan(new ForegroundColorSpan(Color.parseColor("#4285F4")), 0, text1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append(text1);

        Spannable text2 = new SpannableString("MO");
        text2.setSpan(new ForegroundColorSpan(Color.parseColor("#DB4437")), 0, text2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append(text2);

        Spannable text3 = new SpannableString("DO");
        text3.setSpan(new ForegroundColorSpan(Color.parseColor("#F4B400")), 0, text3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append(text3);

        Spannable text4 = new SpannableString("RO");
        text4.setSpan(new ForegroundColorSpan(Color.parseColor("#0F9D58")), 0, text4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append(text4);

        pomodoroName.setText(stringBuilder, TextView.BufferType.SPANNABLE);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation bulgeAnimation = AnimationUtils.loadAnimation(v.getContext(), R.anim.bulge_animation);
                v.startAnimation(bulgeAnimation);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation returnAnimation = AnimationUtils.loadAnimation(v.getContext(), R.anim.return_animation);
                        v.startAnimation(returnAnimation);
                    }
                }, 100);
                startCountdown(totalTimeInMillis);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation bulgeAnimation = AnimationUtils.loadAnimation(v.getContext(), R.anim.bulge_animation);
                v.startAnimation(bulgeAnimation);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation returnAnimation = AnimationUtils.loadAnimation(v.getContext(), R.anim.return_animation);
                        v.startAnimation(returnAnimation);
                    }
                }, 100);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation bulgeAnimation = AnimationUtils.loadAnimation(v.getContext(), R.anim.bulge_animation);
                v.startAnimation(bulgeAnimation);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation returnAnimation = AnimationUtils.loadAnimation(v.getContext(), R.anim.return_animation);
                        v.startAnimation(returnAnimation);
                    }
                }, 100);
            }
        });

        completedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation bulgeAnimation = AnimationUtils.loadAnimation(v.getContext(), R.anim.bulge_animation);
                v.startAnimation(bulgeAnimation);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation returnAnimation = AnimationUtils.loadAnimation(v.getContext(), R.anim.return_animation);
                        v.startAnimation(returnAnimation);
                    }
                }, 100);
            }
        });

        return view;
    }

    private void startCountdown(long durationInMillis) {
        countDownTimer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long l) {
                int minutes = (int) (l / 1000) / 60;
                int seconds = (int) (l / 1000) % 60;
                String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                pomodoroCountDownTimer.setText(timeLeftFormatted);
                int progress = (int) (((durationInMillis - l) * 100) / durationInMillis);
                progressBar.setProgress(progress);
                progressText.setText(progress + "%");
            }

            @Override
            public void onFinish() {
                Log.d("TIMER", ""+lapsElapsed+ progressStatus.getText());
                Log.d("totaltimeinmillis", durationInMillis+"");
                if (durationInMillis == (2 * 60 * 1000)) {
                    progressStatus.setText("REST");
                    if (lapsElapsed < 2)
                        startCountdown(1 * 60 * 1000);
                    else countDownTimer.cancel();
                } else {
                    progressStatus.setText("FOCUS");
                    lapsElapsed++;
                    progressLaps.setText(""+lapsElapsed);
                    if (lapsElapsed < 2)
                        startCountdown(2 * 60 * 1000);
                    else countDownTimer.cancel();
                }
            }
        }.start();
    }
}

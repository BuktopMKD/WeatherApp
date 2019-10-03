package com.musala_tech.weatherapp.common;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.musala_tech.weatherapp.util.InputViewHandler;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();

            if (view instanceof EditText) {
                InputViewHandler.showCursor(view);
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    view.clearFocus();
                    InputViewHandler.hideKeyboard(view);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    protected abstract void setupActivityComponent();
}

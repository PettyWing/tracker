package com.example.tracker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ViewClickedEventListener listener;
    EditText editText;
    LinearLayout layout, rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        layout = findViewById(R.id.layout);
        rootView = findViewById(R.id.rootView);
        listener = new ViewClickedEventListener();
        MyButton myButton = findViewById(R.id.button1);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "shdsa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listener.setActivityTracker(this);
    }

    private static final String TAG = "MainActivity";

    public void testClick(View view) {
        for (int i = 0; i < 3; i++) {
            TextView textView = new TextView(this);
            textView.setText("aaa");
            textView.setClickable(true);
            layout.addView(textView);
        }

        Log.d(TAG, "testClick: ");
    }


}

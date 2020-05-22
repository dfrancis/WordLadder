package com.google.engedu.wordladder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.Log;

import java.util.ArrayList;

public class SolverActivity extends AppCompatActivity {

    private ArrayList<EditText> editTextArray = new ArrayList<>();
    private String[] words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_solver);
        TextView startTextView = (TextView) findViewById(R.id.startTextView);
        TextView endTextView = (TextView) findViewById(R.id.endTextView);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action)) {
            Bundle bundle = intent.getExtras();
            words = bundle.getStringArray("com.google.engedu.wordladder.words");

            startTextView.setText(words[0]);
            endTextView.setText(words[words.length - 1]);
            RelativeLayout smvrl = findViewById(R.id.solverMiddleView);

            for (int idx = 0; idx < words.length - 1; ++idx) {
                EditText editText = new EditText(this);
                editText.setId(idx);
                Log.d("Word ladder", "idx= " + idx + " Initial ID: " + editText.getId());

                RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                //RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                //        RelativeLayout.LayoutParams.WRAP_CONTENT,
                //        RelativeLayout.LayoutParams.WRAP_CONTENT);
                if (idx == 0) {
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                } else {
                    int viewId = editTextArray.get(idx - 1).getId();
                    Log.d("Word ladder", "viewId = " + viewId);
                    rlp.addRule(RelativeLayout.BELOW, viewId);
                }

                editText.setLayoutParams(rlp);
                smvrl.addView(editText);
                editTextArray.add(editText);
            }
        }
    }

    public boolean onSolve(View view) {
        for (int idx = 0; idx < words.length - 2; ++idx) {
            EditText editText = editTextArray.get(idx + 1);
            editText.setText(words[idx + 1]);
        }
        return true;
    }
}

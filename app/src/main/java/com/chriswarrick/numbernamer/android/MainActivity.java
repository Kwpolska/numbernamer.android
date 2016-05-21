package com.chriswarrick.numbernamer.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chriswarrick.numbernamer.EnglishNumberNamer;
import com.chriswarrick.numbernamer.GermanNumberNamer;
import com.chriswarrick.numbernamer.NumberNamer;
import com.chriswarrick.numbernamer.PolishNumberNamer;

import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    protected NumberNamer namer;
    protected boolean hasNamer = false;
    TextView outputView;
    EditText numberField;
    Random r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = (Spinner) findViewById(R.id.languageSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String lang = (String) adapterView.getItemAtPosition(i);
                switch (lang) {
                    case "English":
                        namer = new EnglishNumberNamer();
                        break;
                    case "Polski":
                        namer = new PolishNumberNamer();
                        break;
                    case "Deutsch":
                        namer = new GermanNumberNamer();
                        break;
                }
                hasNamer = true;
                run();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                hasNamer = false;
                run();
            }
        });
        namer = new EnglishNumberNamer();
        hasNamer = true;
        r = new Random();
        outputView = (TextView) findViewById(R.id.outputView);
        numberField = (EditText) findViewById(R.id.numberField);
        numberField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                run();
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.randomAction:
                long l = r.nextLong();
                while (l == namer.MIN) l = r.nextLong();
                numberField.setText(String.format(Locale.US, "%d", l));
                return true;
            case R.id.aboutAction:
                Intent i = new Intent(this, AboutActivity.class);
                this.startActivity(i);
        }
        return false;
    }

    public void run() {
        if (!hasNamer) {
            outputView.setText("");
            return;
        }
        Editable e = numberField.getText();
        try {
            long number = Long.valueOf(e.toString().trim());
            outputView.setText(namer.name(number));
        } catch (NumberFormatException ex) {
            if (e.toString().trim().equals("")) {
                outputView.setText("");
            } else {
                outputView.setText(R.string.invalidNumber);
            }
        }
    }
}

package de.diercks.ichhabnochnie;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    RelativeLayout mainLayout;
    TextView question;
    Button buttonWeb;
    Button buttonFile;
    int i = 0;
    ArrayList<String> questions;
    boolean shuffeled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processUI();

    }

    private void processUI() {

        questions = new ArrayList<>();

        LoadQuestions load = new LoadQuestions(this);

        this.mainLayout = (RelativeLayout) findViewById(R.id.mainlayout);
        this.question = (TextView) findViewById(R.id.question);
        this.buttonWeb = (Button) findViewById(R.id.buttonWeb);
        this.buttonFile = (Button) findViewById(R.id.buttonFile);

        this.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (questions.size() > 0 && !shuffeled) {
                    Collections.shuffle(questions);
                    shuffeled = true;
                    buttonFile.setVisibility(View.GONE);
                    buttonWeb.setVisibility(View.GONE);
                } else if (questions.size() == 0) {
                    return;
                }
                question.setText(String.valueOf(questions.get(i)));
                if (i + 1 == questions.size()) {
                    question.setText(questions.get(i) + "\n \n Letzte Frage!");
                    i = 0;
                } else {
                    i++;
                }
            }
        });

        this.buttonWeb.setOnClickListener((v) -> {
            if (questions.size() == 0)
                new Thread(load).start();
            if (questions.size() != 0) {
                buttonFile.setVisibility(View.GONE);
                buttonWeb.setVisibility(View.GONE);
            }
        });
        this.buttonFile.setOnClickListener((v -> {
            if (questions.size() == 0)
                loadFileQuestions(mainLayout.getContext(), "questions.txt");

        }));

    }

    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody) {
        File dir = new File(getFilesDir(), "ichhabnochnie");
        if (!dir.exists()) {
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFileQuestions(Context mcoContext, String sFileName) {
        File dir = new File(getFilesDir(), "ichhabnochnie");
        if (!dir.exists()) {
            this.question.setText("Es wurden keine Fragen gespeichert");
            return;
        }

        try {
            File gpxfile = new File(dir, sFileName);
            if (!gpxfile.exists()) {
                this.question.setText("Es wurden keine Fragen gespeichert");
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(gpxfile));
            String fi;
            while ((fi = reader.readLine()) != null) {
                questions.add(fi);
            }
            this.question.setText("Fragen geladen");

            buttonFile.setVisibility(View.GONE);
            buttonWeb.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
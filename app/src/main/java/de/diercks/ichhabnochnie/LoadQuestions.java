package de.diercks.ichhabnochnie;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LoadQuestions implements Runnable {

    MainActivity main;

    public LoadQuestions(MainActivity main) {
        this.main = main;
    }

    @Override
    public void run() {
        String urlToRead = "https://www.warpixel.net/ihnn/questions.html";
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try {
            url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                main.questions.add(line);
            }
            rd.close();

            main.requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
            String body = "";
            for (String question : main.questions) {
                body += question + "\n";
            }
            main.writeFileOnInternalStorage(main.mainLayout.getContext(), "questions.txt", body);
            main.question.setText("Fragen Geladen");
        } catch (Exception e) {
            main.question.setText("Fehler");
            e.printStackTrace();
        }
    }


}

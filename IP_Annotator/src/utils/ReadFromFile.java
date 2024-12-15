package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ReadFromFile {

    String path;

    public ReadFromFile(String pth) {
        path = pth;
    }

    public String sentence(int num) {

        BufferedReader reader = null;
        String sentence = null;
        try {

            reader = new BufferedReader(new FileReader(path));

            int line = 0;

            while ((sentence = reader.readLine()) != null) {
                line++;
                if (num == line) {
                    reader.close();
                    break;
                }
            }
        } catch (Exception e) {

        }
        return sentence;
    }

    public List<String> sentences() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = null;
            String ls = System.getProperty("line.separator");

            try {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }

            } finally {
                reader.close();
            }
        } catch (Exception e) {

        }
        List<String> pureSent = new ArrayList<>();
        String[] sentences = stringBuilder.toString().split("\\.|:|\n|\\?");
        for (String sentence : sentences) {
            String sent = sentence.trim();
            if (!"".equals(sent)) {
                pureSent.add(sent + ".");
            }
        }
        return pureSent;
    }
}

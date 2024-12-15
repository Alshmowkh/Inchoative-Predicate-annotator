package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static utils.Miss.next;
import static utils.Miss.pl;

public class TreeBank {

    private final String folderPath = "F:\\Master\\Thesis\\Tools\\Dataset\\PADT_LDC\\data";

    private void readTreeBank1() {
        String afpPath = "AFP\\syntax";
        String filePath;// = "20000715_AFP_ARB.0002.fs";
        File[] folders = new File(folderPath + "\\" + afpPath).listFiles();
        filePath = folders[1].getPath();
        List<String> tokens = null;
        try {
            tokens = Files.lines(Paths.get(filePath)).filter(i -> i.startsWith("[")).collect(Collectors.toList());
        } catch (IOException ex) {
            Logger.getLogger(TreeBank.class.getName()).log(Level.SEVERE, null, ex);
        }

//        pl(tokens.size());
        List<List<String>> sentences = new ArrayList();
        List<String> sentence = new ArrayList();
        Iterator<String> it = tokens.iterator();
        String token = it.next();

        while (it.hasNext()) {
            sentence.add(token);
            while (it.hasNext() && (token = it.next()).charAt(1) != '#') {
                sentence.add(token);
//                pl(token);
            }
            sentences.add(sentence);
            sentence = new ArrayList();
        }
//        sentence = sentences.get(0);
//        String sent;
//        pl(sentences.get(5));
//        sentences.stream().flatMap(i->i.stream()).forEach(i->pl(i+"\n\n"));
//                sentences.stream().forEach(i->pl(i+"\n\n"));

        sentences.stream()
                .map(k -> k.stream()
                        .filter(j -> j.charAt(1) != '#')
                        .map(i -> i.substring(i.indexOf("ord=") + 4, i.indexOf("sentord=") - 1) + "\\" + i)
                        .sorted((String t1, String t2) -> Integer.parseInt(t1.substring(0, t1.indexOf("\\"))) - Integer.parseInt(t2.substring(0, t2.indexOf("\\"))))
                        .filter(i -> !i.split(",")[3].contains("="))
                        .map(i -> i.split(",")[3])
                        .reduce("", (t1, t2) -> t1 + " " + t2))
                .forEach(System.out::println);

//        sent = sentence.stream().filter(i -> i.charAt(1) != '#')
//                .map(i -> i.substring(i.indexOf("ord=") + 4, i.indexOf("sentord=") - 1) + "\\" + i)
//                .sorted((String t1, String t2) -> Integer.parseInt(t1.substring(0, t1.indexOf("\\"))) - Integer.parseInt(t2.substring(0, t2.indexOf("\\"))))
//                .filter(i -> !i.split(",")[4].contains("="))
//                .map(i -> i.split(",")[4])
//                .reduce("", (t1, t2) -> t1 + " " + t2);
//        pl(sent);
//                .forEach(i -> pl(i));
//        it = sentence.iterator();
//        StringBuilder sb = new StringBuilder();
////        for (String tok : tokens) {
//        it.next();
//        sentence = new ArrayList();
//        while (it.hasNext()) {
//            token = it.next();
//            token = token.substring(token.indexOf("ord=") + 4, token.indexOf("sentord=") - 1) + "\\" + token;
////            sb.append(token).append(" ");
//            sentence.add(token);
//
//        }
//        Collections.sort(sentence, (String t1, String t2) -> Integer.parseInt(t1.substring(0, t1.indexOf("\\"))) - Integer.parseInt(t2.substring(0, t2.indexOf("\\"))));
//        token = sentence.stream().filter(i -> !i.split(",")[4].contains("=")).map(i -> i.split(",")[4]).reduce("",(t1,t2)-> t1+" "+t2);
//      pl(token);
//        sentence.forEach(System.out::println);
//        pl(sb);
//            //            if (f.isFile()) {
//            //                filePath = f.getPath();
//            //                
//            //            }
//
//        }
    }

    private void readTreeBank1Copy() {
        String afpPath = "UMH\\syntax";
        String filePath = "20000715_AFP_ARB.0002.fs";
        File[] folders = new File(folderPath + "\\" + afpPath).listFiles();
        for (int f = 0; f < 133; f++) {
            if (folders[f].isDirectory()) {
                continue;
            }
            filePath = folders[f].getPath();
            List<String> tokens = null;
            try {
                tokens = Files.lines(Paths.get(filePath)).filter(i -> i.startsWith("[")).collect(Collectors.toList());
            } catch (IOException ex) {
                Logger.getLogger(TreeBank.class.getName()).log(Level.SEVERE, null, ex);
            }
            List<List<String>> sentences = new ArrayList();
            List<String> sentence = new ArrayList();
            Iterator<String> it = tokens.iterator();
            String token = it.next();

            while (it.hasNext()) {
                sentence.add(token);
                while (it.hasNext() && (token = it.next()).charAt(1) != '#') {
                    sentence.add(token);
                }
                sentences.add(sentence);
                sentence = new ArrayList();
            }
//            pl(sentences.size());
            sentences.stream().map(k -> k.stream()
                    .filter(j -> j.charAt(1) != '#')
                    .map(i -> i.substring(i.indexOf("ord=") + 4, i.indexOf("sentord=") - 1) + "\\" + i)
                    .sorted((String t1, String t2) -> Integer.parseInt(t1.substring(0, t1.indexOf("\\"))) - Integer.parseInt(t2.substring(0, t2.indexOf("\\"))))
                    .filter(i -> !i.split(",")[3].contains("="))
                    .map(i -> i.split(",")[3])
                    .reduce("", (t1, t2) -> t1 + " " + t2)
            ).forEach(System.out::println);
            next();
        }
    }

    void Xinhua_Dataset() {
        String newF = "F:\\Master\\Thesis\\Prototype\\Papers\\Inchoative and Predicate Annotation in Arabic Nominal Sentence\\Dataset\\Xinhua00.txt";
        String sourceFo = "F:\\Master\\Thesis\\Tools\\Dataset\\PADT_LDC\\data\\XIA\\corpus";
        String oldF = sourceFo + "\\XIA20030501.0081_story.sgm";
        File[] files = new File(sourceFo).listFiles(i -> i.isFile());
        int cnt = files.length;
//        pl(cnt);
        try {
            for (int i = 0; i < cnt; i++) {
                oldF = files[i].getPath();

                Stream strm = Files.lines(Paths.get(oldF));
                strm = strm.skip(7).filter(sr -> !sr.toString().startsWith("<"));
                Files.write(Paths.get(newF),  strm::iterator, StandardOpenOption.APPEND);
//                strm.forEach(Miss::pl); 
//                next();
                pl(i);
            }
        } catch (IOException ex) {
            Logger.getLogger(TreeBank.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    public static void main(String[] args) {
        TreeBank tb = new TreeBank();
        //        tb.readTreeBank1Copy();
//        tb.Xinhua_Dataset();
        
    }

}

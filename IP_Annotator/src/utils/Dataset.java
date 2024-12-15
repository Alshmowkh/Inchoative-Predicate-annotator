/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import static utils.Miss.p;
import static utils.Miss.pl;
import static utils.Miss.plist;

/**
 *
 * @author bakee
 */
public class Dataset {

    private final String pathDS = "F:\\Master\\Thesis\\Prototype\\Papers\\Inchoative and Predicate Annotation in Arabic Nominal Sentence\\Dataset\\PADT(AFP-UMH-XIA).txt";

    void statistics() throws IOException {
        Path path = Paths.get(pathDS);

        ////sentence count
//        pl(Files.lines(path).count());
        ////words acount
//        pl(Files.lines(path).flatMap(i-> Arrays.stream(i.split("\\s|\\t"))).count());
        ////words acount
//        pl(Files.lines(path).flatMap(i-> Arrays.stream(i.split("\\s|\\t"))).count());
    }

    void getAllWords() throws IOException {
        Path path = Paths.get(pathDS);
        ////words into file
        Stream strm = Files.lines(path).flatMap(i -> Arrays.stream(i.split("\\s|\\t"))).filter(i -> !i.trim().isEmpty());
        String wordsFile = new File(pathDS).getParent() + "\\wordsDS01.txt";
        Files.write(Paths.get(wordsFile), strm::iterator);
//        pl(wordsFile);

    }

    void reduceCorpus() {
        String oldF = "F:\\Master\\Thesis\\Prototype\\Papers\\Inchoative and Predicate Annotation in Arabic Nominal Sentence\\Dataset\\xmlMadaDS_Out.xml";
        String newF = "F:\\Master\\Thesis\\Prototype\\Papers\\Inchoative and Predicate Annotation in Arabic Nominal Sentence\\Dataset\\xmlMadaDS_Out04.xml";
        String line, temp;
        String Lines = null;
        StringBuilder lines = new StringBuilder();
        String svm;
        List list = new ArrayList();
        int i = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(oldF));
//            br.close();

//            line=br.readLine();
//            list.add(line);
////            i++;
//            br.readLine();
//            list.add(line);
////            i++;
            line = br.readLine();
            list.add(line + System.lineSeparator());
            i++;
//            Lines = line;

            while ((line = br.readLine()) != null) {
//                pl(line);
//                lines.append(lines);
//                sent=br.readLine();
//                line = br.readLine();
                i++;
                svm = line;
                if (svm.trim().startsWith("<svm_prediction>")) {
                    String morph = br.readLine();
                    i++;
                    String svmC = br.readLine();
                    i++;
                    line = br.readLine();
                    i++;
                    if (!line.trim().startsWith("<analysis rank=")) {
                        list.add(svm + System.lineSeparator());
                        list.add(morph + System.lineSeparator());
                        list.add(svmC + System.lineSeparator());
                    }
                }
//                line = br.readLine();
//                i++;
                list.add(line + System.lineSeparator());
//                Lines += line + System.lineSeparator();
            }
            br.close();
        } catch (Exception ex) {
            pl("error occured in line " + i);
        }
        pl(list.size());
        try {
            FileWriter fw = new FileWriter(newF);
            list.stream().forEach(j -> {
                try {
                    fw.write((String) j);
                } catch (IOException ex) {
                    Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
//            fw.write(list.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void trimSomeData() {
        String oldF = "F:\\Master\\Thesis\\Prototype\\Papers\\Inchoative and Predicate Annotation in Arabic Nominal Sentence\\Dataset\\xmlMadaDS_Out04.xml";
        String newF = "F:\\Master\\Thesis\\Prototype\\Papers\\Inchoative and Predicate Annotation in Arabic Nominal Sentence\\Dataset\\xmlMadaDS_Out05.xml";
        String line, analysis;
        try {
            FileWriter fw = new FileWriter(newF);
            BufferedReader br = new BufferedReader(new FileReader(oldF));

//            list.add(line + System.lineSeparator());
//            i++;
//            Lines = line;
//            line = br.readLine();
//            fw.write(line);
            while ((line = br.readLine()) != null) {
//                pl(line);
//                lines.append(lines);
//                sent=br.readLine();
//                line = br.readLine();
//                i++;
                analysis = line;
                if (analysis.trim().startsWith("<analysis rank=")) {
                    line = line.substring(0, line.indexOf("rank=") - 1) + ">";
                }
                fw.write(line + System.lineSeparator());
            }
            br.close();
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void madaOutFlat() {
        String oldF = "F:\\Master\\Thesis\\Prototype\\Papers\\Inchoative and Predicate Annotation in Arabic Nominal Sentence\\Dataset\\xmlMadaDS_Out05.xml";
        String newF = "F:\\Master\\Thesis\\Prototype\\Papers\\Inchoative and Predicate Annotation in Arabic Nominal Sentence\\Dataset\\PADTds_02.pds";
      String sid,wid,
                value,diac,gloss,
                pos,prc3,prc2,prc1,prc0,
                per,asp,vox,mod,gen,num,
                stt,cas,enc0,stem;
      
        String line, word;
        String[] morphs = new String[18];
        try {
            FileWriter fw = new FileWriter(newF);
            BufferedReader br = new BufferedReader(new FileReader(oldF));
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("<out_seg")) {
                    sid = line.substring(line.indexOf("=") + 6, line.lastIndexOf("\""));
//                    p("SENT:" + sid);
                    while ((line = br.readLine()) != null && !line.trim().startsWith("</out_seg>")) {
                        if (line.trim().startsWith("<word id")) {
                            wid = line.substring(line.indexOf("id=") + 4, line.indexOf("word=") - 2);
                            pl(sid + "-" + wid);
                            value = line.substring(line.indexOf("rd=") + 4, line.indexOf(">") - 1);
                            br.readLine();

                            line = br.readLine();

                            if (line.trim().startsWith("<morph")) {
                                if (!line.trim().contains("gloss=")) {
                                    word = line.substring(0, line.indexOf("pos="));
                                    word = word + "gloss=\"0\" ";
                                    word = word + line.substring(line.indexOf("pos="));
                                    line = word;
                                }
                                if (!line.trim().contains("stem=")) {
                                    word = line.substring(0, line.lastIndexOf("\""));
                                    word = word + "\" stem=\"0\"/>";
                                    line = word;
                                }
                                morphs = line.trim().split(" ");

//                                plist(Arrays.asList(morphs));
                                diac = morphs[1].split("\"")[1];
                                gloss = morphs[2].split("\"")[1];
                                pos = morphs[3].split("\"")[1];
                                prc3 = morphs[4].split("\"")[1];
                                prc2 = morphs[5].split("\"")[1];
                                prc1 = morphs[6].split("\"")[1];
                                prc0 = morphs[7].split("\"")[1];
                                per = morphs[8].split("\"")[1];
                                asp = morphs[9].split("\"")[1];
                                vox = morphs[10].split("\"")[1];
                                mod = morphs[11].split("\"")[1];
                                gen = morphs[12].split("\"")[1];
                                num = morphs[13].split("\"")[1];
                                stt = morphs[14].split("\"")[1];
                                cas = morphs[15].split("\"")[1];
                                enc0 = morphs[16].split("\"")[1];
                                stem = morphs[17].split("\"")[1];

                                word = sid + "-" + wid + " " + value
                                        + " " + diac + " " + gloss + " " + pos
                                        + " " + prc3 + " " + prc2 + " " + prc1 + " " + prc0
                                        + " " + per + " " + asp + " " + vox + " " + mod + " "
                                        + gen + " " + num + " " + stt + " " + cas + " "
                                        + enc0 + " " + stem;
                                fw.write(word + System.lineSeparator());
                            }
                        }
                    }
                }
            }
            br.close();
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void enterToOutSeg(int id) {
        String oldF = "F:\\Master\\Thesis\\Prototype\\Papers\\Inchoative and Predicate Annotation in Arabic Nominal Sentence\\Dataset\\xmlMadaDS_Out05.xml";
        String newF = "F:\\Master\\Thesis\\Prototype\\Papers\\Inchoative and Predicate Annotation in Arabic Nominal Sentence\\Dataset\\retrieval.xml";

    }

    public static void main(String[] args) throws IOException {
        Dataset ds = new Dataset();
        ds.enterToOutSeg(3);
        ds.madaOutFlat();
//        ds.trimSomeData();
//        ds.reduceCorpus();
//        ds.getAllWords();
//        ds.statistics();
    }
}

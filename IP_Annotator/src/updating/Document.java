/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updating;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static utils.Miss.pl;

/**
 *
 * @author bakee
 */
public class Document extends ArrayList<Sentence> {

    private final String sourceDS;

    private Document(String path) {
        sourceDS = path;
    }

    public static Document load(String pathFile) {
        Document doc = new Document(pathFile);
        Sentence sentence;
        List lines = new ArrayList();

        try {
            lines = Files.readAllLines(Paths.get(pathFile));
        } catch (IOException ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
        }
        Iterator<String> it = lines.iterator();
        String w1;
        String sid1, sid2;
        w1 = it.next();
        sid1 = w1.split("-")[0];
        lines = new ArrayList();
        while (it.hasNext()) {
            lines.add(w1);
            w1 = it.next();
            sid2 = w1.split("-")[0];
            while (it.hasNext() && sid1.equals(sid2)) {
                lines.add(w1);
                w1 = it.next();
                sid1 = w1.split("-")[0];
            }
            pl(sid1+"\t"+sid2);
            sentence = doc.sentenceFactory(lines);
            if (sentence != null) {
                doc.add(sentence);
            }
            lines = new ArrayList();
        }
        return doc;
    }

    private Sentence sentenceFactory(List lines) {
        Sentence sentence = new Sentence(lines);
        return sentence;
    }

}

package chunking;

import edu.columbia.ccls.madamira.configuration.OutSeg;
import inchoative_predicate.Letter;
import inchoative_predicate.Phrase;
import inchoative_predicate.Sentence;
import inchoative_predicate.Word;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import madamiray.Madamiray;
import utils.Miss;
import static utils.Miss.p;
import static utils.Miss.pl;
import utils.ReadFromFile;

public class Phrasing {

    void next() {
        try {
            char c = (char) System.in.read();
            if (c == 'q' || c == '0') {
                System.exit(1);
            }
        } catch (Exception e) {

        }

    }

    Sentence reIndexing(Sentence sentence) {
        for (Word w : sentence) {
            w.index = w.index() - 1;
        }
        sentence.count = sentence.count() - 1;
        return sentence;
    }

    Sentence normlizeI(Sentence words, int index) {
        Word fw = words.get(index);
        String valuef = fw.value().trim();
        List<String> apolishers = new Letter().abolishers();
        if (apolishers.contains(valuef) || fw.isConj() || fw.isPartNeg()) {
            words.remove(index);
            words = reIndexing(words);
            words = normlizeI(words, index);
        }
        if (fw.hasConj()) {
            fw.changeFeature("value", valuef.substring(1));

        }
        return words;
    }

    void ini() throws FileNotFoundException, IOException {
        String[] pathFile = {"./IP/article01.txt", "./IP/simple_corpus_NS.txt"};
        p("Enter sentence ID:");

        String sent = new Scanner(System.in).next();
        List<String> sentences = new ReadFromFile(pathFile[1]).sentences();
        Madamiray mada;
        OutSeg outseg;
        Sentence sentence;
        int s = Integer.parseInt(sent);
        boolean startWithZero = sent.startsWith("0");
        if (!startWithZero) {
            mada = new Madamiray(sentences.get(s - 1));
            outseg = mada.getMorpholizing().get(0);
            sentence = new Sentence(outseg);
            new Miss().printAllSeg(outseg);
            iniProcessing(sentence);
        } else {
            mada = new Madamiray(sentences.subList(s > 0 ? s - 1 : 0, sentences.size()));
            for (int i = 0; i < sentences.size(); i++) {
                outseg = mada.getMorpholizing().get(i);
//                new Miss().printAllSeg(outseg);
                sentence = new Sentence(outseg);
                iniProcessing(sentence);
                next();
            }
        }
    }

    void iniProcessing(Sentence sentence) {
        sentence = normlizeI(sentence, 0);
        Phrase phrase;
        int index = 0;
        while (index < sentence.count() - 1) {
            phrase = this.readPhrase2(sentence, index);
            index = phrase.get(phrase.size() - 1).index() + 1;
            pl(phrase);
        }

    }

    Phrase readPhrase(Sentence sentence, int index) {
        Phrase phrase = new Phrase();
        Word w1, w2;
        int i = index;
        boolean reduce = true;

        for (i = index; i < sentence.size() - 1 && reduce; i++) {
            w1 = sentence.get(i);
            w2 = sentence.get(i + 1);
            if (w1.isPrep() && !w1.hasEnc()
                    || w1.isPart()
                    || w1.isPartNeg()
                    || w1.isConj()
                    || w1.isInterrogation()
                    || w1.isPunc()
                    || w1.isRel()) {
                phrase.add(w1);
                i++;
                w1 = sentence.get(i);
            } else if (w1.hasPrc1()) {
                String newv = w1.value().substring(1);
                if (w1.hasDT()) {
                    newv = w1.value().charAt(0) + " ا" + newv;
                }
                w1.changeFeature("value", newv);
                w1.changeFeature("clitic", "0");
            }
            if (w1.hasConj()) {
                String newv = w1.value().charAt(0) + " " + w1.value().substring(1);
                w1.changeFeature("value", newv);
                w1.changeFeature("clitic", "0");
            }
            if (w1.hasEnc()) {

                phrase.add(w1);
                return phrase;
            }
//            if (w1.isStop()) {
//                phrase.add(w1);
//                return phrase;
//            }

            reduce = noReduce(w1, w2);
            phrase.add(w1);
        }
        return phrase;
    }

    Phrase readPhrase2(Sentence sentence, int index) {
        Phrase phrase = new Phrase();
        Word w1, w2;
        int i = index;
        boolean reduce = true;

        for (i = index; i < sentence.size() - 1 && reduce; i++) {
            w1 = sentence.get(i);

            if (w1.isPrep() && !w1.hasEnc()
                    || w1.isPart()
                    || w1.isPartNeg()
                    || w1.isConj()
                    || w1.isInterrogation()
                    || w1.isPunc()
                    || w1.isRel()) {
                phrase.add(w1);
                i++;
                w1 = sentence.get(i);
            }
            if (i < sentence.size() - 1) {
                w2 = sentence.get(i + 1);
                reduce = noReduce(w1, w2);
            }
            phrase.add(w1);
        }
        return phrase;
    }

    boolean noReduce(Word w1, Word w2) {
//        pl(w1 + "\t" + w2);
        return w1.isNoun() && w2.isPunc()
                || w1.isAdj() && w2.isPunc()
                || w1.isDTadj() && w2.isPunc()
                || w1.isPnoun() && w2.isPunc()
                || w1.isNoun() && w2.isNoun() && !w1.hasEnc()
                || w1.isNoun() && w2.isNoun() && !w1.hasEnc() && !w2.hasEnc()
                || w1.isNoun() && w2.isDTnoun()
                || w1.isDTnoun() && w2.isDTnoun() && !w2.hasPrc1()
                || w1.isNoun() && w2.isPnoun()
                || w1.isPnoun() && w2.isPnoun() && !w2.hasConj()
                || w1.isDTnoun() && w2.isPnoun()
                || w1.isPnoun() && w2.isDTnoun()
                || w1.isPronX() && w2.isDTnoun()
                //-------------------------------
                || w1.isNoun() && w2.isDTadj() //                || w1.isAdj() && w2.isAdj()
                || w1.isDTnoun() && w2.isDTadj() //                || w1.isAdj() && w2.isAdj()
                || w1.isAdj() && w2.isAdj()
                || w1.isDTadj() && w2.isDTadj();

    }

    public static void main(String[] args) throws IOException {
        Phrasing cls = new Phrasing();

        cls.ini();
//        cls.detectDocument("");
    }

}
/*
 adj adj adj adjمشكلة   
 part_neg

 */

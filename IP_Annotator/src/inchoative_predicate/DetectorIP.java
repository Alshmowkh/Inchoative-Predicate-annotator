package inchoative_predicate;

import edu.columbia.ccls.madamira.configuration.OutSeg;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import madamiray.Madamiray;
import utils.Miss;

public class DetectorIP {

    String corpus;
    List<String> isoTags;
    List<String> reportBeginningTags;
    Map<Integer, List<String>> buffer;
    List<String> results;

    List<String> canEnSisters;

    public DetectorIP() {
        corpus = "F:\\Master\\Thesis\\Implementations\\IRDetector\\Shom_lib\\Documents\\simple_corpus_NS.txt";
        buffer = new HashMap();

//        corpus = "./Shom_lib/Documents/doc02.txt";
        reportBeginningTags = new ArrayList();
        reportBeginningTags.add("NN");
        reportBeginningTags.add("NNS");
        reportBeginningTags.add("JJ");

        isoTags = new ArrayList();
        isoTags.add("VBP");
        isoTags.add("VBN");
        isoTags.add("VBG");
        isoTags.add("VBD");
        isoTags.add("VB");
        isoTags.add("VN");
        //--------------
        isoTags.add("IN");
//        isoTags.add("PUNC");
//        isoTags.add("WRB");
//        isoTags.add("WP");
//        isoTags.add("CC");
        //---------------
        isoTags.add("JJ");
//        isoTags.add("CD");
        isoTags.add("ADJ");
        isoTags.add("JJR");
        //----------------
        isoTags.add("NN");
        isoTags.add("NNS");
        isoTags.add("PRP");

        canEnSisters = fullCanEn();
    }

    public List<String> sentence_segmentor(String file) throws IOException {
        if (!file.trim().equals("")) {
            corpus = file;
        }
        BufferedReader reader = new BufferedReader(new FileReader(corpus));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

        } finally {
            reader.close();
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

    Boolean isNominal(List<Word> words) {
        Word ini = words.get(0);
        if (ini.tag().startsWith("v")) {
            canEnSisters = this.fullCanEn();
            if (canEnSisters.contains(ini.value())) {
                words.remove(ini.index());
                return isNominal(reIndexing(words));
            } else {
                return false;
            }
        } else if (ini.tag().startsWith("CC")) {
            words.remove(ini.index());

            return isNominal(reIndexing(words));
        } else if (ini.tag().equals("PUNC")) {
            pl("No sentence");
            return false;
        } else {
            return true;
        }
    }

    List<Word> reIndexing(List<Word> oldIndex) {
        List<Word> newIndex = new ArrayList();
        for (int i = 0; i < oldIndex.size(); i++) {
            oldIndex.get(i).setIndex(i);
            newIndex.add(oldIndex.get(i));
        }
        return newIndex;
    }

    private List<String> fullCanEn() {
        List<String> canEn = new ArrayList();
        canEn.add("ان");
        canEn.add("إن");

        canEn.add("كان");
        canEn.add("كانت");
        canEn.add("كانوا");
        canEn.add("كنتم");

        canEn.add("كأن");
        canEn.add("ظل");
        canEn.add("بات");
        canEn.add("اضحى");
        canEn.add("اصبح");
        canEn.add("امسى");
        canEn.add("صار");
        canEn.add("مازال");
        canEn.add("ليس");

        return canEn;
    }

    List<Word> getPredicate(List<Word> words, int seek) {
        List<Word> Plist;
        Word ini = words.get(seek);
        if (this.canEnSisters.contains(ini.value().trim())) {
            Plist = getPredicate(words, seek + 1);
        } else if (ini.tag().startsWith("WP")) {
            Plist = this.parseRelative(words, seek);
        } else {
            Plist = parseReport(words, seek);
        }

        return Plist;
    }

    List<Word> parseRelative(List<Word> sent, int seek) {
        List<Word> relative = new ArrayList();
        Word shifter = sent.get(seek);
        String shT = shifter.tag();
        List<Word> temp;
        if (!shT.startsWith("WP")) {
            System.out.println("relative sentence not recognized!");
            return null;
        }
        Word vb = sent.get(shifter.index() + 1);
//        relative.add(shifter);
        relative.add(vb);
        if (vb.tag().startsWith("V")) {
            if (vb.index() + 1 >= sent.size() - 1) {
                return relative;
            }
            temp = this.readPhraseEx2(sent, vb.index() + 1);
            Word last = temp.get(temp.size() - 1);
            Word first = temp.get(0);
//            p(last.index() + "\t" + sent.size());
            if (last.index() + 1 >= sent.size() - 1) {
                //check verb transitivaty

                if (this.reportBeginningTags.contains(first.tag())) {
                    return temp;
                } else {
                    relative.addAll(temp);
                    return relative;
                }
            } else if (sent.get(last.index() + 1).tag().startsWith("V")) {
                relative = new ArrayList();
                relative.add(sent.get(last.index() + 1));
                return relative;
            }
            recursion:
            if (first.tag().startsWith("IN")) {
                relative = new ArrayList();
                relative.add(vb);
                relative.add(first);
                do {
                    temp = this.readPhraseEx2(sent, first.index() + 1);
                    last = temp.get(temp.size() - 1);
                    first = temp.get(0);
                    relative.addAll(temp);
                    if (last.index() + 1 >= sent.size() - 1) {
                        return relative;
                    }
                } while (first.tag().startsWith("IN"));
            }
            temp = this.readPhraseEx2(sent, last.index() + 1);
            last = temp.get(temp.size() - 1);
            first = temp.get(0);
            if (last.index() + 1 >= sent.size() - 1) {
                //check verb transitivaty
                if (this.reportBeginningTags.contains(first.tag())) {
                    return temp;
                } else {
                    relative.addAll(temp);
                    return relative;
                }
            }

        }
//        pl(relative);
        return relative;
    }

    List<Word> readPhraseEx2(List<Word> remainder, int seek) {
        List<Word> phrase = new ArrayList();
        Word preTag = remainder.get(seek);
        phrase.add(preTag);

        int i = 1;

        if (phrase.get(i - 1).tag().equals("NN") || phrase.get(i - 1).tag().equals("NNS")) {
            while (remainder.get(i + seek).tag().equals("NN") || remainder.get(i + seek).tag().equals("NNS") || remainder.get(i + seek).tag().equals("DT")) {
                phrase.add(remainder.get(i + seek));
                i++;
            }
        }
        if (phrase.get(i - 1).tag().equals("DT") || phrase.get(i - 1).tag().equals("DTNN") || phrase.get(i - 1).tag().equals("DTNNS") || phrase.get(i - 1).tag().equals("NN") || phrase.get(i - 1).tag().equals("NNS")) {
            while (remainder.get(i + seek).tag().equals("DTNN") || remainder.get(i + seek).tag().equals("DTNNS")) {
                phrase.add(remainder.get(i + seek));
                i++;
            }
        }
        if (phrase.get(i - 1).tag().equals("DT") || phrase.get(i - 1).tag().equals("DTJJ") || phrase.get(i - 1).tag().equals("DTJJR") || phrase.get(i - 1).tag().equals("DTNN") || phrase.get(i - 1).tag().equals("DTNNS") || phrase.get(i - 1).tag().equals("NN") || phrase.get(i - 1).tag().equals("NNS")) {
            while (remainder.get(i + seek).tag().equals("DTJJ") || remainder.get(i + seek).tag().equals("DTJJR")) {
                phrase.add(remainder.get(i + seek));
                i++;
            }
        }
        if (phrase.get(i - 1).tag().equals("NNP") || phrase.get(i - 1).tag().equals("NNPS") || phrase.get(i - 1).tag().equals("DTJJ") || phrase.get(i - 1).tag().equals("DTJJR") || phrase.get(i - 1).tag().equals("DTNN") || phrase.get(i - 1).tag().equals("DTNNS") || phrase.get(i - 1).tag().equals("NN") || phrase.get(i - 1).tag().equals("NNS")) {
            while (remainder.get(i + seek).tag().equals("NNP") || remainder.get(i + seek).tag().equals("NNPS") || remainder.get(i + seek).tag().equals("DTNNP")) {
                phrase.add(remainder.get(i + seek));
                i++;
            }
        }
        if (phrase.get(i - 1).tag().equals("DTNN") || phrase.get(i - 1).tag().equals("DTNNS") || phrase.get(i - 1).tag().equals("NNP") || phrase.get(i - 1).tag().equals("NNPS")) {
            while (remainder.get(i + seek).tag().equals("DTNN") || remainder.get(i + seek).tag().equals("DTNNS")) {
                phrase.add(remainder.get(i + seek));
                i++;
            }
        }
        if (phrase.get(i - 1).tag().equals("DTJJ") || phrase.get(i - 1).tag().equals("DTJJR") || phrase.get(i - 1).tag().equals("DTNN") || phrase.get(i - 1).tag().equals("DTNNS") || phrase.get(i - 1).tag().equals("NNP") || phrase.get(i - 1).tag().equals("NNPS")) {
            while (remainder.get(i + seek).tag().equals("DTJJ") || remainder.get(i + seek).tag().equals("DTJJR")) {
                phrase.add(remainder.get(i + seek));
                i++;
            }
        }

        return phrase;
    }

    List<Word> parseReport(List<Word> sent, int seek) {
        Word shifter = sent.get(seek);
//        pl(shifter.value());
        String shT = shifter.tag();
        List<Word> report = new ArrayList();
        List<Word> temp;
        if (shT.startsWith("V")) {
            report.add(shifter);
        } else if (shT.startsWith("NN") || shT.startsWith("JJ") || shT.startsWith("ADJ") || shT.startsWith("PRP")) {
            report.addAll(this.readReport(sent, seek));
        } else if (shT.startsWith("IN")) {
            temp = readPP(sent, seek, shifter);
            pl(sent.get(0).index());
            seek = temp.get(temp.size() - 1).index();
            pl(sent.get(seek).value() + ":" + seek);
            Word afterpp = sent.get(seek + 1);

            String tagShr = sent.get(afterpp.index()).tag();
//            System.out.println(tagShr);
            if (tagShr.startsWith("PUNC") || tagShr.startsWith("IN") || afterpp == null) {
                report = temp;
//                 pl(report);
            } else {
                report = this.readReport(sent, afterpp.index() + 1);
            }
        }

        return report;
    }

    List<Word> readPP(List<Word> sent, int seek, Word shifter) {
        String in = shifter.tag();
        if (!in.startsWith("IN")) {
            System.out.println("Error ocures at readPP method");
            return null;
        }
        List<Word> PP = new ArrayList();
        PP.add(shifter);
        PP.addAll(readPhraseEx2(sent, seek + 1));
        return PP;
    }

    List<Word> readReport(List<Word> remainder, int seek) {
        List<Word> phrase = new ArrayList();
        Word preTag = remainder.get(seek);
        phrase.add(preTag);
        int i = 1;
        if (phrase.get(i - 1).tag().equals("NNPS") || phrase.get(i - 1).tag().equals("NNP") || phrase.get(i - 1).tag().equals("NN") || phrase.get(i - 1).tag().equals("NNS") || phrase.get(i - 1).tag().equals("PRP")) {
            while (remainder.get(i + seek).tag().equals("NN") || remainder.get(i + seek).tag().equals("NNS") || remainder.get(i + seek).tag().equals("NNP") || remainder.get(i + seek).tag().equals("NNPS")) {
                phrase.add(remainder.get(i + seek));
                i++;
            }
        }
        if (phrase.get(i - 1).tag().startsWith("JJ") || phrase.get(i - 1).tag().startsWith("ADJ") || phrase.get(i - 1).tag().equals("NN") || phrase.get(i - 1).tag().equals("NNS")) {
            while (remainder.get(i + seek).tag().startsWith("JJ") || remainder.get(i + seek).tag().startsWith("ADJ")) {
                phrase.add(remainder.get(i + seek));
                i++;
            }
        }
        return phrase;
    }

    void parseVerbalSent(List<Word> sent) {
        String verbT = sent.get(0).tag();
        p("Verbial sentence!" + "  " + verbT);

    }

//    void detectDocument(String file) throws FileNotFoundException, IOException {
//        if (!file.trim().equals("")) {
//            corpus = file;
//        }
//        List<Word> phraseI = null, Plist = null;
//        Word inchoative = null, predicate = null;
//        List<String> sentences = this.sentence_segmentor("");
//         OutSeg outseg = new Madamiray(sentences).getMorpholizing().get(0);
//        List<Word> sentence = new Word().wording(outseg);
//        List<TaggedWord> sentTagged;
//        List<Word> sentence;
//        for (int i = 0; i < sentences.size(); i++) {
//            results = new ArrayList();
//            results.add(sentences.get(i));
//            sentTagged = maxentTagger(sentences.get(i));
//            results.add(sentTagged.toString());
//            sentence = wording(sentTagged);
//            if (isNominal(sentence)) {
////                phrase = getInchoative(sentence);
////                Plist = getPredicate(sentence, phrase.size());
////                //--------------------------------------------
////
////                inchoative = phrase.get(0);
////                System.out.print(inchoative + "  :  ");
////
////                if (Plist != null && !Plist.isEmpty()) {
////                    predicate = Plist.get(0);
////                    System.out.println(predicate);
////                } else {
////                    System.out.println("null");
////                }
//                pl(sentTagged);
//                pl(phraseI = readPhrase(sentence));
//            } else {
//                pl("Verbal Sentence.");
//            }
////            pl(Plist);
//
//            buffer.put(i, results);
//            char c = (char) System.in.read();
//            if (c == 'q') {
//                break;
//            }
//        }
//
////        while (itr.hasNext()) {
////            List<HasWord> sentence = (List<HasWord>) itr.next();
////            System.out.print(sentence + "\t");
////            sentenceKind(getTaggedLabel(sentence));
////            char c = (char) System.in.read();
////            if (c == 'q') {
////                break;
////            }
////        }
//    }
    List<Word> readPhrase(List<Word> sentence) {
        List<Word> phrase = new ArrayList();
        Word w;
        int i = 0;

        w = sentence.get(i++);
        if (w.isPron()) {
            phrase.add(w);
            return phrase;
        } else if (w.isPronX()) {
            phrase.add(w);
            return phrase;
        } else if (w.isAdj()) {
            while (w.isAdj()) {
                phrase.add(w);
                w = sentence.get(i++);
            }
            return phrase;
        } else if (w.isVerb()) {
            phrase.add(w);
            return phrase;
        }
        while (w.isNoun()) {
            phrase.add(w);
            w = sentence.get(i++);
        }
        while (w.isPnoun()) {
            phrase.add(w);
            w = sentence.get(i++);
        }

        while (w.isDTJJ()) {
            phrase.add(w);
            w = sentence.get(i++);
        }
        return phrase;
    }

    Word clitic(Word w) {
        if (w.hasConj()) {
            pl(w.prc2());
        }
        if (w.hasEnc()) {
            pl(w.enc0());
        }
        w.changeFeature("value", w.stem());
        w.changeFeature("clitic", "0");
        return w;
    }

    void ini2() throws FileNotFoundException, IOException {
        Word inchoative, predicate = null;
        List<Word> Ilist, Plist;
        int s = 27;
        List<String> sentences = sentence_segmentor("");
        OutSeg outseg = new Madamiray(sentences).getMorpholizing().get(s - 1);
        List<Word> words = new Sentence(outseg);
        new Miss().printAllSeg(outseg);
        if (isNominal(words)) {
//            phrase = readInchoative(sentence);
//            Plist = getPredicate(sentence, phrase.size());
//            //-----------------
//            inchoative = phrase.get(0);
//            if (Plist != null && !Plist.isEmpty()) {
//                predicate = Plist.get(0);
//            }
            Ilist = readPhrase(words, 0);
            pl(Ilist);
        } else {
            pl("Verbal Sentence.");
        }
    }

    void detectSentence() throws IOException {
        String sentenceString = "عندي لك مفاجأة.";
        int sentNum = 1;
        List<String> sentences = sentence_segmentor("");
        OutSeg outseg = new Madamiray(sentences).getMorpholizing().get(0);
        Sentence sentence = new Sentence(outseg);
//        List<AnnGen> anngens = new AnnGenDetector(sentence).getAnnGen3();
//        pl(anngens);
//        this.printAllSeg(outseg);
    }

    void next() {
        try {
            char c = (char) System.in.read();
            if (c == 'q') {
                System.exit(1);
            }
        } catch (Exception e) {

        }

    }

    List<Word> getInchoative(List<Word> words) {
        List<Word> Ilist;
        Word ini = words.get(0);
        if (ini.tag().equals("CC")) {
            words.remove(ini);

            Ilist = getInchoative(reIndexing(words));
        } else {
            Ilist = readInchoative(words);
        }
        return Ilist;
    }

    List<Word> readInchoative(List<Word> sentence) {
        List<Word> phrase = new ArrayList();
        Word preTag = sentence.get(0);
        phrase.add(preTag);

        int i = 1;

        if (phrase.get(i - 1).tag().equals("NN") || phrase.get(i - 1).tag().equals("NNS")) {
            while (sentence.get(i).tag().equals("NN") || sentence.get(i).tag().equals("NNS") || sentence.get(i).tag().equals("DT")) {
                phrase.add(sentence.get(i));
                i++;
            }
        }
        if (phrase.get(i - 1).tag().equals("DT") || phrase.get(i - 1).tag().equals("DTNN") || phrase.get(i - 1).tag().equals("DTNNS") || phrase.get(i - 1).tag().equals("NN") || phrase.get(i - 1).tag().equals("NNS")) {
            while (sentence.get(i).tag().equals("DTNN") || sentence.get(i).tag().equals("DTNNS")) {
                phrase.add(sentence.get(i));
                i++;
            }
        }
        if (phrase.get(i - 1).tag().equals("DT") || phrase.get(i - 1).tag().equals("DTJJ") || phrase.get(i - 1).tag().equals("DTJJR") || phrase.get(i - 1).tag().equals("DTNN") || phrase.get(i - 1).tag().equals("DTNNS") || phrase.get(i - 1).tag().equals("NN") || phrase.get(i - 1).tag().equals("NNS")) {
            while (sentence.get(i).tag().equals("DTJJ") || sentence.get(i).tag().equals("DTJJR")) {
                phrase.add(sentence.get(i));
                i++;
            }
        }
        if (phrase.get(i - 1).tag().equals("NNP") || phrase.get(i - 1).tag().equals("NNPS") || phrase.get(i - 1).tag().equals("DTJJ") || phrase.get(i - 1).tag().equals("DTJJR") || phrase.get(i - 1).tag().equals("DTNN") || phrase.get(i - 1).tag().equals("DTNNS") || phrase.get(i - 1).tag().equals("NN") || phrase.get(i - 1).tag().equals("NNS")) {
            while (sentence.get(i).tag().equals("NNP") || sentence.get(i).tag().equals("NNPS") || sentence.get(i).tag().equals("DTNNP")) {
                phrase.add(sentence.get(i));
                i++;
            }
        }
        if (phrase.get(i - 1).tag().equals("DTNN") || phrase.get(i - 1).tag().equals("DTNNS") || phrase.get(i - 1).tag().equals("NNP") || phrase.get(i - 1).tag().equals("NNPS")) {
            while (sentence.get(i).tag().equals("DTNN") || sentence.get(i).tag().equals("DTNNS")) {
                phrase.add(sentence.get(i));
                i++;
            }
        }
        if (phrase.get(i - 1).tag().equals("DTJJ") || phrase.get(i - 1).tag().equals("DTJJR") || phrase.get(i - 1).tag().equals("DTNN") || phrase.get(i - 1).tag().equals("DTNNS") || phrase.get(i - 1).tag().equals("NNP") || phrase.get(i - 1).tag().equals("NNPS")) {
            while (sentence.get(i).tag().equals("DTJJ") || sentence.get(i).tag().equals("DTJJR")) {
                phrase.add(sentence.get(i));
                i++;
            }
        }

        return phrase;
    }

    void chunking(List<Word> words) throws FileNotFoundException, IOException {
        List<Word> phrase;
        int index = 0;
        Word fw;
        while (index < words.size()) {
            fw = words.get(index);
            if (fw.hasClitic()) {
                fw = this.clitic(fw);
                words.remove(fw);
                words.add(index, fw);
            }
            phrase = readPhrase(words, index);
            if (phrase.size() > 0) {
                index = phrase.get(phrase.size() - 1).index() + 1;
            } else {
                index++;
            }
            if (!phrase.isEmpty()) {
                pl(phrase);
            }
        }
    }

    List<Word> readPhrase(List<Word> sentence, int index) {
        List<Word> phrase = new ArrayList();
        Word w;
        int i = index;

        w = sentence.get(i++);
        if (w.isPron()) {
            phrase.add(w);
            return phrase;
        } else if (w.isPronX()) {
            phrase.add(w);
            return phrase;
        } else if (w.isAdj()) {
            while (w.isAdj()) {
                if (w.hasClitic()) {
                    return phrase;
                }
                phrase.add(w);
                w = sentence.get(i++);
            }
            return phrase;
        } else if (w.isVerb()) {
            phrase.add(w);
            return phrase;
        }
        while (w.isNoun()) {
            if (w.hasClitic()) {
                return phrase;
            }
            phrase.add(w);
            w = sentence.get(i++);

        }
        while (w.isPnoun()) {
            if (w.hasClitic()) {
                return phrase;
            }
            phrase.add(w);
            w = sentence.get(i++);

        }
        while (w.isDTnoun()) {
            if (w.hasClitic()) {
                return phrase;
            }
            phrase.add(w);
            w = sentence.get(i++);
        }
        while (w.isDTJJ()) {
            if (w.hasClitic()) {
                return phrase;
            }
            phrase.add(w);
            w = sentence.get(i++);
        }
//        pl(phrase);
        return phrase;
    }

    List<Word> inchoativePhrase1(List<Word> sentence) {
        List<Word> phrase = new ArrayList();
        Word w;
        int i = 0;

        w = sentence.get(i++);
        if (w.isPron()) {
            phrase.add(w);
            return phrase;
        } else if (w.isPronX()) {
            phrase.add(w);
            return phrase;
        } else if (w.isAdj()) {
            while (w.isAdj()) {
                if (w.hasClitic()) {
                    return phrase;
                }
                phrase.add(w);
                w = sentence.get(i++);
            }
            return phrase;
        } else if (w.isVerb()) {
            return phrase;
        }
        while (w.isNoun()) {
            if (w.hasClitic()) {
                w.cliticTrim();
                phrase.add(w);
                return phrase;
            }
            phrase.add(w);
            w = sentence.get(i++);

        }
        while (w.isPnoun()) {
            phrase.add(w);
            w = sentence.get(i++);

        }
        while (w.isDTnoun()) {
            if (w.hasClitic()) {
                return phrase;
            }
            phrase.add(w);
            w = sentence.get(i++);
        }
        while (w.isDTJJ()) {
            if (w.hasClitic()) {
                return phrase;
            }
            phrase.add(w);
            w = sentence.get(i++);
        }
//        pl(phrase);
        return phrase;
    }

    /*
    
    
    
    
    
    
    
    
    
    
    
    
    
     */
    Sentence reIndexing(Sentence sentence) {
        for (Word w : sentence) {
            w.index = w.index() - 1;
        }
        sentence.count = sentence.count() - 1;
        return sentence;
    }

    Phrase prepPhrase(Sentence sentence, int index) {

        Phrase temp;
        Word prep = sentence.get(index);
        index += 1;
        pl(prep);
        if (new Letter().jointPrepositions().contains(prep.value().charAt(0))) {
            String newv = prep.value().substring(1);
            if (prep.hasDT()) {
                newv = "ا" + newv;
            }
            prep.changeFeature("value", newv);
            prep.changeFeature("clitic", "0");
//            sentence.replace(prep.index(), prep);
            index -= 1;
        }

        Word w;
        temp = readPhrase4(sentence, index);
        index = temp.get(temp.size() - 1).index();
        w = sentence.get(index);
        pl(w);
        if (w.index() >= sentence.size() - 2) {
            if (w.isPrep()) {
                pl(w.prc1());
                temp.add(0, w);
            }
            return temp;
        }
        index++;
//        w = sentence.get(index);
//        if (w.isConj()) {
//            temp = readPhrase3(sentence, index + 1);
//        }
        return predicatePhrase(sentence, index);

    }

    Phrase relativePhrase(Sentence sentence, int index) {
        Phrase phrase = new Phrase();
        Word fw = sentence.get(index + 1);
        if (fw.isVerb() || fw.isPrep()) {
            phrase = readPhrase3(sentence, index + 2);

        } else if (fw.isPart() && sentence.size() > index + 1 && sentence.get(index + 2).isVerb()) {
            phrase = readPhrase3(sentence, index + 3);

        }
        return phrase;
    }

    boolean noReduceP(Word w1, Word w2) {

        return w1.isNoun() && w2.isPunc()
                || w1.isAdj() && w2.isPunc()
                || w1.isNoun() && w2.isDTnoun()
                || w1.isDTnoun() && w2.isDTnoun()
                || w1.isNoun() && w2.isPnoun()
                || w1.isPnoun() && w2.isPnoun()
                || w1.isPnoun() && w2.isDTnoun()
                || w1.isPronX() && w2.isDTnoun()
                //-------------------------------
                || w1.isNoun() && w2.isAdj();
    }

    Phrase readPhrase3(Sentence sentence, int index) {
//        List<Word> phrase = new ArrayList();
        Phrase phrase = new Phrase();
        Word w;
        int i = index;

        for (i = index; i < sentence.size() - 1 && noReduceP(sentence.get(i), sentence.get(i + 1)); i++) {
            w = sentence.get(i);
            phrase.add(w);
        }

        w = sentence.get(i);
        if (!w.isPunc()) {
            phrase.add(w);
        }

        return phrase;
    }

    Phrase readPhrase4(Sentence sentence, int index) {
        Phrase phrase = new Phrase();
        Word w;
        int i = index;
        w = sentence.get(i);
        if (w.isPrep()
                || w.isPart()
                || w.isPartNeg()
                || w.isConj()
                || w.isInterrogation()
                || w.isPunc()
                || w.isRel()) {
            phrase.add(w);
            return phrase;
        }
        for (i = index; i < sentence.size() - 1 && noReduceP(sentence.get(i), sentence.get(i + 1)); i++) {
            w = sentence.get(i);
            phrase.add(w);
        }

        w = sentence.get(i);
        if (!w.isPunc()) {
            phrase.add(w);
        }

        return phrase;
    }

    Phrase predicatePhrase(Sentence sentence, int index) {
        sentence = this.normlizeI(sentence, index);
        if (sentence.size() <= index) {
            return null;
        }
//        List<Word> phrase = new ArrayList();
        Phrase phrase = new Phrase();
        Phrase temp;
        Word fw;
        fw = sentence.get(index);

        if (fw.isPunc()) {
            return null;
        } else if (new Letter().abolishers().contains(fw.value().trim())) {
            return predicatePhrase(sentence, index + 1);
        } else if (fw.isVerb()) {
            phrase.add(fw);
            return phrase;
        } else if (fw.isPrep() || fw.hasPrc1()) {
//            pl(fw.hasPrc1());
            temp = prepPhrase(sentence, index);
            return temp;

        } else if (fw.isRel()) {
            return relativePhrase(sentence, index);
        }
        phrase = readPhrase3(sentence, index);
//        pl(phrase);

        if ((sentence.size()
                > phrase.size()) && (fw = sentence.get(phrase.size() - 1)).hasConj()) {
            if (fw.isConj()) {
                phrase.add(fw);
                phrase.addAll(predicatePhrase(sentence, phrase.size() + index));
            } else {
                phrase.addAll(predicatePhrase(sentence, phrase.size() + index));
            }
        }
        return phrase;
    }
    /*
    
    
    
     */

    boolean shifting(Word w1, Word w2) {

        return w1.isNoun() && w2.isNoun() && !w1.hasEnc() && !w2.hasPrep()
                || w1.isNoun() && w2.isDTnoun() && !w2.hasPrep()
                || w1.isDTnoun() && w2.isDTnoun() && !w2.hasPrep()
                || w1.isNoun() && w2.isPnoun()
                || w1.isPnoun() && w2.isPnoun()
                || w1.isPnoun() && w2.isDTnoun() && !w2.hasPrep()
                || w1.isDTnoun() && w2.isPnoun() && !w2.hasPrep() && !w1.hasPrep()
                || w1.isPronX() && w2.isDTnoun() && !w2.hasPrep()
                //-------------------------------
                || w1.isNoun() && w2.isAdj() && w2.hasDT() //-----------------------------
                ;
    }

    Phrase inchoativePhrase(Sentence sentence, int index) {
//        List<Word> phrase = new ArrayList();
//        pl(sentence);
        Phrase phrase = new Phrase();
        Word w1, w2;
        int i = index;
        boolean reduced = true;
        for (i = index; i < sentence.size() && reduced; i++) {

            w1 = sentence.get(i);
            w2 = sentence.get(i + 1);
            reduced = shifting(w1, w2);
//            w1 = sentence.get(i);
            phrase.add(w1);
        }
//        if ((sentence.size() > phrase.size() + index) && (w1 = sentence.get(i)).hasConj()) {
//            if (w1.isConj()) {
//                phrase.add(w1);
//                phrase.addAll(inchoativePhrase(sentence, phrase.size()));
//            } else {
//                phrase.addAll(inchoativePhrase(sentence, phrase.size()));
//            }
//        }
        return phrase;
    }

    char sentenceKind(Sentence words) {
        Word fw = words.get(0);
        if (fw.isNominal()) {
            return 'N';
        } else if (fw.isInterrogation()) {
            return 'Q';
        } else {
            return 'V';
        }
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
   
    /*
    
    
     */

    void iniProcessing(Sentence words) {
//        pl(sentence);
        words = normlizeI(words, 0);
        char type = sentenceKind(words);

        Word inchoative, predicate = null;
        List<Word> phraseI, phraseP;
        if (type == 'V') {
            pl("Verbal sentence.");
            return;
        } else if (type == 'Q') {
            pl("Interrogated sentence.");
            return;
        }
        phraseI = inchoativePhrase(words, 0);
        p(phraseI);
        phraseP = predicatePhrase(words, phraseI.size());
        pl("\t" + phraseP);
    }

    void ini() throws FileNotFoundException, IOException {
        p("Enter sentence ID:");
//        int s =Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine()) ;
        int s = new Scanner(System.in).nextInt();
        List<String> sentences = sentence_segmentor("");
        Madamiray mada;
        OutSeg outseg;
        Sentence sentence;
        if (s > 0) {
            mada = new Madamiray(sentences.get(s - 1));
            outseg = mada.getMorpholizing().get(0);
            sentence = new Sentence(outseg);
            new Miss().printAllSeg(outseg);
            iniProcessing(sentence);
        } else {
            mada = new Madamiray(sentences);
            for (int i = 0; i < sentences.size(); i++) {
                outseg = mada.getMorpholizing().get(i);
                sentence = new Sentence(outseg);
                iniProcessing(sentence);
                next();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        DetectorIP cls = new DetectorIP();

        cls.ini();
//        cls.detectDocument("");
    }

    static void p(Object o) {
        System.out.print(o);
    }

    static void pl(Object o) {
        System.out.println(o);
    }
}
//notes
//JJR  التاق الخاص باسلوب التفضيل
/*
 pending work
 1- nouns modifiers processing (nomural,interogent....)
 2- Replace Stanford Parser by Stanford POS Tagger.
 */

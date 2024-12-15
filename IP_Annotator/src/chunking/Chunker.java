package chunking;

import edu.columbia.ccls.madamira.configuration.OutSeg;
import inchoative_predicate.DetectorIP;
import inchoative_predicate.Sentence;
import inchoative_predicate.Word;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import madamiray.Madamiray;
import utils.Miss;
import static utils.Miss.pl;
import utils.ReadFromFile;

public class Chunker {
         final   String  corpus = "F:\\Master\\Thesis\\Implementations\\IRDetector\\Shom_lib\\Documents\\simple_corpus_NS.txt";

    void chunking(List<Word> words) throws FileNotFoundException, IOException {
        List<Word> phrase;
        int index = 0;
        Word fw;
        while (index < words.size()) {
            fw = words.get(index);
            if (fw.hasClitic()) {
                fw = clitic(fw);
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

    void next() {
        try {
            char c = (char) System.in.read();
            if (c == 'q') {
                System.exit(1);
            }
        } catch (Exception e) {

        }

    }

    void ini() throws FileNotFoundException, IOException {
        Word inchoative, predicate = null;
        List<Word> Ilist, Plist;
        int s = 126;
        List<String> sentences = new ReadFromFile(corpus).sentences();
        Madamiray mada;
        OutSeg outseg;
        Sentence sentence;
        if (s > 0) {
            mada = new Madamiray(sentences.get(s - 1));
            outseg = mada.getMorpholizing().get(0);
            sentence = new Sentence(outseg);
            new Miss().printAllSeg(outseg);
            this.chunking(sentence);
        } else {
            mada = new Madamiray(sentences);
            for (int i = 0; i < sentences.size(); i++) {
                outseg = mada.getMorpholizing().get(i);
                sentence = new Sentence(outseg);
                this.chunking(sentence);
                next();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Chunker cls = new Chunker();

//        cls.chunking();
        cls.ini();
//        cls.detectDocument("");
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

}

package inchoative_predicate;

import edu.columbia.ccls.madamira.configuration.MorphFeatureSet;
import edu.columbia.ccls.madamira.configuration.OutSeg;
import java.util.ArrayList;
import java.util.List;

public class Sentence extends ArrayList<Word> {

    public int count;
    private String id;
//    Boolean hasFullStop;
    OutSeg outseg;
    int seek;

    public Sentence(OutSeg outs) {
        outseg = outs;
        wording();
    }

    public int count() {
        return count;
    }

    public String id() {
        return id;
    }

//    public Boolean finish() {
//        
//    }
    public void replace(int i, Word w) {
        this.remove(i);
        this.add(i, w);

    }

    public Sentence newInstance() {
        return this;
    }

    private void wording() {
//        sentence = new Sentence();
        Word word = null;
        List<edu.columbia.ccls.madamira.configuration.Word> morphs = outseg.getWordInfo().getWord();
        int index = 0;
        MorphFeatureSet wf;// = null;
        for (edu.columbia.ccls.madamira.configuration.Word w : morphs) {
            word = new Word();
            word.value = w.getWord();
            try {
                wf = w.getAnalysis().get(0).getMorphFeatureSet();
                word.diac = wf.getDiac();
                word.gloss = wf.getGloss();
                word.tag = wf.getPos();
                word.enc0 = wf.getEnc0();
                word.prc0 = wf.getPrc0();
                word.prc1 = wf.getPrc1();
                word.prc2 = wf.getPrc2();
                word.prc3 = wf.getPrc3();
                word.stem = wf.getStem();
                word.per = wf.getPer();
                word.num = wf.getNum();
                word.index = index++;
                
                if (word.hasClitic()) {
                    word.clitic = word.clitic();
                }
            } catch (Exception e) {
            }
            if (word.hasPos()) {
                this.add(word);
            }
        }
        count = index;
        id = outseg.getId();
//        if (wf != null && wf.getPos().equals("punc")) {
//            hasFullStop = true;
//        } else {
//            hasFullStop = false;
//        }

    }
}

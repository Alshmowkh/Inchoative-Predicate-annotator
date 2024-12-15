package updating;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import updating.phraser.Phrase;
import static utils.Accessories.deDiacritic;
import static utils.Miss.pl;

public class Sentence extends ArrayList<Word> {

    private int count;
    private int id;

    private List<Phrase> phrases;
    private Sentence tokens;
    private String rawSentence;
    private Entity IPentity;
    private final List lines;

    public Sentence(List lns) {
        lines = lns;
        mapping();
    }

    public int count() {
        return this.size();
    }

    public int id() {
        return id;
    }

    public String rawSentence() {
        return this.rawSentence;
    }

    private void mapping() {
        if (lines == null || lines.isEmpty() || lines.size() < 2) {
            return;
        }
//        pl(outseg.getSegmentInfo().getNer().getNe());
        Word clitic = null, word = null;
        int index = 0;

        Iterator itr = lines.iterator();
        while (itr.hasNext()) {
            String w = (String) itr.next();
            String[] wf = w.split(" ");
            word = new Word();
            id = Integer.parseInt(wf[0].split("-")[0]);
            word.id(Integer.parseInt(wf[0].split("-")[1]));
            word.value(wf[1]);

            word.diacValue(wf[2] != null && !wf[2].equals("0") ? wf[2] : word.value());
            word.setGloss(!wf[3].equals("0") ? wf[3] : null);
            word.tag(!wf[4].equals("0") ? wf[4] : null);
            word.prc3(!"na".equals(wf[5]) && !"0".equals(wf[5]) ? wf[5] : null);
            word.prc2(!"na".equals(wf[6]) && !"0".equals(wf[6]) ? wf[6] : null);
            word.prc1(!"na".equals(wf[7]) && !"0".equals(wf[7]) ? wf[7] : null);
            word.prc0(!"na".equals(wf[8]) && !"0".equals(wf[8]) ? wf[8] : null);
            word.per(!"na".equals(wf[9]) && !"0".equals(wf[9]) ? wf[9] : null);
            word.asp(!"na".equals(wf[10]) && !"0".equals(wf[10]) ? wf[10] : null);
            word.vox(!"na".equals(wf[11]) && !"0".equals(wf[11]) ? wf[11] : null);
            word.mod(!"na".equals(wf[12]) && !"0".equals(wf[12]) ? wf[12] : null);
            word.gen(!"na".equals(wf[13]) && !"0".equals(wf[13]) ? wf[13] : null);
            word.num(!"na".equals(wf[14]) && !"0".equals(wf[14]) ? wf[14] : null);
            word.stt(!"na".equals(wf[15]) && !"0".equals(wf[15]) ? wf[15] : null);
            word.cas(!"na".equals(wf[16]) && !"0".equals(wf[16]) ? wf[16] : null);
            word.enc0(!"na".equals(wf[17]) && !"0".equals(wf[17]) ? wf[17] : null);
            word.stem(wf[18]);
            word.modifiedStem(word.stem());

            word.isLetter = false;

            deClitic(word);

            word.modifyStem();
            word.tokens(this.tokenize(word));

            word.setIndex(index++);

            if (word.diacValue() != null) {
                this.add(word);
                count++;
            }
        }
    }

    String tokenize(Word word) {

        String toks = "";
        toks = word.prc3Val() != null ? word.prc3Val() + "-" : "";
        toks += word.prc2Val() != null ? word.prc2Val() + "-" : "";
        toks += word.prc1Val() != null ? word.prc1Val() + "-" : "";
        toks += word.prc0Val() != null ? word.prc0Val() + "-" : "";

        toks += word.modifiedStemDet();

        toks += word.encVal() != null ? "-" + word.encVal() : "";

        return toks;
    }

    void deClitic(Word word) {

        word.prc3Val(word.hasPrc3() ? word.getprc3() : null);
        word.prc2Val(word.hasPrc2() ? word.getprc2() : null);
        word.prc1Val(word.hasPrc1() ? word.getprc1() : null);
        word.prc0Val(word.hasPrc0() && !word.prc0().contains("det") ? word.getprc0() : null);
        word.tag(word.hasDT() ? word.tag() + "_det" : word.tag());
        word.encVal(word.hasEnc() ? word.encliticing() : null);
    }

    void cliticing(Sentence sent, Word word, int index) {

        Word clitic;
        if (word.hasPrc3()) {
            clitic = new Word();
            clitic.isLetter = true;
            clitic.value(deDiacritic(word.getprc3()));
            clitic.diacValue(word.getprc3());
            word.prc3Val(clitic.diacValue());
            clitic.tag("ques");
            clitic.setIndex(index++);
            count++;
            sent.add(clitic);
        }
        if (word.hasPrc2()) {
            clitic = new Word();
            clitic.isLetter = true;
            clitic.value(deDiacritic(word.getprc2()));
            clitic.diacValue(word.getprc2());
            word.prc2Val(clitic.diacValue());
            clitic.tag("conj");
            clitic.setIndex(index++);
            count++;
            sent.add(clitic);
        }
        if (word.hasPrc1()) {
            clitic = new Word();
            clitic.isLetter = true;
            clitic.value(deDiacritic(word.getprc1()));
            clitic.diacValue(word.getprc1());
            word.prc1Val(clitic.diacValue());
            clitic.tag(clitic.value().startsWith("و") || clitic.value().startsWith("ف") ? "conj"
                    : clitic.value().startsWith("س") ? "fut"
                            : clitic.value().startsWith("ي") ? "voc" : "prep");
            clitic.setIndex(index++);
            count++;
            sent.add(clitic);
        }
        if (word.hasPrc0() && !word.prc0().contains("det")) {
            clitic = new Word();
            clitic.isLetter = true;
            clitic.value(deDiacritic(word.getprc0()));
            clitic.diacValue(word.getprc0());
            word.prc0Val(clitic.diacValue());
            clitic.tag("part_neg");
            clitic.setIndex(index++);
            count++;
            sent.add(clitic);
        }
        if (word.hasDT()) {
            word.tag(word.tag() + "_det");
        }
        word.modifyStem();
        word.encVal(word.hasEnc() ? word.encliticing() : null);
    }

    /*
     phrasing
     */
    public void addPhrase(Phrase phrase) {
        if (phrases == null) {
            phrases = new ArrayList();
        }
        phrases.add(phrase);
//        this.addAll(phrase);
    }

    public List<Phrase> getPhrases() {
        return phrases;
    }

    public Phrase getPhrase(int inx) {

        for (Phrase ph : phrases) {
            for (Word w : ph) {
                if (w.index() == inx) {
                    return ph;
                }
            }
        }
        return null;
    }

    public boolean isNominal() {
        return !this.get(0).isVerb() || this.get(0).isPousdoVerb();
    }

    public boolean hasVerb() {
        return this.stream().anyMatch((w) -> (w.isVerb()));
    }

    public boolean hasRelative() {
        return this.stream().anyMatch((w) -> (w.isRel()));
    }

    public boolean hasPhrase(int phraseIndex) {
        try {
            this.getPhrases().get(phraseIndex);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public Sentence removePousdo() {
        Word w;
        int j;
        List<String> can = new Letter().abolishers();
        for (int i = 0; i < this.count(); i++) {
            w = this.get(i);
            if (can.contains(deDiacritic(w.value())) || w.tag().equals("part_verb") || w.isPousdoVerb()) {
                this.remove(i);
                if (w.hasPrc()) {
                    j = i - 1;
                    while (j >= 0 && (w = this.get(j)).isLetter) {
                        this.remove(j);
                    }
                }
            }

        }
        for (int i = 0; i < this.count(); i++) {
            this.get(i).setIndex(i);
        }
        return this;
    }

    /*
     full entites
     */
    public Entity IPentity() {
        return IPentity;
    }

    public void IPentity(Entity ent) {
        IPentity = ent;
    }

    public void setId(int i) {
        id = i;
    }
//    public boolean isEmpty(){
//        return this.isEmpty();
//    }

    public Sentence removeWordAndReorder(int index) {
        Word w;

        this.remove(index);
//        for (int i = 0; i < this.count(); i++) {
//            w = this.get(i);
//            if (can.contains(deDiacritic(w.value())) || w.tag().equals("part_verb") || w.isPousdoVerb()) {
//                this.remove(i);
//            }
//        }
        for (int i = 0; i < this.count(); i++) {
            this.get(i).setIndex(i);
        }
        return this;
    }

    public boolean hasPausdoVerb() {
        return this.stream().anyMatch((w) -> (w.isPousdoVerb()));
    }

    public void removeInsidePunc() {

        int i = 0;
        Word w = this.get(0);
        List paren = Arrays.asList("(", ")", "[", "]", "{", "}");
        for (i = 1; i < this.size(); i++) {
            if (w.isPunc()) {
                this.remove(w);
            }

            if (paren.contains(w.value())) {
                w = this.get(i++);
                while (!w.isLast() && !paren.contains(w)) {
                    this.remove(w);
                    w = this.get(i++);
                    pl(w);
                }
            }
            w = this.get(i);
        }
//        while (!(w = this.get(i)).isLast()) {
//
//            if (w.isPunc()) {
//                this.remove(w);
//                if (paren.contains(w.value())) {
//                    while (!(w = this.get(i)).isLast()) {
//                        pl(w);
//                        if (paren.contains(w)) {
//                            this.remove(w);
//                            break;
//                        }
//                        this.remove(w);
//                    }
//                }
//            }
//            i++;
//        }
        for (i = 0; i < this.count(); i++) {
            this.get(i).setIndex(i);
        }
    }

    public void dumpPhrases() {
        this.phrases = null;
    }

}

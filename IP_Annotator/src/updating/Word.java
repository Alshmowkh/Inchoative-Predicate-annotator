package updating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static utils.Accessories.deDiacritic;
import static utils.Accessories.diacriticList;

public class Word {

    private int index;
    private String value;
    private String diacValue;
    private String tag;
    private String stem;
    private String modifiedStem;
    private String enc0;
    private String prc0;
    private String prc1;
    private String prc2;
    private String prc3;
    private String encVal;
    private String prc0Val;
    private String prc1Val;
    private String prc2Val;
    private String prc3Val;
    private String gloss;
    private String per;
    private String num;
    private String vox;
    private String asp;
    public boolean isLetter;
    private String gen;
    private String cas;
    private String tokens;
    private String ner;
    private int phraseID;
    private String tr;
    private boolean last;
    private List ontoPath;
    private String root;
    private int id;
    private String mod;
    private String stt;

    public void ner(String nr) {
        ner = nr;
    }

    public String ner() {
        return ner;
    }

    public int index() {
        return index;
    }

    public String tag() {
        return tag;
    }

    public void tag(String tg) {
        this.tag = tg;
    }

    public String value() {
        return value;
    }

    public void value(String vl) {
        value = vl;
    }

    public void setIndex(int newIndex) {
        this.index = newIndex;
    }

    public String diacValue() {
        return diacValue;
    }

    public void diacValue(String value) {
        this.diacValue = value;
    }

    public String noDiacValue() {
        return deDiacritic(diacValue);
    }

    public String modifiedStem() {

        return modifiedStem;
    }

    public String modifiedStemDet() {
        String stemDet = this.modifiedStem();
        if (this.hasDT()) {
            if (stemDet.startsWith("ل")) {
                stemDet = "ا" + stemDet;
            } else {
                stemDet = "ال" + stemDet;
            }
        }
        return stemDet;
    }

    public void modifiedStem(String steem) {

        modifiedStem = steem;
        if (steem == null) {
            modifiedStem = diacValue() != null ? diacValue() : value();
        }
        if (deDiacritic(this.value()).endsWith("ة") && !deDiacritic(this.diacValue()).endsWith("ة")) {
            if (deDiacritic(this.diacValue()).endsWith("ه")) {
                this.diacValue = this.diacValue().substring(0, trimChar(this.diacValue()).length());
            }
            this.diacValue += "ة";

        }
        if (deDiacritic(this.value()).endsWith("ة") && !deDiacritic(steem).endsWith("ة")) {

            this.modifiedStem += "ة";
            this.enc0 = null;
        }

    }

    private String trimChar(String comein) {
        while (diacriticList().contains(comein.charAt(comein.length() - 1))) {
            comein = comein.substring(0, comein.length() - 1);
        }
        return comein.substring(0, comein.length() - 1);
    }

    public void modifyStem() {
        if (isLetter) {
            modifiedStem = this.diacValue;
            return;
        }
        int prcLength = prc3 != null ? prc3Val.length() : 0;
        prcLength += prc2 != null ? prc2Val.length() : 0;
        prcLength += prc1 != null ? prc1Val.length() : 0;
        prcLength += prc0Val != null ? prc0Val.length() : !hasDT() ? 0 : hasPrep() ? 1 : 2;
        int encLength = this.encDiacLength();
        modifiedStem = this.diacValue().substring(prcLength, this.diacValue().length() - encLength);
        if (this.hasEnc()) {

            if (this.noDiacValue().endsWith("ك")) {
                if (deDiacritic(this.modifiedStem()).endsWith("ت")) {
                    this.modifiedStem(this.modifiedStem().replace("ت", "ة"));
                }
            }
        }

//        modifiedStem = this.modifiedStemDet().substring(prcLength, this.modifiedStemDet().length() - encLength);
    }

    public void modifyTag() {
        String noun = deDiacritic(this.modifiedStem());

        if (new Letter().placeNouns().contains(noun)) {
            tag += "_place";
        } else if (new Letter().timeNouns().contains(noun)) {
            tag += "_time";
        }

    }

    public String stem() {
        return stem;
    }

    public void stem(String value) {
        stem = value;
    }

    public String stemNoDiac() {
        return deDiacritic(stem());
    }

    public String enc0() {
        return enc0;
    }

    public String prc0() {
        return prc0;
    }

    public String prc1() {
        return prc1;
    }

    public String prc2() {
        return prc2;
    }

    public String prc3() {
        return prc3;
    }

    public void enc0(String value) {
        enc0 = value;
    }

    public void prc0(String value) {
        prc0 = value;
    }

    public void prc1(String value) {
        prc1 = value;
    }

    public void prc2(String value) {
        prc2 = value;
    }

    public void prc3(String value) {
        prc3 = value;
    }

    public String per() {
        return per;
    }

    public void per(String value) {
        per = value;
    }

    public String num() {
        return num;
    }

    public void num(String value) {
        num = value;
    }

    public String vox() {
        return vox;
    }

    public void vox(String vx) {
        vox = vx;
    }

    public String asp() {
        return asp;
    }

    public void asp(String aspct) {
        asp = aspct;
    }

    public String gen() {
        return gen;
    }

    public void gen(String gender) {
        gen = gender;
    }

    public String cas() {
        return cas;
    }

    public void cas(String cass) {
        cas = cass;
    }

    public String tr() {
        return tr;
    }

    public void tr(String t) {
        tr = t;
    }

    public String gloss() {
        return gloss;
    }

    public void setGloss(String value) {
        gloss = value;
    }

    public String encVal() {
        return encVal;
    }

    public String prc0Val() {
        return prc0Val;
    }

    public String prc1Val() {
        return prc1Val;
    }

    public String prc2Val() {
        return prc2Val;
    }

    public String prc3Val() {
        return prc3Val;
    }

    public void encVal(String v) {
        encVal = v;
    }

    public void prc0Val(String v) {
        prc0Val = v;
    }

    public void prc1Val(String v) {
        prc1Val = v;
    }

    public void prc2Val(String v) {
        prc2Val = v;
    }

    public void prc3Val(String v) {
        prc3Val = v;
    }

    public String tokens() {
        return tokens;
    }

    public void tokens(String tok) {
        tokens = tok;
    }

    public boolean isPunc() {
        return this.tag.contains("punc");
    }

    public boolean isStop() {
        return value.equals(".")
                || value.equals("?")
                || value.equals("!");
    }

    public boolean isPart() {
        return this.tag.startsWith("part");
    }

    public boolean isPartNeg() {
        return this.tag.equals("part_neg");
    }

    public boolean isNoun() {
        return this.tag.equals("noun") && !this.hasDT();
    }

    public boolean isDTnoun() {
        return this.tag.equals("noun_det");
    }

    public boolean isPron() {
        return this.tag.equals("pron")
                || this.tag.equals("im_pron");
    }

    public boolean isPronX() {
        return this.tag.startsWith("pron_");
    }

    public boolean isPnoun() {
        return this.tag.equals("noun_prop");
    }

    public boolean isAdj() {
        return this.tag.startsWith("adj") && !this.hasDT();
    }

    public boolean isDTadj() {
        return this.tag.startsWith("adj") && this.hasDT();
    }

    public boolean isVerb() {
        return this.tag.equals("verb");
    }

    public boolean isPousdoVerb() {
        return this.tag.equals("verb_pseudo")
                || new Letter().abolishers().contains(deDiacritic(this.value()));
    }

    public boolean isPrep() {
        return this.tag.equals("prep");
    }

    public boolean isAdv() {
        return this.tag().startsWith("adv");
    }

    public boolean hasDT() {
        return prc0 != null && this.prc0.equals("Al_det");
    }

    public boolean hasEnc() {
        return enc0 != null
                && enc0.matches("\\d+.+");
    }

    public boolean hasPrc() {
        return this.hasPrc0()
                || this.hasPrc1()
                || this.hasPrc2()
                || this.hasPrc3();
    }

    public boolean hasPrep() {

        return prc1 != null
                && (this.prc1.startsWith("bi")
                || this.prc1.startsWith("la")
                || this.prc1.startsWith("li")
                || this.prc1.endsWith("prep"));
    }

    public boolean hasConj() {

        return (prc1 != null && this.prc1.startsWith("wa"))
                || prc2 != null
                && (this.prc2.startsWith("fa")
                || this.prc2.startsWith("wa")
                || this.prc2.endsWith("prep")
                || isConj());

    }

    public boolean isConj() {

        return this.tag.startsWith("conj");

    }

    public boolean isRel() {
        return this.tag.equals("pron_rel");
    }

    public boolean hasClitic() {

        return this.hasEnc()
                || this.hasPrc();

    }

    public boolean hasPos() {
        return this.tag() != null;
    }

    public boolean hasPrc1() {
        return prc1 != null;
    }

    public boolean hasPrc2() {
        return prc2 != null;
    }

    public boolean hasPrc3() {
        return prc3 != null;
    }

    public boolean hasPrc0() {
        return prc0 != null;
    }

    public boolean isShifted() {
        return this.isNoun() && !this.hasPrep() && !this.hasConj();
    }

    public boolean isNominal() {
        return this.tag.startsWith("noun")
                || this.isPron()
                || this.isPnoun()
                || this.isPronX();
    }

    public boolean isInterrogation() {
        return this.tag.contains("interrog")
                || this.tag.equals("adv_rel");
    }

    String getprc3() {
        String v = this.diacValue().substring(0, 2);
        if (diacriticList().contains(v.charAt(1))) {
            return v;
        } else {
            return v.charAt(0) + "";
        }
    }

    String getprc2() {
        String v = this.hasPrc3() ? this.diacValue().substring(2, 4) : this.diacValue().substring(0, 2);
        if (diacriticList().contains(v.charAt(1))) {
            return v;
        } else {
            return v.charAt(0) + "";
        }
    }

    String getprc1() {
        int length = this.prc1.split("_")[0].length();
        int inx = this.hasPrc3() ? 2 : 0;
        inx += this.hasPrc2() ? 2 : 0;
        String temp = this.diacValue().substring(inx, length + inx);
        return temp;

    }

    String getprc0() {
        int length = this.prc0.split("_")[0].length();
        int inx = !prc3Val.isEmpty() ? 2 : 0;
        inx += !prc2Val.isEmpty() ? 2 : 0;
        inx += !prc1Val.isEmpty() ? prc1Val.length() : 0;
        String temp = this.diacValue().substring(inx, length + inx);
        return temp;

    }

    String encliticing() {

        String enc, enc1 = "", enc2;
        String[] slices = this.diacValue().split(this.modifiedStem());
        if (slices.length > 1) {
            enc1 = slices[1];
        }

        enc2 = this.encMap().get(this.enc0().split("_")[0]);
        if (enc2.equals("ي") || enc2.equals("ني")) {
            if (this.isVerb()) {
                enc2 = "ني";
            } else {
                enc2 = "ي";
            }
        }
//        pl(this.value() + " : " + enc2);
        enc = deDiacritic(this.value()).endsWith(enc2) ? enc2 : enc1;
        if (deDiacritic(this.modifiedStem()).endsWith(enc)) {
            modifiedStem = modifiedStem.substring(0, modifiedStem.length() - enc.length());
        }
        return enc;
    }

    Map<String, String> encMap() {
        Map map = new HashMap();
        map.put("1s", "ي");
        map.put("1s", "ني");
        map.put("1p", "نا");
        map.put("2d", "كما");
        map.put("2p", "كم");
        map.put("2ms", "ك");
        map.put("2mp", "كم");
        map.put("2fs", "ك");
        map.put("2fp", "كن");
        map.put("3d", "هما");
        map.put("3p", "هم");
        map.put("3ms", "ه");
        map.put("3mp", "هم");
        map.put("3fs", "ها");
        map.put("3fp", "هن");
        return map;
    }

    public short encDiacLength() {
        if (enc0 == null || encVal == null) {
            return 0;
        }
        short L = 0;
        String enc = this.encVal;
        String diacr = this.diacValue();
        for (int i = 0; i < enc.length(); i++) {

            while (diacriticList().contains(diacr.charAt(diacr.length() - 1))) {
                diacr = diacr.substring(0, diacr.length() - 1);
                L++;
            }
            diacr = diacr.substring(0, diacr.length() - 1);
            L++;
        }

        return L;
    }

    public String clitic() {
        String stringWord = prc3Val() != null ? prc3Val() + " " : "";
        stringWord += prc2Val() != null ? prc2Val() + " " : "";
        stringWord += prc1Val() != null ? prc1Val() + " " : "";
        stringWord += prc0Val() != null ? prc0Val() + " " : "";
        stringWord += this.modifiedStem();
        stringWord += encVal() != null ? " " + encVal() : "";

        return stringWord;
    }

    @Override
    public String toString() {
//        if (isLetter) {
//            return this.diacValue();
//        }
//        return this.modifiedStemDet() + (encVal() != null ? " " + encVal() : "");
        return tokens;
    }

    public Word separateEnc() {
        Word encWord = new Word();
        encWord.diacValue(this.encVal());
        encWord.value(deDiacritic(this.encVal()));
        encWord.modifiedStem(this.encVal());
        encWord.tag("part");
        encWord.isLetter = true;
        encWord.tokens(encWord.value());
        this.encVal(null);
        return encWord;
    }

    public void phraseID(int indx) {
        phraseID = indx;
    }

    public int phraseID() {
        return phraseID;
    }

    public boolean isIndef() {
        return this.isNoun() && !this.hasEnc();
    }

    public boolean isLast() {
        return this.last;
    }

    public void last(boolean b) {
        last = b;
    }

    public void ontoPath(List<String> path) {
        this.ontoPath = path;
    }

    public List ontoPath() {
        return this.ontoPath;
    }

    public String root() {
        return root;
    }

    public void root(String rot) {
        root = rot;
    }

    void id(int parseInt) {
        this.id = parseInt;
    }

    public int id() {
        return this.id;
    }

    public void mod(String m) {
        this.mod = m;
    }

    public void stt(String st) {
        this.stt = st;
    }

    public String mod() {
        return this.mod;
    }

    public String stt() {
        return this.stt;
    }
}

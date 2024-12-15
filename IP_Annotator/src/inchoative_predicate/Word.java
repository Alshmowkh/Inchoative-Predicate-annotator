package inchoative_predicate;

import edu.columbia.ccls.madamira.configuration.OutSeg;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static utils.Miss.pl;

public class Word {

    public int index;
    String value;
    String diac;
    String tag;
    String stem;
    String enc0;
    String prc;
    String prc0;
    String prc1;
    String prc2;
    String prc3;
    String encVal;
    String prc0Val;
    String prc1Val;
    String prc2Val;
    String prc3Val;
    String gloss;
    String per;
    String num;
    public String clitic;
    public String nodiac;
    Sentence sentence;

    public Word() {
        clitic = "";
    }

    public Word(int id, String t, String val) {
        index = id;
        tag = t;
        value = val;
        clitic = "";
    }

    public int index() {
        return index;
    }

    public String tag() {
        return tag;
    }

    public String value() {
        return value;
    }

    public void setValue(String vl) {
        value = vl;
    }

    public String toString() {
        return clitic.isEmpty() ? value : clitic;
    }

    public void setIndex(int newIndex) {
        this.index = newIndex;
    }

    public String diac() {
        return diac;
    }

    public String stem() {
        return stem;
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

    public String per() {
        return per;
    }

    public String num() {
        return num;
    }

    public boolean isPunc() {
        return this.tag.equals("punc");
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
        return this.tag.equals("noun") && this.hasDT();
    }

    public boolean isPron() {
        return this.tag.equals("pron");
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
        return this.tag.startsWith("verb");
    }

    public boolean isDTJJ() {
        return this.tag.startsWith("adj") && hasDT();
    }

    public boolean isPrep() {
        return this.tag.equals("prep");
    }

    public boolean hasDT() {
        return this.prc0.equals("Al_det");
    }

    public boolean hasEnc() {
        return !this.enc0.toLowerCase().trim().equals("0")
                && !this.enc0.toLowerCase().trim().equals("na");
    }

    public boolean hasPrc() {
        return this.hasPrc1()
                || this.hasPrc2()
                || this.hasPrc3();
    }

    public boolean hasPrep() {

        return this.prc1.startsWith("bi")
                || this.prc1.startsWith("la")
                || this.prc1.startsWith("li")
                || this.prc1.endsWith("prep");
    }

    public boolean hasConj() {

        return this.prc1.startsWith("wa")
                || this.prc2.startsWith("fa")
                || this.prc2.startsWith("wa")
                || this.prc2.endsWith("prep")
                || isConj();

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
        return !this.prc1.equals("0") && !this.prc1.equals("na");
    }

    public boolean hasPrc2() {
        return !this.prc2.equals("0") && !this.prc2.equals("na");
    }

    public boolean hasPrc3() {
        return this.prc3 != null && this.prc3.equals("0") && !this.prc3.equals("na");
    }

    public boolean hasPrc0() {
        return !this.prc0.equals("0") && !this.prc0.equals("na");
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

    public void changeFeature(String name, String newV) {
        switch (name) {
            case "value": {
                value = newV;
                break;
            }
            case "index": {
                index = Integer.getInteger(newV);
                break;
            }
            case "clitic": {
                enc0 = newV;
                prc1 = newV;
                prc2 = newV;
                break;
            }
        }
    }

    public void cliticTrim() {
        this.changeFeature("value", this.stem());
        this.changeFeature("clitic", "0");
    }

    public String clitic() {
        this.nodiac = this.deDiacritic(this.diac());
        if (!nodiac.equals(value())) {
            System.out.println("Problem with dedicritic of :" + nodiac + "   and   " + value());
//            value = nodiac;
        }
        prc = this.hasPrc() ? precliticing() : null;
        if (this.hasDT()) {
            if (!this.deDiacritic(stem.substring(0, 2)).startsWith("ال")) {
                stem = "ال" + stem;
            }
        }

        if (this.value().endsWith("ة") && !this.stem().endsWith("ة")) {
            stem = stem + "ة";
            enc0 = "0";
        }

        encVal = this.hasEnc() ? this.encliticing() : null;

        clitic = this.prc != null ? prc : "";
        clitic += stem;
        clitic += this.encVal != null ? "/" + encVal : "";

        return this.deDiacritic(clitic);

    }

    String precliticing() {
        this.prc3Val = this.hasPrc3() ? this.getprc3() : null;
        this.prc2Val = this.hasPrc2() ? this.getprc2() : null;
        this.prc1Val = this.hasPrc1() ? this.getprc1() : null;
        String pr;
        pr = this.prc3Val != null ? prc3Val + "_" : "";
        pr += this.prc2Val != null ? prc2Val + "_" : "";
        pr += this.prc1Val != null ? prc1Val + "_" : "";
        return pr;
    }

    String getprc3() {
        return this.diac().substring(0, 2);
    }

    String getprc2() {
        return this.prc3Val != null ? this.diac().substring(2, 4) : this.diac().substring(0, 2);
    }

    String getprc1() {
        int length = this.prc1.split("_")[0].length();
        int inx = prc3Val != null ? 2 : 0;
        inx += prc2Val != null ? 2 : 0;
        return this.diac().substring(inx, length + inx);

    }

    List<Character> diacriticList() {
        Character[] diacritics = new Character[]{'َ', 'ِ', 'ُ', 'ْ', 'ّ', 'ً', 'ٍ', 'ٌ'};
        return Arrays.asList(diacritics);

    }

    public String deDiacritic(String term) {
        char[] chars = term.toCharArray();
        String res = "";

        for (Character ch : chars) {
            if (diacriticList().contains(ch)) {
                continue;
            } else if (ch.equals('ٱ')) {
                ch = 'ا';
            }
            res = res + ch;
        }
        return res;
    }

    String encliticing() {
        if (this.value().endsWith("ة")) {
//            this.nodiac = this.value();
            return null;
        }
        String enc, enc1 = "", enc2;
        String stemNodiac = this.deDiacritic(stem);
        if (this.hasDT()) {
            stemNodiac += "ال" + stemNodiac;
        }

        String[] slices = this.nodiac.split(stemNodiac);
        if (slices.length > 1) {
            enc1 = slices[1];
        }

        enc2 = this.encMap().get(this.enc0().split("_")[0]);
        if (!enc1.equals(enc2)) {
            System.out.println("Problem in enclitic: " + enc1 + "\t" + enc2);
        }

        enc = this.value().endsWith(enc2) ? enc2 : enc1;
        if (stemNodiac.endsWith(enc)) {
            stem = stemNodiac.substring(0, stemNodiac.length() - enc.length());
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
}

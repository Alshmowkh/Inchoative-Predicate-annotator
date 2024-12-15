package updating.phraser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import saarr_5.morphalize.Word;

public class Phrase extends ArrayList<Word> {

    private Type type;
    private Type tb;
    private int index;
    private int begin;
    private int end;

    public Phrase() {

    }

    public void index(int indx) {
        index = indx;
    }

    public int index() {
        return index;
    }

    public void begins(int start) {
        begin = start;
    }

    public void ends(int finish) {
        end = finish;
    }

    public int begins() {
        return begin;
    }

    public int ends() {
        return end;
    }

    public String text() {
        StringBuilder sb = new StringBuilder();
        for (Word w : this) {
            sb.append(w.value() + " ");
        }
        return sb.toString();
    }

    static class Types extends HashMap<String, Type> {

        /*
         part_voc يا
         part_fut سوف
         
         part أياكم
         part_verb قد
         abbrev كلغ
         
         interj;كلاهما،كلهم ،...
         part_restrict
         part_focus أما
         */
        public Types() {
            this.put("noun", Type.NOUN);
            this.put("noun_num", Type.NOUN);
            this.put("noun_quant", Type.NOUN);
            this.put("noun_prop", Type.DTNOUN);
            this.put("noun_det", Type.DTNOUN);
            this.put("adj_det", Type.DTNOUN);//..............
            this.put("noun_place", Type.PLACE);
            this.put("noun_time", Type.TIME);
            this.put("part_neg", Type.VERBAL);
            this.put("verb", Type.VERBAL);
            this.put("digit", Type.NUMBIRICAL);
            this.put("verb_pseudo", Type.VERBAL);
            this.put("latin", Type.FORIGEN);
            this.put("conj", Type.CONJUNCTION);
            this.put("conj_sub", Type.CONJUNCTION);
            this.put("adj", Type.ADJECTIVE);
            this.put("adj_num", Type.ADJECTIVE);
            this.put("part_interrog", Type.INTERROGATION);
            this.put("pron_interrog", Type.INTERROGATION);
            this.put("adv_interrog", Type.INTERROGATION);
            this.put("adv_rel", Type.INTERROGATION);
            this.put("prep", Type.PREPOSITION);
            this.put("pron", Type.PRONOUN);
            this.put("pron_rel", Type.RELATIVE);
            this.put("pron_dem", Type.DEMONSTRATIVE);
            this.put("adv", Type.ADVERBIAL);
            this.put("adj_comp", Type.ADJECTIVE);
            this.put("pron_exclam", Type.EXCLAMATION);
            this.put("punc", Type.Punc);
            //------------ PATB tagset

        }

        public Type getType(String tag) {

            return this.get(tag);
        }

    }

    static class TBTags extends HashMap<Type, Type> {

        public TBTags() {
            this.put(Type.NOUN, Type.NP);
            this.put(Type.DTNOUN, Type.DTNP);
            this.put(Type.VERBAL, Type.VP);
            this.put(Type.CONJUNCTION, Type.CP);
            this.put(Type.ADJECTIVE, Type.AJP);
            this.put(Type.INTERROGATION, Type.QP);
            this.put(Type.PREPOSITION, Type.PP);
            this.put(Type.PRONOUN, Type.PRP);
            this.put(Type.RELATIVE, Type.RP);
            this.put(Type.DEMONSTRATIVE, Type.DP);
            this.put(Type.ADVERBIAL, Type.AVP);
            this.put(Type.EXCLAMATION, Type.EP);
            this.put(Type.Punc, Type.S);

        }

        public Type getType(String tag) {

            return this.get(tag);
        }

    }

    public enum Type {

        NOUN, ADJECTIVE, DTNOUN, PREPOSITION, RELATIVE,
        CONJUNCTION, ADVERBIAL, VERBAL, PRONOUN, PLACE,
        TIME, NUMBIRICAL, FORIGEN, INTERROGATION,
        DEMONSTRATIVE, EXCLAMATION, Punc,
        NP, AJP, DTNP, PP, RP, CP, AVP, VP, PRP, PTP, QP, DP, EP, S;
    }

    public void setType() {
        Word fw;

        if (!this.isEmpty() && this != null) {
            fw = this.get(0);
            this.type = new Types().getType(fw.tag());
            this.tb = new TBTags().get(this.type);
        }
    }

    public Type getType() {

        return type;
    }

    public Type getTypeTB() {

        return tb;
    }

    public boolean isNP() {
        return this.getTypeTB() == Type.NP;
    }

    public boolean isDTNP() {
        return this.getTypeTB() == Type.DTNP;
    }

    public boolean isAJP() {
        return this.getTypeTB() == Type.AJP;
    }

    public boolean isPP() {
        return this.getTypeTB() == Type.PP;
    }

    public boolean isRP() {
        return this.getTypeTB() == Type.RP;
    }

    public boolean isCP() {
        return this.getTypeTB() == Type.CP;
    }

    public boolean isAVP() {
        return this.getTypeTB() == Type.AVP;
    }
//     NP, AJP, DTNP, PP, RP, CP, AVP, VP, PRP, PTP, QP, DP, EP;

    public boolean isVP() {
        return this.getTypeTB() == Type.VP;
    }

    public boolean isPRP() {
        return this.getTypeTB() == Type.PRP;
    }

    public boolean isPTP() {
        return this.getTypeTB() == Type.PTP;
    }

    public boolean isQP() {
        return this.getTypeTB() == Type.QP;
    }

    public boolean isDP() {
        return this.getTypeTB() == Type.DP;
    }

    public boolean isEP() {
        return this.getTypeTB() == Type.EP;
    }

    public boolean idafa() {
        return this.isAJP() || this.isAVP() || this.isDP()
                || this.isDTNP() || this.isNP() || this.isPRP() || this.isPTP();
    }

    public boolean isS() {
        return this.getTypeTB() == Type.S;
    }
}

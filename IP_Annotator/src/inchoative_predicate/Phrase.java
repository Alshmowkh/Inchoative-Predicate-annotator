package inchoative_predicate;

import java.util.ArrayList;

public class Phrase extends ArrayList<Word> {

    public Phrase() {

    }
public enum type{
    NOUN,
    ADJECTIVE,
    DTNOUN,
    PREPOSITION,
    RELATIVE,
    CONJUNCTION,
    ADVERBIAL,
    VERBAL,
    PRONOUN,
    PLACE,
    TIME,
    NUMBIRICAL,
    FORIGEN,
    INTERROGATION,
}
//    public Phrase(Sentence sentence, int index) {
//        sentence = (Sentence) sentence.subList(index, sentence.size());
//        this.addAll((Phrase) sentence.clone());
//    }

    public boolean isVerbal() {
        return this.get(0).isVerb()
                || this.get(0).isPrep() && this.size() > 0 && this.get(1).isVerb();
    }

}

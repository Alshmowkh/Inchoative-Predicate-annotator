package updating.phraser;

import saarr_5.morphalize.Sentence;
import saarr_5.morphalize.Word;

public class Phraser {

    private Sentence reIndexing(Sentence sentence) {
        sentence.stream().forEach((w) -> {
            w.setIndex(w.index() - 1);
        });

        return sentence;
    }

    private Sentence normlize(Sentence words, int index) {

        Word fw = words.get(index);
        if (fw.isConj()) {
            words.remove(index);
            words = reIndexing(words);
            words = normlize(words, index);
        }
        return words;
    }

    private Phrase readPhrase(Sentence sentence, int offset) {
        Phrase phrase = new Phrase();
        Word w1, w2;
        int i = offset;
        boolean reduce = true;

        for (i = offset; i < sentence.size() - 1 && reduce; i++) {
            w1 = sentence.get(i);

            if (w1.isPrep() && !w1.hasEnc()
                    || w1.isConj()
                    || w1.isInterrogation()
                    || w1.isPunc()
                    || w1.isRel()) {

                phrase.add(w1);
                phrase.setType();
                return phrase;
//                i++;
//                w1 = sentence.get(i);
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
                || w1.isNoun() && w2.isDTnoun() && !w1.hasEnc()
                || w1.isDTnoun() && w2.isDTnoun() && !w2.hasPrc1()
                || w1.isNoun() && w2.isPnoun() && !w1.hasEnc()
                || w1.isPnoun() && w2.isPnoun() && !w2.hasConj()
                || w1.isDTnoun() && w2.isPnoun()
                || w1.isPnoun() && w2.isDTnoun()
                || w1.isPronX() && w2.isDTnoun()
                //-------------------------------
                || w1.isNoun() && w2.isDTadj() //                || w1.isAdj() && w2.isAdj()
                || w1.isDTnoun() && w2.isDTadj() //                || w1.isAdj() && w2.isAdj()
                || w1.isAdj() && w2.isAdj()
                || w1.isDTadj() && w2.isDTadj()
                //--------------------------------
                || w1.isPartNeg() && w2.isVerb()
                //--------------------------------- 2019
//                || w1.isNoun() && w1.isPnoun()
                ;
    }

    public static Sentence phrasing(Sentence taggedSentence) {

        if (taggedSentence == null) {
            return null;
        }
        if (taggedSentence.isEmpty()) {
            return null;
        }
        if (taggedSentence.getPhrases() != null) {
            return taggedSentence;
        }
        if (taggedSentence.size() < 2) {
            return taggedSentence;
        }
        Phraser phraser = new Phraser();
        taggedSentence = phraser.normlize(taggedSentence, 0);
        Phrase phrase;

        int index = 0;
        int size;
        int phraseIndex = 0;
        while (index < taggedSentence.count() - 1) {
            if (taggedSentence.get(index).isPunc()) {
                phrase = new Phrase();
                while (index < taggedSentence.count() - 1) {

                    phrase.add(taggedSentence.get(index));
                    index++;
                    if (taggedSentence.get(index).isPunc()) {
                        phrase.add(taggedSentence.get(index));
                        index++;
                        break;
                    }
                }

                taggedSentence.addPhrase(phrase);
                size = phrase.size();
                phrase.setType();
                phrase.begins(phrase.get(0).index());
                phrase.ends(phrase.get(size - 1).index());
                phrase.index(phraseIndex++);
            }
            phrase = phraser.readPhrase(taggedSentence, index);
            size = phrase.size();
            if (size > 0) {
                phrase.setType();
                phrase.begins(index);
                phrase.ends(index + size);
                phrase.index(phraseIndex++);
                taggedSentence.addPhrase(phrase);
                index = phrase.get(size - 1).index() + 1;
            }
            for (Word w : phrase) {
                w.phraseID(phrase.index());
            }
        }

        return taggedSentence;
    }

    public static Sentence rePhrasing(Sentence taggedSentence) {

        if (taggedSentence == null) {
            return null;
        }
        if (taggedSentence.isEmpty()) {
            return null;
        }

        if (taggedSentence.size() < 2) {
            return taggedSentence;
        }
        taggedSentence.dumpPhrases();
        Phraser phraser = new Phraser();
        taggedSentence = phraser.normlize(taggedSentence, 0);
        Phrase phrase;

        int index = 0;
        int size;
        int phraseIndex = 0;
        while (index < taggedSentence.count() - 1) {
            if (taggedSentence.get(index).isPunc()) {
                phrase = new Phrase();
                while (index < taggedSentence.count() - 1) {

                    phrase.add(taggedSentence.get(index));
                    index++;
                    if (taggedSentence.get(index).isPunc()) {
                        phrase.add(taggedSentence.get(index));
                        index++;
                        break;
                    }
                }

                taggedSentence.addPhrase(phrase);
                size = phrase.size();
                phrase.setType();
                phrase.begins(phrase.get(0).index());
                phrase.ends(phrase.get(size - 1).index());
                phrase.index(phraseIndex++);
            }
            phrase = phraser.readPhrase(taggedSentence, index);
            size = phrase.size();
            if (size > 0) {
                phrase.setType();
                phrase.begins(index);
                phrase.ends(index + size);
                phrase.index(phraseIndex++);
                taggedSentence.addPhrase(phrase);
                index = phrase.get(size - 1).index() + 1;
            }
            for (Word w : phrase) {
                w.phraseID(phrase.index());
            }
        }

        return taggedSentence;
    }

}

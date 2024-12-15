/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updating;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author bakee
 */
public class Letter extends Word {

    String value;
    String type;
    private List<String> abolishers;
    private List<String> disjointPrepositions;
    private List<Character> jointPrepositions;
    private List<Character> jointConjunctions;
    private String[] enna;
    private String[] cana;
    private String[] array;

    private List<String> placeNouns;
    private List<String> timeNouns;

    public List<String> abolishers() {
        this.enna = new String[]{"إن", "لكن", "ليت","لعلي", "لعلى", "كأن", "أن","ان"};
        this.cana = new String[]{"كان", "كانت","يكون", "كانوا", "كنت", "بات", "يصير", "ظل", "امسى","اصبحت", "اصبح", "صار", "مادام", "مابرح", "ما انفك", "ليس", "مازال"};
        abolishers = new ArrayList();
        abolishers.addAll(Arrays.asList(enna));
        abolishers.addAll(Arrays.asList(cana));
        return abolishers;
    }

    public List<String> disjointPrepositions() {
        array = new String[]{"متى", "كي", "منذ", "عدا", "على", "عن", "إلى", "من"};
        disjointPrepositions.addAll(Arrays.asList(array));
        return disjointPrepositions;
    }

    public List<Character> jointPrepositions() {
        jointPrepositions = new ArrayList();
        jointPrepositions.add('ب');
        jointPrepositions.add('ل');
        jointPrepositions.add('ك');
        return jointPrepositions;
    }

    public List<Character> jointConjunctions() {
        jointConjunctions = new ArrayList();
        jointConjunctions.add('و');
        jointConjunctions.add('ف');
        jointConjunctions.add('ك');
        return jointConjunctions;
    }

    public List<String> placeNouns() {
        if (placeNouns != null) {
            return placeNouns;
        }
        this.placeNouns = Arrays.asList("فوق", "تحت", "يمين", "شمال", "يسار",
                "أمام", "قدام", "خلف", "وراء", "بعد", "قبل", "عند", "لدى",
                "لد", "لدن");

        return placeNouns;
    }
     public List<String> timeNouns() {
        if (timeNouns != null) {
            return timeNouns;
        }
        this.timeNouns = Arrays.asList("أمس","امس","يوم","غد","غدا","عام","قبل","بعد","الآن","الان","آن");

        return timeNouns;
    }

}

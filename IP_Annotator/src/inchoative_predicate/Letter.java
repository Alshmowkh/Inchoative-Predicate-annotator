/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inchoative_predicate;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
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
    private String[] enna;
    private String[] cana;
    private String[] array;
    
    public List<String> abolishers() {
        this.enna = new String[]{"إن", "لكن", "ليت", "لعلى", "كأن", "أن"};
        this.cana = new String[]{"كان", "كانت", "كانوا", "كنت", "بات", "يصير", "ظل", "امسى", "اصبح", "صار", "مادام", "مابرح", "ما انفك", "ليس", "مازال"};
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
}

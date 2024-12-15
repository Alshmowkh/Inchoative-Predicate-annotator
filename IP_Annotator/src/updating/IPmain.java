/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updating;

import static utils.Miss.pl;

/**
 *
 * @author bakee
 */
public class IPmain {

    private final String sourceDS = "F:\\Master\\Thesis\\Prototype\\Papers\\Inchoative and Predicate Annotation in Arabic Nominal Sentence\\Dataset\\PADTds_00.pds";

    void loadDocument() {
        Document doc =Document.load(sourceDS);
        pl(doc.size());
        
    }

    public static void main(String[] args) {
        IPmain main = new IPmain();
        main.loadDocument();
    }
}

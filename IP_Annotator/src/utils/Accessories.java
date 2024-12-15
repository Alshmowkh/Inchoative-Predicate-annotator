package utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Accessories {

    public static List<Character> diacriticList() {
        Character[] diacritics = new Character[]{'َ', 'ِ', 'ُ', 'ْ', 'ّ', 'ً', 'ٍ', 'ٌ'};
        return Arrays.asList(diacritics);

    }

    public static String deDiacritic(String term) {
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

    public String encArb(String enc) {

        switch (enc) {
            case "1s": {
                return "ي";
            }
            case "lp": {
                return "نا";
            }
            case "2d": {
                return "ا";
            }
            case "2p": {
                return "";
            }
            default:
                return "";
        }
    }


}

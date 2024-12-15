package madamiray;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SourceFile
import edu.columbia.ccls.common.almor.LanguageOrDialect;
import edu.columbia.ccls.common.almor.b;
import edu.columbia.ccls.common.almor.i;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.log4j.Logger;

// Referenced classes of package edu.columbia.ccls.madamira:
//            CogentModeledType, GazetteerType, LMModeledType, ModeledType, 
//            a, d
public class py2 {

    private static py2 b;
    private static b c;
    private static Map d;
    private static Set l;
    private static HashMap m = new HashMap();

    private py2() throws ClassNotFoundException, IOException {
        c = edu.columbia.ccls.common.almor.b.a();
        d = new EnumMap(Class.forName("edu.columbia.ccls.common.almor.LanguageOrDialect"));
    }

    public static py2 b()  {
        if (b == null) {
            l = new HashSet(Arrays.asList(LanguageOrDialect.values()));
            try {
                synchronized (Class.forName("madamiray.py2")) //            synchronized(edu/columbia/ccls/madamira/p)
                {
                    if (b == null) {
                        try {
                            b = new py2();
                        } catch (IOException ex) {
                            Logger.getLogger(py2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(py2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return b;
    }

    public static i a(LanguageOrDialect languageordialect) {
        if (!d.containsKey(languageordialect)) {
            Object obj = languageordialect;
         
            obj = new i(c.a(((LanguageOrDialect) (obj))));
            d.put(languageordialect, obj);
        }
        return (i) d.get(languageordialect);
    }

}

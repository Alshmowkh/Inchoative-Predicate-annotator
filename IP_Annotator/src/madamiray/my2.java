// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SourceFile
package madamiray;

import edu.columbia.ccls.common.almor.AnalyzerBackoffMode;
import edu.columbia.ccls.common.almor.LanguageOrDialect;
import edu.columbia.ccls.common.almor.d;
import edu.columbia.ccls.common.almor.i;
import edu.columbia.ccls.madamira.CacheMap;
import edu.columbia.ccls.madamira.MADAWordType;
import edu.columbia.ccls.madamira.configuration.MadamiraConfiguration;
import edu.columbia.ccls.madamira.g;
import edu.columbia.ccls.madamira.k;
import java.util.*;

// Referenced classes of package edu.columbia.ccls.madamira:
//            CacheMap, MADAWordType, g, k, 
//            p
public final class my2 {

    private my2(LanguageOrDialect languageordialect, AnalyzerBackoffMode analyzerbackoffmode) {
        a = analyzerbackoffmode;
        b = languageordialect;
//        int a = java.time.LocalTime.now().getSecond();
        c = py2.a(languageordialect);

//        int z = java.time.LocalTime.now().getSecond();
//        pl(z - a);

        d = new CacheMap(1000);
    }

    public my2(MadamiraConfiguration madamiraconfiguration) {
        this(LanguageOrDialect.a, AnalyzerBackoffMode.a);
    }

    public final void a(g g1) {
        g1.a(a);
        g1.a(b);

//        System.out.println(MADAWordType.a);
        for (int j = 0; j < g1.a(); j++) {

            if (g1.c(j).b() != MADAWordType.a) {
                continue;
            }
            String s = g1.a(j);
            k k1 = g1.c(j);
//            System.out.println(d);

            List list;
            if (!d.containsKey(s)) {
                list = c.a(s, a);
                d.put(s, a(list));
            } else {
                list = a((List) d.get(s));
            }
            k1.a(list);
            if (list.size() <= 0) {
                k1.a(MADAWordType.c);
            }
        }

    }

    private static List a(List list) {
        ArrayList arraylist = new ArrayList(list.size());
        d d1;
        for (Iterator itr = list.iterator(); itr.hasNext(); arraylist.add(new d(d1))) {
            d1 = (d) itr.next();
        }

        return arraylist;
    }

    private AnalyzerBackoffMode a;
    private LanguageOrDialect b;
    private i c;
    private CacheMap d;
}

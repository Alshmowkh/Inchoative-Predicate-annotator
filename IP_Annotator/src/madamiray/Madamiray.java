package madamiray;

import edu.columbia.ccls.madamira.TaskSet;
import edu.columbia.ccls.madamira.configuration.InSeg;
import edu.columbia.ccls.madamira.configuration.MadamiraConfiguration;
import edu.columbia.ccls.madamira.configuration.OutSeg;
import edu.columbia.ccls.madamira.configuration.OverallVars;
import edu.columbia.ccls.madamira.configuration.Preprocessing;
import edu.columbia.ccls.madamira.configuration.ReqVariable;
import edu.columbia.ccls.madamira.configuration.RequestedOutput;
import edu.columbia.ccls.madamira.configuration.Scheme;
import edu.columbia.ccls.madamira.configuration.Tokenization;
import edu.columbia.ccls.madamira.g;
import edu.columbia.ccls.madamira.l;
import edu.columbia.ccls.madamira.o;
import edu.columbia.ccls.utils.arabic.Transliteration;
import java.util.ArrayList;
import java.util.List;

public class Madamiray {

//    String sentence;
    List<String> sentences;

    public Madamiray(String sent) {
//        sentence = sent;
        sentences = new ArrayList();
        sentences.add(sent);
    }

    public Madamiray(List<String> sents) {
        sentences = sents;
    }

    public List<OutSeg> getMorpholizing() {

        MadamiraConfiguration Config = createConfig();
        Transliteration trans = Transliteration.c;

        Boolean flag = false;
        OutSeg outseg;
        List<OutSeg> outsegs = new ArrayList();
        Config = edu.columbia.ccls.madamira.c.a(b(Config), Config);

        py2.b();
        TaskSet taskset = new TaskSet(Config);

        my2 m1 = new my2(Config);

        InSeg inSeg;
        for (String sentence : sentences) {
            inSeg = new InSeg();
            outseg = new OutSeg();
            inSeg.setValue(sentence);
            g g1 = o.a(Config, inSeg.getValue());
            m1.a(g1);
            l.a(g1, Config, taskset, outseg, flag, trans);
            outsegs.add(outseg);
        }

        return outsegs;
    }

    public MadamiraConfiguration createConfig() {
        MadamiraConfiguration config = new MadamiraConfiguration();
        Preprocessing preproc = new Preprocessing();
        preproc.setInputEncoding("UTF8");
        preproc.setSentenceIds(false);
        preproc.setSeparatePunct(true);
        config.setPreprocessing(preproc);

        OverallVars overV = new OverallVars();
        overV.setDialect("MSA");
        overV.setMorphBackoff("NONE");
        overV.setOutputAnalyses("TOP");
        overV.setOutputEncoding("UTF8");
        config.setOverallVars(overV);

        RequestedOutput reqO = createRequestedOutput();
        config.setRequestedOutput(reqO);

        Tokenization tokens = new Tokenization();
        Scheme sc = new Scheme();
        sc.setAlias("ATB");
        tokens.getScheme().add(sc);
        sc = new Scheme();
        sc.setAlias("D3_BWPOS");
        tokens.getScheme().add(sc);

        config.setTokenization(tokens);

        config.getOverallVars().setAnalyzeOnly(true);
        return config;
    }

    RequestedOutput createRequestedOutput() {
        RequestedOutput reqO = new RequestedOutput();
        ReqVariable rv;
        String[] vars = {"PREPROCESSED", "DIAC", "POS", "STEM", "GLOSS", "PER", "PRC0", "PRC1", "PRC2", "ENC0", "BW"};
        for (String v : vars) {
            rv = new ReqVariable();
            rv.setName(v);
            rv.setValue(true);
            reqO.getReqVariable().add(rv);
        }
        return reqO;
    }

    MadamiraConfiguration b(MadamiraConfiguration config) {
        MadamiraConfiguration b = new MadamiraConfiguration();

        if (b.getPreprocessing() == null) {

            b.setPreprocessing(new Preprocessing());
        }
        if (b.getOverallVars() == null) {
            b.setOverallVars(new OverallVars());
        }
        if (b.getRequestedOutput() == null) {

            b.setRequestedOutput(new RequestedOutput());
        }
        if (b.getTokenization() == null) {
            b.setTokenization(new Tokenization());
        }

        return b;
    }

}

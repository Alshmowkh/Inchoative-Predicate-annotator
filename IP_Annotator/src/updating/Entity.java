/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updating;

import java.util.List;
import updating.phraser.Phrase.Type;

/**
 *
 * @author bakee
 */
public class Entity {

    private Word alpha;
    private Word beta;
    private Type type;
    private List alphaTree;
    private List betaTree;
    private String alphaEntity;
    private String betaEntity;

    public Entity() {

    }

    public void setAlphaOntoPath(List list) {
        alphaTree = list;
    }

    public void setBetaOntoPath(List list) {
        betaTree = list;
    }

    public List getAlphaOntoPath() {
        return alphaTree;
    }

    public List getBetaOntoPath() {
        return betaTree;
    }

    public void setAlphaEntity(String ent) {
        alphaEntity = ent;
    }

    public void setBetaEntity(String ent) {
        betaEntity = ent;
    }

    public String getAlphaEntity() {
        return alphaEntity;
    }

    public String getBetaEntity() {
        return betaEntity;
    }

    public void setType(Type typ) {
        type = typ;
    }

    public Type getType() {
        return type;
    }

    public Entity(Word left, Word right) {
        alpha = left;
        beta = right;
    }

    public Entity(Word left, Word right, Type typ) {
        alpha = left;
        beta = right;
        type = typ;
    }

    public void setAlpha(Word left) {
        alpha = left;
    }

    public void setBeta(Word right) {
        beta = right;
    }

    public Word getAlpha() {
        return alpha;
    }

    public Word getBeta() {
        return beta;
    }

    @Override
    public String toString() {
        return alpha + " : " + beta;
    }

//    public enum Type {
//
//        ESF_IP, ESF_AG, ESF_Cg, ESF_Cr,
//        Cognater_EMPHASIS, Cognater_DESCRIPTIVE, Cognater_NUMERICAL;
//    }
}

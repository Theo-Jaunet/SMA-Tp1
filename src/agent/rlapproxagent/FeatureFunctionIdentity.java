package agent.rlapproxagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import environnement.Action;
import environnement.Action2D;
import environnement.Etat;
import javafx.util.Pair;

/**
 * Vecteur de fonctions caracteristiques phi_i(s,a): autant de fonctions
 * caracteristiques que de paire (s,a),
 * <li> pour chaque paire (s,a), un seul phi_i qui vaut 1 (vecteur avec un seul
 * 1 et des 0 sinon).
 * <li> pas de biais ici
 *
 * @author laetitiamatignon
 */
public class FeatureFunctionIdentity implements FeatureFunction {

    //*** VOTRE CODE
//    protected double[] vecteurFunc;
    //    protected ArrayList<Integer> etatsDejaVu;
    protected HashMap<Pair<Etat, Action>, double[]> memory;
    protected int nbAction;
    protected int nbEtats;
    protected int memoryLength;

    public FeatureFunctionIdentity(int _nbEtat, int _nbAction) {
        //*** VOTRE CODE
//        this.vecteurFunc = new double[_nbEtat * _nbAction];
//        this.etatsDejaVu = new ArrayList<>();
        this.memory = new HashMap<>();
        this.nbAction = _nbAction;
        this.nbEtats = _nbEtat;
        this.memoryLength = 0;
    }

    @Override
    public int getFeatureNb() {
        //*** VOTRE CODE
//        return this.vecteurFunc.length;
        return this.nbAction * this.nbEtats;
//		return 0;
    }

    @Override
    public double[] getFeatures(Etat e, Action a) {
        //*** VOTRE CODE
/*        int index;
        if (!this.etatsDejaVu.contains(e.hashCode())) {
            index = this.etatsDejaVu.size();
            this.etatsDejaVu.add(e.hashCode());
        } else {
            index = this.etatsDejaVu.indexOf(e.hashCode());
        }
        for (int indCouple = 0; indCouple < this.getFeatureNb(); indCouple++) {
            this.vecteurFunc[indCouple] = 0;
        }

        this.vecteurFunc[index * this.nbAction + a.ordinal()] = 1;
        return this.vecteurFunc;
    }*/
        int size = this.getFeatureNb();
        Pair couple = new Pair(e, a);
        if (this.memory.containsKey(couple)){
            return this.memory.get(couple);
        }
        else{
            double[] newVec = new double[size];
            for (int ind = 0; ind < size; ind ++){
                newVec[ind] = 0;
            }
            newVec[this.memoryLength] = 1;
            this.memoryLength += 1;
            this.memory.put(couple, newVec);
        }
        return this.memory.get(couple);
    }

}

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
 *
 */
public class FeatureFunctionIdentity implements FeatureFunction {

    //*** VOTRE CODE
    protected double[] vecteurFunc;
    protected ArrayList<Integer> etatsDejaVu;
    protected int nbAction;

    public FeatureFunctionIdentity(int _nbEtat, int _nbAction) {
        //*** VOTRE CODE
        this.vecteurFunc = new double[_nbEtat * _nbAction];
        this.etatsDejaVu = new ArrayList<>();
        this.nbAction = _nbAction;
    }

    @Override
    public int getFeatureNb() {
        //*** VOTRE CODE
        return this.vecteurFunc.length;
//		return 0;
    }

    @Override
    public double[] getFeatures(Etat e, Action a) {
        //*** VOTRE CODE
        int index;
        if (!this.etatsDejaVu.contains(e.hashCode())) {
            index = this.etatsDejaVu.size();
            this.etatsDejaVu.add(e.hashCode());
        }
        else{
            index = this.etatsDejaVu.indexOf(e.hashCode());
        }
        for (int indCouple = 0; indCouple < this.getFeatureNb(); indCouple++) {
            this.vecteurFunc[indCouple] = 0;
        }

        this.vecteurFunc[index * this.nbAction + a.ordinal()] = 1;
        return this.vecteurFunc;
    }

}

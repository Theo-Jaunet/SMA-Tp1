package agent.planningagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.Random;

import util.HashMapUtil;

import java.util.HashMap;

import environnement.Action;
import environnement.Etat;
import environnement.IllegalActionException;
import environnement.MDP;
import environnement.Action2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cet agent met a jour sa fonction de valeur avec value iteration et choisit
 * ses actions selon la politique calculee.
 *
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent {

    /**
     * discount facteur
     */
    protected double gamma;
    private final double defaultV0 = 0.0;
    private int nbIterations;

    /**
     * fonction de valeur des etats
     */
    protected HashMap<Etat, Double> V;

    /**
     *
     * @param gamma
     * @param nbIterations
     * @param mdp
     */
    public ValueIterationAgent(double gamma, MDP mdp) {
        super(mdp);

        this.gamma = gamma;
        this.nbIterations = 0;
        V = new HashMap<>();
        ArrayList<Etat> etats = (ArrayList<Etat>) mdp.getEtatsAccessibles();

        for (Etat e : etats) {
            V.put(e, defaultV0);
        }

        //*** VOTRE CODE
    }

    public ValueIterationAgent(MDP mdp) {
        this(0.9, mdp);

    }

    /**
     *
     * Mise a jour de V: effectue UNE iteration de value iteration (calcule
     * V_k(s) en fonction de V_{k-1}(s')) et notifie ses observateurs. Ce n'est
     * pas la version inplace (qui utilise nouvelle valeur de V pour mettre a
     * jour ...)
     */
    @Override
    public void updateV() {
        //delta est utilise pour detecter la convergence de l'algorithme
        //lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
        //delta < epsilon 
        this.delta = 0.0;

        HashMap<Etat, Double> oldV = (HashMap<Etat, Double>) V.clone();

        //*** VOTRE CODE
        for (Map.Entry<Etat, Double> entry : oldV.entrySet()) {
            Etat s = entry.getKey();
            if (!mdp.estAbsorbant(s)) {
                ArrayList<Action> actions = (ArrayList<Action>) mdp.getActionsPossibles(s);

                ArrayList<Double> listefinale = new ArrayList<Double>();

                for (Action a : actions) {

                    Map<Etat, Double> transProba = null;
                    try {
                        transProba = mdp.getEtatTransitionProba(s, a);
                    } catch (Exception ex) {
                        Logger.getLogger(ValueIterationAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    double sommeListeSp = 0.0;

                    for (Map.Entry<Etat, Double> entry2 : transProba.entrySet()) {
                        Etat sp = entry2.getKey();
                        Double prob = entry2.getValue();
                        double recomp = mdp.getRecompense(s, a, sp);

                        sommeListeSp += prob * (recomp + this.getGamma() * oldV.get(sp));
                    }
                    listefinale.add(sommeListeSp);
                }
                V.put(s, Collections.max(listefinale));
                double tempDelta = Math.abs(V.get(s) - oldV.get(s));
                if (tempDelta > this.delta){
                    this.delta = tempDelta;
                }
            }
        }

        this.vmax = Collections.max(V.values());
        this.vmin = Collections.min(V.values());
        //*** VOTRE CODE

        // mise a jour vmax et vmin pour affichage du gradient de couleur:
        //vmax est la valeur de max pour tout s de V
        //vmin est la valeur de min pour tout s de V
        // ...
        //******************* laisser notification a la fin de la methode	
        this.notifyObs();
    }

    /**
     * renvoi l'action executee par l'agent dans l'etat e Si aucune actions
     * possibles, renvoi Action2D.NONE
     */
    @Override
    public Action getAction(Etat e) {
        //*** VOTRE CODE
        List<Action> possibleActions = new ArrayList<Action>();
        possibleActions = this.getPolitique(e);
        if (possibleActions.isEmpty()){
            return Action2D.NONE;
        }
        else{
            if (possibleActions.size() == 1){
                return possibleActions.get(0);
            }
            else{
                Random rand = new Random();
                int randomNum = rand.nextInt((possibleActions.size()) + 1);
                return possibleActions.get(randomNum);
            }
        }

    }

    @Override
    public double getValeur(Etat _e) {
        //*** VOTRE CODE
        return V.get(_e);
    }

    /**
     * renvoi action(s) de plus forte(s) valeur(s) dans etat (plusieurs actions
     * sont renvoyees si valeurs identiques, liste vide si aucune action n'est
     * possible)
     */
    @Override
    public List<Action> getPolitique(Etat _e) {
        //*** VOTRE CODE
        // retourne action de meilleure valeur dans _e selon V, 
        // retourne liste vide si aucune action legale (etat absorbant)
        List<Action> returnactions = new ArrayList<Action>();
        if (!mdp.estAbsorbant(_e)) {
            ArrayList<Action> actions = (ArrayList<Action>) mdp.getActionsPossibles(_e);
            HashMap<Action, Double> mapActionValue = new HashMap<Action, Double>();

            for (Action a : actions) {
                Map<Etat, Double> transProba = null;
                try {
                    transProba = mdp.getEtatTransitionProba(_e, a);
                } catch (Exception ex) {
                    Logger.getLogger(ValueIterationAgent.class.getName()).log(Level.SEVERE, null, ex);
                }

                double sommeListeSp = 0.0;

                for (Map.Entry<Etat, Double> entry2 : transProba.entrySet()) {
                    Etat sp = entry2.getKey();
                    Double prob = entry2.getValue();
                    double recomp = mdp.getRecompense(_e, a, sp);

                    sommeListeSp += prob * (recomp + this.getGamma() * V.get(sp));
                }
                mapActionValue.put(a, sommeListeSp);
            }
            double maxv = Double.NEGATIVE_INFINITY;
            for (Map.Entry<Action, Double> couple : mapActionValue.entrySet()) {
                Double val = couple.getValue();
                if (val > maxv) {
                    maxv = val;
                }
            }
            for (Map.Entry<Action, Double> couple : mapActionValue.entrySet()) {
                Double val = couple.getValue();
                if (val == maxv) {
                    returnactions.add(couple.getKey());
                }
            }
        }
        return returnactions;
    }

    @Override
    public void reset() {
        super.reset();

        this.V.clear();
        for (Etat etat : this.mdp.getEtatsAccessibles()) {
            V.put(etat, 0.0);
        }
        this.notifyObs();
    }

    public HashMap<Etat, Double> getV() {
        return V;
    }

    public double getGamma() {
        return gamma;
    }

    @Override
    public void setGamma(double _g) {
        System.out.println("gamma= " + gamma);
        this.gamma = _g;
    }

}

package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.util.Pair;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
import java.util.Collections;
import java.util.Map;

/**
 * Renvoi 0 pour valeurs initiales de Q
 *
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {

    /**
     * format de memorisation des Q valeurs: utiliser partout setQValeur car
     * cette methode notifie la vue
     */
//	protected HashMap<Etat,HashMap<Action,Double>> qvaleurs;
    //AU CHOIX: vous pouvez utiliser une Map avec des Pair pour clés si vous préférez
    protected HashMap<Pair<Etat, Action>, Double> qvaleurs;

    /**
     *
     * @param alpha
     * @param gamma
     * @param Environnement
     * @param nbS attention ici il faut tous les etats (meme obstacles) car Q
     * avec tableau ...
     * @param nbA
     */
    public QLearningAgent(double alpha, double gamma,
        Environnement _env) {
        super(alpha, gamma, _env);
        this.qvaleurs = new HashMap<Pair<Etat, Action>, Double>();
        this.alpha = alpha;
        this.gamma = gamma;
        this.env = _env;

    }

    /**
     * renvoi action(s) de plus forte(s) valeur(s) dans l'etat e (plusieurs
     * actions sont renvoyees si valeurs identiques) renvoi liste vide si
     * aucunes actions possibles dans l'etat (par ex. etat absorbant)
     *
     */
    @Override
    public List<Action> getPolitique(Etat e) {
        // retourne action de meilleures valeurs dans _e selon Q : utiliser getQValeur()
        // retourne liste vide si aucune action legale (etat terminal)
        List<Action> returnactions = new ArrayList<Action>();
        if (this.getActionsLegales(e).size() == 0) {//etat  absorbant; impossible de le verifier via environnement
            System.out.println("aucune action legale");
            return new ArrayList<Action>();
        }
        List<Action> actionsNext = this.env.getActionsPossibles(e);
        HashMap<Action, Double> mapActionToValue = new HashMap<>();
        double refQ = Double.NEGATIVE_INFINITY;
        double tempQ = 0;
        for (Action act : actionsNext) {
            tempQ = this.getQValeur(e, act);
            mapActionToValue.put(act, tempQ);
            if (tempQ > refQ) {
                refQ = tempQ;
            }
        }
        double maxQ = Double.NEGATIVE_INFINITY;
        for (Map.Entry<Action, Double> couple : mapActionToValue.entrySet()) {
            Double val = couple.getValue();
            if (val > maxQ) {
                maxQ = val;
            }
        }
        for (Map.Entry<Action, Double> couple : mapActionToValue.entrySet()) {
            Double val = couple.getValue();
            if (val == maxQ) {
                returnactions.add(couple.getKey());
            }
        }

        //*** VOTRE CODE
        return returnactions;

    }

    @Override
    public double getValeur(Etat e) {
        //*** VOTRE CODE
        if (this.getActionsLegales(e).isEmpty()) {//etat  absorbant; impossible de le verifier via environnement
            return 0.0;
        }
        double refQ = Double.NEGATIVE_INFINITY;
        double tempQ;
        List<Action> actionsNext = this.env.getActionsPossibles(e);
        for (Action act : actionsNext) {
            tempQ = this.getQValeur(e, act);
            if (tempQ > refQ) {
                refQ = tempQ;
            }
        }
        return refQ;

    }

    @Override
    public double getQValeur(Etat e, Action a) {
        //*** VOTRE CODE
        try {
            return this.qvaleurs.get(new Pair<>(e, a));
        } catch (NullPointerException ex) {
            return 0.0;
        }
    }

    @Override
    public void setQValeur(Etat e, Action a, double d) {
        //*** VOTRE CODE
        this.qvaleurs.put(new Pair<>(e, a), d);
        this.vmax = Collections.max(this.qvaleurs.values());
        this.vmin = Collections.min(this.qvaleurs.values());

        // mise a jour vmax et vmin pour affichage du gradient de couleur:
        //vmax est la valeur de max pour tout s de V
        //vmin est la valeur de min pour tout s de V
        // ...
        this.notifyObs();

    }

    /**
     * mise a jour du couple etat-valeur (e,a) apres chaque interaction
     * <etat e,action a, etatsuivant esuivant, recompense reward>
     * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement
     * apres avoir realise une action.
     *
     * @param e
     * @param a
     * @param esuivant
     * @param reward
     */
    @Override
    public void endStep(Etat e, Action a, Etat esuivant, double reward) {
        if (RLAgent.DISPRL) {
            System.out.println("QL va être mis a jour etat " + e + " action " + a + " etat' " + esuivant + " r " + reward);
        }
        double refQ = this.getValeur(esuivant);
        double currentValue = this.getQValeur(e, a);
        this.setQValeur(e, a, currentValue + this.alpha * (reward + this.gamma * refQ - currentValue));
        //*** VOTRE CODE
    }

    @Override
    public Action getAction(Etat e) {
        this.actionChoisie = this.stratExplorationCourante.getAction(e);
        return this.actionChoisie;
    }

    @Override
    public void reset() {
        super.reset();
        //*** VOTRE CODE

        this.qvaleurs.clear();

        this.episodeNb = 0;

        this.notifyObs();
    }

}

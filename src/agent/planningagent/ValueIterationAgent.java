package agent.planningagent;

import java.util.*;

import environnement.*;
import util.HashMapUtil;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration
 * et choisit ses actions selon la politique calculee.
 *
 * @author laetitiamatignon
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
     * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
     * et notifie ses observateurs.
     * Ce n'est pas la version inplace (qui utilise nouvelle valeur de V pour mettre a jour ...)
     */
    @Override
    public void updateV() throws Exception {
        //delta est utilise pour detecter la convergence de l'algorithme
        //lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
        //delta < epsilon
        this.delta = 0.0;

        HashMap<Etat,Double> oldV = (HashMap<Etat, Double>) V.clone();

        //*** VOTRE CODE


        for (Map.Entry<Etat, Double> entry : oldV.entrySet()) {
            Etat s = entry.getKey();
            if (!mdp.estAbsorbant(s)) {
                ArrayList<Action> actions = (ArrayList<Action>) mdp.getActionsPossibles(s);

                ArrayList<Double> listefinale = new ArrayList<Double>();

                for (Action a : actions) {

                    Map<Etat, Double> transProba = mdp.getEtatTransitionProba(s, a);
                    double sommeListeSp = 0.0;

                    for (Map.Entry<Etat, Double> entry2 : transProba.entrySet()) {
                        Etat sp = entry2.getKey();
                        Double prob = entry2.getValue();
                        double recomp = mdp.getRecompense(s, a, sp);

                        sommeListeSp += prob * (recomp + this.getGamma() * oldV.get(sp));
                    }

                    listefinale.add(sommeListeSp);

                }

                V.put(s,Collections.max(listefinale));

            }
        }

        this.vmax = Collections.max(V.values());
        this.vmin = Collections.min(V.values());


        // mise a jour vmax et vmin pour affichage du gradient de couleur:
        //vmax est la valeur de max pour tout s de V
        //vmin est la valeur de min pour tout s de V
        // ...


        //******************* laisser notification a la fin de la methode
        this.notifyObs();
    }


    /**
     * renvoi l'action executee par l'agent dans l'etat e
     * Si aucune actions possibles, renvoi Action2D.NONE
     */
    @Override
    public Action getAction(Etat e) {
        //*** VOTRE CODE

        return Action2D.NONE;

    }

    @Override
    public double getValeur(Etat _e) {
        //*** VOTRE CODE

        return V.get(_e);
    }

    /**
     * renvoi action(s) de plus forte(s) valeur(s) dans etat
     * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
     */
    @Override
    public List<Action> getPolitique(Etat _e) {
        //*** VOTRE CODE

g
        for (Map.Entry<Etat, Double> entry : V.entrySet()) {
            Etat s = entry.getKey();
            if (!mdp.estAbsorbant(s)) {
                ArrayList<Action> actions = (ArrayList<Action>) mdp.getActionsPossibles(s);

                ArrayList<Double> listefinale = new ArrayList<Double>();

                for (Action a : actions) {
                    try {
                        Map<Etat, Double> transProba = mdp.getEtatTransitionProba(s, a);
                    }
                    catch (Exception e) {
                        System.out.println(e);
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

            }
        }

        // retourne action de meilleure valeur dans _e selon V,
        // retourne liste vide si aucune action legale (etat absorbant)
        List<Action> returnactions = new ArrayList<Action>();

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

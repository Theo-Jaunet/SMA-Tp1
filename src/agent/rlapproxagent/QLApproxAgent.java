package agent.rlapproxagent;

import java.util.ArrayList;
import java.util.List;

import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
import java.util.Random;

/**
 * Agent qui apprend avec QLearning en utilisant approximation de la Q-valeur :
 * approximation lineaire de fonctions caracteristiques
 *
 * @author laetitiamatignon
 *
 */
public class QLApproxAgent extends QLearningAgent {

    protected double qValeur;
    protected FeatureFunction vecFeat;
    protected double[] weights;

    public QLApproxAgent(double alpha, double gamma, Environnement _env, FeatureFunction _featurefunction) {
        super(alpha, gamma, _env);
        //*** VOTRE CODE
        this.vecFeat = _featurefunction;
        int nbFeatures = this.vecFeat.getFeatureNb();
        this.weights = new double[nbFeatures];
        for (int indFeat = 0; indFeat < nbFeatures; indFeat++) {
            this.weights[indFeat] = 1;
        }

    }

    @Override
    public double getQValeur(Etat e, Action a) {
        //*** VOTRE CODE
        double[] features = this.vecFeat.getFeatures(e, a);
        this.qValeur = 0;
        for (int indFeat = 0; indFeat < this.weights.length; indFeat++) {
            this.qValeur += this.weights[indFeat] * features[indFeat];
        }
        return this.qValeur;
    }
    

    @Override
    public void endStep(Etat e, Action a, Etat esuivant, double reward) {
        if (RLAgent.DISPRL) {
            System.out.println("QL: mise a jour poids pour etat \n" + e + " action " + a + " etat' \n" + esuivant + " r " + reward);
        }
        double[] features = this.vecFeat.getFeatures(e, a);
        double maxQ = this.getValeur(esuivant);
        for (int indWeights = 0; indWeights < this.weights.length; indWeights ++){
            this.weights[indWeights] = this.weights[indWeights] + this.alpha * (reward + this.gamma * maxQ - this.getQValeur(e, a))*features[indWeights];
        }
        //inutile de verifier si e etat absorbant car dans runEpisode et threadepisode 
        //arrete episode lq etat courant absorbant	
        //*** VOTRE CODE
    }

    @Override
    public void reset() {
        super.reset();
        this.qvaleurs.clear();
        //*** VOTRE CODE
        this.episodeNb = 0;
        this.notifyObs();
    }

}

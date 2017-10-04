package agent.strategy;

import agent.rlagent.QLearningAgent;
import java.util.List;
import java.util.Random;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Etat;
/**
 * Strategie qui renvoit un choix aleatoire avec proba epsilon, un choix glouton (suit la politique de l'agent) sinon
 * @author lmatignon
 *
 */
public class StrategyGreedy extends StrategyExploration{
	/**
	 * parametre pour probabilite d'exploration
	 */
	protected double epsilon;
	private Random rand=new Random();
	
	
	public StrategyGreedy(RLAgent agent,double epsilon) {
		super(agent);
		this.epsilon = epsilon;
                this.agent =(QLearningAgent) this.agent;
	}

	@Override
	public Action getAction(Etat _e) {//renvoi null si _e absorbant
		double d =rand.nextDouble();
		List<Action> actions = this.agent.getActionsLegales(_e);
		if (actions.isEmpty()){
			return null;
		}
                double randomNum = rand.nextDouble();
                int randomInt = rand.nextInt((actions.size()));
                if (randomNum < this.epsilon){
                    return actions.get(randomInt);
                }
		//VOTRE CODE ICI
		actions = this.agent.getPolitique(_e);
                randomInt  = rand.nextInt(actions.size());
		return actions.get(randomInt);
	}

	public double getEpsilon() {
		return this.epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
		System.out.println("epsilon:"+epsilon);
	}

}

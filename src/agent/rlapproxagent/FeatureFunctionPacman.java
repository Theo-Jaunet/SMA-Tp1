package agent.rlapproxagent;

import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import pacman.environnementRL.EnvironnementPacmanMDPClassic;
import environnement.Action;
import environnement.Etat;

/**
 * Vecteur de fonctions caracteristiques pour jeu de pacman: 4 fonctions phi_i(s,a)
 *
 * @author laetitiamatignon
 */
public class FeatureFunctionPacman implements FeatureFunction {
    private double[] vfeatures;

    private static int NBACTIONS = 4;//5 avec NONE possible pour pacman, 4 sinon
    //--> doit etre coherent avec EnvironnementPacmanRL::getActionsPossibles


    public FeatureFunctionPacman() {

    }

    @Override
    public int getFeatureNb() {
        return 4;
    }

    @Override
    public double[] getFeatures(Etat e, Action a) {
        vfeatures = new double[4];
        StateGamePacman stategamepacman;
        //EnvironnementPacmanMDPClassic envipacmanmdp = (EnvironnementPacmanMDPClassic) e;

        //calcule pacman resulting position a partir de Etat e
        if (e instanceof StateGamePacman) {
            stategamepacman = (StateGamePacman) e;
        } else {
            System.out.println("erreur dans FeatureFunctionPacman::getFeatures n'est pas un StateGamePacman");
            return vfeatures;
        }

        StateAgentPacman pacmanstate_next = stategamepacman.movePacmanSimu(0, new ActionPacman(a.ordinal()));

        //*** VOTRE CODE
        vfeatures[0] = 1;
        vfeatures[1] = this.getNb1StepGhost(pacmanstate_next, stategamepacman);
        vfeatures[2] = stategamepacman.getMaze().isFood(pacmanstate_next.getX(), pacmanstate_next.getY()) ? 1 : 0;
        vfeatures[3]= stategamepacman.getClosestDot(pacmanstate_next)/ (stategamepacman.getMaze().getSizeX() * stategamepacman.getMaze().getSizeY() - stategamepacman.getMaze().getNbwall());


        return vfeatures;
    }

    public int getNb1StepGhost(StateAgentPacman next, StateGamePacman stategamepacman) {

        int count = 0;
        int tempDist;
        int nbGhost = stategamepacman.getNumberOfGhosts();
        StateAgentPacman currentState;
        for (int indGhost = 0; indGhost < nbGhost; indGhost++) {
            currentState = stategamepacman.getGhostState(indGhost);
            tempDist = this.getManatthanDist(next.getX(), next.getY(), currentState.getX(), currentState.getY());

            if (tempDist == 1)
                count += 1;

        }

        return count;
    }

    public void reset() {
        vfeatures = new double[4];

    }

    public int getManatthanDist(int pacX, int pacY, int ghostX, int ghostY) {
        return Math.abs(pacX - ghostX) + Math.abs(pacY - ghostY);
    }

    public int getNumberDotAround(StateAgentPacman next, StateGamePacman stategamepacman){
        int count = 0;

        int[] xOffset ={-1,1};
        int[] yOffset ={-1,1};



        return count;
    }


}

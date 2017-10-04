package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;

import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
import java.util.HashMap;
import pacman.elements.MazePacman;

/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning
 * tabulaire
 *
 */
public class EtatPacmanMDPClassic implements Etat, Cloneable {

    private int visionSize = 2;
    private HashMap<String, ArrayList<Pair>> configState = new HashMap<>();

    public EtatPacmanMDPClassic(StateGamePacman _stategamepacman) {

        this.init_HashMap();
        MazePacman maze = _stategamepacman.getMaze();
        int nbGhost = maze.getNumberOfGhosts();
        int Xmaze = maze.getSizeX();
        int Ymaze = maze.getSizeY();
        int xVisited;
        int yVisited;

        int[] visionRange = this.getOffsetVision(this.visionSize);

        StateAgentPacman currentPacmanState = _stategamepacman.getPacmanState(0);
        for (int xoff : visionRange) {
            for (int yoff : visionRange) {
                xVisited = currentPacmanState.getX() + xoff;
                yVisited = currentPacmanState.getY() + yoff;
                if ((xVisited >= 0) && (yVisited >= 0) && (xVisited < Xmaze) && (yVisited < Ymaze)) {
                    if (maze.isFood(xVisited, yVisited)) {
                        configState.get("food").add(new Pair(xVisited, yVisited));
                    }
                    else if (maze.isWall(xVisited, yVisited)){
                        configState.get("wall").add(new Pair(xVisited, yVisited));
                    }
                    else if (_stategamepacman.isGhost(xVisited, yVisited)){
                        configState.get("ghost").add(new Pair(xVisited, yVisited));
                    }
                    
                }
            }
        }
    }

    @Override
    public String toString() {

        return "";
    }
    
    public void init_HashMap() {
        this.configState.put("food", new ArrayList<Pair>());
        this.configState.put("ghost", new ArrayList<Pair>());
        this.configState.put("wall", new ArrayList<Pair>());

    }

    public int[] getOffsetVision(int visRange) {
        int[] result = new int[visRange * 2];
        int index = 0;
        for (int i = -visRange; i < visRange + 1; i++) {
            if (i != 0) {
                result[index] = i;
                index += 1;
            }
        }
        return result;
    }

    public Object clone() {
        EtatPacmanMDPClassic clone = null;
        try {
            // On recupere l'instance a renvoyer par l'appel de la 
            // methode super.clone()
            clone = (EtatPacmanMDPClassic) super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver car nous implementons 
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }

        // on renvoie le clone
        return clone;
    }

}

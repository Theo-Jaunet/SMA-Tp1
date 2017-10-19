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
    
    private MazePacman maze;
    private int[][] coordPacman;
    private int[][] coordGhost;
    private int[] closestDot;
//    private int[][] coordDots;

    public EtatPacmanMDPClassic(StateGamePacman _stategamepacman) {
        
//        MazePacman maze = _stategamepacman.getMaze();
        this.maze = _stategamepacman.getMaze();
        int nbGhost = maze.getNumberOfGhosts();
        int nbPacman = maze.getNumberOfPacmans();
//        int nbFood = maze.getNbfood();
        this.coordGhost = new int[nbGhost][2];
        this.coordPacman = new int[nbPacman][2];
//        this.coordDots = new int[nbFood][2];
        this.closestDot = new int[nbPacman];
        StateAgentPacman currentState;

        for (int indGhost = 0; indGhost < nbGhost; indGhost++) {
            currentState = _stategamepacman.getGhostState(indGhost);
            this.coordGhost[indGhost][0] = currentState.getX();
            this.coordGhost[indGhost][1] = currentState.getY();

        }
        for (int indPac = 0; indPac < nbPacman; indPac++) {
            currentState = _stategamepacman.getPacmanState(indPac);
            this.coordPacman[indPac][0] = currentState.getX();
            this.coordPacman[indPac][1] = currentState.getY();
            this.closestDot[indPac] = _stategamepacman.getClosestDot(currentState);
        }
//        nbFood = 0;
//        for (int indX = 0; indX < maze.getSizeX(); indX ++){
//            for (int indY = 0; indY < maze.getSizeY(); indY ++){
//                if (maze.isFood(indX, indY)){
//                    this.coordDots[nbFood][0] = indX;
//                    this.coordDots[nbFood][1] = indY;
//                    nbFood = nbFood + 1;
//                }
//            }
//        }
    }
    
    public int getDimensions(){
        int nbEtats = 0;
        for (int x = 0; x < this.maze.getSizeX(); x++){
            for (int y = 0; y < this.maze.getSizeY(); y++){
                if (! this.maze.isWall(x, y)){
                    nbEtats += 1;
                }
            }
        }
        return nbEtats;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Arrays.deepHashCode(this.coordPacman);
        hash = 97 * hash + Arrays.deepHashCode(this.coordGhost);
        hash = 97 * hash + Arrays.hashCode(this.closestDot);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EtatPacmanMDPClassic other = (EtatPacmanMDPClassic) obj;
        if (!Arrays.deepEquals(this.coordPacman, other.coordPacman)) {
            return false;
        }
        if (!Arrays.deepEquals(this.coordGhost, other.coordGhost)) {
            return false;
        }
        if (!Arrays.equals(this.closestDot, other.closestDot)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {

        return "";
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

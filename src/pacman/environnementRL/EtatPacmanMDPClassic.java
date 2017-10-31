package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.util.Pair;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;

import java.util.HashMap;

import pacman.elements.MazePacman;

/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning
 * tabulaire
 */
public class EtatPacmanMDPClassic implements Etat, Cloneable {

    private MazePacman maze;

    /** unusable **/
/*    private int[] closestDot;
    private int[][] coordPacman;
    private int[][] coordGhost;*/

    /** Few info**/
/*    private int[][] GhostInfo;
    private int[] DotInfo;*/

    /** Dots direction**/
/*    private int[][] GhostInfo;
    private int[][] closestDot;*/

    /** trade off**/
    private int[] GhostInfo;
    private int[] DotInfo;

    public EtatPacmanMDPClassic(StateGamePacman _stategamepacman) {
        this.maze = _stategamepacman.getMaze();
        int nbGhost = maze.getNumberOfGhosts();
        int nbPacman = maze.getNumberOfPacmans();

        /** unusable**/
/*        this.coordGhost = new int[nbGhost][2];
        this.coordPacman = new int[nbPacman][2];
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
        }*/


        /** few info**/
/*        this.GhostInfo = new int[nbGhost][2];
        this.DotInfo = new int[nbPacman];

        StateAgentPacman ghostState;
        StateAgentPacman pacState;
        int tempDist;
        for (int indPac = 0; indPac < nbPacman; indPac++) {
            pacState = _stategamepacman.getPacmanState(indPac);

            for (int indGhost = 0; indGhost < nbGhost; indGhost++) {
                ghostState = _stategamepacman.getGhostState(indGhost);
                tempDist = this.getManatthanDist(pacState.getX(), pacState.getY(), ghostState.getX(), ghostState.getY());

                this.GhostInfo[indGhost][0] = this.getDirection(pacState.getX(), pacState.getY(), ghostState.getX(), ghostState.getY());
                // Majoration a 3
                this.GhostInfo[indGhost][1] = tempDist < 3 ? tempDist : 4;
            }
            tempDist = _stategamepacman.getClosestDot(pacState);
            this.DotInfo[indPac] = tempDist < 3 ? tempDist : 4;
        }*/

        /** Directions dots **/
/*        this.GhostInfo = new int[nbGhost][2];
        this.closestDot = new int[nbPacman][2];

        StateAgentPacman ghostState;
        StateAgentPacman pacState;
        int tempDist;

        for (int indPac = 0; indPac < nbPacman; indPac++) {
            pacState = _stategamepacman.getPacmanState(indPac);

            for (int indGhost = 0; indGhost < nbGhost; indGhost++) {
                ghostState = _stategamepacman.getGhostState(indGhost);
                tempDist = this.getManatthanDist(pacState.getX(), pacState.getY(), ghostState.getX(), ghostState.getY());

                this.GhostInfo[indGhost][0] = this.getDirection(pacState.getX(), pacState.getY(), ghostState.getX(), ghostState.getY());
                this.GhostInfo[indGhost][1] = tempDist < 3 ? tempDist : 4;
            }
            this.closestDot[indPac][0] = this.getDotDirection(pacState.getX(), pacState.getY());
            this.closestDot[indPac][1] = _stategamepacman.getClosestDot(pacState) < 3 ? _stategamepacman.getClosestDot(pacState) : 4;

        }*/

        /** trade off **/
        this.GhostInfo = new int[2];
        this.DotInfo = new int[2];

        StateAgentPacman ghostState;
        StateAgentPacman pacState;
        int tempDist;
        int closestIndex = -1;

        pacState = _stategamepacman.getPacmanState(0);

        int compDist = Integer.MAX_VALUE;

        for (int indGhost = 0; indGhost < nbGhost; indGhost++) {
            ghostState = _stategamepacman.getGhostState(indGhost);
            tempDist = this.getManatthanDist(pacState.getX(), pacState.getY(), ghostState.getX(), ghostState.getY());
            if (tempDist < compDist) {
                closestIndex = indGhost;
                compDist = tempDist;
            }
        }

        this.GhostInfo[0] = this.getDirection(pacState.getX(), pacState.getY(), _stategamepacman.getGhostState(closestIndex).getX(), _stategamepacman.getGhostState(closestIndex).getY());
        this.GhostInfo[1] = compDist < 3 ? compDist : 4;
        tempDist = _stategamepacman.getClosestDot(pacState);
        this.DotInfo[0] = tempDist < 3 ? tempDist : 4;
        this.DotInfo[1] = this.getDotDirection(pacState.getX(), pacState.getY());

    }


    public int getDimensions() {
        /** unusable **/
//        return (int) Math.pow((this.maze.getSizeX() * this.maze.getSizeY() - this.maze.getNbwall()), 3.0);
        /**few info**/
//        return 4 * (int) Math.pow(3.0, 2.0 * this.maze.getNumberOfGhosts());
        /**Dot direction **/
//        return (int) (Math.pow(4.0, 2.0 * this.maze.getNumberOfGhosts()) * Math.pow(3.0, 2.0 * this.maze.getNumberOfGhosts()));
        /** trade off **/
        return 4 * 4 * 4 * 4;
    }

    /**
     * @param pacX
     * @param pacY
     * @param ghostX
     * @param ghostY
     * @return
     */
    public int getManatthanDist(int pacX, int pacY, int ghostX, int ghostY) {
        return Math.abs(pacX - ghostX) + Math.abs(pacY - ghostY);
    }

    public int getDirection(int pacX, int pacY, int ghostX, int ghostY) {

        int dX = pacX - ghostX;
        int dY = pacY - ghostY;

        if (dX > 0 && dX > Math.abs(dY)) {
            return MazePacman.EAST;
        }
        if (dX < 0 && Math.abs(dX) > Math.abs(dY)) {
            return MazePacman.WEST;
        }
        if (dY > 0 && dY > Math.abs(dX)) {
            return MazePacman.NORTH;
        }
        if (dY < 0 && Math.abs(dY) > Math.abs(dX)) {
            return MazePacman.SOUTH;
        }

        /* Diag */

        if (dX == dY && dX > 0) {
            return MazePacman.EAST;
        }
        if (dX == dY && dX < 0) {
            return MazePacman.WEST;
        }

        if (Math.abs(dY) == Math.abs(dX) && dX < 0) {
            return MazePacman.NORTH;
        }
        if (Math.abs(dY) == Math.abs(dX) && dX > 0) {
            return MazePacman.SOUTH;
        }


        return 0;
    }

    public int getDotDirection(int pacX, int pacY) {
        boolean found = false;
        int distToTest = 1;
        int y;
        int tempX = -1;
        int tempY = -1;
        while (!found) {
            if (this.maze.getNbfood() == 0) {
                break;
            }
            for (int x = -distToTest; x < distToTest + 1; x++) {
                tempX = pacX + x;
                y = distToTest - Math.abs(x);
                tempY = pacY + y;
                if (tempX < this.maze.getSizeX() && tempX >= 0 && tempY < this.maze.getSizeY() && tempY >= 0 && this.maze.isFood(tempX, tempY)) {
                    found = true;
                    break;
                }
                y = -y;
                tempY = pacY + y;
                if (tempX < this.maze.getSizeX() && tempX >= 0 && tempY < this.maze.getSizeY() && tempY >= 0 && this.maze.isFood(tempX, tempY)) {
                    found = true;
                    break;
                }

            }
            distToTest++;
        }
        return this.getDirection(pacX, pacY, tempX, tempY);
    }


    /**unusable**/
/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EtatPacmanMDPClassic that = (EtatPacmanMDPClassic) o;

        if (!Arrays.equals(closestDot, that.closestDot)) return false;
        if (!Arrays.deepEquals(coordPacman, that.coordPacman)) return false;
        return Arrays.deepEquals(coordGhost, that.coordGhost);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(closestDot);
        result = 31 * result + Arrays.deepHashCode(coordPacman);
        result = 31 * result + Arrays.deepHashCode(coordGhost);
        return result;
    }*/


    /** few info**/
/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EtatPacmanMDPClassic that = (EtatPacmanMDPClassic) o;

        if (!Arrays.deepEquals(GhostInfo, that.GhostInfo)) return false;
        return Arrays.equals(DotInfo, that.DotInfo);
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(GhostInfo);
        result = 31 * result + Arrays.hashCode(DotInfo);
        return result;
    }*/


    /**
     * Dot Direction
     **/
/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EtatPacmanMDPClassic that = (EtatPacmanMDPClassic) o;

        if (!Arrays.deepEquals(GhostInfo, that.GhostInfo)) return false;
        return Arrays.deepEquals(closestDot, that.closestDot);
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(GhostInfo);
        result = 31 * result + Arrays.deepHashCode(closestDot);
        return result;
    }*/


    /** trade off**/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EtatPacmanMDPClassic that = (EtatPacmanMDPClassic) o;

        if (!Arrays.equals(GhostInfo, that.GhostInfo)) return false;
        return Arrays.equals(DotInfo, that.DotInfo);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(GhostInfo);
        result = 31 * result + Arrays.hashCode(DotInfo);
        return result;
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

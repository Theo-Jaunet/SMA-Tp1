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

    /** 98 % version but too much states **/
/*    private int[] closestDot;
    private int[][] coordPacman;
    private int[][] coordGhost;*/

    /** 85% version but with less states **/
/*    private int[] closestDot;
    private int[][] coordGhost;*/

    /**
     * First 95 % 288 states
     **/
    private int[][] GhostInfo;
    private int[] closestDot;

    /**
     * New New test
     **/
   /* private int[][] GhostInfo;
    private int[] DotInfo;*/


    public EtatPacmanMDPClassic(StateGamePacman _stategamepacman) {
        this.maze = _stategamepacman.getMaze();
        int nbGhost = maze.getNumberOfGhosts();
        int nbPacman = maze.getNumberOfPacmans();

        /** 98% version**/
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

        /** Version 85% **/
/*
        this.coordGhost = new int[nbGhost][2];
        this.closestDot = new int[nbPacman];
        StateAgentPacman currentState;
        for (int indGhost = 0; indGhost < nbGhost; indGhost++) {
            currentState = _stategamepacman.getGhostState(indGhost);
            this.coordGhost[indGhost][0] = currentState.getX();
            this.coordGhost[indGhost][1] = currentState.getY();
        }
        for (int indPac = 0; indPac < nbPacman; indPac++) {
            currentState = _stategamepacman.getPacmanState(indPac);
            this.closestDot[indPac] = _stategamepacman.getClosestDot(currentState);
        }
*/
        /** First 95 % 288 states **/
        this.GhostInfo = new int[nbGhost][2];
        this.closestDot = new int[nbPacman];

        StateAgentPacman ghostState;
        StateAgentPacman pacState;
        int tempDist;

        for (int indPac = 0; indPac < nbPacman; indPac++) {
            pacState = _stategamepacman.getPacmanState(indPac);

            for (int indGhost = 0; indGhost < nbGhost; indGhost++) {
                ghostState = _stategamepacman.getGhostState(indGhost);
                tempDist = this.getManatthanDist(pacState.getX(), pacState.getY(), ghostState.getX(), ghostState.getY());

                this.GhostInfo[indGhost][0] = this.getDirection(pacState.getX(), pacState.getY(), ghostState.getX(), ghostState.getY());
                 //Majoration a 3
                this.GhostInfo[indGhost][1] = tempDist < 3 ? tempDist : 4;
            }
            this.closestDot[indPac] = _stategamepacman.getClosestDot(pacState);
        }
        /** New New Test **/
       /* this.GhostInfo = new int[nbGhost][2];
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
    }


    public int getDimensions() {
        /** 85 % **/
     /*   return (this.maze.getSizeX() * this.maze.getSizeY() - this.maze.getNbwall()) * (this.maze.getSizeX() * this.maze.getSizeY() - this.maze.getNbwall());*/
        /** 95% 288 States**/
        return (this.maze.getSizeX() * this.maze.getSizeY() - this.maze.getNbwall()) * (int)Math.pow(4.0,2.0*this.maze.getNumberOfGhosts()) ;
        /**New New Test**/
        /*return 4* (int)Math.pow(4.0,2.0*this.maze.getNumberOfGhosts());*/
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

    /** Hashcode and equals for 85% version **/
   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EtatPacmanMDPClassic that = (EtatPacmanMDPClassic) o;

        if (!Arrays.equals(closestDot, that.closestDot)) return false;
        return Arrays.deepEquals(coordGhost, that.coordGhost);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(closestDot);
        result = 31 * result + Arrays.deepHashCode(coordGhost);
        return result;
    }*/

    /**
     * Hashcode and equals for 98% version
     **/
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

    /** 95% 288 States **/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EtatPacmanMDPClassic that = (EtatPacmanMDPClassic) o;

        if (!Arrays.deepEquals(GhostInfo, that.GhostInfo)) return false;
        return Arrays.equals(closestDot, that.closestDot);
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(GhostInfo);
        result = 31 * result + Arrays.hashCode(closestDot);
        return result;
    }


    /**
     * New New Test
     **/
  /*  @Override
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

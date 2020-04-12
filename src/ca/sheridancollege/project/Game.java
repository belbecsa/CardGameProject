/**
 * SYST 17796 Project Winter 2020 Deliverable
 * @author WarDogs
 */
package ca.sheridancollege.project;

import java.util.ArrayList;

/**
 * The class that models your game. You should create a more specific child of
 * this class and instantiate the methods given.
 *
 * @author dancye
 * @author Paul Bonenfant Jan 2020
 * @modifier Marcin Koziel Apr 2020
 */
public abstract class Game {

    private final String name;  //the title of the game
    private ArrayList<Player> players;  // the players of the game

    public Game(String name) {
        this.name = name;
        players = new ArrayList();
    }

    /**
     * @return the players of this game.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * @return the number of player in the game.
     */
    public int getPlayerSize() {
        return players.size();
    }

    /**
     * @param players the players of this game.
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }


    /**
     * @return a String stating the names of each player.
     */
    public String getPlayerNames() {
        String names = "";
        for (int i = 0; i < players.size(); i++) {
            if (i == 0) {
                names += String.format("%s", players.get(i).getName());
            } else if (i > 0 && i != players.size()) {
                names += String.format(", %s", players.get(i).getName());
            } else {
                names += String.format("and %s", players.get(i).getName());
            }
        }
        return names;
    }

    /**
     * Play the game.
     */
    public abstract void play();

    /**
     * Declare and display a winning player.
     */
    public abstract void declareWinner();

}//end class

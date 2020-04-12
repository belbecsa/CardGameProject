/**
 * SYST 17796 Project Winter 2020 Base code.
 */
package ca.sheridancollege.project;

/**
 *
 * @author Marcin Koziel
 */
public class PlayerProfile {

    private String name;
    private int wins;
    private int losses;

    public PlayerProfile(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
    }

    public String getName() {
        return name;
    }

    public void addWin() {
        this.wins += 1;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void addLoss() {
        this.losses += 1;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void updateStates(int wins, int losses) {
        this.wins = wins;
        this.losses = losses;
    }

//    public WarPlayer toWarPlayer(){
//        
//    }
    public void play() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

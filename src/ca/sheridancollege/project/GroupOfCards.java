/**
 * SYST 17796 Project Winter 2020 Deliverable
 * @author WarDogs
 */
package ca.sheridancollege.project;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A class that represents any grouping of cards for Game.
 *
 * @author dancye
 * @author Paul Bonenfant Jan 2020
 * @modifier Marcin Koziel Apr 2020
 */
public class GroupOfCards {

    //The group of cards, stored in an ArrayList
    private ArrayList<Card> cards;

    public GroupOfCards() {
        this.cards = new ArrayList();
    }

    /**
     * A method that will get the group of cards as an ArrayList
     *
     * @return the group of cards.
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * @return the size of the group of cards
     */
    public int getSize() {
        return cards.size();
    }

}//end class

/**
* SYST 17796 Project Winter 2020 Deliverable
 */
package ca.sheridancollege.project;

import java.util.ArrayList;

/**
 *
 * @author Marcin Koziel
 */
public class WarPlayer extends Player {

    private GroupOfCards deck = new GroupOfCards();
    private GroupOfCards hand = new GroupOfCards();

    public WarPlayer(String name) {
        super(name);
    }

    public Card getCurrentCard() {
        return deck.getCards().get(0);
    }

    public GroupOfCards getDeck() {
        return deck;
    }

    public GroupOfCards getHand() {
        return hand;
    }

    public int getHandValue() {
        int value = 0;
        for (int i = 0; i < hand.getSize(); i++) {
            value += hand.getCards().get(i).getValue();
        }
        return value;
    }

    public Card flipCard() {
        return hand.getCards().get(0);
    }

    public boolean hasCards() {
        if (hand.getSize() > 0) {
            return true;
        }
        return false;
    }

    public void pushHandToDeck() {
        while (hand.getSize() > 0) {
            deck.getCards().add(hand.getCards().remove(0));
        }
    }

    public void pushToDeck(Card card) {
        deck.getCards().add(card);
    }

    public void pushAllToHand(ArrayList<Card> cards) {
        int cardSize = cards.size();
        for (int i = 0; i < cardSize; i++) {
            hand.getCards().add(cards.remove(0));
        }
    }

    public void pushToHand() {
        hand.getCards().add(deck.getCards().remove(0));
    }

    @Override
    public String toString() {
        return String.format("%s has %s cards", getName(), getDeck().getSize());
    }

    @Override
    public void play() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

/**
 * SYST 17796 Project Winter 2020 Deliverable
 * @author WarDogs
 */
package ca.sheridancollege.project;

import java.util.ArrayList;

/**
 * @author Marcin Koziel
 * @modifier Sheldon Allen
 */
public class WarPlayer extends Player {

    private GroupOfCards deck = new GroupOfCards();
    private GroupOfCards hand = new GroupOfCards();

    public WarPlayer(String name) {
        super(name);
    }

    /**
     * @return the Card most recently added to hand.
     */
    public Card getCurrentCard() {
        return hand.getCards().get(hand.getSize() - 1);
    }

    public GroupOfCards getDeck() {
        return deck;
    }

    public GroupOfCards getHand() {
        return hand;
    }

    /**
     * @return the cumulative value of all the cards in WarPlayer's hand
     */
    public int getHandValue() {
        int value = 0;

        for (Card c : hand.getCards())
            value += c.getValue();

        return value;
    }

    /**
     * Moves one Card from the deck to the hand
     * @return the card just added to hand.
     */
    public Card flipCard() {
        Card c = deck.pop();
        hand.push(c);
        return c;
    }

    public boolean hasCards() {
        if (hand.getSize() > 0) {
            return true;
        }
        return false;
    /**
     * @return whether or not the WarPlayer has any cards in deck.
     */
    public boolean hasCardsInDeck() {
        return deck.getSize() > 0;
    }

    /**
     * @return whether or not the WarPlayer has any cards in hand.
     */
    public boolean hasCardsInHand() {
        return hand.getSize() > 0;
    }

    public void pushToDeck(Card card) {
        deck.getCards().add(card);
    /**
     * Moves all Cards in hand back to the deck.
     */
    public void returnHandToDeck() {
        deck.getCards().addAll(hand.getCards());
        hand.getCards().clear();
    }

    /**
     * Takes the cards from one players hand, adds it to their deck, and shuffles
     * 
     * @param otherPlayer is the player that this WarPlayer will take the cards of.
     */
    public void takeHand(WarPlayer otherPlayer) {
        deck.getCards().addAll(otherPlayer.getHand().getCards());
        otherPlayer.getHand().getCards().clear();
    }

    @Override
    public String toString() {
        return String.format("%s", getName());
    }

    @Override
    /**
     * because of the nature of our game, there is no need to implement the
     * WarPlayer.play() function.
     */
    public void play() { }
}

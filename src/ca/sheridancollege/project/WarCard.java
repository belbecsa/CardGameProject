/**
 * SYST 17796 Project Winter 2020 Base code.
 */
package ca.sheridancollege.project;

/**
 *
 * @author Marcin Koziel
 */
public class WarCard extends Card {

    private String[] faceDown = {
        "┌─────────┐",
        "│░░░░░│",
        "│░░░░░│",
        "│░░░░░│",
        "│░░░░░│",
        "│░░░░░│",
        "└─────────┘"};

    WarCard(String suit, int value) {
        super(suit, value);
    }

    public String getFaceDownCard(int cardNo) {
        String formatted = "";
        String spaceBet = "";

        for (int i = 0; i < cardNo; i++) {
            spaceBet += " ";
        }

        for (int i = 0; i < faceDown.length; i++) {
            for (int j = 0; j < cardNo; j++) {
                faceDown[i] = faceDown[i] + spaceBet;
            }
            formatted = String.format("%s", faceDown[i]);
        }

        return formatted;
    }

    @Override
    public String toString() {
        return String.format("%s of %s", getValue(), getSuit());
    }

}

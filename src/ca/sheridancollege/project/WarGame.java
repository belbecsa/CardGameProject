/**
 * SYST 17796 Project Winter 2020 Deliverable
 * @author WarDogs
 */
package ca.sheridancollege.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * WarGame is a replication of the game War you see in Casinos, expect it's not
 * rigged! User will be able to move between different states of the game and
 * keep logs of their wins and losses for the number of players playing. (2-4)
 *
 * @author Marcin Koziel
 * @modifier Sheldon Allen
 */
public class WarGame extends Game {

    private final ProfileContainer profileContainer = new ProfileContainer();
    private final Scanner scan = new Scanner(System.in);

    private GameState state = GameState.MAIN_MENU;

    public WarGame(String name) {
        super(name);
    }

    public static void main(String[] args) {
        WarGame warGame = new WarGame("War");
        warGame.play();
    }

    @Override
    public void play() {
        // Starting message
        printLogo();
        System.out.println("Welcome to WarDogs. Let's get started!\n");

        // Setup game
        initializeMatch();

        // String input from user prompt
        String userInput;

        // Game Loop
        while (true) {

            switch (state) {

                case MAIN_MENU:
                    // Main menu options printed
                    printLogo();
                    System.out.printf(
                        "%n=============%n# MAIN MENU #%n=============%n"
                        + "\t[1] Play Match%n"
                        + "\t[2] View Stats%n"
                        + "\t[3] Exit%n%n> ");

                    userInput = scan.next();

                    // Main menu logic for user input
                    switch (userInput) {
                        case "1":
                            state = GameState.PLAYING_MATCH;
                            break;
                        case "2":
                            state = GameState.STAT_MENU;
                            break;
                        case "3":
                            declareWinner();
                            System.out.println("Thank you for playing!");
                            System.exit(0);
                            break;
                        default:
                            System.out.printf(
                                "\"%s\" is not a valid input", userInput);
                    }
                    break;

                case STAT_MENU:
                    // Stats for players printed
                    System.out.printf(
                        "%n==============%n# VIEW STATS #%n==============%n");

                    for (PlayerProfile profile : profileContainer.getPlayerList()) {
                        System.out.printf(
                            "%n  Name: %s%n\tWins: %s%n\tLosses %s%n%n",
                            profile.getName(),
                            profile.getWins(),
                            profile.getLosses());
                    }

                    pause(250);

                    // Stats logic for user input
                    System.out.printf(
                        "==============%nPress [ENTER] to return to MAIN MENU%n");

                    // Wait for user to press ENTER key before continueing
                    try {
                        System.in.read();
                    } catch (IOException e) { }

                    state = GameState.MAIN_MENU;

                    break;

                case PLAYING_MATCH:
                    // Round of War is printed
                    System.out.printf("%n==========%n#  PLAY  #%n==========%n");

                    for (Player p : getPlayers()) {
                        System.out.printf("\t%s has %d cards remaining%n",
                            p.getName(), ((WarPlayer) p).getDeck().getSize());
                    }
                    System.out.printf("%n\t[1] Play Round%n\t[2] Return to MAIN MENU%n> ");

                    // End of round logic for user input
                    userInput = scan.next();

                    switch (userInput) {
                        case "1":
                            playRound();
                            pause(300);
                            break;
                        case "2":
                            state = GameState.MAIN_MENU;
                            break;
                        default:
                            System.out.println("Please enter a valid input");
                    }
                    break;

                default:
                    break;
            }

        }
    }

    /**
     * Player will be declared eliminated and removed from the WarGame Player
     * ArrayList.
     *
     * @param player WarPlayer object to eliminate
     */
    private void eliminatePlayer(WarPlayer player) {
        System.out.printf("Player %s has been ELIMINATED from the game!%n",
                player.getName());
        getPlayers().remove(player);
    }

    /**
     * Before rounds are taken place, a deck is created in this class and
     * distributed between all players. (2-4)
     *
     * @throws InputMismatchException Based on user input, exception will be
     * thrown.
     */
    private void initializeMatch() throws InputMismatchException {
        // Request number of players from user between 2 and 4.
        int playerCount = 0;
        do {
            System.out.printf("Please enter the number of players: (2-4)%n> ");

            // necessary to have buffer in case player enters a String or null
            String buffer = scan.next();
            try {
                playerCount = Integer.parseInt(buffer);
            } catch(NumberFormatException e) {
                playerCount = 0;
            }

            if (playerCount > 4 || playerCount < 2)
                System.out.println("Invalid number of players!");

        } while (playerCount > 4 || playerCount < 2);

        // Get user to enter WarPlayer names and add WarPlayers to game
        String playerName;
        for (int i = 0; i < playerCount; i++) {

            playerName = null;

            // Remain locked in this loop until player enters valid player name.
            while (playerName == null) {
                System.out.printf("Please enter the name of Player #%s%n> ", i + 1);

                playerName = scan.next();

                if (!profileContainer.validatePlayerName(playerName)) {
                    System.out.println(
                        "Invalid! "
                        + "Name must be unique and between 3 and 14 chars.");
                    playerName = null;
                }
            }

            // Create PlayerProfile and add Player to game using playerName
            profileContainer.createPlayerProfile(playerName);
            getPlayers().add(new WarPlayer(playerName));
        }

        // Create deck
        GroupOfCards warDeck = new GroupOfCards();
        String[] suits = {"Clubs", "Spades", "Diamonds", "Hearts"};
        for (String suit : suits) {
            // Added 2 to value because it adjusts the values to match the card
            for (int value = 2; value < 15; value++) {
                warDeck.getCards().add(
                    new WarCard(suit, value));
            }
        }
        warDeck.shuffle();

        // Split deck and administer cards to players.
        WarPlayer warPlayer;
        int cardsPerPlayer = (int) Math.floor(warDeck.getSize() / playerCount);

        for (Player player : getPlayers()) {
            warPlayer = (WarPlayer) player;
            for (int c = 0; c < cardsPerPlayer; c++) {
                warPlayer.getDeck().push(warDeck.getCards().remove(0));
            }
        }

        // Shuffling deck console message (Visual aspect)
        try {
            System.out.printf("%n%nShuffling deck...%n");

            for (int i = 3; i > 0; i--) {
                System.out.println(i + "...");
                Thread.sleep(1000);
            }
            System.out.printf("%n%n");

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void playRound() {

        // The leading value
        int leadingValue;

        // Track and display how many hands are played in a round.
        int hand = 0;

        // We store combatants, leaders, and losers.
        // Combatants are the players still competeing in the round.
        // Leaders are the players currently at war.
        // Losers are the players that have been eliminated from the round.
        // At the end of each hand, leaders continue to the next round and
        // losers are removed.
        ArrayList<WarPlayer> combatants = new ArrayList();
        ArrayList<WarPlayer> leaders = new ArrayList();
        ArrayList<WarPlayer> losers = new ArrayList();

        // convert Players array to WarPlayer
        for (Player p : getPlayers())
            combatants.add((WarPlayer) p);

        // Continue to loop until there is just one combatant remaining.
        while (combatants.size() > 1) {
            System.out.println("### HAND " + ++hand + " ###");

            leadingValue = 0;

            // We iterate over each combatant and get them to draw a Card.
            // At this point, every combatant should be able to draw at least
            // one.
            for (WarPlayer turnPlayer : combatants) {
                System.out.println("Turn: " + turnPlayer.getName());

                // Check if Player can draw a card, if not they are eliminated
                // from the round (and game later).
                if (!turnPlayer.hasCardsInDeck()) {
                    System.out.println("\tCannot draw anymore cards.\nEliminated!");
                    losers.add(turnPlayer);
                    continue;
                }

                // Draw a card
                Card drawnCard = turnPlayer.flipCard();
                int drawnCardValue = drawnCard.getValue();
                System.out.printf(
                    "\tDraws a %d of %s%n", drawnCardValue, drawnCard.getSuit());

                // Player is the new leader if the card they drew has the new
                // highest value.
                if (drawnCardValue > leadingValue) {
                    System.out.printf(
                        "\t%sDraws high and is now in the lead!%n",
                        turnPlayer.getName());

                    if (leaders.size() > 0) {
                        System.out.println(
                            "ELIMINATED " + leaders + " from this round");

                        losers.addAll(leaders);
                        leaders.clear();
                    }

                    // Make this combatant the sole leader
                    leaders.add(turnPlayer);
                    leadingValue = drawnCardValue;
                }
                // Player goes to war if the card they drew ties for the
                // highest value.
                else if (drawnCardValue == leadingValue) {
                    System.out.printf("\t%s has gone to War!%n", turnPlayer.getName());
                    leaders.add(turnPlayer);
                }
                // Player loses if the card they drew is less than the highest
                // value card.
                else {
                    System.out.println(
                        "\tDraws short and is ELIMINATED from this round.");

                    losers.add(turnPlayer);
                }
            }

            // Set the combatants array to only contain the continuing players
            combatants = (ArrayList) leaders.clone();
            leaders.clear();
        }

        // At this point we should have only one winner inside the combatants array.
        // First we document their victory and award them the losers cards

        WarPlayer winner = combatants.get(0);
        updatePlayerStats(winner);

        // We iterate over all the losers and give their cards to the winning
        // player. If a losers is out of cards then they are eliminated.
        for (WarPlayer loser : losers) {
            winner.takeHand(loser);
            updatePlayerStats(loser);
            if (!loser.hasCardsInDeck())
                eliminatePlayer(loser);
        }
        winner.returnHandToDeck();
        winner.shuffle();

        System.out.printf("%n%s has WON the round!%nPress [ENTER] to continue", winner);

        // Wait for user to press ENTER key before continueing
        try {
            System.in.read();
        } catch (IOException e) { }
    }

    /**
     * WarPlayer stats are updated based on their hand status.
     *
     * If the warPlayer has a card at the end of the round, they win, otherwise
     * they loss.
     *
     * @param warPlayer WarPlayer to update stat for
     */
   private void updatePlayerStats(WarPlayer warPlayer) {
       if (warPlayer.hasCardsInHand()) {
           profileContainer.getPlayerProfile(warPlayer.getName()).addWin();
       } else {
           profileContainer.getPlayerProfile(warPlayer.getName()).addLoss();
       }
   }

    /**
     * Player with the greatest number of cards within their deck is the winner
     * and wins a Lamborghini...
     */
    @Override
    public void declareWinner() {
        // WarPlayer winner;
        String winner = null;
        int maxCards = 0;

        WarPlayer activePlayer;

        for (int i = 0; i < getPlayers().size(); i++) {
            activePlayer = (WarPlayer) getPlayers().get(i);
            if (activePlayer.getDeck().getSize() > maxCards) {
                maxCards = activePlayer.getDeck().getSize();
                winner = activePlayer.getName();
            }
        }

        System.out.printf("[Winner]"
                + "Player %s is the winner with a total of %s cards!%n",
                winner, maxCards);
        System.exit(0);

    }

    /**
     * Print the WarDogs logo in ASCII art.
     */
    private void printLogo() {
        String[] lines = {
            " _    _           ______                ",
            "| |  | |          |  _  \\               ",
            "| |  | | __ _ _ __| | | |___   __ _ ___ ",
            "| |/\\| |/ _` | '__| | | / _ \\ / _` / __|",
            "\\  /\\  / (_| | |  | |/ / (_) | (_| \\__ \\",
            " \\/  \\/ \\__,_|_|  |___/ \\___/ \\__, |___/",
            "                               __/ |    \n" +
            "                              |___/     "};

        for (String line : lines) {
            System.out.println(line);
            pause(150);
        }
    }

    /**
     * A convenience function to simulate a pause... for dramatic effect.
     * @param milli time in milliseconds to pause for.
     */
    private void pause(int milli) {
        try {
            Thread.sleep(milli);
        } catch (InterruptedException ex) { /* do nothing, no need */ }
    }
}

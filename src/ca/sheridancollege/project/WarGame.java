/**
 * SYST 17796 Project Winter 2020 Deliverable
 * @author WarDogs
 */
package ca.sheridancollege.project;

/**
 * SYST 17796 Project Winter 2020 Deliverable
 */
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

    private WarPlayer activePlayer;
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
        // String input from user prompt
        String userInput;

        // Settinging up game 
        initializeMatch();

        // Game Loop
        while (true) {
            switch (state) {
                case MAIN_MENU:
                    // Main menu options printed
                    System.out.printf(
                        "%n=====%nMenu:%n=====%n"
                        + "1. Player Stats%n"
                        + "2. Play War%n"
                        + "3. Exit%n%n");

                    userInput = scan.next();

                    // Main menu logic for user input
                    switch (userInput) {
                        case "1":
                            state = GameState.STAT_MENU;
                            break;
                        case "2":
                            state = GameState.PLAYING_MATCH;
                            break;
                        case "3":
                            declareWinner();
                            System.out.println("Thank you for playing!");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Please enter a valid input");
                    }
                    break;

                case STAT_MENU:
                    // Stats for players printed
                    System.out.print("%nStats%n=====%n");

                    for (PlayerProfile profile : profileContainer.getPlayerList()) {
                        System.out.printf(
                            "%nName: %s%nWins: %s%nLosses %s%n%n",
                            profile.getName(),
                            profile.getWins(),
                            profile.getLosses());
                    }

                    // Stats logic for user input
                    System.out.printf(
                        "=====%n"
                        + "1. Go back%n");

                    userInput = scan.next();
                    switch (userInput) {
                        case "1":
                            state = GameState.MAIN_MENU;
                            break;
                        default:
                            System.out.println("Please enter a valid input");
                    }
                    break;

                case PLAYING_MATCH:
                    // Round of War is printed
                    pushToAllHands();
                    playRound();
                    System.out.printf(
                        "%n=====%n"
                        + "1. Main Menu%n"
                        + "2. Continue playing%n");

                    // End of round logic for user input
                    userInput = scan.next();
                    switch (userInput) {
                        case "1":
                            state = GameState.MAIN_MENU;
                            break;
                        case "2":
                            state = GameState.PLAYING_MATCH;
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
        System.out.printf("Player %s has been eliminated.%n",
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

            try {
                playerCount = scan.nextInt();

                if (playerCount > 4 || playerCount < 2)
                    System.out.println("Invalid number of players!");

            } catch (InputMismatchException ime) {
                System.out.println(ime.getMessage());
            }
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
                warPlayer.pushToDeck(warDeck.getCards().remove(0));
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

    /**
     * Proceed to play a round of war and will loop if two players match cards
     * until ultimate winner of round is declared
     */
    private void playRound() {
        GroupOfCards warPot = new GroupOfCards();
        int maxValue = 0;  // Max value in a round of war between players
        int tempValue = 0;  // Previous number (Used for winner logic)
        int warValue = 0;  // Value that is matched between players

        do {
            // If War is declared between players, players with existing cards
            // in hands will pick up another card
            if (warValue > 0) {
                System.out.printf("%n[WAR]%n"
                        + "Players are going to war!%n");
                for (int i = 0; i < getPlayerSize(); i++) {
                    warValue = 0;  // Reset
                    maxValue = 0;  // Reset
                    activePlayer = (WarPlayer) getPlayers().get(i);
                    if (activePlayer.hasCards() && activePlayer.getDeck().getSize() > 0) {
                        activePlayer.pushToHand();
                        System.out.printf("\nPlayer %s has %s of cards in deck "
                                + "and puts down another card, %s.%n",
                                activePlayer.getName(), activePlayer.getDeck().getSize(),
                                activePlayer.getCurrentCard());
                        // Push one card from deck to hand
                    } else if (activePlayer.getDeck().getSize() == 0) {
                        // Eliminate player if got to war and no more cards are left in deck
                        eliminatePlayer(activePlayer);
                    }
                }
            }

            // Logic for declaring a winner within a round of war
            for (int i = 0; i < getPlayerSize(); i++) {
                activePlayer = (WarPlayer) getPlayers().get(i);
                if (activePlayer.getHandValue() > maxValue) {
                    maxValue = activePlayer.getHandValue();
                } else if (activePlayer.getHandValue() == tempValue
                        || activePlayer.getHandValue() == maxValue) {
                    warValue = activePlayer.getHandValue();
                }
                tempValue = activePlayer.getHandValue();
            }

            // Remove cards from hands of losers and put them into the pot (warPot)
            for (int i = 0; i < getPlayers().size(); i++) {
                activePlayer = (WarPlayer) getPlayers().get(i);
                if (activePlayer.getHandValue() != maxValue) {
                    int handSize = activePlayer.getHand().getCards().size();
                    for (int j = 0; j < handSize; j++) {
                        warPot.getCards().add(activePlayer.getHand().getCards().remove(0));
                    }
                }
            }
        } while (warValue > 0 && warValue >= maxValue);

        // Winner collects cards from warPot
        for (int i = 0; i < getPlayers().size(); i++) {
            activePlayer = (WarPlayer) getPlayers().get(i);
            updatePlayerStats(activePlayer);  // After a round stats are updated
            if (activePlayer.hasCards()) {
                System.out.printf("\n[Winner]%n"
                        + "Player %s is declared the winner and takes the pot "
                        + "of %s cards!%n",
                        activePlayer.getName(), warPot.getSize());
                //for each card in the pot do this
                activePlayer.pushAllToHand(warPot.getCards());
                activePlayer.pushHandToDeck();
            }
        }
        // If any decks are empty by the end of round, player is eliminated
        for (int i = 0; i < getPlayers().size(); i++) {
            activePlayer = (WarPlayer) getPlayers().get(i);
            if (activePlayer.getDeck().getCards().isEmpty()) {
                eliminatePlayer(activePlayer);
            }
        }
        // If one player is left in players array, they are declared a winner
        if (getPlayers().size() == 1) {
            declareWinner();
        }

    }

    /**
     * Before a round is started, players are told to pick up one card for the
     * round
     * Called at the start of a round
     */
    private void pushToAllHands() {
        System.out.printf(
            "%n[Round Start]%nPlayers %s ready? Place one card down!%n",
            getPlayerNames());

        // Console prints players put down a card that is drawn (facedown)
        for (int i = 0; i < getPlayerSize(); i++) {
            activePlayer = (WarPlayer) getPlayers().get(i);

            System.out.printf(
                "\nPlayer %s has %s of cards in deck and puts down a, %s.%n",
                activePlayer.getName(), 
                activePlayer.getDeck().getSize(),
                activePlayer.getCurrentCard()
            );

            // Push one card to hand from deck
            // Printing the card that was used by player
            activePlayer.pushToHand(); 
        }
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
        if (warPlayer.hasCards()) {
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
        int maxValue = 0;
        String winner = null;
        int maxCards = 0;

        for (int i = 0; i < getPlayers().size(); i++) {
            activePlayer = (WarPlayer) getPlayers().get(i);
            if (activePlayer.getDeck().getSize() > maxValue) {
                maxValue = activePlayer.getDeck().getSize();
                winner = activePlayer.getName();
                maxCards = activePlayer.getDeck().getSize();
            }
        }

        System.out.printf("[Winner]"
                + "Player %s is the winner with a total of %s cards!%n",
                winner, maxCards);
        System.exit(0);

    }
}

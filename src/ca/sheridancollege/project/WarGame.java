package ca.sheridancollege.project;

/**
 * SYST 17796 Project Winter 2020 Base code.
 */
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * WarGame is a replication of the game War you see in Casinos, expect it's not
 * rigged! User will be able to move between different states of the game and
 * keep logs of their wins and losses for the number of players playing. (2-4)
 *
 * @author Marcin Koziel
 */
public class WarGame extends Game {

    private WarPlayer activePlayer;
    private GameState state = GameState.MAIN_MENU;
    private ProfileContainer profilecontainer = new ProfileContainer();
    private Scanner sc = new Scanner(System.in);

    public WarGame(String name) {
        super(name);
    }

    public static void main(String[] args) {
        WarGame warGame = new WarGame("War");
        warGame.play();
    }

    @Override
    public void play() {
        String userInput; // String input from user prompt
        // Settinging up game 
        initalizeMatch();
        while (true) {
            switch (state) {
                case MAIN_MENU:
                    // Main menu options printed
                    System.out.printf("%n=====%nMenu:%n=====%n"
                            + "1. Player Stats%n"
                            + "2. Play War%n"
                            + "3. Exit%n%n");

                    userInput = sc.next();
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
                    System.out.printf("%nStats%n=====%n");
                    for (PlayerProfile profile : profilecontainer.getPlayerList()) {
                        System.out.printf("%nName: %s%nWins: %s%nLosses %s%n%n",
                                profile.getName(), profile.getWins(), profile.getLosses());
                    }
                    // Stats logic for user input
                    System.out.printf("=====%n"
                            + "1. Go back%n");
                    userInput = sc.next();
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
                    System.out.printf("%n=====%n"
                            + "1. Main Menu%n"
                            + "2. Continue playing%n");
                    // End of round logic for user input
                    userInput = sc.next();
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
     * Player will be declared eliminated from the WarGame Player ArrayList
     *
     * @param player WarPlayer object to eliminate
     */
    private void eliminatePlayer(WarPlayer player) {
        System.out.printf("Player %s has been eliminated.",
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
    private void initalizeMatch() throws InputMismatchException {
        GroupOfCards warDeck = new GroupOfCards();
        Card warCard;
        String playerName; // inputVariable for names
        int playerNo = 0;
        boolean isValid = false; // For validating users

        // Creating a deck for dealer later to be used as pot (warPot)
        for (int j = 0; j < 4; j++) { // Number of suits
            // Added 2 to k because it adjustes the values to match the card
            for (int k = 2; k < 15; k++) { // Number of ranks (values)
                switch (j) {
                    case 0:
                        warCard = new WarCard("Clubs", k);
                        break;
                    case 1:
                        warCard = new WarCard("Spades", k);
                        break;
                    case 2:
                        warCard = new WarCard("Diamonds", k);
                        break;
                    default:
                        warCard = new WarCard("Hearts", k);
                        break;
                }
                warDeck.getCards().add(warCard);
            }
        }
        warDeck.shuffle();  // Shuffled for randomness between player rounds

        // Number of players requested to be input
        System.out.printf("Please enter the number of player(s): (2-4)%n");
        do {
            try {
                playerNo = sc.nextInt();
                if (playerNo >= 2 && playerNo <= 4) {
                    isValid = true;
                } else {
                    System.out.printf("Invalid number of players. "
                            + "Please re-enter the valid number of player(s): (2-4)%n");
                }
            } catch (InputMismatchException ime) {
                System.out.println(ime.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!isValid);

        // Names for number of players requested to be input
        for (int i = 0; i < playerNo; i++) {
            System.out.printf("Please enter the name of player #%s%n", i + 1);
            isValid = false;
            do {
                playerName = sc.next();
                if (profilecontainer.validatePlayerName(playerName)) {
                    profilecontainer.createPlayerProfile(playerName);
                    activePlayer = new WarPlayer(playerName);
                    getPlayers().add(activePlayer);
                    isValid = true;
                } else {
                    System.out.printf("Please re-enter the valid name of player #%s%n", i + 1);
                }
            } while (!isValid);
        }
        // Dealing cards to players from warDeck until no cards are left
        do {
            for (int i = 0; i < getPlayerSize(); i++) {
                activePlayer = (WarPlayer) getPlayers().get(i);
                activePlayer.pushToDeck(warDeck.getCards().remove(0));
            }
        } while (warDeck.getSize() - (warDeck.getSize() % getPlayerSize()) != 0);
        // Discarding any cards if need for even split between players
        for (int i = 0; i < warDeck.getSize(); i++) {
            warDeck.getCards().remove(i);
        }

        // Shuffling deck console message (Visual aspect)
        try {
            System.out.printf("%n%nShuffling deck...");

            for (int i = 3; i > 0; i--) {
                System.out.printf("%s...", i);
                Thread.sleep(1000);
            }
            System.out.printf("%n%n");
        } catch (Exception e) {
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
                System.out.printf("%nPlayers %s are going to war!", getPlayerNames());
                for (int i = 0; i < getPlayerSize(); i++) {
                    warValue = 0;  // Reset
                    maxValue = 0;  // Reset
                    activePlayer = (WarPlayer) getPlayers().get(i);
                    if (activePlayer.hasCards() && activePlayer.getDeck().getSize() > 0) {
                        activePlayer.pushToHand();  // Push one card from deck to hand
                    } else {
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
                System.out.printf("\nPlayer %s is the winner and takes the pot!%n",
                        activePlayer.getName());
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
     */
    private void pushToAllHands() {
        System.out.printf("%nPlayers %s ready? Place one card down!%n", getPlayerNames());
        // Console prints players put down a card that is drawn (facedown)
        for (int i = 0; i < getPlayerSize(); i++) {
            activePlayer = (WarPlayer) getPlayers().get(i);
            System.out.printf("\nPlayer %s has %s of cards in deck and puts down a card. ",
                    activePlayer.getName(), activePlayer.getDeck().getSize());
            activePlayer.pushToHand();  // Push one card to hand from deck
            // Printing the card that was used by player
            System.out.println(activePlayer.getHand().getCards().toString());
        }
    }

    /**
     * User player stats are updated based on their hand status If they still
     * have a card at the end of the round, they win, otherwise loss.
     *
     * @param warPlayer WarPlayer object to update stat for
     */
    private void updatePlayerStats(WarPlayer warPlayer) {
        if (warPlayer.hasCards()) {
            profilecontainer.getPlayerProfile(warPlayer.getName()).addWin();
        } else {
            profilecontainer.getPlayerProfile(warPlayer.getName()).addLoss();
        }
    }

    /**
     * Player will the greatest number of cards within their deck will be
     * subject to winner and wins a lamborghini...
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

        System.out.printf("Player %s is the winner with a total of %s cards!%n",
                winner, maxCards);
        System.exit(0);

    }
}

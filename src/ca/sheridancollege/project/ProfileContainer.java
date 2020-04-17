/**
 * SYST 17796 Project Winter 2020 Deliverable
 * @author WarDogs
 */
package ca.sheridancollege.project;

import java.util.ArrayList;

/**
 *
 * @author Marcin Koziel
 * @modifier Sheldon Allen
 * @modifier Sam Belbeck
 */
public class ProfileContainer {

    private ArrayList<PlayerProfile> playerProfiles = new ArrayList<>();

    public PlayerProfile createPlayerProfile(String name) {
        PlayerProfile player = new PlayerProfile(name.trim());
        playerProfiles.add(player);
        return player;
    }

    public PlayerProfile getPlayerProfile(String name) {
        for (PlayerProfile playerProfile : playerProfiles) {
            if (playerProfile.getName().equals(name)) {
                return playerProfile;
            }
        }
        return null;
    }

    public ArrayList<PlayerProfile> getPlayerList() {
        return playerProfiles;
    }

    public void removePlayerProfile(String name) {
        for (PlayerProfile playerProfile : playerProfiles) {
            if (playerProfile.getName().equals(name)) {
                playerProfiles.remove(playerProfile);
            }
        }
    }

    /**
     * Validate that the pendingName is between 0 and 14 characters and that
     * it is not in use by any existing PlayerProfiles.
     *
     * @param pendingName the name to be validated
     */
    public boolean validatePlayerName(String pendingName) {
        for (PlayerProfile profile : playerProfiles) {
            if (profile.getName().equals(pendingName))
                return false;
        }

        return pendingName.length() > 2 && pendingName.length() < 15;
    }
}

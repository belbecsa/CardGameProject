/**
 * SYST 17796 Project Winter 2020 Base code.
 */
package ca.sheridancollege.project;

import java.util.ArrayList;

/**
 *
 * @author Marcin Koziel
 */
public class ProfileContainer {

    private ArrayList<PlayerProfile> playerProfiles = new ArrayList<>();

    public PlayerProfile createPlayerProfile(String name) {
        PlayerProfile player = new PlayerProfile(name);
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

    public boolean validatePlayerName(String name) {
        if (name.length() > 0 || name.length() < 15) {
            return true;
        }
        return false;
    }
}

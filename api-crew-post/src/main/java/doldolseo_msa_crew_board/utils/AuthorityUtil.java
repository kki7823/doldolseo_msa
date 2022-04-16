package doldolseo_msa_crew_board.utils;

import org.springframework.stereotype.Component;

@Component
public class AuthorityUtil {
    public boolean areYouCrewLeader(String role) {
        if (!role.equals("CREWLEADER")) return false;
        return true;
    }

    public boolean areYouUser(String role){
        if (!role.equals("USER")) return false;
        return true;
    }

    public boolean isYou(String userId, String ownerId){
        return userId.equals(ownerId);
    }
}

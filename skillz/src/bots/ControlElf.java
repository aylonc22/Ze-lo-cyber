package bots;
import elf_kingdom.*;
import java.util.*;
public class ControlElf
{
    // check (and execute) if elf can move to a location return true if cant return false
    public static boolean moveTo(Location location,Elf chosenElf)
    {
        if(!location.inMap())
            return false;
        if(chosenElf.getLocation()==location)
            return false;
        chosenElf.moveTo(location);
        return true;
    }
    // check if there is a need to build a defensive portal  (and execute it) 
    public static boolean buildDefensivePortal(Game game)
    {
        if(!getLocations.oppositePortal(game))
            {
                 Location location = new Location(getLocations.getDefensivePortal(game).row,getLocations.getDefensivePortal(game).col);
                 Elf closeElf = getLocations.getCloseElf(game,location);
                 if(closeElf.getLocation()==location)
                    {
                        closeElf.buildPortal();
                        return true;
                    }
                  else
                    {
                        closeElf.moveTo(location);
                        return true;
                    }
            }
        return false;
    }
}

package bots;
import elf_kingdom.*;
import java.util.*;
public class getLocations 
{
    // find the closest enemy elf if can't find return null
    public static Elf getCloseEnemyElf(Game game,Elf chosenElf)
    {
        Elf[] check = game.getEnemyLivingElves();
        if(check.length==0)
            return null;
        Elf closeEnemyElf = game.getEnemyLivingElves()[0];
        for(Elf enemyElf: game.getEnemyLivingElves())
            {
                if(chosenElf.distance(closeEnemyElf)>chosenElf.distance(enemyElf))
                    closeEnemyElf = enemyElf;
            }
        return closeEnemyElf;
    }
    
    // find the closest enemy portal if cant find return null
    public static Portal getCloseEnemyPortal(Game game,Elf chosenElf)
    {
        Portal[] check = game.getEnemyPortals();
        if(check.length==0)
            return null;
        Portal closeEnemyPortal = game.getEnemyPortals()[0];
        for(Portal enemyPortal : game.getEnemyPortals())
        {
            if(chosenElf.distance(closeEnemyPortal)>chosenElf.distance(enemyPortal))
                closeEnemyPortal = enemyPortal;
        }
        return closeEnemyPortal;
    }
    // find the closest Ice troll if can't find return null
    public static IceTroll getCloseEnemyIceTroll(Game game,Elf chosenElf)
    {
        IceTroll[] check = game.getEnemyIceTrolls();
        if(check.length==0)
            return null;
        IceTroll closeEnemyIceTroll = game.getEnemyIceTrolls()[0];
        for(IceTroll enemyIceTroll:game.getEnemyIceTrolls())
            {
                if(chosenElf.distance(closeEnemyIceTroll)>chosenElf.distance(enemyIceTroll))
                    closeEnemyIceTroll = enemyIceTroll;
            }
        return closeEnemyIceTroll;
    }
    //gets elf and array of threatening creatures return the best escape root.
    public static Location getEscapeLocation(Game game,Elf chosenElf,IceTroll[] creatures) 
    {
    	int VectorSize=game.elfMaxSpeed;
    	Location escapePoint , finalEscapePoint = new Location(0,0);
    	for(IceTroll iceTrol : creatures) 
    	{
            escapePoint = chosenElf.getLocation().towards(iceTrol.getLocation(),-VectorSize);
            finalEscapePoint.add(escapePoint);
    	}
    	finalEscapePoint.col /= creatures.length;
    	finalEscapePoint.row /= creatures.length;
    	finalEscapePoint = chosenElf.getLocation().towards(finalEscapePoint, VectorSize-finalEscapePoint.distance(chosenElf));
    	return finalEscapePoint;
    }
}
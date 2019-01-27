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
    // find the closest enemy portal to our castle if cant find return null
    public static Portal getCloseEnemyPortal(Game game)
    {
        Portal[] check = game.getEnemyPortals();
        if(check.length==0)
            return null;
        Castle myCastle = game.getMyCastle();
        Portal closeEnemyPortal = game.getEnemyPortals()[0];
        for(Portal enemyPortal : game.getEnemyPortals())
        {
            if(myCastle.distance(closeEnemyPortal)>myCastle.distance(enemyPortal))
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
    
     // get an optimal location for a defensive Portal
    public static Location getDefensivePortal(Game game)
        {
            Castle myCastle = game.getMyCastle();
            Portal enemyPortal = getCloseEnemyPortal(game);
            return myCastle.getLocation().towards(enemyPortal,(int)(Math.sqrt(myCastle.size)) );
        }
    // get the closest lava giant to our castle if cant return null
    public static LavaGiant getCloseLavaGiant(Game game)
    {
        LavaGiant[] check = game.getEnemyLavaGiants();
        if(check.length==0)
            return null;
        Castle myCastle = game.getMyCastle();
        LavaGiant closeLavaGiant = game.getEnemyLavaGiants()[0];
        for(LavaGiant enemyLava: game.getEnemyLavaGiants())
        {
            if(myCastle.distance(closeLavaGiant)>myCastle.distance(enemyLava))
                closeLavaGiant = enemyLava;
        }
        return closeLavaGiant;
    }
    // get the closest portal to a LavaGiant if cant return null
    public static Portal getClosePortal(Game game)
    {
        LavaGiant closeLavaGiant = getCloseLavaGiant(game);
        if(closeLavaGiant==null)
            return null;
        Portal[] check = game.getEnemyPortals();
        if(check.length==0)
            return null;
        Portal closePortal = game.getMyPortals()[0];
        for(Portal myPortal:game.getMyPortals())
            {
                if(closePortal.distance(closeLavaGiant)>myPortal.distance(closeLavaGiant))
                    closePortal = myPortal;
            }
        return closePortal;
    }
    // get the closer elf to a spesific location if cant return null
    public static Elf getCloseElf(Game game, Location location)
    {
        Elf[] check = game.getMyLivingElves();
        if(check.length==0)
            return null;
        Elf closeElf = game.getMyLivingElves()[0];
        for(Elf myElf:game.getMyLivingElves())
            {
                if(closeElf.distance(location)>myElf.distance(location))
                    closeElf = myElf;
            }
        return closeElf;
    }
    //gets elf and array of threatening creatures return the best escape root.
    public static Location getEscapeLocation(Game game,Elf chosenElf,IceTroll[] creatures) 
    {
    	int VectorSize=game.elfMaxSpeed;
    	Location escapePoint , finalEscapePoint = new Location(0,0);
    	for(IceTroll iceTrol : creatures) 
    	{
            escapePoint = iceTrol.getLocation().towards(chosenElf.getLocation(),VectorSize);
            finalEscapePoint.add(escapePoint);
    	}
    	finalEscapePoint.col /= creatures.length;
    	finalEscapePoint.row /= creatures.length;
    	finalEscapePoint = chosenElf.getLocation().towards(finalEscapePoint, VectorSize-finalEscapePoint.distance(chosenElf));
    	return finalEscapePoint;
    }
}

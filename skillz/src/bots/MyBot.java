package bots;
import elf_kingdom.*;
import java.util.*;


/**
 * This is an example for a bot.
 */
public class MyBot implements SkillzBot {
    /**
     * Makes the bot run a single turn.
     *
     * @param game - the current game state.
     */
    @Override
    public void doTurn(Game game) {
       
        // Give orders to my elves.
        handleElves(game);
        // Give orders to my portals.
        handlePortals(game);
        
    }
    

    /**
     * Gives orders to my elves.
     *
     * @param game - the current game state
     */
    private void handleElves(Game game) {
        // Get enemy castle
        Castle enemyCastle = game.getEnemyCastle();
        // Go over all the living elves.
        for (Elf elf : game.getMyLivingElves())
        {
            System.out.println(getLocations.getCloseEnemyIceTroll(game,elf));
            System.out.println("trolls around: " +getElfAura(game,elf).length);
            //try to run away
            if(canKite(game,elf))
                // kite for one direction is not fully complete yet for now when cannot move the elf is attacking the IceTroll
                kite(game,elf);
                else
                {
                    //try to attack Castle
                    if(elf.inAttackRange(enemyCastle))
                        //attack Castle
                        elf.attack(enemyCastle);
                    else
                    {
                        //try to attack or chase enemy elf or portal
                        if(canElfAttack(game,elf))
                            // attack or chase enemy elf or portal
                            elfAttack(game,elf);
                        else
                        {
                            //if our elf can't do something from the about by default he will go to the enemy castle
                            elf.moveTo(enemyCastle);
                        }
                    }
                }
           
        }
            
    }

    /**
     * Gives orders to my portals.
     *
     * @param game - the current game state
     */
    private void handlePortals(Game game) {
        // Go over all of my portals.
        for (Portal portal : game.getMyPortals()) 
        {
		   // a boolean alpha version of inDanger
		    if(inDanger(game,game.getMyCastle()))
                {
		        if(portal.canSummonIceTroll())
		            portal.summonIceTroll();
                }
		      else
		      {
    			// If the portal can summon a lava giant - do that.
                if (portal.canSummonLavaGiant())
                {
                    // Summon the lava giant.
                    portal.summonLavaGiant();
                    // Print a message.
                    System.out.println("portal " + portal + " summons a lava giant");
                }
		      } 
        }
    }
      // try to get the closest enemy Elf to our elf if failed return null
      private Elf getCloseEnemyElf(Game game,Elf chosenElf)
       {
           Elf[] check=game.getEnemyLivingElves();
           // check if there are any enemy elves alive
           if(check.length==0)
            return null;
           Elf closeEnemyElf=game.getEnemyLivingElves()[0];
           for(Elf EnElf:game.getEnemyLivingElves())
            {
                // try to find who is he closest
                if(chosenElf.distance(closeEnemyElf)>chosenElf.distance(EnElf))
                    closeEnemyElf = EnElf;
            }
            return closeEnemyElf;
       }
   
       // try attack enemy Elf or chase him in a radious of 2 times his attack range 
      private boolean canAttackElf(Game game,Elf chosenElf)
       {
            Elf closeEnemyElf=getCloseEnemyElf(game,chosenElf);
           if(closeEnemyElf==null)
            return false;
            if(chosenElf.distance(closeEnemyElf)<chosenElf.attackRange*2)
                 return true;
            return false;
        
                
       }
   
        // attack or chase
        private void attackEnemyElf(Game game,Elf chosenElf)
        {
             Elf closeEnemyElf=getCloseEnemyElf(game,chosenElf);
             System.out.println(chosenElf + " is attacking " + closeEnemyElf);
            if(chosenElf.inAttackRange(closeEnemyElf))
                chosenElf.attack(closeEnemyElf);
            else
            chosenElf.moveTo(closeEnemyElf);
        }
   
      
   // try to run away from one direction
   private boolean canKite(Game game,Elf chosenElf)
   {
       IceTroll closeIceTroll = getCloseEnemyIceTroll(game,chosenElf);
       // check if there is a near IceTroll
       if(closeIceTroll==null)
        return false;
       // check if ice troll is in range to be a danger to our elf
       if(chosenElf.distance(closeIceTroll)<game.iceTrollAttackRange*2)
        return true;
        
        return false;
   }
   
   // run away from one direction
   private void kite(Game game,Elf chosenElf)
   {
        IceTroll closeIceTroll = getCloseEnemyIceTroll(game,chosenElf);
        System.out.println(chosenElf + " is kiting " + closeIceTroll);    
        if(chosenElf.getLocation().towards(closeIceTroll.getLocation(),-(game.elfMaxSpeed)).inMap())
            
                chosenElf.moveTo(chosenElf.getLocation().towards(closeIceTroll.getLocation(),-(game.elfMaxSpeed)));
            else
                {
                // for now when the elf is on the end of the map and cannot move he attack the ice troll
                if(chosenElf.inAttackRange(closeIceTroll))
                    chosenElf.attack(closeIceTroll);
                else 
                    chosenElf.moveTo(closeIceTroll);
                }
   }
   
   
   // try to find the closest ice troll to our elf if failed return null
   private IceTroll  getCloseEnemyIceTroll(Game game, Elf chosenElf)
    {
        IceTroll[] check=game.getEnemyIceTrolls();
        // check if there are any ice trolls
        if(check.length==0)
            return null;
        IceTroll closeIceTroll=game.getEnemyIceTrolls()[0];
        for(IceTroll troll:game.getEnemyIceTrolls())
            {
                //try to find the closest ice gold
                if(chosenElf.distance(closeIceTroll)>chosenElf.distance(troll))
                    closeIceTroll = troll;
            }
        return closeIceTroll;
    }
    // try to find the closest enemy portal to our castle if failed to find return null
    private Portal getEnemyClosePortal(Game game,Elf chosenElf)
    {
        Portal[] check=game.getEnemyPortals();
        // check if there are any portals
        if(check.length==0)
            return null;
        Portal closePortal=game.getEnemyPortals()[0];
        // go all over enemy portals
        for(Portal EnPortal:game.getEnemyPortals())
            {
                // who is closer to our elf
                if(chosenElf.distance(closePortal)>chosenElf.distance(EnPortal))
                    closePortal = EnPortal;
            }
        return closePortal;
    }
    
    // check if the portal is a defensive one or an offensive one true = defense false = offense
   private boolean portalKind(Game game, Portal myPortal)
   {
       // defesive portal
       if(myPortal.distance(game.getMyCastle())<myPortal.distance(game.getEnemyCastle()))
        return true;
        // offensive portal
        return false;
   }
  // try to find the closest lava giant to our castle if failed return null
  private LavaGiant getCloseEnemyLavaGiant(Game game,Castle myCastle)
  {
      LavaGiant[] check = game.getEnemyLavaGiants();
      if(check.length==0)
        return null;
      LavaGiant closeLavaGiant = game.getEnemyLavaGiants()[0];
      for(LavaGiant lava:game.getEnemyLavaGiants())
        {
            if(myCastle.distance(closeLavaGiant)>myCastle.distance(lava))
                closeLavaGiant = lava;
        }
        return closeLavaGiant;
  }
  //try to find the closes lava giant to our portal if failed return null
  private LavaGiant getCloseEnemyLavaGiant(Game game,Portal myPortal)
  {
      LavaGiant[] check= game.getEnemyLavaGiants();
      if(check.length==0)
        return null;
     LavaGiant closeLavaGiant = game.getEnemyLavaGiants()[0];
     for(LavaGiant lava: game.getEnemyLavaGiants())
        {
            if(myPortal.distance(closeLavaGiant)>myPortal.distance(lava))
                closeLavaGiant = lava;
        }
        return closeLavaGiant;
  }
  
  // try to find the closest defensive portal to a LavaGiant if failed return null
  private Portal getBestPortal(Game game)
  {
     //check if there are any LavaGiants
     LavaGiant[] check2= game.getEnemyLavaGiants();
      if(check2.length==0)
        return null;
      // check if we have any portals
      Portal[] check3 = game.getMyPortals();
      if(check3.length==0)
        return null;
      Portal bestPortal = game.getMyPortals()[0];
      // go all over my portals
      for(Portal myPortal:game.getMyPortals())
      {
            // try to find the best defesive portal to defend again the LavaGiants
          if(portalKind(game,myPortal) && bestPortal.distance(getCloseEnemyLavaGiant(game,bestPortal))>myPortal.distance(getCloseEnemyLavaGiant(game,myPortal)))
                bestPortal = myPortal ;
      }
      
      if(portalKind(game,bestPortal))
            return bestPortal;
            
      return null;
  }
  // check if our castle is in danger from an attack of LavaGiants 
  // eventually This method will be provide us a number 0 to 10 which indicate the level of danger we about to witness 
  private boolean inDanger(Game game, Castle myCastle)
  {
      //check if there are any LavaGiants
     LavaGiant[] check2= game.getEnemyLavaGiants();
      if(check2.length==0)
        return false;
        // check if lava giant is in his attack range multiply by 6 or less to our Castle
       if(myCastle.distance(getCloseEnemyLavaGiant(game,myCastle))< game.lavaGiantAttackRange*6)
            return true;
        return false;
  }
//   // the future of inDanger
//   private short inDanger(Game game, Castle myCastle)
//   {
//       //check if there are any LavaGiants
//      LavaGiant[] check2= game.getEnemyLavaGiants();
//       if(check2.length==0)
//         return 0;
//         // check if lava giant is in his attack range multiply by 2 or less to our Castle
//       if(myCastle.distance(getCloseEnemyLavaGiant(game,myCastle))< game.lavaGiantAttackRange*2)
//             return 10;
//         if(myCastle.distance(getCloseEnemyLavaGiant(game,myCastle))< game.lavaGiantAttackRange*3)
//             return 9;
//         if(myCastle.distance(getCloseEnemyLavaGiant(game,myCastle))< game.lavaGiantAttackRange*4)
//             return 8;
//         if(myCastle.distance(getCloseEnemyLavaGiant(game,myCastle))< game.lavaGiantAttackRange*5)
//             return 7;
//         if(myCastle.distance(getCloseEnemyLavaGiant(game,myCastle))< game.lavaGiantAttackRange*6)
//             return 6;
//         if(myCastle.distance(getCloseEnemyLavaGiant(game,myCastle))< game.lavaGiantAttackRange*7)
//             return 5;
//         if(myCastle.distance(getCloseEnemyLavaGiant(game,myCastle))< game.lavaGiantAttackRange*8)
//             return 4;
        
//   }
    // try to attack enemy portalKind
    private boolean canAttackPortal (Game game,Elf chosenElf)
    {
        Portal enemyPortal = getEnemyClosePortal(game, chosenElf);
        // check if there are any enemy portals
        if(enemyPortal==null)
            return false;
        // check if our elf is near the portal in radious of 3 times of his own attack range or less
        if(chosenElf.distance(enemyPortal)< game.elfAttackRange*3)
            return true;
        return false;
                
            
    }
    // attack or moveTo enemy close portal
    private void attackPortal(Game game,Elf chosenElf)
    {
        Portal enemyPortal = getEnemyClosePortal(game, chosenElf);
        if(chosenElf.inAttackRange(enemyPortal))
            chosenElf.attack(enemyPortal);
        else
            chosenElf.moveTo(enemyPortal);
    }
    
    // decide between elf or portal whom to attack or follow
    private void elfAttack(Game game,Elf chosenElf)
    {
        // try to attack or follow an enemy Elf
        if(canAttackElf(game,chosenElf))
            attackEnemyElf(game,chosenElf);
            //try to attack or move towards a portal
            else if(canAttackPortal(game,chosenElf))
                        attackPortal(game,chosenElf);
    }
    // try to active an elf attack manuver
    private boolean canElfAttack(Game game,Elf chosenElf)
    {
        // try to attack or follow an enemy Elf
        if(canAttackElf(game,chosenElf))
            return true;
        //try to attack or move towards a portal
        if(canAttackPortal(game,chosenElf))
            return true;
        return false;
    }
    // return an array of all our defensive portals if there are no portals return null
    private  Portal[] getDefensivePortals(Game game)
    {
        int count=0;
        for(Portal myPortal:game.getMyPortals())
            {
                if(portalKind(game,myPortal))
                    count++;
            }
        Portal[] defensivePortals= new Portal[count];
        if(count==0)
            return defensivePortals;// there are no portals in the array
        int i=0;// index for our array in an each for
        for(Portal myPortal:game.getMyPortals())
        {
            if(portalKind(game,myPortal))
                defensivePortals[i]=myPortal;
            i+=1;
        }
        return defensivePortals;
    }
    // return an array of all our offensive portals if there are no portals return 0
    private Portal[] getOffensivePortals(Game game)
    {
        int count=0;
        for(Portal myPortal:game.getMyPortals())
            {
                if(!portalKind(game,myPortal))
                    count++;
            }
        
        Portal[] offensivePortals= new Portal[count];
        if(count==0)
            return offensivePortals;
        int i=0;// index for our array in an each for
        for(Portal myPortal:game.getMyPortals())
        {
            if(portalKind(game,myPortal))
                offensivePortals[i]=myPortal;
            i+=1;
        }
        return offensivePortals;
    }
    
    // try to find all the ice trolls in 2 times their attack range or less 
    private Stack<IceTroll> elfAura(Game game, Elf chosenElf)
    {
        Stack <IceTroll> closeIceTrolls = new Stack<IceTroll>();
        for(IceTroll enemyIceTroll: game.getEnemyIceTrolls())
            {
                if(chosenElf.distance(enemyIceTroll)<= game.iceTrollAttackRange * 2 )
                    closeIceTrolls.push(enemyIceTroll);
            }
        
        return closeIceTrolls;
    }
     // try to find all the ice trolls in 2 times their attack range or less if not founnd return 0 
    private IceTroll[] getElfAura(Game game, Elf chosenElf)
    {
        Stack <IceTroll> iceTrolls = elfAura(game,chosenElf);
        int count =0;
        Stack <IceTroll> copyIceTroll = copyS(iceTrolls);
        while(!copyIceTroll.isEmpty())
        {
            copyIceTroll.pop();
            count+=1;
        }
        IceTroll[] closeIceTrolls = new IceTroll[count];
        if(count==0)
            return closeIceTrolls;
        int i=0; // index for the array
        while(!iceTrolls.isEmpty())
        {
            closeIceTrolls[i] = iceTrolls.pop();
            i+=1;
        }
        return closeIceTrolls;
    }
    // copy a Stack
    private <T>Stack<T> copyS(Stack<T> s)
    {
        Stack <T> temp = new Stack<T>();
        Stack <T> copyS = new Stack<T>();
        while(!s.isEmpty())
        {
            temp.push(s.pop());
        }
        while(!temp.isEmpty())
        {
            copyS.push(temp.peek());
            s.push(temp.pop());
        }
        return copyS;
    }
    // check if there is at least one defensivePortal
    private boolean isDefensive(Game game)
    {
       Portal[] defensivePortals = getDefensivePortals(game);
       if(defensivePortals.length==0)
            return false;
        
        return true;
    }
    //check if there is at least one offensivePortals
    private boolean isOffensive(Game game)
    {
        Portal[] offensivePortals = getOffensivePortals(game);
        if(offensivePortals.length==0)
            return false;
        return true;
    }
    // if there is no defensive portal around our castle return true
    private boolean shouldMakeDefensivePortal(Game game)
    {
        if(!isDefensive(game))
            return false;
        return true;
        
    }
    
}

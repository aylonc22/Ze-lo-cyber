package bots;
import elf_kingdom.*;
import java.util.*;
public class Vectors
{
    private int Power;// Priority of the vector 
    private Location location;// location of the object which the vector implements 
    public Vectors(GameObject object)
    {
        this.location = object.getLocation();
        this.Power = setPower(object);
    }
    // calculate the power of an object
    private static int setPower(GameObject object)
    {
       if(object.type.equals("IceTrol"))
    	   return 1;
        if(object.type.equals("Elf"))
            return 2;
        if(object.type.equals("Portal"))
            return -1;
        if(object.type.equals("Castle"))
            return -2;
        return 0; //if(object.type.equals("LavaGiant"))
    }
    public Location getLocation()
    {
        return this.location;
    }
    public int getPower()
    {
        return this.Power;
    }
}

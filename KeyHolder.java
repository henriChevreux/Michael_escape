
/**
 * This class is part of the "Michael's escape" application. 
 * "Michael's escape" is a very simple, text based adventure game.
 * 
 * The unique key holder of the game is essential as he must be found and talked to win the game. 
 * 
 * The KeyHolder class is a child of the Entity class as they share most properties.
 * A key holder is the same as a Entity with an extra digitCode that is entered in the escape pod.
 *
 * @author Henri Chevreux
 * @version 2016.02.29
 */
public class KeyHolder extends Entity
{
    private static String digitCode;
    
    /**
     * Constructor for objects of class KeyHolder
     */
    public KeyHolder(String nameOfEntity, String identityOfEntity)
    {
        super(nameOfEntity, identityOfEntity);
    }
    
    public KeyHolder(String nameOfEntity)
    {
        super(nameOfEntity,"Human");
    }
    
    public static String getDigitCode()
    {
        return digitCode;
    }
    
    public static void setDigitCode(String newCode)
    {
        digitCode = newCode;
    }
    
    public String getDescription()
    {
        return "Ah! Finally you found me.\n"+
        "Quick, let's find the escape pod so we can get out of there. The digit code to unlock the door is: "+digitCode;
    }
}

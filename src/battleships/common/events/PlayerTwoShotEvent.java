package battleships.common.events;

/**
 * Игрок 2 выстрелил.
 * @author ALEX
 */
public class PlayerTwoShotEvent extends ShotEvent 
{
    private static final long serialVersionUID = 1L;
    public PlayerTwoShotEvent(int x, int y) 
    {
        super(x, y);
    }
}

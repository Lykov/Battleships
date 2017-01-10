package battleships.common.events;

/**
 * Игрок 1 выстрелил.
 * @author ALEX
 */
public class PlayerOneShotEvent extends ShotEvent 
{
    private static final long serialVersionUID = 1L;
    public PlayerOneShotEvent(int x, int y) {
        super(x, y);
    }
}

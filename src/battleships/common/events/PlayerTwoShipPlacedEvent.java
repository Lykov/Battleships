package battleships.common.events;

import battleships.common.ShipType;

/**
 * Игрок 2 разместил корабли.
 * @author ALEX
 */
public class PlayerTwoShipPlacedEvent extends ShipPlacedEvent 
{
    private static final long serialVersionUID = 1L;
    public PlayerTwoShipPlacedEvent(int x, int y, ShipType ship) 
    {
        super(x, y, ship);
    }
}

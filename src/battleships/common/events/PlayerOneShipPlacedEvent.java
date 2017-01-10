package battleships.common.events;

import battleships.common.ShipType;

/**
 * Игрок 1 разместил корабли.
 * @author ALEX
 */
public class PlayerOneShipPlacedEvent extends ShipPlacedEvent 
{
    private static final long serialVersionUID = 1L;
    public PlayerOneShipPlacedEvent(int x, int y, ShipType ship) {
        super(x, y, ship);
    }

}

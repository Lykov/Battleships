package battleships.common.events;

import battleships.common.ShipType;

/**
 * Игрок разместил на поле корабль
 * @author ALEX
 */
public abstract class ShipPlacedEvent extends GameEvent
{
  protected final ShipType shipType;
  protected final int x,y;
      
  /** 
  * Конструктор ShipPlacedEvent
  * @param x координата по x
  * @param y координата по y
  * @param ship тип корабля 
  */
  public ShipPlacedEvent(final int x, final int y, final ShipType ship) {
    super();
    this.x = x;
    this.y = y;
    shipType = ship;
  }
 
  /** @return x*/
  public int getX() {
    return x;
  }

  /** @return y*/
  public int getY() {
    return y;
  }

  /** @return тип корабля */
  public ShipType getShipType() {
    return shipType;
  }
}

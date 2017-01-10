package battleships.common.events;

/**
 * Игрок сделал выстрел
 * @author ALEX
 */
public abstract class ShotEvent extends GameEvent 
{
    private final int x,y;
  
  /** 
   * ShotEvent constructor 
     * @param x координата по x
     * @param y координата по y
     * 
   */
  public ShotEvent(final int x, final int y) {
    super();
    this.x = x;
    this.y = y;
  }
    
  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}

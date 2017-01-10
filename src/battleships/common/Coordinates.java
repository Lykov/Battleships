package battleships.common;
/** Инкапсулирует координаты корабля
 * @author ALEX 
 */
public class Coordinates 
{
  private final int x, y;
  
  /** Создание объекта "Координаты"
     * @param newX Координата по X<br>
     * @param newY Координата по Y */
  public Coordinates(int newX, int newY) {
    if(newX < 0 || newX > 9 || newY < 0 || newY > 9) 
    {
      throw new IllegalArgumentException("Неверные координаты!");
    } 
    else 
    {
      x = newX;
      y = newY;
    }
  }

  /** @return координата по X */
  public int getX() {
    return x;
  }

  /** @return координата по Y */
  public int getY() {
    return y;
  }
}

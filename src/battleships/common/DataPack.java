package battleships.common;

import java.io.Serializable;

/**
 * @author ALEX
 * Содержит данные, необходимые для отображения кораблей.
 */
public class DataPack implements Serializable 
{
  private static final long serialVersionUID = 1L;
  public ShipType playerShips[][], opponentShips[][];
  public ShotField playerShots[][], opponentShots[][];
  public DataPack() 
  {
    playerShips = new ShipType[10][10];
    opponentShips = new ShipType[10][10];
    playerShots = new ShotField[10][10];
    opponentShots = new ShotField[10][10];
  }
 
}

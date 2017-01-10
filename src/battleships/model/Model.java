package battleships.model;

import java.util.LinkedList;
import java.util.List;
import battleships.common.Coordinates;
import battleships.common.DataPack;
import battleships.common.ShipType;
import battleships.common.ShotField;

/**
 * Модель - отвечает за хранение и модификацию текущего состояния игры.
 * @author ALEX
 */
public class Model {
  //Поля, хранящие текущее состояние игры
  private final List<Ship> playerOneShips;
  private final List<Ship> playerTwoShips;
  private final ShotField[][] playerOneShots;
  private final ShotField[][] playerTwoShots;
  
  /** Список кораблей, которые осталось поставить на игровые поля */
  private final List<ShipType> playerOneShipsLeft, playerTwoShipsLeft;
  
  public Model() {
    playerOneShips = new LinkedList<>();
    playerTwoShips = new LinkedList<>();
    playerOneShots = new ShotField[10][10];
    playerTwoShots = new ShotField[10][10];
    playerOneShipsLeft = generateShipSet();
    playerTwoShipsLeft = generateShipSet();
  }
  
  /** Добавляем корабль на поле первого игрока
     * @param begin координаты первой клетки
     * @param type тип корабля
   *  @return true если корабль был успешно добавлен */
  public boolean putAndCheckPlayerOneShip(final Coordinates begin, final ShipType type) 
  {
    try 
    {
      Ship newShip = new Ship(begin, type);
        //Проверка координат
        if (!playerOneShips.stream().noneMatch((ship) -> (ship.checkIfCollides(newShip)))) 
        {
            return false;
        }
      
      //Координаты верные - добавляем!
      playerOneShips.add(newShip);
      
      //кдаляем корабль из playerOneShipsLeft
      if(type.isHorizontal())
        playerOneShipsLeft.remove(type);
      else
        playerOneShipsLeft.remove(type.returnRotatedShip());
      
    } catch(IllegalArgumentException e) 
    {
      return false;
    }
    return true;
  }
  
  /** Добавляем корабль на поле первого игрока
     * @param begin координаты первой клетки
     * @param type тип корабля
   *  @return true если корабль был успешно добавлен */
  public boolean putAndCheckPlayerTwoShip(final Coordinates begin, final ShipType type) 
  {
    try {
      Ship newShip = new Ship(begin, type);
      
        //Проверка координат
        if (!playerTwoShips.stream().noneMatch((ship) -> (ship.checkIfCollides(newShip)))) 
        {
            return false;
        }
      
      //Координаты верные - добавляем!
      playerTwoShips.add(newShip);
      
      //Удаляем корабль из playerTwoShipsLeft
      if(type.isHorizontal())
        playerTwoShipsLeft.remove(type);
      else
        playerTwoShipsLeft.remove(type.returnRotatedShip());
      
    } catch(IllegalArgumentException e) 
    {
      return false;
    }
    
    return true;
  }
  
  /** Игрок 1 сделал выстрел
     * @param coordinates координаты выстрела
     * @return true, если игрок попал и false в противном случае 
     */
  public boolean checkPlayerOneShot(final Coordinates coordinates) {
    
    //Смотрим, попал ли игрок в хотя бы один из кораблей
    for(Ship ship : playerTwoShips) {
      if(ship.shotAndCheckIfHit(coordinates)) {
        playerOneShots[coordinates.getX()][coordinates.getY()] = ShotField.HIT;
        return true;
      }
    }
    
    playerOneShots[coordinates.getX()][coordinates.getY()] = ShotField.MISHIT;
    return false;
  }
  
  /** Игрок 2 сделал выстрел
     * @param coordinates координаты выстрела
     * @return true, если игрок попал и false в противном случае 
     */
  public boolean checkPlayerTwoShot(final Coordinates coordinates) {
    
    //Смотрим, попал ли игрок в хотя бы один из кораблей
    for(Ship ship : playerOneShips) {
      if(ship.shotAndCheckIfHit(coordinates)) {
        playerTwoShots[coordinates.getX()][coordinates.getY()] = ShotField.HIT;
        return true;
      }
    }
    
    playerTwoShots[coordinates.getX()][coordinates.getY()] = ShotField.MISHIT;
    return false;
  }
  
  /** 
   * Сохранение текущего состояния Игрока 1 в объект класса DataPack
     * @return объект DataPack Игрока 1
     * @see battleships.common.DataPack
   */
  public DataPack generatePlayerOneDataPack() {
    DataPack data = new DataPack();
    
    //Сделанные выстрелы
    for(int i = 0; i != 10; ++i)
      for(int j = 0; j != 10; ++j) {
        data.playerShots[i][j] = playerOneShots[i][j];
        data.opponentShots[i][j] = playerTwoShots[i][j];
      }
    
      //Свои корабли
    playerOneShips.stream().forEach((ship) -> {
        ShipType type = ship.getShipType();
        Coordinates location = ship.getBeginingCoordinates();
        data.playerShips[location.getX()][location.getY()] = type;
    });
    
      //Корабли противника
    playerTwoShips.stream().filter((ship) -> (ship.isSunken())).forEach((ship) -> {
        ShipType type = ship.getShipType();
        Coordinates location = ship.getBeginingCoordinates();
        data.opponentShips[location.getX()][location.getY()] = type;
    });
    
    return data;
  }
  
  /** 
   * Сохранение текущего состояния Игрока 2 в объект класса DataPack
     * @return объект DataPack Игрока 2
     * @see battleships.common.DataPack
   */
  public DataPack generatePlayerTwoDataPack() {
    DataPack data = new DataPack();
    
    //Сделанные выстрелы
    for(int i = 0; i != 10; ++i)
      for(int j = 0; j != 10; ++j) {
        data.playerShots[i][j] = playerTwoShots[i][j];
        data.opponentShots[i][j] = playerOneShots[i][j];
      }
    
      //Свои корабли
    playerTwoShips.stream().forEach((ship) -> {
        ShipType type = ship.getShipType();
        Coordinates location = ship.getBeginingCoordinates();
        data.playerShips[location.getX()][location.getY()] = type;
    });
    
      //Корабли противника
    playerOneShips.stream().filter((ship) -> (ship.isSunken())).forEach((ship) -> 
    {
        ShipType type = ship.getShipType();
        Coordinates location = ship.getBeginingCoordinates();
        data.opponentShips[location.getX()][location.getY()] = type;
    });
    
    return data;
  }
  
  /** 
   * Генерирует список кораблей, которые необходимо поместить на игровое поле 
   */
  private List<ShipType> generateShipSet() {
    List<ShipType> shipSet = new LinkedList<>();
    shipSet.add(ShipType.BATTLESHIP_HORIZONTAL);
    shipSet.add(ShipType.SUBMARINE_HORIZONTAL);
    shipSet.add(ShipType.CRUISER_HORIZONTAL);
    shipSet.add(ShipType.PATROL_BOAT);
    shipSet.add(ShipType.PATROL_BOAT);
    return shipSet;
  }
  
  /** 
   * Получает очередной тип корабля, который необходимо 
   * поместить на поле Игрока 1 
   * @return тип корабля 
   */
  public ShipType getNextShipForPlayerOne() {
    return playerOneShipsLeft.get(0);
  }
  
  /** 
   * Получает очередной тип корабля, который необходимо 
   * поместить на поле Игрока 2 
   * @return тип корабля 
   */
  public ShipType getNextShipForPlayerTwo() {
    return playerTwoShipsLeft.get(0);
  }
  
  /** Проверяет поместил ли игрок 2 все корабли
     * @return  true - все корабли размещены,
     * false - в противном случае 
     */
  public boolean playerOnePlacedAllShips() {
    return playerOneShipsLeft.isEmpty();
  }
  
  /** Проверяет поместил ли игрок 2 все корабли
     * @return  true - все корабли размещены,
     * false - в противном случае 
     */
  public boolean playerTwoPlacedAllShips() {
    return playerTwoShipsLeft.isEmpty();
  }
  
  /** Проверяет, выиграл ли Игрок 1
     * @return true - все корабли противника уничтожены, 
     * false в противном случае
     */
  public boolean playerOneWon() 
  {
    return playerTwoShips.stream().noneMatch((ship) -> (!ship.isSunken()));
  }
  
  /** Проверяет, выиграл ли Игрок 2
     * @return true - все корабли противника уничтожены, 
     * false в противном случае
     */
  public boolean playerTwoWon() 
  {
    return playerOneShips.stream().noneMatch((ship) -> (!ship.isSunken()));
  }
   
}

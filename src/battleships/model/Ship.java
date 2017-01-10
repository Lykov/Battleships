package battleships.model;

import java.util.LinkedList;
import java.util.List;
import battleships.common.Coordinates;
import battleships.common.ShipType;

/**
 * Класс корабля.
 * @author ALEX
 */
class Ship 
{
  /** Палубы */
  private final List<Mast> mastsList;
  /** Тип корабля */
  private final ShipType shipType;
  /** Уничтожен или нет */
  private boolean sunken;

  /** 
   * Конструктор класса
   * 
   */
  public Ship(final Coordinates begin, final ShipType ship) 
  {
    sunken = false;
    shipType = ship;
    mastsList = new LinkedList<>();
    int x = begin.getX(), y = begin.getY();
    
    if(ship.isHorizontal()) 
    {
      for(int mastsCount = ship.getLength(); mastsCount != 0; --mastsCount) 
      {
        mastsList.add(new Mast(x, y));
        ++x;
      }
    } 
    else 
    {
      for(int mastsCount = ship.getLength(); mastsCount != 0; --mastsCount) 
      {
        mastsList.add(new Mast(x, y));
        ++y;
      }
    }
    
  }
  
  /** Проверяем, попали ли по кораблю */
  public boolean shotAndCheckIfHit(final Coordinates coords) 
  {
    boolean result = false;
    sunken = true;
    //Проверяем каждую палубу
    for(Mast mast : mastsList)
      if(mast.shotAndCheck(coords)) result = true;
      
    //Проверяем, был ли корабль уничтожен и изменяем при необходимости
    //соответствующую переменную
    mastsList.stream().filter((mast) -> (!mast.isHit())).forEach((_item) -> 
    {
        sunken = false;
    });
    
    return result;
  }

  /** @return true, если корабль был уничтожен */
  public boolean isSunken() {
    return sunken;
  }

  /** @return true если произошла коллизия при размещении корабля */
  public boolean checkIfCollides(final Ship another) 
  {
    return mastsList.stream().anyMatch((thisMast) -> 
            (another.mastsList.stream().anyMatch((anotherMast) -> 
                    (thisMast.checkIfCollides(anotherMast)))));
  }
  
  /** @return тип корабля */
  public ShipType getShipType() {
    return shipType;
  }
  
  /** @return координаты корабля */
  public Coordinates getBeginingCoordinates() {
    return mastsList.get(0).getCoordinates(); 
  }
}

/** Класс палубы корабля */
class Mast 
{
  private final Coordinates coordinates;
  private boolean hit;
  
  public Mast(final int x, final int y) {
    coordinates = new Coordinates(x, y);
    hit = false;
  }

  /** Проверяем, подбили ли одну из палуб 
   * и изменяем при необходимости переменную hit 
   */
  public boolean shotAndCheck(final Coordinates coords) {
    if(getCoordinates().getX() != coords.getX() ||
       getCoordinates().getY() != coords.getY()) return false;
    if(getCoordinates().getX() == coords.getX() &&
       getCoordinates().getY() == coords.getY()) hit = true;
    return hit;
  }

  /** @return true если попали по одной из палуб */
  public Boolean isHit() {
    return hit;
  }
  
  /** @return true, если прозошла коллизия при попытке размещения корабля */
  public boolean checkIfCollides(final Mast another) 
  {
      return Math.abs(this.getCoordinates().getX() - another.getCoordinates().getX()) <= 1 &&
              Math.abs(this.getCoordinates().getY() - another.getCoordinates().getY()) <= 1;
  }

  /** @return координаты корабля */
  public Coordinates getCoordinates() {
    return coordinates;
  }
}

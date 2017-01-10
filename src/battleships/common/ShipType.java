package battleships.common;

/**
 * @author ALEX
 * Типы кораблей на игровом поле.
 */
public enum ShipType 
{
  
  /** 
   * 1-палубный корабль 
   */
  PATROL_BOAT {
    @Override
    public ShipType returnRotatedShip() { return ShipType.PATROL_BOAT; }
    @Override
    public boolean isHorizontal() { return false; }
    @Override
    public int getLength() { return 1; }
  },
  /**
   * SUNKEN - "убит"
   */
  PATROL_BOAT_SUNKEN {
    @Override
    public ShipType returnRotatedShip() { return ShipType.PATROL_BOAT_SUNKEN; }
    @Override
    public boolean isHorizontal() { return false; }
    @Override
    public int getLength() { return 1; }
  },
  
  /** 
   * 2-палубный корабль 
   */
  CRUISER_VERTICAL {
    @Override
    public ShipType returnRotatedShip() { return ShipType.CRUISER_HORIZONTAL; }
    @Override
    public boolean isHorizontal() { return false; }
    @Override
    public int getLength() { return 2; }
  },
  CRUISER_HORIZONTAL {
    @Override
    public ShipType returnRotatedShip() { return ShipType.CRUISER_VERTICAL; }
    @Override
    public boolean isHorizontal() { return true; }
    @Override
    public int getLength() { return 2; }
  },
  CRUISER_VERTICAL_SUNKEN {
    @Override
    public ShipType returnRotatedShip() { return ShipType.CRUISER_HORIZONTAL_SUNKEN; }
    @Override
    public boolean isHorizontal() { return false; }
    @Override
    public int getLength() { return 2; }
  },
  CRUISER_HORIZONTAL_SUNKEN {
    @Override
    public ShipType returnRotatedShip() { return ShipType.CRUISER_VERTICAL_SUNKEN; }
    @Override
    public boolean isHorizontal() { return true; }
    @Override
    public int getLength() { return 2; }
  },
  
  /**
   * 3-палубный корабль 
   */
  SUBMARINE_VERTICAL {
    @Override
    public ShipType returnRotatedShip() { return ShipType.SUBMARINE_HORIZONTAL; }
    @Override
    public boolean isHorizontal() { return false; }
    @Override
    public int getLength() { return 3; }
  },
  SUBMARINE_HORIZONTAL {
    @Override
    public ShipType returnRotatedShip() { return ShipType.SUBMARINE_VERTICAL; }
    @Override
    public boolean isHorizontal() { return true; }
    @Override
    public int getLength() { return 3; }
  },
  SUBMARINE_VERTICAL_SUNKEN {
    @Override
    public ShipType returnRotatedShip() { return ShipType.SUBMARINE_HORIZONTAL_SUNKEN; }
    @Override
    public boolean isHorizontal() { return false; }
    @Override
    public int getLength() { return 3; }
  },
  SUBMARINE_HORIZONTAL_SUNKEN {
    @Override
    public ShipType returnRotatedShip() { return ShipType.SUBMARINE_VERTICAL_SUNKEN; }
    @Override
    public boolean isHorizontal() { return true; }
    @Override
    public int getLength() { return 3; }
  },
  
  /** 
   * 4-палубный корабль 
   */
  BATTLESHIP_VERTICAL {
    @Override
    public ShipType returnRotatedShip() { return ShipType.BATTLESHIP_HORIZONTAL; }
    @Override
    public boolean isHorizontal() { return false; }
    @Override
    public int getLength() { return 4; }
  },
  BATTLESHIP_HORIZONTAL {
    @Override
    public ShipType returnRotatedShip() { return ShipType.BATTLESHIP_VERTICAL; }
    @Override
    public boolean isHorizontal() { return true; }
    @Override
    public int getLength() { return 4; }
  },
  BATTLESHIP_VERTICAL_SUNKEN {
    @Override
    public ShipType returnRotatedShip() { return ShipType.BATTLESHIP_HORIZONTAL_SUNKEN; }
    @Override
    public boolean isHorizontal() { return false; }
    @Override
    public int getLength() { return 4; }
  },
  BATTLESHIP_HORIZONTAL_SUNKEN {
    @Override
    public ShipType returnRotatedShip() { return ShipType.BATTLESHIP_VERTICAL_SUNKEN; }
    @Override
    public boolean isHorizontal() { return true; }
    @Override
    public int getLength() { return 4; }
  };

  public abstract ShipType returnRotatedShip();
  public abstract boolean isHorizontal();
  public abstract int getLength();
}

package battleships.view;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import battleships.common.Coordinates;
import battleships.common.ShipType;
import battleships.common.ShotField;

/**
 * Игровое поле, которое отображает свои корабли
 * и корабли противника.
 * @author ALEX
 */
class Board extends JPanel 
{
  private static final long serialVersionUID = 1L;
  private final Map<ShipType, Image> imageMap;
  private ShipType currentlyPlacedShip;
  private Coordinates mousePosition;
  private Image board, hit, mishit;
  private ShotField shotsBoard[][];
  private ShipType shipsBoard[][];

  public Board() {
    currentlyPlacedShip = null;
    imageMap = new TreeMap<>();
    fillImageMap();
    try 
    {
      hit = ImageIO.read(getClass().getResource("img/hit.gif"));
      board = ImageIO.read(getClass().getResource("img/board.gif"));
      mishit = ImageIO.read(getClass().getResource("img/mishit.gif"));
    } catch(IOException e) 
    {
      System.out.println(e);
    }
  }

  /** Переопределенный метод отрисовки поля */
  @Override 
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(board, 0, 0, null);
    
    //отрисовка корабля, который сейчас размещается на поле
    if(currentlyPlacedShip != null)
      g.drawImage(imageMap.get(currentlyPlacedShip),
          mousePosition.getX()*40, mousePosition.getY()*40, null);
    
    //отрисовка кораблей и сделанных выстрелов
    if(shotsBoard != null && shipsBoard != null) {
      for(int i = 0; i != 10; ++i)
        for(int j = 0; j != 10; ++j) {
          if(shipsBoard[i][j] != null)
            g.drawImage(imageMap.get(shipsBoard[i][j]), i*40, j*40, null);
          if(shotsBoard[i][j] == ShotField.HIT)
            g.drawImage(hit,  i*40, j*40, null);
          if(shotsBoard[i][j] == ShotField.MISHIT)
            g.drawImage(mishit,  i*40, j*40, null);
        }
    }
  }
  
  /** Обновление игрового поля */
  public void refreshBoard(final ShipType ships[][], final ShotField shots[][]) 
  {
    shotsBoard = shots;
    shipsBoard = ships;
    repaint();
  }
  
  /** Заполнение Map, ассоциирующего тип корабля с соответствующим изображением */
  private void fillImageMap() 
  {
    Image shipImage;
    try 
    {
      shipImage = ImageIO.read(getClass().getResource("img/patrol_boat.png"));
      imageMap.put(ShipType.PATROL_BOAT, shipImage);
      shipImage = ImageIO.read(getClass().getResource("img/patrol_boat_sunken.png"));
      imageMap.put(ShipType.PATROL_BOAT_SUNKEN, shipImage);
      shipImage = ImageIO.read(getClass().getResource("img/cruiser_horizontal.png"));
      imageMap.put(ShipType.CRUISER_HORIZONTAL, shipImage);
      shipImage = ImageIO.read(getClass().getResource("img/cruiser_horizontal_sunken.gif"));
      imageMap.put(ShipType.CRUISER_HORIZONTAL_SUNKEN, shipImage);
      shipImage = ImageIO.read(getClass().getResource("img/cruiser_vertical.png"));
      imageMap.put(ShipType.CRUISER_VERTICAL, shipImage);
      shipImage = ImageIO.read(getClass().getResource("img/cruiser_vertical_sunken.gif"));
      imageMap.put(ShipType.CRUISER_VERTICAL_SUNKEN, shipImage);
      shipImage = ImageIO.read(getClass().getResource("img/submarine_horizontal.png"));
      imageMap.put(ShipType.SUBMARINE_HORIZONTAL, shipImage);
      shipImage = ImageIO.read(getClass().getResource("img/submarine_horizontal_sunken.gif"));
      imageMap.put(ShipType.SUBMARINE_HORIZONTAL_SUNKEN, shipImage);
      shipImage = ImageIO.read(getClass().getResource("img/submarine_vertical.png"));
      imageMap.put(ShipType.SUBMARINE_VERTICAL, shipImage);
      shipImage = ImageIO.read(getClass().getResource("img/submarine_vertical_sunken.gif"));
      imageMap.put(ShipType.SUBMARINE_VERTICAL_SUNKEN, shipImage);
      shipImage = ImageIO.read(getClass().getResource("img/battleship_horizontal.gif"));
      imageMap.put(ShipType.BATTLESHIP_HORIZONTAL, shipImage);
      shipImage = ImageIO.read(getClass().getResource("img/battleship_horizontal_sunken.gif"));
      imageMap.put(ShipType.BATTLESHIP_HORIZONTAL_SUNKEN, shipImage);
      shipImage = ImageIO.read(getClass().getResource("img/battleship_vertical.gif"));
      imageMap.put(ShipType.BATTLESHIP_VERTICAL, shipImage);
      shipImage = ImageIO.read(getClass().getResource("img/battleship_vertical_sunken.gif"));
      imageMap.put(ShipType.BATTLESHIP_VERTICAL_SUNKEN, shipImage);
    } 
    catch(IOException e) 
    {
        System.out.println(e);
    }
  }

  /** @return корабль, размещаемый в данный момент */
  public ShipType getCurrentlyPlacedShip() {
    return currentlyPlacedShip;
  }

  /** @param currentlyPlacedShip корабль, котрый нужно разместить на поле */
  public void placeShip(ShipType currentlyPlacedShip) 
  {
    this.currentlyPlacedShip = currentlyPlacedShip;
  }

  /** * @param mouseBoardPosition позиция мыши, которую необходимо установить */
  public void setMousePosition(Coordinates mousePosition) 
  {
    this.mousePosition = mousePosition;
  }
  
}

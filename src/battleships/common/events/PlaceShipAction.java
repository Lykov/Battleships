package battleships.common.events;

import battleships.common.ShipType;
import battleships.view.View;

/**
 * Произошло размешение корабля на игровом поле.
 * @author ALEX
 */
public class PlaceShipAction extends ActionEvent {
  private static final long serialVersionUID = 1L;
  public final ShipType shipToPlace;

  public PlaceShipAction(ShipType ship) 
  {
    shipToPlace = ship;
  }

  /* @see battleships.common.events.ActionEvent#execute(battleships.view.View) */
  @Override public void execute(View view) 
  {
    view.placeShip(shipToPlace);
    view.changeStatus("Пожалуйста разместите корабль на игровом поле (нажмите правую кнопку мыши, чтобы повернуть корабль).");
  }

}

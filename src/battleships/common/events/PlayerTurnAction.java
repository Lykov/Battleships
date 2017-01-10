package battleships.common.events;

import battleships.view.View;

/**
 * Событие задает очередность ходов.
 * @author ALEX
 */
public class PlayerTurnAction extends ActionEvent {
  private static final long serialVersionUID = 1L;

  /* @see battleships.common.events.ActionEvent#execute(battleships.view.View) */
  @Override public void execute(View view) {
     view.placeShip(null);
     view.changeStatus("Ваш ход");
  }

}

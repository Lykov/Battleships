package battleships.common.events;

import battleships.view.View;

/**
 * Противник сделал ход.
 * @author ALEX
 */
public class OpponentTurnAction extends ActionEvent 
{
  private static final long serialVersionUID = 1L;

  /* @see battleships.common.events.ActionEvent#execute(battleships.view.View) */
  @Override public void execute(View view) 
  {
     view.placeShip(null);
     view.changeStatus("Дождитесь Вашей очереди");
  }

}


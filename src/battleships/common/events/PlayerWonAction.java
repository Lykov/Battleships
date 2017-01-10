package battleships.common.events;

import battleships.view.View;

/**
 * Уведомляет игрока о выигрыше
 * @author ALEX
 */
public class PlayerWonAction extends ActionEvent 
{
  private static final long serialVersionUID = 1L;

  /* @see battleships.common.events.ActionEvent#execute(battleships.view.View) */
  @Override public void execute(View view) {
    view.changeStatus("Вы выиграли!");
  }

}

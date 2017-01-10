package battleships.common.events;

import battleships.view.View;

/**
 * Уведомляет игрока о проигрыше
 * @author ALEX
 */
public class PlayerLostAction extends ActionEvent {
  private static final long serialVersionUID = 1L;

  /* @see battleships.common.events.ActionEvent#execute(battleships.view.View) */
  @Override public void execute(View view) 
  {
    view.changeStatus("Вы проиграли.");
  }
}

package battleships.common.events;

import battleships.common.DataPack;
import battleships.view.View;

/**
 * Обновление View игрока.
 * @author ALEX
 */
public class RefreshViewAction extends ActionEvent {
  private static final long serialVersionUID = 1L;
  private final DataPack dataPack;

  public RefreshViewAction(DataPack dataPack) {
    this.dataPack = dataPack;
  }

  /* @see battleships.common.events.ActionEvent#execute(battleships.view.View) */
  @Override public void execute(View view) {
    view.refreshView(dataPack);
  }

}

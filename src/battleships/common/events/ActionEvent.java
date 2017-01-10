package battleships.common.events;

import java.io.Serializable;

import battleships.view.View;

/**
 * Класс, представляющий события, используемые, чтобы вызывать методы View и
 * уведомлять контроллер.
 * @author ALEX
 */
public abstract class ActionEvent implements Serializable 
{
  private static final long serialVersionUID = 1L;
  public abstract void execute(View view);
}

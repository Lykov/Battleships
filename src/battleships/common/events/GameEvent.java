package battleships.common.events;

import java.io.Serializable;

/**
 * Класс игровых событий.
 * Используется для передачи сообщений между всеми компонентами MVC 
 * @author ALEX
 */
public abstract class GameEvent implements Serializable 
{
  private static final long serialVersionUID = 1L;
}

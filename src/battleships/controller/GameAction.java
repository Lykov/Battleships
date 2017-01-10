package battleships.controller;

import battleships.common.events.GameEvent;

/**
 * Объекты, реализующие данный интерфейс определяют
 * реакцию контроллера на игровые события
 * @author ALEX
 */
public interface GameAction 
{
  abstract public void execute(GameEvent e);
}

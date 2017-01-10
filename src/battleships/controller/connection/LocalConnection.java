package battleships.controller.connection;

import java.util.concurrent.BlockingQueue;

import battleships.common.ShipType;
import battleships.common.events.ActionEvent;
import battleships.common.events.GameEvent;
import battleships.common.events.NewGameEvent;
import battleships.common.events.PlayerOneShipPlacedEvent;
import battleships.common.events.PlayerOneShotEvent;
import battleships.view.View;

/**
 * Инкапсулирует локальное взаимодействие View-Controller.
 * @author ALEX
 */
public class LocalConnection implements Connection 
{
  private final BlockingQueue<GameEvent> eventQueue;
  private final View localView;
  
  public LocalConnection(BlockingQueue<GameEvent> queue, View view) {
    eventQueue = queue;
    localView = view;
  }
  
  /** Вызов методов View
     * @param event событие */
  @Override 
  public void sendActionEvent(final ActionEvent event) {
    event.execute(localView);
  }
  
  /** Посылаем событие о выстреле контроллеру
     * @param x координата x выстрела
     * @param y координата y выстрела 
     */
  @Override 
  public void sendShotEvent(final int x, final int y) {
    GameEvent event = new PlayerOneShotEvent(x, y);
    try 
    {
      eventQueue.put(event);
    } 
    catch(InterruptedException e) 
    {
        System.out.println(e);
    }
  }

  /** Посылаем событие о размещении корабля контроллеру
     * @param x координата x выстрела
     * @param y координата y выстрела 
     * @param ship тип корабля
     */
  @Override 
  public void sendShipPlacedEvent(final int x, final int y, final ShipType ship) 
  {
    GameEvent event = new PlayerOneShipPlacedEvent(x, y, ship);
    try 
    {
      eventQueue.put(event);
    } catch(InterruptedException e) 
    {
      System.out.println(e);
    }
  }

  /** Посылаем событие о начале новой игры контроллеру*/
  @Override 
  public void sendNewGameEvent() 
  {
    try 
    {
      eventQueue.put(new NewGameEvent());
    } 
    catch(InterruptedException e) 
    {
      System.out.println(e);
    }
  }

}

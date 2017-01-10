package battleships.controller.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import battleships.common.ShipType;
import battleships.common.events.ActionEvent;
import battleships.common.events.GameEvent;
import battleships.common.events.PlayerTwoShipPlacedEvent;
import battleships.common.events.PlayerTwoShotEvent;
import battleships.view.View;

/**
 * Инкапсулирует взаимодействие компонентов системы по Сети.
 * @author ALEX
 */
public class RemoteConnection implements Runnable, Connection 
{
  private final View localView;
  private final String ipAddress;
  private Socket socket;
  private ObjectOutputStream outputStream;
  private ObjectInputStream inputStream;

  public RemoteConnection(String ip, View view) 
  {
    localView = view;
    ipAddress = ip;
  }
  
  /** Главный метод класса - ожидание ActionEvent от серверной стороны
   *  и их выполнение
   *   
   */
  @Override
  public void run() 
  {
    try 
    {
      socket = new Socket(ipAddress, 56777);
      outputStream = new ObjectOutputStream(socket.getOutputStream());
      inputStream = new ObjectInputStream(socket.getInputStream());
    } 
    catch(IOException e) 
    {
      System.out.println(e);
    }  
    
    //Забираем от сервера ActionEvent
    ActionEvent event;
    while(true) {
      try 
      {
        event = (ActionEvent)inputStream.readObject();
        //И выполняем его
        if(event != null) event.execute(localView);
      } 
      catch(ClassNotFoundException | IOException e) 
      {
        System.out.println(e);
      }
    }
  }
  
  /** Закрытие соединений */
  @Override
  protected void finalize() {
    try 
    {
      inputStream.close();
      outputStream.close();
      socket.close();  
    } 
    catch(IOException e) 
    {
      System.out.println(e);
    }  
  }
  
  /** Посылаем событие о выстреле контроллеру
     * @param x координата x выстрела
     * @param y координата y выстрела 
     */
  @Override public void sendShotEvent(final int x, final int y) {
    GameEvent event = new PlayerTwoShotEvent(x, y);
    try 
    {
      outputStream.writeObject(event);
    } 
    catch(IOException e) 
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
  public void sendShipPlacedEvent(final int x, final int y, final ShipType ship) {
    GameEvent event = new PlayerTwoShipPlacedEvent(x, y, ship);
    try 
    {
      outputStream.writeObject(event);
    } 
    catch(IOException e) 
    {
      System.out.println(e);
    }
  }
  @Override public void sendActionEvent(final ActionEvent event) {}
  @Override public void sendNewGameEvent() {}
}

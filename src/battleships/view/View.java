package battleships.view;

import battleships.controller.connection.RemoteConnection;
import battleships.controller.connection.LocalConnection;
import battleships.controller.connection.Connection;
import javax.swing.SwingUtilities;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import battleships.common.DataPack;
import battleships.common.ShipType;
import battleships.common.events.GameEvent;
import battleships.controller.Controller;

/**
 * @author ALEX
 * Класс представления
 */
public class View 
{
  private BattleshipsFrame frame;
  Connection controllerConnection;

  /** Создание экземпляра View. */
  public View() {}
  
  /** Показывает главное окно приложения */
  public void showFrame() {
    SwingUtilities.invokeLater(() -> {
        frame = new BattleshipsFrame(View.this);
    });
  }
  
  /** Создание "Сервера" */
  public void createServer() {
    final View view = this;
    SwingUtilities.invokeLater(() -> {
        //Создание необходимых объектов и инициализация контроллера
        BlockingQueue<GameEvent> eventQueue = new LinkedBlockingQueue<>();
        controllerConnection = new LocalConnection(eventQueue, view);
        frame.changeStatus("Ждем другого игрока...");
        
        Controller controller = Controller.getInstance(eventQueue, (LocalConnection)controllerConnection);
        Thread controllerThread = new Thread(controller);
        controllerThread.start();
        frame.changeNewGameButtonAction();
    });
  }
  
  /** 
   * Соединение с сервером
   * @param ipAddress IP сервера
   */
  public void connectToServer(final String ipAddress) {
    final View view = this;
    SwingUtilities.invokeLater(() -> {
        controllerConnection = new RemoteConnection(ipAddress, view);
        Thread connectionThread = new Thread((Runnable)controllerConnection);
        connectionThread.start();
        frame.changeStatus("Соединение с " + ipAddress + "...");
        frame.changeNewGameButtonAction();
    });
  }
  
  /** Обновляет Представление
     * @param data статус игры*/
  public void refreshView(final DataPack data) 
  {
    SwingUtilities.invokeLater(() -> {
        frame.refreshBoards(data);
    });   
  }
  
  /** Проверка корректности введенного IP
     * @param ipAddress IP-адрес
     * @return true - IP введен корректно<br>
               false - IP содержит ошибки*/
  public final static boolean ipAddressIsValid(final String ipAddress) {
    String[] parts = ipAddress.split("\\.");
    if(parts.length != 4) return false;
    for(String s : parts) {
      int i = Integer.parseInt(s);
      if ((i < 0) || (i > 255)) return false;
    }
    return true;
  }
  
  /** Разместить корабль на поле
     * @param ship корабль*/
  public void placeShip(final ShipType ship) {
    SwingUtilities.invokeLater(() -> {
        frame.playerBoard.placeShip(ship);
    });   
  }
  
  /** Смена сообщения в статусной строке
     * @param status новый статус*/
  public void changeStatus(final String status) {
     SwingUtilities.invokeLater(() -> {
         frame.changeStatus(status);
     });   
  }
 
}

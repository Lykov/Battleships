package battleships.controller.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import battleships.common.events.ActionEvent;
import battleships.common.events.GameEvent;
import battleships.common.events.PlayerTwoConnectedEvent;
/**
 * Серверная сторона коммуникации двух игроков.
 * @author ALEX
 */
public class Server implements Runnable 
{
  private final BlockingQueue<GameEvent> eventQueue;
  private ServerSocket serverSocket;
  private Socket socket;
  private ObjectOutputStream outputStream;
  private ObjectInputStream inputStream;  
  private final LocalConnection localView;

  /** Server class constructor
     * @param queue очередь событий
     * @param viewConnection ссылка на локальный View */
  public Server(BlockingQueue<GameEvent> queue, LocalConnection viewConnection) 
  {
    eventQueue = queue;
    localView = viewConnection;
  }
  
  /** Главный метод сервера - получает события от второго игрока
   *  и помещает их в очерель */
  @Override
  public void run() 
  {
    try 
    {
      serverSocket = new ServerSocket(56777);
      socket = serverSocket.accept();  
      outputStream = new ObjectOutputStream(socket.getOutputStream());
      inputStream = new ObjectInputStream(socket.getInputStream());
      eventQueue.put(new PlayerTwoConnectedEvent());
    } 
    catch(IOException | InterruptedException e) 
    {
        System.out.println(e);
    }  
    
    //Извлечение события из очереди второго игрока и передача их в
    //свою локальную очередь событий
    GameEvent event;
    while(true) {
      try 
      {
        event = (GameEvent)inputStream.readObject();
        if(event != null) eventQueue.put(event);
      } catch(ClassNotFoundException | InterruptedException e) 
      {
          System.out.println(e);
      } 
      catch(IOException el) 
      {
        try 
        {
          inputStream.close();
          outputStream.close();
          socket.close();  
          serverSocket.close();
        } 
        catch(IOException e) 
        {
          System.out.println(e);
        }
      }
    }
  }
  
  /** Закрытие соединений */
  @Override
  protected void finalize() 
  {
    try {
      inputStream.close();
      outputStream.close();
      socket.close();  
      serverSocket.close();
    } catch(IOException e) {
      System.out.println(e);
    }  
  }
  
  /** Отправить событие первому игроку
     * @param event событие*/
  public void sendActionEventToPlayerOne(final ActionEvent event) {
    localView.sendActionEvent(event);
  }
  
  /** Отправить событие второму игроку
     * @param event событие*/
  public void sendActionEventToPlayerTwo(final ActionEvent event) {
    try 
    {
      outputStream.writeObject(event);
    } catch(IOException e) 
    {
        System.out.println(e);
    }
  }
  
}

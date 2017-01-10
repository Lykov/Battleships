package battleships.controller;

import battleships.controller.connection.Server;
import battleships.common.events.GameEvent;
import battleships.common.events.NewGameEvent;
import battleships.common.events.PlayerTwoShipPlacedEvent;
import battleships.common.events.PlayerTwoShotEvent;
import battleships.common.events.PlayerOneShipPlacedEvent;
import battleships.common.events.PlayerWonAction;
import battleships.common.events.PlayerOneShotEvent;
import battleships.common.events.OpponentTurnAction;
import battleships.common.events.RefreshViewAction;
import battleships.common.events.PlaceShipAction;
import battleships.common.events.PlayerTwoConnectedEvent;
import battleships.common.events.PlayerTurnAction;
import battleships.common.events.PlayerLostAction;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import battleships.common.Coordinates;
import battleships.model.Model;
import battleships.controller.connection.LocalConnection;

/**
 * Класс контроллера. Ответственен за обработку игровых событий и
 * принятия решения, что делать в определенных ситуациях.
 * @author ALEX
 */
public class Controller implements Runnable 
{
  
  private final BlockingQueue<GameEvent> eventQueue;
  private final Server server;
  private Model model;
  
  /** Словарь, связывающий игровые события с соответствующими действиями */
  private final Map<Class<? extends GameEvent>, GameAction> eventActionMap;
  
  /** Текущие состояния игры */
  private enum State {PLAYER_ONE_TURN, PLAYER_TWO_TURN};
  private State state;
  
  /** Реализация паттерна Одиночка (Singleton) */
  private static Controller instance = null;
  /**
     * @param queue очередь событий 
     *  @return ссылка на контроллер
     * @param viewConnection ссылка на View*/
  public static synchronized Controller getInstance(
      BlockingQueue<GameEvent> queue, LocalConnection viewConnection) 
  {
    if(instance == null) instance = new Controller(queue, viewConnection);
    return instance;
  }
  
  private Controller(BlockingQueue<GameEvent> queue, LocalConnection viewConnection) {
    eventQueue = queue;
    model = new Model();
    server = new Server(queue, viewConnection);
    Thread thread = new Thread((Runnable)server);
    thread.start();
    
    //Заполняем словарь с событиями
    eventActionMap = new HashMap<>();
    fillEventActionMap();
    state = null;
  }
  
  /** Главный метод контроллера - ответственнен за извлечение
   *  из очереди события и его обработку */
  @Override
  public void run() {
    while(true) {
      try {
        GameEvent event = eventQueue.take();
        GameAction gameAction = eventActionMap.get(event.getClass());
        gameAction.execute(event);
      } catch(Exception e) 
      {
        System.out.println(e);
        throw new RuntimeException(e);
      }
    }
  }
 
  /** Заполняет контейнер eventActionMap событиями */
  private void fillEventActionMap() 
  {
    
    //Обработка PlayerTwoConnectedEvent
    eventActionMap.put(PlayerTwoConnectedEvent.class, (GameAction) (GameEvent e) -> {
        server.sendActionEventToPlayerOne(new PlaceShipAction(model.getNextShipForPlayerOne()));
        server.sendActionEventToPlayerTwo(new PlaceShipAction(model.getNextShipForPlayerTwo()));
    });
    
    //Обработка PlayerOneShipPlacedEvent
    eventActionMap.put(PlayerOneShipPlacedEvent.class, (GameAction) (GameEvent e) -> {
        if(model.playerOnePlacedAllShips()) return;
        PlayerOneShipPlacedEvent event = (PlayerOneShipPlacedEvent) e;
        
        if(model.putAndCheckPlayerOneShip(new Coordinates(event.getX(), event.getY()), event.getShipType()))
            server.sendActionEventToPlayerOne(new RefreshViewAction(model.generatePlayerOneDataPack()));
        if(!model.playerOnePlacedAllShips())
            server.sendActionEventToPlayerOne(new PlaceShipAction(model.getNextShipForPlayerOne()));
        else
            server.sendActionEventToPlayerOne(new OpponentTurnAction());
        
        if(model.playerOnePlacedAllShips() && model.playerTwoPlacedAllShips()) {
            server.sendActionEventToPlayerTwo(new PlayerTurnAction());
            state = State.PLAYER_TWO_TURN;
        }
    });
    
    //Обработка PlayerTwoShipPlacedEvent
    eventActionMap.put(PlayerTwoShipPlacedEvent.class, (GameAction) (GameEvent e) -> {
        if(model.playerTwoPlacedAllShips()) return;
        PlayerTwoShipPlacedEvent event = (PlayerTwoShipPlacedEvent) e;
        
        if(model.putAndCheckPlayerTwoShip(new Coordinates(event.getX(), event.getY()), event.getShipType()))
            server.sendActionEventToPlayerTwo(new RefreshViewAction(model.generatePlayerTwoDataPack()));
        if(!model.playerTwoPlacedAllShips())
            server.sendActionEventToPlayerTwo(new PlaceShipAction(model.getNextShipForPlayerTwo()));
        else
            server.sendActionEventToPlayerTwo(new OpponentTurnAction());
        
        if(model.playerOnePlacedAllShips() && model.playerTwoPlacedAllShips()) {
            server.sendActionEventToPlayerOne(new PlayerTurnAction());
            state = State.PLAYER_ONE_TURN;
        }
    });
    
    //Обработка PlayerOneShotEvent
    eventActionMap.put(PlayerOneShotEvent.class, (GameAction) (GameEvent e) -> {
        if(state != State.PLAYER_ONE_TURN) return;
        PlayerOneShotEvent event = (PlayerOneShotEvent) e;
        
        boolean result = model.checkPlayerOneShot(new Coordinates(event.getX(), event.getY()));
        
        if(!result) {
            server.sendActionEventToPlayerOne(new OpponentTurnAction());
            server.sendActionEventToPlayerTwo(new PlayerTurnAction());
            state = State.PLAYER_TWO_TURN;
        }
          
        server.sendActionEventToPlayerOne(new RefreshViewAction(model.generatePlayerOneDataPack()));
        server.sendActionEventToPlayerTwo(new RefreshViewAction(model.generatePlayerTwoDataPack()));
        
        if(model.playerOneWon()) {
            server.sendActionEventToPlayerOne(new PlayerWonAction());
            server.sendActionEventToPlayerTwo(new PlayerLostAction());
            state = null;
        }
    });
    
    //Обработка PlayerTwoShotEvent
    eventActionMap.put(PlayerTwoShotEvent.class, (GameAction) (GameEvent e) -> {
        if(state != State.PLAYER_TWO_TURN) return;
        PlayerTwoShotEvent event = (PlayerTwoShotEvent) e;
        
        boolean result = model.checkPlayerTwoShot(new Coordinates(event.getX(), event.getY()));
        
        if(!result) {
            server.sendActionEventToPlayerOne(new PlayerTurnAction());
            server.sendActionEventToPlayerTwo(new OpponentTurnAction());
            state = State.PLAYER_ONE_TURN;
        }
        
        server.sendActionEventToPlayerOne(new RefreshViewAction(model.generatePlayerOneDataPack()));
        server.sendActionEventToPlayerTwo(new RefreshViewAction(model.generatePlayerTwoDataPack()));
        
        if(model.playerTwoWon()) {
            server.sendActionEventToPlayerOne(new PlayerLostAction());
            server.sendActionEventToPlayerTwo(new PlayerWonAction());
            state = null;
        }
    });
  
    //Обработка PlayerTwoShotEvent
    eventActionMap.put(NewGameEvent.class, (GameAction) (GameEvent e) -> {
        model = new Model();
        server.sendActionEventToPlayerOne(new RefreshViewAction(model.generatePlayerOneDataPack()));
        server.sendActionEventToPlayerTwo(new RefreshViewAction(model.generatePlayerTwoDataPack()));
        server.sendActionEventToPlayerOne(new PlaceShipAction(model.getNextShipForPlayerOne()));
        server.sendActionEventToPlayerTwo(new PlaceShipAction(model.getNextShipForPlayerTwo()));
    });
  }
}

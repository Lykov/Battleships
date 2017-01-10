package battleships.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import battleships.common.Coordinates;
import battleships.common.DataPack;

/**
 * @author ALEX
 * Класс главного окна игры.
 */
public class BattleshipsFrame extends JFrame {
  private static final long serialVersionUID = 1L;
  private final Board opponentBoard;
  private final JButton startNewGameButton;
  private final JLabel statusLabel;
  final Board playerBoard;
  private final View view;

  /** Конструктор класса. 
   *  @param view Ссылка на View
   *  @see battleships.view.View
   */
  public BattleshipsFrame(View view) {
    super("Battleships");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    this.view = view;
    
    //Установка менеджера компоновки
    setFocusable(true);
    setLayout(new BorderLayout());
    JPanel bottom = new JPanel();
    bottom.setLayout(new BorderLayout());
    add(BorderLayout.SOUTH, bottom);
    JPanel boards = new JPanel();       
    boards.setLayout(new GridLayout(1, 2));
    add(BorderLayout.CENTER, boards);
    
    //Создание м отображение игровых полей
    playerBoard = new Board();
    opponentBoard = new Board();
    boards.add(playerBoard);
    boards.add(opponentBoard);
    
    //Добавления слушателей события от мыши
    PlayerBoardListener mouseListener = new PlayerBoardListener();
    playerBoard.addMouseListener(mouseListener);
    playerBoard.addMouseMotionListener(mouseListener);
    opponentBoard.addMouseListener(new OpponentBoardListener());
    
    //Отображение статуса игры и кнопки "Новая игра"
    statusLabel = new JLabel("Нажмите кнопку 'Новая игра'");
    bottom.add(BorderLayout.LINE_START, statusLabel);
    startNewGameButton = new JButton("Новая игра");
    bottom.add(BorderLayout.LINE_END, startNewGameButton);
    startNewGameButton.addActionListener((ActionEvent e) -> 
    {
        createNewGameDialog();
    });
    
    setResizable(false);
    setSize(812, 455);
    setVisible(true);
    setLocationRelativeTo(null);
  }
  
  /** Создание и отбражение диалогового окна "Новая игра" */
  private void createNewGameDialog() 
  {
    //Создание и настройка диалогового окна
    final JDialog dialog = new JDialog(BattleshipsFrame.this, "Новая игра", true);
    dialog.setLayout(new GridLayout(4,1));
    dialog.setSize(400, 150);
    dialog.setLocationRelativeTo(this);
    
    //Создание и настройка переключателей
    final JRadioButton hostButton = new JRadioButton("Я сервер");
    final JRadioButton joinButton = new JRadioButton("Присоединиться к другому серверу");
    hostButton.setSelected(true);
    final ButtonGroup bg = new ButtonGroup();
    bg.add(hostButton);
    bg.add(joinButton);
    dialog.add(hostButton);
    dialog.add(joinButton);
    
    //Панель с IP сервера
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(1,2));
    final JLabel ipLabel = new JLabel("IP сервера");
    final JTextComponent ipTextField = new JTextField("127.0.0.1");
    ipLabel.setEnabled(false);
    ipTextField.setEnabled(false);
    panel.add(ipLabel);
    panel.add(ipTextField);            
    dialog.add(panel);
    
    //Кнопка OK
    panel = new JPanel();
    final JButton okButton = new JButton("OK");
    panel.add(okButton);
    dialog.add(panel);
    okButton.addActionListener((ActionEvent e) -> {
        if(hostButton.isSelected()) 
        {
            view.createServer();
            setTitle("Battleships (Сервер)");
            dialog.dispose();
        } 
        else 
        {
            if(View.ipAddressIsValid(ipTextField.getText())) 
            {
                view.connectToServer(ipTextField.getText());
                dialog.dispose();
            }
        }
    });
    
    //Скрываем/показываем поле с ip в зависомости от переключателя
    hostButton.addActionListener((ActionEvent e) -> 
    {
        ipLabel.setEnabled(false);
        ipTextField.setEnabled(false);
    });
    joinButton.addActionListener((ActionEvent e) -> 
    {
        ipLabel.setEnabled(true);
        ipTextField.setEnabled(true);
    });
   
    dialog.setVisible(true);
  }

  /** Смена текста в statusLabel */
  public void changeStatus(final String newStatus) {
    statusLabel.setText(newStatus);
  }
  
  /** Обновление игровых полей */
  public void refreshBoards(final DataPack data) 
  {
    playerBoard.refreshBoard(data.playerShips, data.opponentShots);
    opponentBoard.refreshBoard(data.opponentShips, data.playerShots);
  }
  
  /** Измняет действие кнопки "Новая игра" после того, как партия закончена */
  public void changeNewGameButtonAction() 
  {
    ActionListener[] listeners = startNewGameButton.getActionListeners();
    startNewGameButton.removeActionListener(listeners[0]);
    startNewGameButton.addActionListener((ActionEvent e) -> 
    {
        view.controllerConnection.sendNewGameEvent();
    });
  }
  
  /** Слушатели событий от мыши на игровом поле игрока */
  public class PlayerBoardListener extends MouseAdapter 
  {
    @Override public void mousePressed(MouseEvent event) 
    {
      try 
      {
        //Размещение корабля
        if(event.getButton() == MouseEvent.BUTTON1 && view.controllerConnection != null)
          view.controllerConnection.sendShipPlacedEvent(event.getX()/40, event.getY()/40,
              playerBoard.getCurrentlyPlacedShip());
        //Поворот корабля
        if(event.getButton() == MouseEvent.BUTTON3) {
          playerBoard.placeShip(playerBoard.getCurrentlyPlacedShip().returnRotatedShip());
          playerBoard.repaint();
        }
      } 
      catch(Exception e) 
      {
          System.out.println(e);
      }
    }
    //Перемещение корабля
    @Override public void mouseMoved(MouseEvent event) 
    {
      if(playerBoard.getCurrentlyPlacedShip() != null) 
      {
        try
        {
            playerBoard.setMousePosition(new Coordinates(event.getX()/40, event.getY()/40));
            playerBoard.repaint();
        }
        catch (IllegalArgumentException e) {}
      }
    }   
  }
  
  /** Слушатели событий от мыши на игровом поле противника */
  private class OpponentBoardListener extends MouseAdapter 
  {
    //Выстрел
    @Override public void mousePressed(MouseEvent event) 
    {
      try 
      {
        if(event.getButton() == MouseEvent.BUTTON1 && view.controllerConnection != null)
          view.controllerConnection.sendShotEvent(event.getX()/40, event.getY()/40);
      } catch(Exception e) 
      {
          System.out.println(e);
      }
    }   
  }
     
}
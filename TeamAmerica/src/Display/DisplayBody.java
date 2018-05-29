package Display;

import Database.Write_Read;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Morris
 */
public class DisplayBody extends JPanel{
    private final int panelHeight;
    private final int panelWidth;
    
    private Timer timer;
    
    private JPanel centralPanel;
    private JButton goButton;
    private TextField text_pseudo;
    private TimerTask timerTask;
    private JComboBox box_nations;
    private DefaultTableModel model;
    private JLabel label;
    public static final int GO_BUTTON_HEIGHT = 100;
    public static final int GO_BUTTON_WIDTH = 400;
    public static final int TEXT_FIELD_HEIGHT = 20;
    public static final int TEXT_FIELD_WIDTH = 200;
    public static final int GIF_HEIGHT = 300;
    public static final int GIF_WIDTH = 300;
    
    private Write_Read database;
    private Paint draw;
    
    
    public DisplayBody(int screenWidth, int screenHeight) {
        //Set the height, the width, the position of this panel
        panelHeight = (int) screenHeight*9/10;
        panelWidth = screenWidth;
        this.setSize(panelWidth,panelHeight);
        this.setLocation(0,(int) panelHeight/10);
        this.setLayout(null);
        
        centralPanel = new JPanel();
        centralPanel.setSize(panelWidth,(int) panelHeight*8/10);
        centralPanel.setLocation(0,0);
        centralPanel.setLayout(null);
        add(centralPanel);
        
        database = new Write_Read();
                
        addButton(); 
    }
    
    public void addButton() {
        //Add multifunctionnale button on this panel
        goButton = new JButton();
        goButton.setText("Play!");
        goButton.setLocation((int)(panelWidth/2-GO_BUTTON_WIDTH/2),(int)panelHeight*10/12);
        goButton.setSize(GO_BUTTON_WIDTH,GO_BUTTON_HEIGHT);
        //goButton.setBounds((int)(screenWidth/2-GO_BUTTON_WIDTH/2),(int)screenHeight*10/12,GO_BUTTON_WIDTH,GO_BUTTON_HEIGHT);
        goButton.setFont(new Font("Arial", Font.PLAIN, 30));
        goButton.setFocusable(false);
        this.add(goButton);
        goButton.addActionListener(jButtonActionListener);
    }
    
    ActionListener jButtonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if(button.getText() == "Play!") {
                signUp();
            } else if(button.getText() == "Connect To The Server!") {
                try {
                    database.createMainPlayer(text_pseudo.getText(), (String) box_nations.getSelectedItem());
                    waitingRoom();
                } catch(SQLException exception) {
                    if(exception.getErrorCode() == 1062) {
                        //SQLIntegrityConstraintViolationException: Duplicata du champ pour la clef 'PRIMARY'
                        JOptionPane.showMessageDialog(null,"Please choose another pseudo","Warning",JOptionPane.WARNING_MESSAGE);
                    } else {
                        //unknown exception
                        exception.printStackTrace();
                    }
                }
            } else if(button.getText() == "Ready!") {                
                waitGameBegins();
            }
        }
    };
    
    ActionListener comboBoxActionListener = new ActionListener() {
        // ActionListener for combo box. Display gif according to the selected item in the combo box
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedNation = (String) box_nations.getSelectedItem();//get the selected item
            ImageIcon icon = new ImageIcon(getClass().getResource("/gif/"+selectedNation+".gif"));
            label.setIcon(icon);                
        }
    };
    public void signUp() {
        //Create input fields in order to sign in as a player
        JLabel label_pseudo = new JLabel("Pseudo");
        JLabel label_nation = new JLabel("Nation");
        text_pseudo = new TextField(20);
        box_nations  = new JComboBox(new String[]{ "America", "Terrorist" });
        box_nations.addActionListener(comboBoxActionListener);
        centralPanel.add(label_pseudo);
        centralPanel.add(text_pseudo);
        centralPanel.add(label_nation);
        centralPanel.add(box_nations);

        int labelPositionX = (int) (panelWidth-TEXT_FIELD_WIDTH)/2;
        label_pseudo.setBounds(labelPositionX,(int)panelHeight*3/24,TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT);
        text_pseudo.setBounds(labelPositionX,(int)panelHeight*4/24,TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT);
        label_nation.setBounds(labelPositionX,(int)panelHeight*6/24,TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT);
        box_nations.setBounds(labelPositionX,(int)panelHeight*7/24,TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT);
        
        // add label for gif
        label = new JLabel();
        label.setBounds((int)(panelWidth-GIF_WIDTH)/2,(int)panelHeight*8/24,GIF_WIDTH,GIF_HEIGHT);
        centralPanel.add(label);
        
        box_nations.setSelectedIndex(0);

        goButton.setText("Connect To The Server!");
}
    public void waitingRoom() {
        // Create a waiting room before launching a party        
        centralPanel.removeAll();
        model = new DefaultTableModel();
        model.addColumn("pseudo");
        model.addColumn("nation");
        JTable waitingPlayersTable = new JTable(model);
        waitingPlayersTable.editCellAt​(0,0);
        JScrollPane waitingPlayersScrollPane = new JScrollPane(waitingPlayersTable);
        waitingPlayersScrollPane.setBounds((int) (panelWidth-TEXT_FIELD_WIDTH*2)/2,(int) panelHeight*3/24,TEXT_FIELD_WIDTH*2, (int) panelHeight/2);
        centralPanel.add(waitingPlayersScrollPane);

        timer = new Timer();
        timerTask = new TimerTask() {
                @Override
                public void run() {
                    database.getWaitingPlayers(model);
                }
            };
        timer.scheduleAtFixedRate(timerTask,0, 5*1000);
        goButton.setText("Ready!");
        }
    
    public void waitGameBegins() {
        goButton.removeActionListener(jButtonActionListener);//remove the action listener on the button
        goButton.setText("Wait for opponent!");
        setCursor(new Cursor(Cursor.WAIT_CURSOR));//Set the cursor on waiting image

        int numberOfPlayers = 2;
        timerTask.cancel();//Kill the old timer
        timerTask = new TimerTask() {
            boolean firstLoop = true;
            @Override
            public void run() {
                
                database.enoughPlayers(model, numberOfPlayers);
                if(model.getRowCount() == numberOfPlayers || database.isMainPayerStatusChanged()) {
                    timerTask.cancel();
                    database.initializePlayers(model);//initialize the players variable in Write_Read
                    database.modifyPlayersStatus();//modify all players' status to true
                    database.createNewMap();                                                       
                    if(firstLoop) {
                        //This joueur is the last one to enter the waiting room,
                        //so he creates the a new party for everyone
                        database.getMap().nouvelleMap();//clear the database and send the actual one to the database
                    } else {
                        //This joueur is not the last one to enter the waiting room,
                        //so he will download the map from the database
                        //Update the player position
                        database.getMap().miseAJourMap();
                    }                    
                    display();
                } else {
                    firstLoop = false;   
                }                    
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,1000);
    }
    
    public void display() {
        remove(goButton);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));//Set the default cursor   
        this.removeAll();
        draw=new Paint(database, timer);
        draw.setBorder(BorderFactory.createLineBorder(Color.black));
        draw.setSize(draw.getPanelWidth(), draw.getPanelHeight());
        draw.setLocation((int)(panelWidth-draw.getPanelWidth())/2, (int)(panelHeight-draw.getPanelHeight())/2);
        this.add(draw);
    }
    public Paint getDraw() {
        return draw;
    }

    public Timer getTimer() {
        return timer;
    }
    
    public void close() {
        try {
            this.timer.cancel();//cancel the timer
            this.database.getMainPlayer().deleteJoueur();//delete entity joueur in the database
            this.database.getConnection().close();//close the connection
        } catch (SQLException ex) {
            Logger.getLogger(DisplayBody.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Write_Read getDatabase() {
        return database;
    }

    public int getPanelHeight() {
        return panelHeight;
    }

    public int getPanelWidth() {
        return panelWidth;
    }
}
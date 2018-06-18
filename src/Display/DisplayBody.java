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
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import teamamerica.WelcomeWindow;

/**
 *
 * @author Morris
 */
public class DisplayBody extends JPanel{
    private final WelcomeWindow frame;
    private final int panelHeight;
    private final int panelWidth;
    
    private Timer timer;
    
    private JPanel centralPanel;
    private JButton goButton;
    private TextField text_pseudo;
    private TimerTask timerTask;
    private JComboBox boxNations;
    private JComboBox boxnumberOfPlayers;
    private DefaultTableModel model;
    private JLabel label = new JLabel();
    public static final int GO_BUTTON_HEIGHT = 100;
    public static final int GO_BUTTON_WIDTH = 400;
    public static final int TEXT_FIELD_HEIGHT = 20;
    public static final int TEXT_FIELD_WIDTH = 200;
    
    private int blockLength = 50;
    private int blockXNumber = 20;
    private int blockYNumber = 13;
    
    private Write_Read database;
    private Paint draw;
    
    private JLabel label_end = new JLabel();
    
    public DisplayBody(WelcomeWindow frame, int screenWidth, int screenHeight) {
        this.frame = frame;        
        this.panelHeight = screenHeight;//Set the height of this panel
        this.panelWidth = screenWidth;//Set the width of this panel        
        this.setLayout(null);
        
        centralPanel = new JPanel();
        centralPanel.setSize(panelWidth,panelHeight);
        centralPanel.setLocation(0,0);
        centralPanel.setLayout(null);
        add(centralPanel);
        
        database = new Write_Read(blockLength, blockXNumber, blockYNumber);
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/Images/Team America.jpg"));
        label.setBounds((int)(panelWidth-icon.getIconWidth())/2,(int)(panelHeight-icon.getIconHeight())/2,icon.getIconWidth(),icon.getIconHeight());
        label.setIcon(icon);
        centralPanel.add(label);
        
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
            if("Play!".equals(button.getText())) {
                signUp();
            } else if("Connect To The Server!".equals(button.getText())) {
                try {
                    database.createMainPlayer(text_pseudo.getText(), (String) boxNations.getSelectedItem());
                    centralPanel.removeAll();
                    centralPanel.repaint();
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
            } else if("Ready!".equals(button.getText())) {            
                waitGameBegins();
            }
        }
    };
    
    ActionListener comboBoxActionListener = new ActionListener() {
        // ActionListener for combo box. Display gif according to the selected item in the combo box
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedNation = (String) boxNations.getSelectedItem();//get the selected item
            ImageIcon icon = new ImageIcon(getClass().getResource("/gif/"+selectedNation+".gif"));
            label.setIcon(icon);
            int gifWidth = icon.getIconWidth();
            int gitHeight = icon.getIconHeight();
            label.setBounds((int)(panelWidth-gifWidth)/2,(int)panelHeight*8/24,gifWidth,gitHeight);
            frame.getSoundEffect().play("player "+selectedNation);
        }
    };
    public void signUp() {
        int MARGIN = 33,
            SECTION_GAP = 43;
        //Create input fields in order to sign in as a player
        JLabel label_pseudo = new JLabel("Pseudo");
        JLabel label_nation = new JLabel("Nation");
        JLabel label_numberOfPlayers = new JLabel("Number of Players");
        text_pseudo = new TextField(20);
        boxNations  = new JComboBox(new String[]{ "America", "Terrorist" });
        boxNations.addActionListener(comboBoxActionListener);
        boxNations.setSelectedIndex(0);
        boxnumberOfPlayers  = new JComboBox(new Integer [] {1,2,3,4});
        boxnumberOfPlayers.setSelectedIndex(1);
        centralPanel.add(label_pseudo);
        centralPanel.add(text_pseudo);
        centralPanel.add(label_nation);
        centralPanel.add(boxnumberOfPlayers);
        centralPanel.add(label_numberOfPlayers);
        centralPanel.add(boxNations);

        int labelPositionX = (int) (panelWidth-TEXT_FIELD_WIDTH)/2;
        label_pseudo.setBounds(labelPositionX,MARGIN,TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT);
        text_pseudo.setBounds(labelPositionX,MARGIN+TEXT_FIELD_HEIGHT,TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT);
        label_nation.setBounds(labelPositionX,MARGIN+TEXT_FIELD_HEIGHT+SECTION_GAP,TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT);
        boxNations.setBounds(labelPositionX,MARGIN+2*TEXT_FIELD_HEIGHT+SECTION_GAP,TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT);        
        label_numberOfPlayers.setBounds(labelPositionX,MARGIN+2*TEXT_FIELD_HEIGHT+2*SECTION_GAP,TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT);
        boxnumberOfPlayers.setBounds(labelPositionX,MARGIN+3*TEXT_FIELD_HEIGHT+2*SECTION_GAP,TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT);
        
        centralPanel.add(label);// add label for gif

        goButton.setText("Connect To The Server!");
}
    public void waitingRoom() {
        // Create a waiting room before launching a party   
        frame.getSoundEffect().stopSoundEffect();
        model = new DefaultTableModel();
        model.addColumn("pseudo");
        model.addColumn("nation");
        model.addColumn("pv");
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

        timerTask.cancel();//Kill the old timer
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(database.enoughPlayers(timer,model, (int)boxnumberOfPlayers.getSelectedItem())) {
                    timerTask.cancel();
                    display();
                }      
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,1000);
    }
    
    public void display() {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));//Set the default cursor   
        this.removeAll();
        this.revalidate();
        this.repaint();
        
        draw=new Paint(frame, database, timer, blockLength);
        frame.getDisplayHeader().addHPbar(database.getPlayers());
        draw.setBorder(BorderFactory.createLineBorder(Color.black));
        int mapHeight = blockYNumber*blockLength;
        int mapWidth = blockXNumber*blockLength;
        draw.setSize(mapWidth, mapHeight);
        draw.setLocation((int)(panelWidth-mapWidth)/2, Math.max((int)(panelHeight-mapHeight)/2,0));
        this.add(draw, -1);
        CountdownDisplay countdownDisplay = new CountdownDisplay(frame,4);
        countdownDisplay.setBounds((int)panelWidth/2-150, 100, panelWidth, panelHeight);
        this.add(countdownDisplay, 0);//adds countdown timer
    }
    
        public void display_end_game(){
        this.removeAll();
        this.revalidate();
        this.repaint();
        ImageIcon icon2 = new ImageIcon(getClass().getResource("/gif/Explosion_mort_final.gif"));
        label_end = new JLabel(icon2);
        int labelWidth = icon2.getIconWidth(),
            labelHeight = icon2.getIconHeight();
        label_end.setBounds((int)(panelWidth-labelWidth)/2,(int)(panelHeight-labelHeight)/2,labelWidth,labelHeight);
        this.add(label_end);
        frame.getSoundEffect().play("explosion perso pour gif");
    }

    public Write_Read getDatabase() {
        return database;
    }
    
    public void close() {
        try {
            this.timer.cancel();//cancel the timer
            this.database.getPlayers().get(0).deleteJoueur();//delete entity joueur in the database
            this.database.getConnection().close();//close the connection
        } catch (SQLException ex) {
            Logger.getLogger(DisplayBody.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
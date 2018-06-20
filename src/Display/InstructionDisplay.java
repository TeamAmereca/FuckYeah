package Display;

import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Morris
 */
public class InstructionDisplay extends JPanel{
    public InstructionDisplay(int screenLength, int screenHeight){
        JLabel labelInstruction = new JLabel("INSTRUCTIONS");
        labelInstruction.setFont(new Font("Arial Bold", Font.ITALIC, 40));
        int x = labelInstruction.getHeight();System.out.println(x);
        labelInstruction.setBounds(0,0, 5000, 1000);
        add(labelInstruction);
        setLayout(null);
        setVisible(true);
    }
    
     public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setBounds(0, 0, 1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        InstructionDisplay ss = new InstructionDisplay(100,100);
        ss.setBounds(0, 0, 1000, 600);
        frame.add(ss);
    }
}

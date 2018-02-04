import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
/**
 * @author Tobias Ziegelmayer
 * @version 1.0.0
 * This class contains the gui for the project TextSummarization
 */
public class GUIForSum implements ActionListener{
    JTextArea textInput, textOutput;
    JTextField headline;
    JButton start;
    JLabel title;


    GUIForSum() {
        JFrame f= new JFrame("Automatische Textzusammenfassung");

        title=new JLabel("Automatische Textzusammenfassung");
        title.setBounds(170,10, 300,30);

        headline=new JTextField("Überschrift");
        headline.setBounds(10,50,300,20);

        textInput=new JTextArea("Text");
        textInput.setBounds(10,90,300,260);
        textInput.setWrapStyleWord(true);
        textInput.setLineWrap(true);

        textOutput=new JTextArea("Hier steht gleich ihre Zusammenfassung.");
        textOutput.setBounds(330,50,300,300);
        textOutput.setEditable(false);
        textOutput.setWrapStyleWord(true);
        textOutput.setLineWrap(true);

        start =new JButton("Starten");
        start.setBounds(260,400,120,30);
        start.addActionListener(this);

        f.add(title);
        f.add(headline);
        f.add(textInput);
        f.add(textOutput);
        f.add(start);
        f.setSize(640,480);
        f.setLayout(null);
        f.setVisible(true);
    }


    public void actionPerformed(ActionEvent e){
        String head=headline.getText();
        String text=textInput.getText();
        String summery = "";
        if (head == "Überschrift" || head.length() == 0
                || text == "Text" || text.length() == 0){
            summery = "Bitte versuchen Sie es erneut.";
        }
        else{
            Classifier classifier = new Classifier();

            try {
                summery = classifier.summarize(text, head);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        textOutput.setText(summery);
    }


    public static void main(String[] args) {
        new GUIForSum();
    }
}
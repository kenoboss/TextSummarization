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
    JScrollPane scrollPaneInput, scrollPaneOutput;


    GUIForSum() {
        JFrame f= new JFrame("Automatische Textzusammenfassung");

        title=new JLabel("Automatische Textzusammenfassung");
        title.setBounds(170,10, 300,30);

        headline=new JTextField("Überschrift");
        headline.setBounds(10,50,300,20);

        textInput=new JTextArea("Text");
        textInput.setWrapStyleWord(true);
        textInput.setLineWrap(true);
        scrollPaneInput = new JScrollPane(textInput);
        scrollPaneInput.setBounds(10,90,300,260);
        scrollPaneInput.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        textOutput=new JTextArea("Hier steht gleich ihre Zusammenfassung.");
        textOutput.setEditable(false);
        textOutput.setWrapStyleWord(true);
        textOutput.setLineWrap(true);
        scrollPaneOutput = new JScrollPane(textOutput);
        scrollPaneOutput.setBounds(330,50,300,300);
        scrollPaneOutput.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        start =new JButton("Starten");
        start.setBounds(260,400,120,30);
        start.addActionListener(this);

        f.add(title);
        f.add(headline);
        f.add(scrollPaneInput);
        f.add(scrollPaneOutput);
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
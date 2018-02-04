import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class GUIForSum implements ActionListener{
    JTextArea textInput, textOutput;
    JTextField headline;
    JButton start;
    JLabel title,l2;


    GUIForSum() {
        JFrame f= new JFrame("Automatic Text Summarizer");

        title=new JLabel("Automatic Text Summarizer");
        title.setBounds(220,10, 200,30);
        title.setToolTipText("This program is the result of a project from students of university Trier");

        headline=new JTextField("Überschrift");
        headline.setBounds(10,50,300,20);

        textInput=new JTextArea("Text");
        textInput.setBounds(10,90,300,260);

        textOutput=new JTextArea("Hier steht gleich ihre Zusammenfassung.");
        textOutput.setBounds(330,50,300,300);

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
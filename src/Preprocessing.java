import java.util.ArrayList;
import java.util.List;

public class Preprocessing {

    //text zu Liste -> Listenelemente sind Sätze
    public List<String> textAsList (String textAsString) {
        List <String> textList = new ArrayList<>();
        /*TO DO: Split texts to Sentences, save Sentences in Arraylist!!!
        Warten auf Commit von Tobias*/
        return textList;
    }

    //relative Position des Satzes im Text
    public float positionInTextRel (String sentence, List<String> text) {
        return text.indexOf(sentence)/countSentences(text);
    }

    //Satzlänge
    public float countSentences (List<String> text) {
        return text.size();
    }

    //Anzahl Wörter in Satz
    public int countWords (String sentence) {
        String [] count = sentence.split(" ");
        return  count.length;
    }

    //Prüfung ob erster Satz
    public boolean isFirst (String sentence, List<String> text) {
        if (text.indexOf(sentence) == 0) {
            return true;
        } else {
            return false;
        }
    }


}

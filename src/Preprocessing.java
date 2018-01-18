import java.util.ArrayList;
import java.util.List;

public class Preprocessing {

    //relative Position des Satzes im Text
    public double positionInTextRel (List<String> sentence, List<List<String>> text) {
        return text.indexOf(sentence)/text.size();
    }

    //Anzahl Wörter in Satz
    public double countWords (List<String> sentence) {
        return  sentence.size();
    }

    //Prüfung ob erster Satz
    public double isFirst (List<String> sentence, List<List<String>> text) {
        if (text.indexOf(sentence) == 0) {
            return 1;
        } else {
            return 0;
        }
    }
}

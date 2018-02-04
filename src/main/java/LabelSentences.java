import scala.Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LabelSentences {

    Entry entry;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public LabelSentences(Entry entry){
        this.setEntry(entry);
    }

    public List<Double> getDistances (){
        Entry entry = this.getEntry();
        List<Double> result = new ArrayList<>();
        WordFrequencies wordFrequencies = new WordFrequencies();
        HashMap<String, Integer> lemmataSummary = wordFrequencies.createFrequencyList(entry.getSummaryLemmata());
        List<List<String>> sentences = entry.getTextLemmata();
        for (List<String> sentence : sentences){
            List<List<String>> tmp = new ArrayList<>();
            tmp.add(sentence);
            HashMap<String, Integer> lemmataSentence = wordFrequencies.createFrequencyList(tmp);
            EuclideanDistance euclideanDistance = new EuclideanDistance(lemmataSummary, lemmataSentence);
            result.add(euclideanDistance.getDistance());
        }

        return result;
    }

    public double getMean(List<Double> distances){
        double sum = 0;
        for (Double aDouble : distances){
            sum += aDouble;
        }
        double avg = sum / distances.size();
        return avg;
    }


    public List<Integer> getLabels (List<Double> distances, double mean){
        List<Integer> result = new ArrayList<>();
        for (Double aDouble : distances){
            if (aDouble <= mean){
                result.add(1);
            }
            else{
                result.add(0);
            }
        }
        return result;
    }



    public static void main(String[] args) {

    }
}

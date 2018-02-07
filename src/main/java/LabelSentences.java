import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Tobias Ziegelmayer
 * @version 1.0.0
 * This class contains functions for calculating the label of sentences for the project TextSummarization
 */
public class LabelSentences {

    Entry entry;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    /**
     * This constructor creates a instance of LabelSentences with an entry as class variable
     * @param entry
     */
    public LabelSentences(Entry entry){
        this.setEntry(entry);
    }

    /**
     * This method creates a list of double values as euclidean distance for each sentence of a text based on
     * the existing summary from the origin corpus.
     * @return List<Double>
     */
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


    /**
     * This method takes as parameter a list of double values and returns
     * the mean of the double values
     * @param distances
     * @return double
     */
    public double getMean(List<Double> distances){
        double sum = 0;
        for (Double aDouble : distances){
            sum += aDouble;
        }
        double avg = sum / distances.size();
        return avg;
    }

    /**
     * This method takes a list of doubles and a single double value as parameter
     * and creates a list of integer values.
     * The integer values are the labels for the sentences of an entry text.
     * If the integer is equal 1, then the sentence will be part of the summary,
     * else if the integer is equal 0, then the sentence will be no part of the summary.
     * @param distances
     * @param mean
     * @return List<Integer>
     */
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
}

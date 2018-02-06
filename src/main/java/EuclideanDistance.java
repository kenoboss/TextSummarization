import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Tobias Ziegelmayer
 * @version 1.0.0
 * This class contains functions for calculating the EuclideanDistance for the project TextSummarization
 */
public class EuclideanDistance {


    public HashMap<String, Integer> getSummary() {
        return summary;
    }

    public void setSummary(HashMap<String, Integer> summary) {
        this.summary = summary;
    }

    public HashMap<String, Integer> getCreatedSummary() {
        return createdSummary;
    }

    public void setCreatedSummary(HashMap<String, Integer> createdSummary) {
        this.createdSummary = createdSummary;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    HashMap<String, Integer> summary;
    HashMap<String, Integer> createdSummary;
    double distance;


    /**
     * This constructor takes two hashmaps as parameter. A hashmap represents a frequency list of
     * content words from a summary. The hashmaps will be maped as a 2-dimensional integer array.
     * From the array the euclidean distance will be calculated.
     * @param summary
     * @param createdSummary
     */
    public EuclideanDistance(HashMap<String, Integer> summary, HashMap<String, Integer> createdSummary){
        this.setCreatedSummary(createdSummary);
        this.setSummary(summary);
        int [] [] inputLists = this.createInputLists(this.getSummary(), this.getCreatedSummary());
        this.setDistance(this.distance(inputLists[0], inputLists[1]));
    }

    /**
     * This method takes two hashmaps as parameter. The method generates a commom list of all
     * content words.
     * @param summary
     * @param createdSummary
     * @return List<String>
     */
    private static List<String> getEntrySet (HashMap<String, Integer> summary, HashMap<String, Integer> createdSummary){
        List<String> entrySet = new ArrayList<>();
        for (String lemma : summary.keySet()){
            if (entrySet.contains(lemma) == false){
                entrySet.add(lemma);
            }
        }

        for (String lemma : createdSummary.keySet()){
            if (entrySet.contains(lemma) == false){
                entrySet.add(lemma);
            }
        }
        return entrySet;
    }

    /**
     * This method takes two hashmaps as parameter. The method generates for each hashmap
     * an int array which only contains the frequency of the content words.
     * The arrays have the same length and the index of each entry represents the same
     * content word.
     * @param summary
     * @param createdSummary
     * @return int [][]
     */
    private int [][] createInputLists (HashMap<String, Integer> summary, HashMap<String, Integer> createdSummary){
        List<String> entrySet = getEntrySet(summary, createdSummary);
        int [] [] result = new int[2][entrySet.size()];
        int counter = 0;
        for (String entry : entrySet){
            if (summary.keySet().contains(entry)){
                result[0][counter] = summary.get(entry);
            }
            else{
                result[0][counter] = 0;
            }
            if (createdSummary.keySet().contains(entry)){
                result[1][counter] = createdSummary.get(entry);
            }
            else{
                result[1][counter] = 0;
            }
            counter++;
        }
        return result;
    }

    /**
     * This method takes two integer arrays as parameter and
     * calculates the euclidean distance between them.
     * @param a
     * @param b
     * @return double
     */
    private double distance(int[] a, int[] b) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return Math.sqrt(diff_square_sum);
    }




}



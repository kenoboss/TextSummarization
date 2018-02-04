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



    public EuclideanDistance(HashMap<String, Integer> summary, HashMap<String, Integer> createdSummary){
        this.setCreatedSummary(createdSummary);
        this.setSummary(summary);
        int [] [] inputLists = this.createInputLists(this.getSummary(), this.getCreatedSummary());
        this.setDistance(this.distance(inputLists[0], inputLists[1]));
    }

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

    private double distance(int[] a, int[] b) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return Math.sqrt(diff_square_sum);
    }




}



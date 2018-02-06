/**
 * @author Tobias Ziegelmayer
 * @version 1.0.0
 * This class contains functions for creating the feature vector for the project TextSummarization
 */
public class FeatureVector {
    double sentencePosRel;
    double sentenceLength;
    double isFirstsentence;

    public double getSentencePosRel() {
        return sentencePosRel;
    }

    public void setSentencePosRel(double sentencePosRel) {
        this.sentencePosRel = sentencePosRel;
    }

    public double getSentenceLength() {
        return sentenceLength;
    }

    public void setSentenceLength(double sentenceLength) {
        this.sentenceLength = sentenceLength;
    }

    public double getIsFirstsentence() {
        return isFirstsentence;
    }

    public void setIsFirstsentence(double isFirstsentence) {
        this.isFirstsentence = isFirstsentence;
    }

    public double getNrThematicWords() {
        return nrThematicWords;
    }

    public void setNrThematicWords(double nrThematicWords) {
        this.nrThematicWords = nrThematicWords;
    }

    public double getNrHeadlineWords() {
        return nrHeadlineWords;
    }

    public void setNrHeadlineWords(double nrHeadlineWords) {
        this.nrHeadlineWords = nrHeadlineWords;
    }

    double nrThematicWords;
    double nrHeadlineWords;

    /**
     * This constructor takes five double values as argument and set these values as properties
     * for a feature vector.
     * @param sentencePos
     * @param sentenceLength
     * @param isFirst
     * @param nrThematic
     * @param nrHead
     */
    public FeatureVector(double sentencePos,double sentenceLength,double isFirst,double nrThematic, double nrHead){
        this.setSentencePosRel(sentencePos);
        this.setSentenceLength(sentenceLength);
        this.setIsFirstsentence(isFirst);
        double temp = nrHead / sentenceLength;
        this.setNrHeadlineWords(temp);
        temp = nrThematic / sentenceLength;
        this.setNrThematicWords(temp);
    }

    /**
     * This method returns an array of double values which represents a feature vector
     * @return double []
     */
    public double[] getVector() {
        double []vector = new double[5];
        vector[0] = this.getSentencePosRel();
        vector[1] = this.getSentenceLength();
        vector[2] = this.getIsFirstsentence();
        vector[3] = this.getNrThematicWords();
        vector[4] = this.getNrHeadlineWords();
        return vector;
    }

    /**
     * This method returns a string from a feature vector
     * @return String
     */
    public String toString (){
        double [] vector = this.getVector();
        StringBuilder sb = new StringBuilder();
        for (double v : vector){
            sb.append(v + ", ");
        }
        return sb.toString().trim();
    }

}

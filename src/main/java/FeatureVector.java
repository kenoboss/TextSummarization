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

    public FeatureVector(double sentencePos,double sentenceLength,double isFirst,double nrThematic, double nrHead){
        this.setSentencePosRel(sentencePos);
        this.setSentenceLength(sentenceLength);
        this.setIsFirstsentence(isFirst);
        this.setNrHeadlineWords(nrHead/sentenceLength);
        this.setNrThematicWords(nrThematic/sentenceLength);
    }

    public double[] getVector() {
        double []vector = new double[5];
        vector[0] = this.getSentencePosRel();
        vector[1] = this.getSentenceLength();
        vector[2] = this.getIsFirstsentence();
        vector[3] = this.getNrThematicWords();
        vector[4] = this.getNrHeadlineWords();
        return vector;
    }

    public String toString (){
        double [] vector = this.getVector();
        StringBuilder sb = new StringBuilder();
        for (double v : vector){
            sb.append(v + ", ");
        }
        return sb.toString().trim();
    }

}

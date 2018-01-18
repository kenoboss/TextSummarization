public class FeatureVector {
    double sentencePosRel;
    double sentenceLength;
    double isFirstsentence;
    double nrThematicWords;
    double nrHeadlineWords;

    public FeatureVector(double sentencePos,double sentenceLength,double isFirst,double nrThematic, double nrHead){
        this.sentencePosRel = sentencePos;
        this.sentenceLength = sentenceLength;
        this.isFirstsentence = isFirst;
        this.nrThematicWords = nrThematic/sentenceLength;
        this.nrHeadlineWords = nrHead/sentenceLength;
    }

    public double[] getVector() {
        double []vector = {sentencePosRel,sentenceLength,isFirstsentence,nrThematicWords,nrHeadlineWords};
        return vector;
    }

}

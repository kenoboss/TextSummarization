/**
 * This Class creates FeatureVectors from a corpus.
 * @author Tobias Ziegelmayer
 * @version 1.0.0
 */
public class CreateCorpus {
    public static void main(String[] args) {

        String basePath = "src/data/";

        Corpus training = new Corpus(1,0);
        training.saveCorpus(basePath+"trainingsSet.csv");

        Corpus test = new Corpus(0,0);
        test.saveCorpus(basePath+"testSet.csv");

    }
}

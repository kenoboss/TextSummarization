import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Tobias Ziegelmayer & Simon Werner
 * @version 1.0.0
 * This class contains functions for Classification of Sentences, based on the trained Neural Network
 */
public class Classifier {

    /**
     * This method takes as parameter a string and creates a MultiLayerNetwork from a stored network.
     * @param pathToNet
     * @return MultiLayerNetwork
     * @throws IOException
     */
    public MultiLayerNetwork loadNet (String pathToNet) throws IOException {
        return ModelSerializer.restoreMultiLayerNetwork(pathToNet);
    }

    /**
     * This method takes as parameter two string and creates a string as summary from
     * the input strings.
     * The output will generated from a trained neural network.
     * @param text
     * @param headline
     * @return String
     * @throws IOException
     * @throws InterruptedException
     */
    public String summarize (String text, String headline) throws IOException, InterruptedException {
        StringBuilder sb = new StringBuilder();
        Entry entry = new Entry(text, headline);

        String pathToFile = "target/classes/temp/"+createPath(headline, "csv");
        saveCorpus(pathToFile, entry);
        pathToFile = "src/main/resources/temp/"+createPath(headline, "csv");
        saveCorpus(pathToFile, entry);
        File f = new File (pathToFile);
        if (!f.exists()){
            System.exit(0);
        }
        pathToFile = "/temp/"+createPath(headline,"csv");
        double[][] ratings = createRatings(entry.getFeatureVectors().size(), pathToFile, entry);
        List<List<String>> tokens = entry.getTextTokens();
        int sumCounter = 0;
        for (int i = 0; i < ratings.length; i++){
            if (ratings[i][0] >= 0.5){
                sumCounter++;
                StringBuilder stringBuilder = new StringBuilder();
                List<String> tmp = tokens.get(i);
                for (String s : tmp){sb.append(s+" ");}
                sb.append(stringBuilder.toString()+"\n");
            }
        }
        return sb.toString();
    }

    /**
     * This method takes two strings as parameter and creates from the input
     * strings a filepath.
     * @param headline
     * @param type
     * @return String
     */
    private static String createPath (String headline, String type){
        headline = headline.replaceAll("[^A-Za-z0-9]", " ");
        headline = headline.replaceAll(" +", " ");
        String [] result = headline.split(" ");
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for (String entry : result){
            if (counter < result.length-1){
                sb.append(entry+"_");
            }
            else{
                sb.append(entry+"."+type);
            }
            counter++;
        }
        return sb.toString();
    }

    /**
     * This method takes as parameter an integer, a string and an entry and creates a
     * 2-dimensional array of double values.
     * The double values are the result of the output layer of the use of the trained neuroal
     * network.
     * @param batchsize
     * @param pathToFeatureVectors
     * @param entry
     * @return double [][]
     * @throws IOException
     * @throws InterruptedException
     */
    public static double [][] createRatings (int batchsize, String pathToFeatureVectors, Entry entry) throws IOException, InterruptedException{
        double [][] result = new double[batchsize][2];
        Classifier test = new Classifier();
        MultiLayerNetwork restored = test.loadNet("src/main/resources/model/trainedNetwork.zip");

        int numLinesToSkip = 0;
        char delimiter = ',';
        RecordReader recordReader = new CSVRecordReader(numLinesToSkip,delimiter);
        recordReader.initialize(new FileSplit(new ClassPathResource(pathToFeatureVectors).getFile()));
        DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader,batchsize);
        DataSet next = iterator.next();

        INDArray output = restored.output(next.getFeatureMatrix());
        for (int i = 0; i < output.size(0); i++){
            result[i][0] = output.getDouble(i, 0);
            result[i][1] = output.getDouble(i, 1);
        }

        return result;
    }

    /**
     * This method takes a path to a folder and a filename
     * to save the created feature vectors of a corpus as csv file
     * @param path
     */
    public void saveCorpus (String path, Entry entry){

        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (FeatureVector featureVector : entry.getFeatureVectors()){
            int counter = 0;
            for (double aDouble : featureVector.getVector()){
                if (counter < featureVector.getVector().length-1){
                    sb.append(aDouble+",");
                }
                else{
                    sb.append(aDouble);
                }
                counter++;
            }
            sb.append("\n");
        }
        result.add(sb.toString().trim());


        Helper helper = new Helper();
        helper.writeLargeFileLines(path, result);
    }



    public static void main(String[] args) throws IOException, InterruptedException {
        Classifier classifier = new Classifier();
        String headline = "India will fire countless bullets if Pakistan fires one: Rajnath Singh";
        String text = "Asserting that Kashmir is a part of India and will continue to be so, Union home minister Rajnath Singh on Saturday said Indian forces will fire countless bullets if Pakistan fires one.\n “As our neighbour, we do not want to attack Pakistan first. We want to live in peace and harmony with all our neighbours. But unfortunately Pakistan is trying to tear down Jammu and Kashmir and continues attacks on our forces and territory. “I have ordered our forces that if one bullet comes from that side, not to think of firing countless bullets at them,” Singh said at a poll rally in Barjala in Agartala.\n The home minister is on a two-day tour of Tripura to boost BJP’s campaign for the February 18 polls. Lambasting the Marxist government in Tripura, he said it had failed to bring development to the state despite being in power for more than two decades and accused the Left government of “25 years’ misrule” through “crime against women, unemployment and corruption”. “Like West Bengal was devastated during CPM’s 35 years’ regime, Tripura also witnessed misrule under CPM government for 25 years. So I appeal to the people to give at least once chance to the BJP to allow improvement of the state like other BJP-ruled states,” said Singh.\n Praising the NDA government, he said people will never bring CPM back to power if they see the good governance in BJP-ruled states. Singh will start a Vijay Rath Yatra from Durgabari to BT College ground in the city on Sunday where he will address another public meeting. He will then tour six assembly constituencies as part of his yatra and conclude with another public meeting at Kshudiram Government English School at Jail Ashram Road in Agartala.";
        String sum = classifier.summarize(text, headline);
        String path = createPath(headline, "txt");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(sum);

        Helper helper = new Helper();
        helper.writeFileBytes("src/main/resources/temp/"+path, stringBuilder.toString());
    }
}

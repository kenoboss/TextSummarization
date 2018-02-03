import org.bytedeco.javacpp.presets.opencv_core;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import java.io.File;
import java.io.IOException;
import java.util.List;

public class Classifier {


    public MultiLayerNetwork loadNet (String pathToNet) throws IOException {
        return ModelSerializer.restoreMultiLayerNetwork(pathToNet);
    }

    public String summarize (String text, String headline) throws IOException, InterruptedException {
        StringBuilder sb = new StringBuilder();
        Entry entry = new Entry(text, headline);
//        String pathToFile = "data/temp/"+createPath(headline);
        String pathToFile = "src/data/temp/test.csv";
        saveCorpus(pathToFile, entry);
        pathToFile = "/data/temp/test.csv";
        double[][] ratings = createRatings(entry.getFeatureVectors().size(), pathToFile);
        List<List<String>> tokens = entry.getTextTokens();
        for (int i = 0; i < ratings.length; i++){
            for (int j = 0; j < ratings[i].length; j++){
                if (ratings[i][j] >= 0.5){
                    StringBuilder stringBuilder = new StringBuilder();
                    List<String> tmp = tokens.get(i);
                    for (String s : tmp){sb.append(s+" ");}
                    sb.append(stringBuilder.toString()+" ");
                }
            }
        }
        return sb.toString();
    }

    private static String createPath (String headline){
        String [] result = headline.split(" ");
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for (String entry : result){
            if (counter < result.length-1){
                sb.append(entry+"_");
            }
            else{
                sb.append(entry+".csv");
            }
            counter++;
        }
        return sb.toString();
    }

    public static double [][] createRatings (int batchsize, String pathToFeatureVectors) throws IOException, InterruptedException{
        double [][] result = new double[batchsize][2];
        Classifier test = new Classifier();
        MultiLayerNetwork restored = test.loadNet("trainedNetwork.zip");

        RecordReader recordReader = new CSVRecordReader(0,",");
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
        Corpus corpus = new Corpus(20,0);
        for (Entry entry : corpus.getEntries()){
            Entry tmp = new Entry (entry.getText(), entry.getHeadlines());
            Classifier classifier = new Classifier();
            String text = tmp.getText();
            String headline = tmp.getHeadlines();
            String sum = classifier.summarize(text, headline);
            System.out.println(sum);
        }

    }
}

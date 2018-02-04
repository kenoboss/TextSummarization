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

public class Classifier {


    public MultiLayerNetwork loadNet (String pathToNet) throws IOException {
        return ModelSerializer.restoreMultiLayerNetwork(pathToNet);
    }

    public String summarize (String text, String headline) throws IOException, InterruptedException {
        StringBuilder sb = new StringBuilder();
        Entry entry = new Entry(text, headline);
        sb.append(entry.getHeadlines()+"\n\n");
        List<List<String>> orgText = entry.getTextTokens();
        for (List<String> sentence : orgText){
            StringBuilder stringBuilder = new StringBuilder();
            for (String token : sentence){
                stringBuilder.append(token+" ");
            }
            sb.append(stringBuilder.toString()+"\n");
        }
        sb.append("\n\n");
        String pathToFile = "src/data/temp/"+createPath(headline, "csv");
        saveCorpus(pathToFile, entry);
        pathToFile = "/data/temp/"+createPath(headline,"csv");
        double[][] ratings = createRatings(entry.getFeatureVectors().size(), pathToFile);
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
        System.out.println("Länge des Textes: "+tokens.size()+" Sätze\nLänge der Zusammenfassung: "+sumCounter+" Sätze");
        return sb.toString();
    }

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

    public static double [][] createRatings (int batchsize, String pathToFeatureVectors) throws IOException, InterruptedException{
        // TODO: File pathToFeatureVectors not existing at the first run
        double [][] result = new double[batchsize][2];
        Classifier test = new Classifier();
        MultiLayerNetwork restored = test.loadNet("src/data/model/trainedNetwork.zip");

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
        ;
    }



    public static void main(String[] args) throws IOException, InterruptedException {
//        Corpus corpus = new Corpus(20,0);
//        for (Entry entry : corpus.getEntries()){
//            Entry tmp = new Entry (entry.getText(), entry.getHeadlines());
//            Classifier classifier = new Classifier();
//            String text = tmp.getText();
//            String headline = tmp.getHeadlines();
//            String sum = classifier.summarize(text, headline);
//            System.out.println(sum);
//
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append(sum);
//
//            Helper helper = new Helper();
//            helper.writeFileBytes("src/data/temp/test.txt", stringBuilder.toString());
//        }


        Classifier classifier = new Classifier();
        String headline = "Daman & Diu revokes mandatory Rakshabandhan in offices order";
        String text = "The Daman and Diu administration on Wednesday withdrew a circular that asked women staff to tie rakhis on male colleagues after the order triggered a backlash from employees and was ripped apart on social media.The union territory?s administration was forced to retreat within 24 hours of issuing the circular that made it compulsory for its staff to celebrate Rakshabandhan at workplace.?It has been decided to celebrate the festival of Rakshabandhan on August 7. In this connection, all offices/ departments shall remain open and celebrate the festival collectively at a suitable time wherein all the lady staff shall tie rakhis to their colleagues,? the order, issued on August 1 by Gurpreet Singh, deputy secretary (personnel), had said.To ensure that no one skipped office, an attendance report was to be sent to the government the next evening.The two notifications ? one mandating the celebration of Rakshabandhan (left) and the other withdrawing the mandate (right) ? were issued by the Daman and Diu administration a day apart. The circular was withdrawn through a one-line order issued late in the evening by the UT?s department of personnel and administrative reforms.?The circular is ridiculous. There are sensitivities involved. How can the government dictate who I should tie rakhi to? We should maintain the professionalism of a workplace? an official told Hindustan Times earlier in the day. She refused to be identified.The notice was issued on Daman and Diu administrator and former Gujarat home minister Praful Kodabhai Patel?s direction, sources said.Rakshabandhan, a celebration of the bond between brothers and sisters, is one of several Hindu festivities and rituals that are no longer confined of private, family affairs but have become tools to push politic al ideologies.In 2014, the year BJP stormed to power at the Centre, Rashtriya Swayamsevak Sangh (RSS) chief Mohan Bhagwat said the festival had ?national significance? and should be celebrated widely ?to protect Hindu culture and live by the values enshrined in it?. The RSS is the ideological parent of the ruling BJP.Last year, women ministers in the Modi government went to the border areas to celebrate the festival with soldiers. A year before, all cabinet ministers were asked to go to their constituencies for the festival.";
        String sum = classifier.summarize(text, headline);
        String path = createPath(headline, "txt");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(sum);

        Helper helper = new Helper();
        helper.writeFileBytes("src/data/temp/"+path, stringBuilder.toString());
    }
}

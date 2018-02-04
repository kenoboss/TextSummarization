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
        String pathToFile = "src/data/temp/"+createPath(headline);
        saveCorpus(pathToFile, entry);
        pathToFile = "/data/temp/"+createPath(headline);
        double[][] ratings = createRatings(entry.getFeatureVectors().size(), pathToFile);
        List<List<String>> tokens = entry.getTextTokens();
        for (int i = 0; i < ratings.length; i++){
            if (ratings[i][0] >= 0.5){
                StringBuilder stringBuilder = new StringBuilder();
                List<String> tmp = tokens.get(i);
                for (String s : tmp){sb.append(s+" ");}
                sb.append(stringBuilder.toString()+"\n");
            }
        }
        return sb.toString();
    }

    private static String createPath (String headline){
        headline = headline.replaceAll("[^A-Za-z0-9]", " ");
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
        MultiLayerNetwork restored = test.loadNet("src/data/model/trainedNetwork.zip");

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
        String headline = "Bus seats mistaken for burqas by Norway anti-immigrant group";
        String text = "A Norwegian anti-immigrant group has been roundly ridiculed after members apparently mistook a photograph of six empty bus seats posted on its Facebook page for a group of women wearing burqas.?Tragic?, ?terrifying? and ?disgusting? were among the comments posted by members of the closed Fedrelandet viktigst, or ?Fatherland first?, group beneath the photograph, according to screenshots on the Norwegian news website Nettavisen.Other members of the 13,000-strong group, for people ?who love Norway and appreciate what our ancestors fought for?, wondered whether the non-existent passengers might be carrying bombs or weapons beneath their clothes. ?This looks really scary,? wrote one. ?Should be banned. You can?t tell who?s underneath. Could be terrorists.?Further comments read: ?Ghastly. This should never happen,? ?Islam is and always will be a curse,? ?Get them out of our country ? frightening times we are living in,? and: ?I thought it would be like this in the year 2050, but it is happening NOW,? according to thelocal.no and other media.The photograph, found on the internet, was posted ?for a joke? last week by Johan Slĺttavik, who has since described himself as ?Norway?s worst web troll and proud of it?, beneath a question asking the group: ?What do people think of this??Slĺttavik told Nettavisen and Norway?s TV2 he wanted to ?highlight the difference between legitimate criticism of immigration and blind racism?, and was ?interested to see how people?s perceptions of an image are influenced by how others around them react. I ended up having a good laugh.?It went viral in Norway after Sindre Beyer, a former Labour party MP who said he has been following Fatherland first for some time, published 23 pages of screenshots of the group?s outraged comments.?What happens when a photo of some empty bus seats is posted to a disgusting Facebook group, and nearly everyone thinks they see a bunch of burqas?? he asked in a post shared more than 1,800 times. The comments suggested the vast majority of the anti-immigrant group?s members saw the photo as evidence of the ongoing ?Islamification? of Norway, although a small number pointed out it was in fact a picture of bus seats. One warned the group was making itself look ridiculous.Beyer told Nettavisen: ?I?m shocked at how much hate and fake news is spread [on the Fedrelandet viktigst page]. So much hatred against empty bus seats certainly shows that prejudice wins out over wisdom.?The head of Norway?s Antiracist Centre, Rune Berglund Steen, told the site that people plainly ?see what they want to see ? and what these people want to see are dangerous Muslims?.Norway recently became the latest European country to propose restrictions on the wearing of burqas and niqabs, tabling a law that will bar them from kindergartens, schools and universities. France, the Netherlands, Belgium, Bulgaria and the German state of Bavaria all restrict full-face veils in some public places.The country?s minority government, a coalition of the centre-right Conservatives and the populist Progress party that faces elections next month, said in June it was confident it would find opposition support for the move.Per Sandberg, then acting immigration and integration minister, told a press conference that face-covering garments such as the niqab or burqa ?do not belong in Norwegian schools. The ability to communicate is a basic value.?";
        String sum = classifier.summarize(text, headline);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(sum);

        Helper helper = new Helper();
        helper.writeFileBytes("src/data/temp/test.txt", stringBuilder.toString());
    }
}

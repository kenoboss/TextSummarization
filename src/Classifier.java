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
        String pathToFile = "data/temp/"+createPath(headline);
        saveCorpus(pathToFile, entry);
        double[][] ratings = createRatings(entry.getTextLemmata().size(), pathToFile);
        int counter = 0;
        for (double [] rating : ratings){
            if (rating[0] >= 0.5){
                StringBuilder stringBuilder = new StringBuilder();
                for (String token : entry.getTextLemmata().get(counter)){
                    stringBuilder.append(token+" ");
                }
                sb.append(stringBuilder.toString()+" ");
            }
            counter++;
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
        double [] [] result = new double[batchsize][2];
        Classifier test = new Classifier();
        MultiLayerNetwork restored = test.loadNet("trainedNetwork.zip");

        RecordReader recordReader = new CSVRecordReader(0,",");
        recordReader.initialize(new FileSplit(new ClassPathResource(pathToFeatureVectors).getFile()));
        DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader,batchsize);
        DataSet next = iterator.next();

        INDArray output = restored.output(next.getFeatureMatrix());
        for (int i = 0; i < batchsize; i++){
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
        Classifier classifier = new Classifier();
        String text = "President Trump was only vaguely aware of a controversial, classified memo about the FBI’s Russia investigation when two House conservatives brought it to his attention in a Jan. 18 phone call.\n" +
                "\n" +
                "The conversation piqued Trump’s interest.\n" +
                "\n" +
                "Over the next two weeks, according to interviews with eight senior administration officials and other advisers to the president, he tuned in to cable television segments about the memo. He talked to friends and advisers about it. And, before he had even read it, Trump became absolutely convinced of one thing: The memo needed to come out.\n" +
                "\n" +
                "“He was listening to a number of members of Congress talk about how concerned they are and what they read. It’s not just a couple of us,” said Rep. Mark Meadows (R-N.C.), one of the House conservatives who spoke with Trump on the Jan. 18 call, which had focused mostly on the impending government shutdown.\n" +
                "\n" +
                "Trump’s decision, expected to culminate Friday with the memo’s release, put the president on course for the most explosive confrontation with his own FBI since he fired then-director James B. Comey last spring. It once again placed him in direct defiance of the recommendations of the country’s intelligence community, whose top official tried this week to change Trump’s mind amid concerns that the document’s disclosure would jeopardize national security.But the president was undeterred.\n" +
                "\n" +
                "“There was never any hesitation,” said one presidential adviser, who spoke on the condition of anonymity to recount private talks with Trump. “The president was resolved on this. He was not going to be persuaded [otherwise]. He wanted it out.”\n" +
                "\n" +
                "The president did not actually see the memo — written by House Intelligence Committee Chairman Devin Nunes (R-Calif.) and Nunes’s staff — until Wednesday afternoon, following the committee’s Monday vote to initiate its release, officials said. White House Chief of Staff John F. Kelly marched the document into the Oval Office so that he and Trump could briefly discuss it before the president’s meeting with regional reporters.\n" +
                "\n" +
                "The president was then left alone to read the memo in its entirety.\n" +
                "\n" +
                "A White House official said Kelly returned a few hours later and shared with the president his opinion: that releasing the memo would not risk national security but that the document was not as compelling as some of its advocates had promised Trump.\n" +
                "\n" +
                "The memo alleges that the FBI used bad information passed on from a dossier written by a former British spy, and that this information was later used to obtain a warrant to conduct surveillance on former Trump campaign adviser Carter Page, according to people familiar with the document.\n" +
                "\n" +
                "Trump told aides and confidants he believed the memo would vindicate his claim early last year that the expansive Russia investigation overseen by special counsel Robert S. Mueller III was a “witch hunt.” He had long expressed frustration, both publicly and privately, with his Department of Justice and, specifically, Deputy Attorney General Rod J. Rosenstein, who is supervising Mueller’s work.\n" +
                "\n" +
                "The president said he thought the release of the memo would help build a public argument against Rosenstein’s handling of the case, according to people familiar with the discussions. Trump suggested to aides and confidants that the memo might give him the justification to fire Rosenstein — something about which Trump has privately mused — or make other changes at the Justice Department, which he had complained was not sufficiently loyal to him.\n" +
                "\n" +
                "Inside the narrow corridors and cramped offices of the West Wing, aides knew that trying to persuade their boss that keeping the memo private would likely be a fruitless endeavor. Even had the entirety of the senior staff counseled him against releasing the document, one aide reasoned, the president might still have remained unconvinced.\n" +
                "\n" +
                "At one point, just before he departed for Davos, Switzerland, Trump became particularly excited watching Rep. Trey Gowdy (R-S.C.) argue on CNN that the public deserved to see the memo and that some of the FBI agents involved in the probe had displayed an anti-Trump bias in text messages, a White House official said.\n" +
                "\n" +
                "After the State of the Union address Tuesday night, Trump inadvertently revealed his mind-set when he was caught on tape speaking with a GOP lawmaker who had asked him to “release the memo.”\n";
        String sum = classifier.summarize(text, "‘Never any hesitation’: Trump was quickly persuaded to support memo’s release");
        System.out.println(sum);
    }
}

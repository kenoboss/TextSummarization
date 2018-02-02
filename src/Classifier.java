import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import java.io.File;
import java.io.IOException;

public class Classifier {
    public MultiLayerNetwork loadNet (String pathToNet) throws IOException {
        return ModelSerializer.restoreMultiLayerNetwork(pathToNet);
    }

    public String summarize (MultiLayerNetwork trainedNet,String text) {
        //To Do: process text to Feature Vectors, Classify Feature Vectors, Add to Summary if Classified as 1
        String summary = "";
        //Use Method "predict" on trainedNet to classify
        return summary;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Classifier test = new Classifier();
        MultiLayerNetwork restored = test.loadNet("trainedNetwork.zip");
        System.out.println(restored.params());

        String filename = new ClassPathResource("/data/temp/classificationTest.csv").getFile().getPath();
        DataSet test = readCSVDataset(filename,
                50, 5, 2);

        restored.feedForward();
    }
}

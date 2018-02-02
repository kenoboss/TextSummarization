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
import org.nd4j.linalg.factory.Nd4j;

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

        //String filenameTest = new ClassPathResource("/data/temp/classificationTest.csv").getFile().getPath();

        RecordReader recordReader = new CSVRecordReader(0,",");
        recordReader.initialize(new FileSplit(new ClassPathResource("/data/temp/classificationTest.csv").getFile()));
        DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader,4);
        DataSet next = iterator.next();
        // Customizing params
        Nd4j.MAX_SLICES_TO_PRINT = 10;
        Nd4j.MAX_ELEMENTS_PER_SLICE = 10;

        restored.predict(next);

    }
}

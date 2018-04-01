import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.distribution.UniformDistribution;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.GravesLSTM;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer.Builder;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Simon Werner and Tobias Ziegelmayer
 * @version 1.0.0
 * This class contains a main method that creates, trains, evaluates and saves a neural network with the given parameters
 */

public class NeuralNetwork {
    private static final int NUMFEATUREVECTORSIZE = 5;
    private static final int NUMHIDDENNODES = 10;
    private static final int NUMFINALCLASSES = 2;
    private static final int HIDDEN_LAYER_CONT = 10;
    private static final int ITERATIONS = 10000;
//    private static final String HOME_PATH = "/home/kenobi/Repos/GitHub/TextSummarization/target/";
    private static final String HOME_PATH = "/home/ziegelmayer/TextSummarization/target/";


    static HashMap<String, List<Double>> results = new HashMap<>();


    public static void main(String[] args) throws IOException, InterruptedException {
        WeightInit [] weightInits = {WeightInit.DISTRIBUTION, WeightInit.ZERO, WeightInit.SIGMOID_UNIFORM,
                WeightInit.UNIFORM, WeightInit.XAVIER, WeightInit.XAVIER_UNIFORM, WeightInit.XAVIER_FAN_IN,
                WeightInit.XAVIER_LEGACY, WeightInit.RELU, WeightInit.RELU_UNIFORM};

        String [] activations = {"CUBE", "ELU", "HARDSIGMOID", "HARDTANH", "IDENTITY", "LEAKYRELU",
                "RATIONALTANH", "RELU", "RRELU", "SIGMOID", "SOFTMAX", "SOFTPLUS", "SOFTSIGN",
                "TANH", "RECTIFIEDTANH", "SELU"};

        for (String activation : activations){
            for (WeightInit weightInit : weightInits){
                long startTime = System.currentTimeMillis();
                runNetwork(activation, weightInit);
                long estimatedTime = System.currentTimeMillis() - startTime;
                double minTime = (estimatedTime / 1000 ) / 60;
                System.out.println("===================\n"+estimatedTime+"\n=================");
            }
        }


//        long startTime = System.currentTimeMillis();
//        runNetwork("IDENTITY", WeightInit.XAVIER_FAN_IN);
//        long estimatedTime = System.currentTimeMillis() - startTime;
//        double minTime = (estimatedTime / 1000 ) / 60;
//        System.out.println("===================\n"+minTime+"\n=================");
        writeResultsToFile(results);

    }

    private static void writeResultsToFile (HashMap<String, List<Double>> input){
        StringBuilder sb = new StringBuilder();
        sb.append("Activation,WeightInit,F1,precision,recall\n");
        for (String key : input.keySet()){
            String [] temp = key.split("  ");
            sb.append(temp[0]+","+temp[1]+",");
            for (Double aDouble : input.get(key)){
                sb.append(aDouble+",");
            }
            sb.append("\n");
        }
        Helper helper = new Helper();
        helper.writeFileBytes(HOME_PATH+"stats.csv", sb.toString());
    }

    public static void runNetwork (String activation, WeightInit weightInit) throws IOException, InterruptedException {
        int batchSize = 1000;
//        final String filenameTrain  = new ClassPathResource("trainingsSetSmall.csv").getFile().getPath();
//        final String filenameTest  = new ClassPathResource("testSetSmall.csv").getFile().getPath();

        final String filenameTrain  = new ClassPathResource("trainingsSet.csv").getFile().getPath();
        final String filenameTest  = new ClassPathResource("testSet.csv").getFile().getPath();

        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(new File(filenameTrain)));
        DataSetIterator trainIter = new RecordReaderDataSetIterator(rr,batchSize,0,NUMFINALCLASSES);
        RecordReader rrTest = new CSVRecordReader();
        rrTest.initialize(new FileSplit(new File(filenameTest)));
        DataSetIterator testIter = new RecordReaderDataSetIterator(rrTest,batchSize,0,NUMFINALCLASSES);
//        MultiLayerNetwork net = new MultiLayerNetwork(getFeedForewardConf(activation, weightInit));
        MultiLayerNetwork net = new MultiLayerNetwork(getRecurrentConf(activation, weightInit));

        net.init();
        // add an listener which outputs the error every 100 parameter updates
        net.setListeners(new ScoreIterationListener(ITERATIONS / 10));
        // C&P from GravesLSTMCharModellingExample
        // Print the number of parameters in the network (and for each layer)
        Layer[] layers = net.getLayers();
        int totalNumParams = 0;
        for (int i = 0; i < layers.length; i++) {
            int nParams = layers[i].numParams();
            System.out.println("Number of parameters in layer " + i + ": " + nParams);
            totalNumParams += nParams;
        }
        System.out.println("Total number of network parameters: " + totalNumParams);

        // start UI
        // use: http://localhost:9000/train
//        startUI(net);

        // here the actual learning takes place
        net.fit(trainIter);
        // create output for every training sample
        System.out.println("Evaluate model....");
        Evaluation eval = new Evaluation(2);
        while(testIter.hasNext()){
            DataSet t = testIter.next();
            INDArray features = t.getFeatureMatrix();
            INDArray lables = t.getLabels();
            INDArray predicted = net.output(features,false);
            eval.eval(lables, predicted);
        }
        //Print the evaluation statistics
        System.out.println(eval.stats());
        Helper helper = new Helper();
        helper.writeFileBytes(HOME_PATH+"model/stats/"+weightInit.name()+"_"+activation+"_RNN.txt", eval.stats());
        if (eval.f1() > 0.0){
            List<Double> temp = new ArrayList<>();
            temp.add(eval.f1());
            temp.add(eval.precision());
            temp.add(eval.recall());
            results.put(activation+"  "+weightInit.name(), temp);
        }
        //Save the network
        File saveLocation = new File(HOME_PATH+"trainedNetwork.zip");
        boolean saveUpdater = true;
        ModelSerializer.writeModel(net,saveLocation,saveUpdater);
    }

    private static void startUI (MultiLayerNetwork net){
        UIServer uiServer = UIServer.getInstance();
        StatsStorage statsStorage = new InMemoryStatsStorage();
        int listenerFrequency = 1;
        net.setListeners(new StatsListener(statsStorage, listenerFrequency));
        uiServer.attach(statsStorage);
    }

    private static MultiLayerConfiguration getFeedForewardConf (String activation, WeightInit weightInit){
        MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder()
                .seed(123)
                .iterations(ITERATIONS)
                .activation(Activation.fromString(activation))
                .weightInit(weightInit)
                .learningRate(0.001)
                .regularization(true).l2(1e-4)
                .list()
                .layer(0, new DenseLayer.Builder().nIn(NUMFEATUREVECTORSIZE).nOut(NUMHIDDENNODES)
                        .build())
                .layer(1, new DenseLayer.Builder().nIn(NUMHIDDENNODES).nOut(NUMHIDDENNODES)
                        .build())
                .layer(2, new DenseLayer.Builder().nIn(NUMHIDDENNODES).nOut(NUMHIDDENNODES)
                        .build())
                .layer(3, new DenseLayer.Builder().nIn(NUMHIDDENNODES).nOut(NUMHIDDENNODES)
                        .build())
                .layer(4, new DenseLayer.Builder().nIn(NUMHIDDENNODES).nOut(NUMHIDDENNODES)
                        .build())
                .layer(5, new DenseLayer.Builder().nIn(NUMHIDDENNODES).nOut(NUMHIDDENNODES)
                        .build())
                .layer(6, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX)
                        .nIn(NUMHIDDENNODES).nOut(NUMFINALCLASSES).build())
                .backprop(true).pretrain(false)
                .build();

        return configuration;
    }

    private static MultiLayerConfiguration getRecurrentConf (String activation, WeightInit weightInit){
        NeuralNetConfiguration.Builder builder = new NeuralNetConfiguration.Builder();
        builder.iterations(1000);
        builder.learningRate(0.01);
        builder.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT);
        builder.seed(123);
        builder.biasInit(0);
        builder.miniBatch(false);
        builder.updater(Updater.RMSPROP);
        builder.weightInit(weightInit);

        ListBuilder listBuilder = builder.list();

        // first difference, for rnns we need to use GravesLSTM.Builder
        for (int i = 0; i < HIDDEN_LAYER_CONT; i++) {
            GravesLSTM.Builder hiddenLayerBuilder = new GravesLSTM.Builder();
            hiddenLayerBuilder.nIn(i == 0 ? NUMFEATUREVECTORSIZE : NUMHIDDENNODES);
            hiddenLayerBuilder.nOut(NUMHIDDENNODES);
            // adopted activation function from GravesLSTMCharModellingExample
            // seems to work well with RNNs
            hiddenLayerBuilder.activation(Activation.fromString(activation));
            listBuilder.layer(i, hiddenLayerBuilder.build());
        }

        // we need to use RnnOutputLayer for our RNN
        RnnOutputLayer.Builder outputLayerBuilder = new RnnOutputLayer.Builder(LossFunctions.LossFunction.MCXENT);
        // softmax normalizes the output neurons, the sum of all outputs is 1
        // this is required for our sampleFromDistribution-function
        outputLayerBuilder.activation(Activation.SOFTMAX);
        outputLayerBuilder.nIn(NUMHIDDENNODES);
        outputLayerBuilder.nOut(NUMFINALCLASSES);
        listBuilder.layer(HIDDEN_LAYER_CONT, outputLayerBuilder.build());

        // finish builder
        listBuilder.pretrain(false);
        listBuilder.backprop(true);

        // create network
        MultiLayerConfiguration conf = listBuilder.build();

        return conf;
    }
}
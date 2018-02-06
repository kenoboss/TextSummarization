import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Tobias Ziegelmayer
 * @version 1.0.0
 * This class contains corpus function for the project TextSummarization
 *
 */
public class Corpus {
	
	List<Entry> entries;
	

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	/**
	 * Constructor of a corpus.
	 * The constructor takes as arguments two integer values.
	 * The first one is the start point for the method createCorpus
	 * and the second one is the maximum border for the size of
	 * the corpus.
	 * @param corpusType
	 * @param max
	 */
	public Corpus (int corpusType, int max) {
		List<Entry> entries = this.createCorpus(corpusType, max);
		this.setEntries(entries);
	}

	/**
	 * This method creates a list of list of string from a csv file
	 * @return List<List<String>>
	 */
	public static List < List < String > > readIn () {
		Helper helper = new Helper();
		String s = System.getProperty("user.dir");
		String path = s+"src/main/resources/summary_news_clear.csv";
		List < List < String > > lines = helper.readCSVFile(path);
		
		return lines;
	}

	/**
	 * This method takes as arguments two integer values.
	 * The first one is the start point for the method createCorpus
	 * and the second one is the maximum border for the size of
	 * the corpus.
	 * The method generates a list of entries and add all information
	 * from a text to an entry.
	 * @param corpusType
	 * @param max
	 * @return List<Entry>
	 */
	public List<Entry> createCorpus(int corpusType, int max){

		StanfordNLP snlp = new StanfordNLP();
		Helper helper = new Helper();
		List < List < String > > cLines = readIn();
		List<Entry> entries = new ArrayList<Entry>();
		int lineCounter = 0;
		int i = 0;
		// calculates the size of the corpus
		int maxSize = 0;
		int minSize = 0;
		Double tmpSize = 0.0;
		tmpSize = cLines.size()*0.66;
		int [] train = {0, tmpSize.intValue()-1};
		int [] test = {tmpSize.intValue(), cLines.size()-1};
		if (corpusType == 1){
			// create trainingscorpus
			minSize = train[0]; maxSize = train[1];
		}
		else if (corpusType == 0){
			// create testcorpus
			minSize = test[0]; maxSize = test[1];
		}
		else{
			// corpusType as corpusSize
			int corpusSize = corpusType;
			minSize = max; maxSize = corpusSize+max;
		}


        for(List<String> line: cLines) {
        	if (lineCounter > minSize && lineCounter <= maxSize) {
        		Entry entry = new Entry();
        		entry.setId(lineCounter);
        		entry.setAuthor(line.get(0));
        		entry.setDate(line.get(1));
        		entry.setHeadlines(line.get(2));
        		entry.setUrl(line.get(3));
        		entry.setSummary(line.get(4));
        		entry.setText(line.get(5));
        		List<List<List<String>>> stanfordText = snlp.stanfordLemmatizerAndTokenizer(entry.getText());
        		entry.setTextTokens(stanfordText.get(0));
        		entry.setTextLemmata(stanfordText.get(1));
				List<List<List<String>>> stanfordSummary = snlp.stanfordLemmatizerAndTokenizer(entry.getSummary());
        		entry.setSummaryTokens(stanfordSummary.get(0));
        		entry.setSummaryLemmata(stanfordSummary.get(1));
				List<List<List<String>>> stanfordHeadline = snlp.stanfordLemmatizerAndTokenizer(entry.getHeadlines());
				entry.setHeadlineTokens(stanfordHeadline.get(0));
				entry.setHeadlineLemmata(stanfordHeadline.get(1));
				WordFrequencies wf = new WordFrequencies();
				entry.setContentWordsText(wf.getTop10(entry.getTextLemmata()));
				entry.setContentWordsHeadline(wf.getList(entry.getHeadlineLemmata()));

				LabelSentences labelSentences = new LabelSentences(entry);
				entry.setDistances(labelSentences.getDistances());
				entry.setMeanDistance(labelSentences.getMean(entry.getDistances()));
				entry.setLabels(labelSentences.getLabels(entry.getDistances(), entry.getMeanDistance()));
				List<FeatureVector> featureVectors = helper.createFeatureVectors(entry);
				entry.setFeatureVectors(featureVectors);
        		entries.add(entry);
        		i++;
        	}
			lineCounter++;
        }
		return entries;
	}


	/**
	 * This method takes a path to a folder and a filename
	 * to save the created feature vectors of a corpus as csv file
	 * @param path
	 */
	public void saveCorpus (String path){

		List<String> result = new ArrayList<>();
		for (Entry entry : this.getEntries()){
			int index = 0;
			StringBuilder sb = new StringBuilder();
			for (FeatureVector featureVector : entry.getFeatureVectors()){
				sb.append(entry.getLabels().get(index)+",");
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
				index++;
			}
			result.add(sb.toString().trim());
		}

		Helper helper = new Helper();
		helper.writeLargeFileLines(path, result);
	}
}

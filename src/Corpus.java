import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Kenoboss
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

	public Corpus (int corpusType, int max) {
		List<Entry> entries = this.createCorpus(corpusType, max);
		this.setEntries(entries);
	}

	public static List < List < String > > readIn () {
		Helper helper = new Helper();
		String s = System.getProperty("user.dir");
		String path = s+"/src/data/summary_news_clear.csv";
		List < List < String > > lines = helper.readCSVFile(path);
		
		return lines;
	}
	
	public List<Entry> createCorpus(int corpusType, int max){

		StanfordNLP snlp = new StanfordNLP();
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
				List<FeatureVector> featureVectors = createFeatureVectors(entry);
				entry.setFeatureVectors(featureVectors);
        		entries.add(entry);
        		i++;
        	}
			lineCounter++;
        }
		return entries;
	}

	private List<FeatureVector> createFeatureVectors(Entry entry) {
		List<FeatureVector> featureVectors = new ArrayList<>();
		List<List<String>> sentences = entry.getTextTokens();
		int counter = 0;
		for (List<String> sentence : sentences){
			Preprocessing preprocessing = new Preprocessing();
			double positionInTextRel = preprocessing.positionInTextRel(sentence, sentences);
			double countWords = preprocessing.countWords(sentence);
			double isFirst = preprocessing.isFirst(sentence, sentences);
			double NrContentWordsInSentence = contentWordsPerSentence(entry.getTextLemmata().get(counter), entry.getContentWordsText());
			double NrContentWordsHeadlineInSentence = contentWordsPerSentence(entry.getTextLemmata().get(counter), entry.getContentWordsHeadline());
			FeatureVector featureVector = new FeatureVector(positionInTextRel, countWords, isFirst, NrContentWordsInSentence, NrContentWordsHeadlineInSentence);
			featureVectors.add(featureVector);
			counter++;
		}
		return featureVectors;
	}

	private static double contentWordsPerSentence (List<String> input, List<String> contentWords){

		double result = 0;
		for (String token : input){
			if (contentWords.contains(token)){
				result++;
			}
		}
		return result;
	}

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

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

	public Corpus (boolean test) {
		List<Entry> entries = this.createCorpus(test);
		this.setEntries(entries);
	}

	public static List < List < String > > readIn () {
		Helper helper = new Helper();
		String s = System.getProperty("user.dir");
		String path = s+"/data/summary_news_clear.csv";
		List < List < String > > lines = helper.readCSVFile(path);
		
		return lines;
	}
	
	public List<Entry> createCorpus(boolean test){

		StanfordNLP snlp = new StanfordNLP();
		List < List < String > > cLines = readIn();
		List<Entry> entries = new ArrayList<Entry>();
		int lineCounter = 0;
		int i = 0;
		int maxSize = 0;

		if (test == false){maxSize = cLines.size()-1;}
		else{maxSize = 10;}

        for(List<String> line: cLines) {
        	if (lineCounter > 0 && lineCounter <= maxSize) {
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


}

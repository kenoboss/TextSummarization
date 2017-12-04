import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.sampled.Line;

/**
 * 
 * @author Kenoboss
 * @version 1.0.0
 * This class contains corpus function for the project TextSummarization
 *
 */
public class Corpus {
	
	List < List < String > > cLines;
	
	public List<List<String>> getcLines() {
		return cLines;
	}


	public void setcLines(List<List<String>> cLines) {
		this.cLines = cLines;
	}
	
	public Corpus () {
		cLines = this.readIn();
	}

	public List < List < String > > readIn () {
		Helper helper = new Helper();
		String s = System.getProperty("user.dir");
		String path = s+"/data/summary_news_clear.csv";
		List < List < String > > lines = helper.readCSVFile(path);
		
		return lines;
	}
	
	public List<HashMap<String, String> > createCorpus(){
		List<HashMap<String, String> > result = new ArrayList<>();
		Corpus corpus = new Corpus();
		List < List < String > > cLines = corpus.readIn();
		int lineCounter = 0;
        for(List<String> line: cLines) {
        	if (lineCounter > 0) {
        		HashMap<String, String> tmp = new HashMap<>();
    	    	tmp.put("author", line.get(0));
    	    	tmp.put("date", line.get(1));
    	    	tmp.put("headlines", line.get(2));
    	    	tmp.put("url", line.get(3));
    	    	tmp.put("summary", line.get(4));
    	    	tmp.put("text", line.get(5));
    			result.add(tmp);
    			System.out.println(tmp.get("author"));
        	}
			lineCounter++;
        }
		
		return result;
	}

}

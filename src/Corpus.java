import java.util.List;

public class Corpus {
	
	public List < List < String > > readIn () {
		Helper helper = new Helper();
		String s = System.getProperty("user.dir");
		String path = s+"/data/news_summary.csv";
		List < List < String > > lines = helper.readCSVFile(path, ",", "\"");
		
		return lines;
	}
	
	
	public static void main(String[] args) {
		Corpus corpus = new Corpus();
		List < List < String > > cLines = corpus.readIn();
		
        for(List<String> line: cLines) {
            for (String value: line) {
                System.out.print(value+"\t");
            }
            System.out.println();
        }
		
	}

}

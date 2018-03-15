import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Tobias Ziegelmayer
 * @version 1.0.0
 * This class contains helper functions for the project TextSummarization
 *
 */
public class Helper {

	/**
	 * This method reads a filename or filepath and returns a list of strings
	 * with the content of the file
	 * @param name
	 * @return List<String>
	 */
	public static List<String> readSmallFileLines(String name) {
		try {
			return Files.readAllLines(
					FileSystems.getDefault().getPath(".", name), 
					Charset.defaultCharset() );
		}
		catch ( IOException ioe ) { ioe.printStackTrace(); }
		return null;
	}

	/**
	 * This method takes a filename or filepath and string, which has to write
	 * to the file
	 * @param name
	 * @param content
	 */
	public static void writeFileBytes(String name, String content) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(name), "utf-8"));
			writer.write(content);
		} catch (IOException ex) {
			// Report
		} finally {
			try {writer.close();} catch (Exception ex) {/*ignore*/}
		}
	}
	
	/**
	 * This method takes a filename or filepath and a list of string, which has to write
	 * to the file
	 * @param name, content
	 */
	public static void writeLargeFileLines(String name, List<String> lines) {
		try ( BufferedWriter nbw =
				Files.newBufferedWriter(
						FileSystems.getDefault().getPath(".", name),
						Charset.forName("UTF-8"),
						StandardOpenOption.TRUNCATE_EXISTING,
						StandardOpenOption.CREATE)

				){
			for ( String line : lines ) {
				nbw.write(line, 0, line.length());
				nbw.newLine();
			}
			nbw.close();
		}
		catch ( IOException ioe ) { ioe.printStackTrace(); }
	}

	/**
	 * This method takes a filename or filepath and a string as separator and 
	 * an optional String to delete in a line as arguments. The method 
	 * returns a list of list of string as row and columns of a csv file.
	 * @param name
	 * @return List<List<String>> 
	 */
	public List<List<String>> readCSVFile (String name) {
		List<List<String>> lines = new ArrayList<>();
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(";(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                columns = clearLineFromTextQuotes(columns);
                lines.add(Arrays.asList(columns));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
	}
	
	
	/**
	 * This method takes a array of strings. In each string the textquotecharacter '"' 
	 * will be replaced through an empty string. 
	 * @param line
	 * @return String [] 
	 */
	public String [] clearLineFromTextQuotes (String [] line) {
		String [] result = new String [line.length];
		for (int i = 0; i < line.length; i++) {
			result[i] = line[i].replace("\"", "");
		}
		
		return result;
	}


	/**
	 * This method takes an entry as parameter and creates a list of feature vectors
	 * for all sentences in the text from the entry
	 * @param entry
	 * @return List<FeatureVector>
	 */
	public List<FeatureVector> createFeatureVectors(Entry entry) {
		List<FeatureVector> featureVectors = new ArrayList<>();
		List<List<String>> sentences = entry.getTextTokens();
		int counter = 0;
		for (List<String> sentence : sentences){
			Preprocessing preprocessing = new Preprocessing();
			double positionInTextRel = preprocessing.positionInTextRel(sentence, sentences);
			double countWords = preprocessing.countWords(sentence, sentences);
			double isFirst = preprocessing.isFirst(sentence, sentences);
			double NrContentWordsInSentence = contentWordsPerSentence(entry.getTextLemmata().get(counter), entry.getContentWordsText());
			double NrContentWordsHeadlineInSentence = contentWordsPerSentence(entry.getTextLemmata().get(counter), entry.getContentWordsHeadline());
			FeatureVector featureVector = new FeatureVector(positionInTextRel, countWords, isFirst, NrContentWordsInSentence, NrContentWordsHeadlineInSentence);
			featureVectors.add(featureVector);
			counter++;
		}
		return featureVectors;
	}

	/**
	 * This method takes two lists of strings as parameter and counts how many words
	 * from the input list are in the contentWords list
	 * @param input
	 * @param contentWords
	 * @return double
	 */
	public static double contentWordsPerSentence (List<String> input, List<String> contentWords){
		double result = 0;
		for (String token : input){
			if (contentWords.contains(token)){
				result++;
			}
		}
		return result;
	}

}

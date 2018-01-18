import java.util.List;

import edu.stanford.nlp.trees.Tree;
/**
 * 
 * @author Kenoboss
 * @version 1.0.0
 * This class constructs an entry of a corpus.
 * The entry contains id, author, date, source url,
 * headlines, summary and text.
 * It also use the class StanfordNLP for annotating 
 * the summary and the text.
 *
 */


public class Entry {
	
	int id;
	String author;
	String date;
	String url;
	String headlines;
	String summary;
	String text;

	List<String> contentWordsText;
	List<String> contentWordsHeadline;
	List<List<String>> textList;
	List<List<String>> summaryList;
	List<FeatureVector> featureVectors;


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHeadlines() {
		return headlines;
	}
	public void setHeadlines(String headlines) {
		this.headlines = headlines;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<String> getContentWordsText() { return contentWordsText; }
	public void setContentWordsText(List<String> contentWordsText) { this.contentWordsText = contentWordsText; }
	public List<String> getContentWordsHeadline() { return contentWordsHeadline; }
	public void setContentWordsHeadline(List<String> contentWordsHeadline) { this.contentWordsHeadline = contentWordsHeadline; }
	public List<FeatureVector> getFeatureVectors() {
		return featureVectors;
	}
	public void setFeatureVectors(List<FeatureVector> featureVectors) {
		this.featureVectors = featureVectors;
	}
	public List<List<String>> getTextList() {
		return textList;
	}
	public void setTextList(List<List<String>> textList) {
		this.textList = textList;
	}
	public List<List<String>> getSummaryList() {
		return summaryList;
	}
	public void setSummaryList(List<List<String>> summaryList) {
		this.summaryList = summaryList;
	}



	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getId()+", ");
		sb.append(this.getAuthor()+", ");
		sb.append(this.getHeadlines()+", ");
		sb.append(this.getUrl()+", ");
		sb.append(this.getDate()+", ");
		sb.append(this.getSummary()+", ");
		sb.append(this.getText()+", ");
		
		
		return sb.toString();
	}
	
	public Entry() {
		
	}
	
	
	
	
}

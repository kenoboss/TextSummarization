
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import edu.stanford.nlp.util.CoreMap;

/**
 * 
 * @author Tobias Ziegelmayer
 * @version 1.0.0
 * This class contains all functions for the handling with the Stanford NLP Software
 */
public class StanfordNLP {

	public StanfordNLP () {
		
	}


	/**
	 * This method takes as parameter a string and creates a list of strings.
	 * The method generates a list of all lemmata from the input string.
	 * @param inputText
	 * @return
	 */
	public List<String> stanfordLemmatizer (String inputText){
		List<String> result = new ArrayList<>();
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation document = new Annotation(inputText);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for(CoreMap sentence: sentences) {
			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
				String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
				result.add(lemma);
			}
		}
		return result;
	}


//	public List<List<String>> stanfordSentenceTokenizer (String inputText){
//		List<List<String>> result = new ArrayList<>();
//		Properties props = new Properties();
//		props.put("annotators", "tokenize, ssplit, pos, lemma");
//		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//		Annotation document = new Annotation(inputText);
//		pipeline.annotate(document);
//		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
//		for(CoreMap sentence: sentences) {
//			List<String> sentenceList = new ArrayList<>();
//			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
//				String word = token.get(TextAnnotation.class);
//				sentenceList.add(word);
//			}
//			result.add(sentenceList);
//		}
//		return result;
//	}

	/**
	 * This method takes as parameter a string and creates a list of lists of lists of strings.
	 * The method generates lists with all tokens and all lemmata of the input string.
	 * @param inputText
	 * @return List<List<List<String>>>
	 */
	public List<List<List<String>>> stanfordLemmatizerAndTokenizer (String inputText){
		List<List<List<String>>> result = new ArrayList<>();
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation document = new Annotation(inputText);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		List<List<String>> tokens = new ArrayList<>();
		List<List<String>> lemmata = new ArrayList<>();
		for(CoreMap sentence: sentences) {
			List<String> TokenList = new ArrayList<>();
			List<String> LemmaList = new ArrayList<>();
			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
				TokenList.add(word);
				LemmaList.add(lemma);
			}
			tokens.add(TokenList);
			lemmata.add(LemmaList);
		}
		result.add(tokens);
		result.add(lemmata);
		return result;
	}


//	public static void main (String [] args) {
//		StanfordNLP nlp = new StanfordNLP();
//		List<String> parse = nlp.stanfordLemmatizer("This is a nice test and another test. I guess i don't like pizza.");
//
//	}
}


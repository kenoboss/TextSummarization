
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
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

	
	public List<Tree> stanfordPipeLine (String inputText) {
		
		List<Tree> result = new ArrayList<Tree>();
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
	    Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, parse");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	    String text = inputText;

	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);

	    // run all Annotators on this text
	    pipeline.annotate(document);

	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

	    for(CoreMap sentence: sentences) {
	      // this is the parsetree of the current sentence
	      Tree tree = sentence.get(TreeAnnotation.class);
	      result.add(tree);
	    }
	    return result;
	}



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


	public List<List<String>> stanfordSentenceTokenizer (String inputText){
		List<List<String>> result = new ArrayList<>();
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation document = new Annotation(inputText);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for(CoreMap sentence: sentences) {
			List<String> sentenceList = new ArrayList<>();
			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				sentenceList.add(word);
			}
			result.add(sentenceList);
		}
		return result;
	}

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


	public static void main (String [] args) {
		StanfordNLP nlp = new StanfordNLP();
		List<String> parse = nlp.stanfordLemmatizer("This is a nice test and another test. I guess i don't like pizza.");

	}
}


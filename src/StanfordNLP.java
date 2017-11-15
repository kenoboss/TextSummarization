
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
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
 * @author Kenoboss
 * @version 1.0.0
 * This class contains all functions for the handling with the Stanford NLP Software
 */
public class StanfordNLP {
	
	public static void stanfordPipeLine () {
		 // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
	    Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    Annotation document = new Annotation("This is a nice test.");
	    pipeline.annotate(document);

	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

	    for(CoreMap sentence: sentences) {
	      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	        String word = token.get(TextAnnotation.class);
	        String pos = token.get(PartOfSpeechAnnotation.class);
	        String ne = token.get(NamedEntityTagAnnotation.class);
	        
	        System.out.println("word: " + word + " pos: " + pos + " ne:" + ne);
	      }

	    }

	    
	}
	

	
	public static void main(String [] args) {
		stanfordPipeLine();
	}
}


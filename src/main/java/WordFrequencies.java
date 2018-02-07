import java.util.*;

/**
 * @author Tobias Ziegelmayer
 * @version 1.0.0
 * This class contains functions for calculating the word frequencies of a text for the project TextSummarization
 */
public class WordFrequencies {

    /**
     * This constructor creates an empty instance of WordFrequencies
     */
    public WordFrequencies(){}


    /**
     * This method creates a list of string with stop words from a txt file
     * @return List<String>
     */
    public List<String> getWordList (){
        Helper h = new Helper();
        List<String> tmp = h.readSmallFileLines("src/main/resources/stopwords-en.txt");
        List<String> result = new ArrayList<>();
        for (String entry : tmp){
            String ts = clear_entry(entry);
            result.add(ts);
        }
        return result;
    }

    /**
     * This method takes a string as parameter and creates a trimed string
     * @param entry
     * @return String
     */
    public static String clear_entry (String entry){
        return entry.trim();
    }

    /**
     * This method takes a string as parameter and creates a boolean value.
     * The method tests if the input string is in the stop word list
     * @param lemma
     * @return boolean
     */
    public boolean isLemmaInWordList (String lemma){
        List<String> wordList = this.getWordList();
        if (wordList.contains(lemma)){return true;}
        return false;
    }

    /**
     * This method takes as parameter a list of list of string and creates a
     * list of string as output
     * @param lemmata
     * @return List<String>
     */
    private static List<String> createSimpleLemmaList (List<List<String>> lemmata){
        List<String> result = new ArrayList<>();
        for (List<String> entries : lemmata){
            for (String lemma : entries){
                result.add(lemma);
            }
        }
        return result;
    }

    /**
     * This method takes as parameter a list of list of string and creates a hash map of
     * strings and integer values. The method generates as frequency list of all strings
     * from the input list
     * @param lemmata
     * @return HashMap<String, Integer>
     */
    public HashMap<String, Integer> createFrequencyList (List<List<String>> lemmata){
        HashMap<String, Integer> result = new HashMap<>();
        List<String> lemmaList = createSimpleLemmaList(lemmata);
        for (String lemma : lemmaList){
            if (this.isLemmaInWordList(lemma) == false){
                if (result.containsKey(lemma)){ result.put(lemma, result.get(lemma) + 1); }
                else{ result.put(lemma, 1); }
            }
        }
        result = sortByValues(result);
        return result;
    }

    /**
     * This method takes as parameter a list of list of string and creates a list
     * of string. The method generates a top 10 list of the most frequency words from
     * the input list
     * @param lemmata
     * @return
     */
    public List<String> getTop10 (List<List<String>> lemmata){
        List<String> result = new ArrayList<>();
        HashMap<String, Integer> freqList = this.createFrequencyList(lemmata);
        Set set2 = freqList.entrySet();
        Iterator iterator2 = set2.iterator();
        int counter = 0;
        while(iterator2.hasNext() && counter < 10) {
                Map.Entry me2 = (Map.Entry)iterator2.next();
                String tmp = me2.getKey().toString();
                result.add(tmp);
            counter++;
        }

        return result;
    }

    /**
     * This method takes as parameter a list of list of string and creates a list
     * of string.
     * @param lemmata
     * @return List<String>
     */
    public List<String> getList (List<List<String>> lemmata){
        List<String> result = new ArrayList<>();
        HashMap<String, Integer> freqList = this.createFrequencyList(lemmata);
        Set set2 = freqList.entrySet();
        Iterator iterator2 = set2.iterator();
        while(iterator2.hasNext()) {
            Map.Entry me2 = (Map.Entry)iterator2.next();
            String tmp = me2.getKey().toString();
            result.add(tmp);
        }

        return result;
    }


    /**
     * This method takes as parameter a hashmap and creates a orderd hashmap by values
     * @param map
     * @return HashMap
     */
    private static HashMap sortByValues(HashMap map) {
        List list = new ArrayList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}

import java.util.*;

public class WordFrequencies {


    public WordFrequencies(){}

    public List<String> getWordList (){
        Helper h = new Helper();
        List<String> tmp = h.readSmallFileLines("/data/stopwords-en.txt");
        List<String> result = new ArrayList<>();
        for (String entry : tmp){
            String ts = clear_entry(entry);
            result.add(ts);
        }
        return result;
    }


    public static String clear_entry (String entry){
        return entry.trim();
    }

    public boolean isLemmaInWordList (String lemma){
        List<String> wordList = this.getWordList();
        if (wordList.contains(lemma)){return true;}
        return false;
    }

    public HashMap<String, Integer> createFrequencyList (String inputText){
        HashMap<String, Integer> result = new HashMap<>();
        StanfordNLP sdnlp = new StanfordNLP();
        List<String> lemmata = sdnlp.stanfordLemmatizer(inputText);


        for (String lemma : lemmata){
            if (this.isLemmaInWordList(lemma) == false){
                if (result.containsKey(lemma)){ result.put(lemma, result.get(lemma) + 1); }
                else{ result.put(lemma, 1); }
            }
        }
        result = sortByValues(result);
        return result;
    }

    public List<String> getTop10 (String inputText){
        List<String> result = new ArrayList<>();
        HashMap<String, Integer> freqList = this.createFrequencyList(inputText);
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


    public List<String> getList (String inputText){
        List<String> result = new ArrayList<>();
        HashMap<String, Integer> freqList = this.createFrequencyList(inputText);
        Set set2 = freqList.entrySet();
        Iterator iterator2 = set2.iterator();
        while(iterator2.hasNext()) {
            Map.Entry me2 = (Map.Entry)iterator2.next();
            String tmp = me2.getKey().toString();
            result.add(tmp);
        }

        return result;
    }


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

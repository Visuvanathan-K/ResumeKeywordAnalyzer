import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ResumeJd {

    public static Map<String, Integer> getWordFrequency(List<String> words) {
        Map<String, Integer> frequency = new HashMap<>();
        for (String word : words) {
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }
        return frequency;
    }

    // compare
    public static void compareKeywords(List<String> resumeWords, List<String> jdWords,
            Map<String, Integer> frequency) {

        Set<String> resumeSet = new HashSet<>(resumeWords);  

        Set<String> matched = new HashSet<>();
        Map<String, Integer> missing = new HashMap<>();

        // match and missing keywords
        for (String word : frequency.keySet()) {

            if (resumeSet.contains(word)) {
                matched.add(word);
            } else {
                missing.put(word, frequency.get(word));
            }
        }

        //calculate percentage
        int totalKeywords = frequency.size();
        int matchedCount = matched.size();

        double percentage = 0.0;
        if (totalKeywords > 0) {
            percentage = (double) matchedCount / totalKeywords * 100;
        }

        // sort
        List<Map.Entry<String, Integer>> sortedMissing = new ArrayList<>(missing.entrySet());

        sortedMissing.sort(
                (a, b) -> {
                    int result = b.getValue().compareTo(a.getValue());

                    if (result == 0) {
                        return a.getKey().compareTo(b.getKey());
                    }

                    return result;
                });

        System.out.printf(
                "Match: %.1f%% (%d of %d keywords)%n",
                percentage,
                matchedCount,
                totalKeywords);

        System.out.println("Missing keywords:");

        for (Map.Entry<String, Integer> entry : sortedMissing) {
            System.out.println("  " + entry.getKey() + " (" + entry.getValue() + ")");
        }

    }

    public static void main(String[] args) throws IOException {
        String resume = "C:\\Users\\vishw\\Job\\resume.txt";
        String stopwords = "C:\\Users\\vishw\\OneDrive\\Documents\\StopWord.txt";
        String JD = "C:\\Users\\vishw\\Job\\jd.txt";

        List<String> resumeWords = new ArrayList<>();
        List<String> jdWords = new ArrayList<>();
        Set<String> stopWords = new HashSet<>();

        BufferedReader reBr = new BufferedReader(new FileReader(resume));
        BufferedReader jdBr = new BufferedReader(new FileReader(JD));
        BufferedReader swBr = new BufferedReader(new FileReader(stopwords));

        String word;
        while ((word = swBr.readLine()) != null) {
            if (!word.isBlank()) {
                stopWords.add(word.trim().toLowerCase());
            }
        }

        // resume
        String resumeLine;

        while ((resumeLine = reBr.readLine()) != null) {
            String cleaned = resumeLine.toLowerCase();

            cleaned = cleaned.replaceAll("[^\\sa-zA-Z0-9]", "");

            if (!cleaned.isBlank()) {
                String[] words = cleaned.split("\\s+");

                for (String w : words) {
                    if (!stopWords.contains(w)) {
                        resumeWords.add(w);
                    }
                }
            }
        }

        // JD
        String jdLine;

        while ((jdLine = jdBr.readLine()) != null) {
            String cleaned = jdLine.toLowerCase();

            cleaned = cleaned.replaceAll("[^\\sa-zA-Z0-9]", "");

            if (!cleaned.isBlank()) {
                String[] words = cleaned.split("\\s+");
                for (String w : words) {
                    if (!stopWords.contains(w)) {
                        jdWords.add(w);
                    }
                }
            }
        }

        Map<String, Integer> jdWordFrequency = getWordFrequency(jdWords);

        compareKeywords(resumeWords, jdWords, jdWordFrequency);

    }

}

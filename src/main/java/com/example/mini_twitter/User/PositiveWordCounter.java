package com.example.mini_twitter.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PositiveWordCounter implements Entity {

    private static Set<String> positiveWords = new HashSet<>(Arrays.asList(
            "good", "happy", "positive", "excellent", "awesome", "great"
    ));

    @Override
    public String getUniqueID() {
        return "positivewordcounter";
    }

    @Override
    public String getName() {
        return "Positive Word Counter";
    }

    @Override
    public String getType() {
        return "PositiveWordCounter";
    }

    public static double calculatePositivePercentage(User user) {
        int totalWords = 0;
        int positiveWordCount = 0;

        // Iterate through the user's tweets and count positive words
        for (String tweet : user.getNewsFeed()) {
            List<String> words = Arrays.asList(tweet.toLowerCase().split("\\s+"));
            totalWords += words.size();
            positiveWordCount += countPositiveWords(words);
        }

        // Calculate the percentage of positive words
        return totalWords > 0 ? ((double) positiveWordCount / totalWords) * 100 : 0;
    }

    private static int countPositiveWords(List<String> words) {
        int count = 0;

        // Check for positive words in the list
        for (String word : words) {
            if (positiveWords.contains(word)) {
                count++;
            }
        }
        return count;
    }
}

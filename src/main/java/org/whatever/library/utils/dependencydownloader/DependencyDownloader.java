package org.whatever.library.utils.dependencydownloader;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DependencyDownloader implements Runnable {

    public static void main(String[] args) {
        DependencyDownloader downloader = new DependencyDownloader();
        System.out.println(downloader.run("jsoup"));
    }

    private final String siteurl = "https://mvnrepository.com";
    private final String search = "/search?q=";

    private final String artifactRegex = ".*(\\/artifact\\/[a-z.-]+\\/[a-z.-]+).*";
    private final String resultsRegex = ".*Found\\s\\<b\\>(\\d+)\\<\\/b\\>\\sresults.*";


    private String extractArtifactFromUrl(String url) {
        Pattern pattern = Pattern.compile("\\/artifact\\/[a-z.]+\\/([a-z-.]+)$");
        Matcher matcher = pattern.matcher(url);

        if (matcher.matches()) {
            return matcher.group(1);
        } else return "";
    }

    private String getDependency(String url) {
        String regex = ".*\\/artifact\\/([a-z.]+)\\/([a-z-]+)\\/([0-9.A-Za-z-]+)";
        Pattern pattern = Pattern.compile(regex);

        StringBuilder dependency = new StringBuilder();
        Matcher matcher = pattern.matcher(url);

        if (matcher.matches()) {
            String group = matcher.group(1);
            String name = matcher.group(2);
            String version = matcher.group(3);
            return dependency.append("implementation '").append(group).append(":").append(name).append(":").append(version).append("'").toString();
        }
        return "";
    }

    private String getLatestVersionUrl(String html, String url) {
        StringBuilder result = new StringBuilder();
        result.append(siteurl);
        result.append(url);

        StringBuilder regex = new StringBuilder();
        regex.append(".*\"").append(extractArtifactFromUrl(url)).append("\\/([0-9.a-zA-Z]+).*");

        TextExtractor<String> latestVersionExtraction = matcher -> {
            StringBuilder stringBuilder = new StringBuilder();
            return stringBuilder.append("/").append(matcher.group(1)).toString();
        };

        result.append(latestVersionExtraction.process(html, regex.toString()));

        return result.toString();
    }

    private String getOuterHtml(String str) throws IOException {
        return Jsoup.connect(str).get().outerHtml();
    }

    private String getSearchResult(String querry) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(siteurl).append(search).append(querry);
        return getOuterHtml(stringBuilder.toString());
    }

    private String getFirstResultHtml(String html) throws IOException {
        String artifact = getFirstResultArtifact(html);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(siteurl).append(artifact);
        return getOuterHtml(stringBuilder.toString());
    }

    private String getFirstResultArtifact(String html) {
        TextExtractor<String> artifactExtraction = matcher -> matcher.group(1);
        return artifactExtraction.process(html, artifactRegex);
    }

    private int getSearchResults(String html) {
        TextExtractor<Integer> searchResultsNumber = matcher -> Integer.valueOf(matcher.group(1));
        return searchResultsNumber.process(html, resultsRegex);
    }

    public void printDependencies(List<String> dependencies) {
        dependencies.forEach(s -> System.out.println(s));
    }

    @Override
    public String run(String arg) {
        try {
            String searchResult = getSearchResult(arg);
            String firstResult = getFirstResultHtml(searchResult);
            String version = getLatestVersionUrl(firstResult, getFirstResultArtifact(searchResult));
            return getDependency(version);
        } catch (IOException e) {
            return "";
        }
    }

    @Override
    public List<String> run(String... args) {
        List<String> results = new ArrayList<>();
        for (String s : args) {
            results.add(run(s));
        }
        return results;
    }


    interface TextExtractor<E> {
        default E process(String text, String regex) {
            String[] lines = text.split("\n");
            Pattern pattern = Pattern.compile(regex);

            for (String s : lines) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.matches()) {
                    return onPatternFound(matcher);
                }
            }
            return null;
        }

        E onPatternFound(Matcher matcher);
    }


}

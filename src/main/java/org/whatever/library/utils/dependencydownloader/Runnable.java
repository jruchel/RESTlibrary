package org.whatever.library.utils.dependencydownloader;

import java.util.List;

public interface Runnable {

    String run(String arg);

    List<String> run(String... args);

}

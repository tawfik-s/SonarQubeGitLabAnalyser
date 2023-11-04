package com.technoverse.platformManager.utils;

import org.sonarsource.scanner.api.EmbeddedScanner;
import org.sonarsource.scanner.api.StdOutLogOutput;

import java.util.HashMap;
import java.util.Map;

public class SonarAnalyzerUtil {
    public static void runSonarAnalyzer(String projectPath, String sonarUrl, String projectKey, String version, String token) {
        EmbeddedScanner scanner = EmbeddedScanner.create(projectKey, version, new StdOutLogOutput())
                .setGlobalProperty("sonar.host.url", sonarUrl)
                .setGlobalProperty("sonar.login", token);

        Map<String, String> properties = new HashMap<>();
        properties.put("sonar.projectKey", projectKey);
        properties.put("sonar.projectBaseDir", projectPath);
        properties.put("sonar.sources", projectPath + "/src/main/java");
        properties.put("sonar.java.binaries", projectPath + "/target/classes");
        scanner.start();
        scanner.execute(properties);
    }
}

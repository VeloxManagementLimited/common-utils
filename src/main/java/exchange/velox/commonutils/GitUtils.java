package exchange.velox.commonutils;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Map;

public class GitUtils {
    private static Logger log = LogManager.getLogger(GitUtils.class);

    public static String getAppVersion() {
        try {
            try (InputStream inputStream = GitUtils.class.getClassLoader().getResourceAsStream("git.properties")) {
                String gitString = IOUtils.toString(inputStream, CommonConstants.UTF_8);
                Map<String, Object> gitInfo = JSONUtils.stringToMap(gitString);
                return String.valueOf(gitInfo.get("git.build.version")) + "-" + String
                            .valueOf(gitInfo.get("git.commit.id.abbrev"));
            }
        } catch (Exception e ) {
            log.warn("Failed to read git.properties from classpath", e);
            return "N/A";
        }
    }
}

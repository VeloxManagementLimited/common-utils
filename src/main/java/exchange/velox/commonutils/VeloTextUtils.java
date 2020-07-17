package exchange.velox.commonutils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class VeloTextUtils {
    private static Logger log = LogManager.getLogger(VeloTextUtils.class);
    private static final JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();

    private static Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String slugify(String originalFileName) {
        if (StringUtils.isBlank(originalFileName)) {
            return StringUtils.EMPTY;
        }
        String fileName = FilenameUtils.getBaseName(originalFileName);
        String extension = FilenameUtils.getExtension(originalFileName);
        String noWhitespace = WHITESPACE.matcher(fileName).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        if (StringUtils.isBlank(slug)) {
            slug = UUID.randomUUID().toString();
        }
        if (StringUtils.isNotBlank(extension)) {
            return slug + "." + extension;
        }
        return slug;
    }

    /**
     * Return a distance between the 2.
     *
     * @param s1
     * @param s2
     * @return 0 <= x <= 1
     */
    public static Double howSimilar(String s1, String s2) {
        s1 = StringUtils.trimToEmpty(s1);
        s2 = StringUtils.trimToEmpty(s2);
        if (s1.equalsIgnoreCase(s2)) {
            return Double.valueOf("1");
        }
        s1 = slugify(StringUtils.normalizeSpace(s1)).toLowerCase();
        s2 = slugify(StringUtils.normalizeSpace(s2)).toLowerCase();

        Double d1 = calculateJaroWinklerDistance(s1, s2);

        s1 = sortWordsInString(StringUtils.replaceAll(s1.trim(), "-", " "));
        s2 = sortWordsInString(StringUtils.replaceAll(s2.trim(), "-", " "));
        Double d2 = calculateJaroWinklerDistance(s1, s2);

        return Math.max(d1, d2);
    }

    public static String sortWordsInString(String s1) {
        String[] parts = s1.split("\\s+");
        List<String> l = new ArrayList<>(parts.length);
        for (String part : parts) {
            if (StringUtils.isNotBlank(part)) {
                l.add(part.trim());
            }
        }
        Collections.sort(l);
        return StringUtils.join(l, " ");
    }


    private static Double calculateJaroWinklerDistance(String s1, String s2) {
        return jaroWinklerDistance.apply(s1, s2);
    }

    public static String getMD5Hash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    private static String bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash).toLowerCase();
    }
}

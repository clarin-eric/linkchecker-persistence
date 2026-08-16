package eu.clarin.linkchecker.persistence.utils;

import lombok.Data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class UrlValidator {

    private final static List<String> VALID_PROTOCOLS = Arrays.asList("http", "https", "ftp");

    public static synchronized ValidationResult validate(String urlString) {

        try {

            URL url = new URL(urlString);

            String groupKey = url.getHost();

            if (groupKey == null || groupKey.length() < 3 || groupKey.contains("localhost") || groupKey.contains("127.0.0")) {
                return new ValidationResult(false, groupKey, "invalid host");
            }

            if (!VALID_PROTOCOLS.contains(url.getProtocol())) {
                return new ValidationResult(false, groupKey, "invalid protocol");
            }

            if(groupKey.equals("hdl.handle.net")){ // in case of handles we group by handle prefix

                String[] parts = urlString.split("/");

                if(parts.length > 3){
                    return new ValidationResult(true, parts[3], "ok");
                }
            }

            return new ValidationResult(true, groupKey, "ok");
        }
        catch (MalformedURLException ex) {
            return new ValidationResult(false, null, "malformed URL");
        }
    }

    public record ValidationResult(boolean isValid, String groupKey, String message) {

    }
}

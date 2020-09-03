package exchange.velox.commonutils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JSONUtils {
    private static Logger log = LogManager.getLogger(JSONUtils.class);
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

    }
    private static ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    public static Map<String, Object> stringToMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        } catch (IOException e) {
            log.error("Could not parse the input json to map", e);
            throw new RuntimeException("Could not parse the input json to map", e);
        }
    }

    public static String objectToString(Object object) {
        try {
            return objectWriter.writeValueAsString(object);
        } catch (IOException e) {
            log.error("Could not parse the object to string", e);
            throw new RuntimeException("Could not parse object to string");
        }
    }

    public static String objectToStringIgnoreNull(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            log.error("Could not parse the object to string", e);
            throw new RuntimeException("Could not parse object to string");
        }
    }

    public static String objectToStringWithView(Object object, Class<?> classView) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        try {
            return mapper.writerWithView(classView).writeValueAsString(object);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    public static Object stringToObject(String string, Class<?> clazz)  {
        try {
            return objectMapper.readValue(string, clazz);
        } catch (Exception e) {
            log.error("Could not parse the input json to " + clazz.getSimpleName(), e);
            throw new RuntimeException("Could not parse the input json to object", e);

        }
    }
}

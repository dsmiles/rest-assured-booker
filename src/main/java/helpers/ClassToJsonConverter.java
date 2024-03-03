package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * Utility class for converting a Java class to JSON format with an extra field.
 */
public class ClassToJsonConverter {

    /**
     * Converts a Java class to JSON format and adds an extra field to the JSON object.
     *
     * @param object          The Java object to convert to JSON.
     * @param extraFieldName  The name of the extra field to add to the JSON object.
     * @param extraFieldValue The value of the extra field to add to the JSON object.
     * @return A JSON string representing the Java object with the extra field.
     */
    public static String convertClassToJsonWithExtraField(Object object, String extraFieldName, Object extraFieldValue) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.convertValue(object, ObjectNode.class);
        objectNode.put(extraFieldName, extraFieldValue.toString()); // Convert the extraFieldValue to String
        try {
            return objectMapper.writeValueAsString(objectNode);
        } catch (Exception e) {
            System.out.println("Error converting class to JSON with extra field: " + e.getMessage());
            return null;
        }
    }
}

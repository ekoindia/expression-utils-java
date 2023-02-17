package in.eko.exprutils;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.Arrays;

/**
 * This class consists exclusively of static utility methods for processing JSON objects.
 * @author Kumar Abhishek (https://abhi.page/)
 */
public class JsonObj {

    // Suppress default constructor for noninstantiability
    private JsonObj() {
        throw new AssertionError();
    }


    /**
     * Retrieve deep/nested value from a JSON Object.
     * @param obj String serialized JSON object to retrieve value from.
     * @param key The key for fetching value from the object. Deep/nested path is represented by dot notation.
     * @param default_value Default value to return if the key was not found or in case of any error in parsing the object.
     * @return The value
     */
    public static Object get(String obj, String key, String default_value) {
        if (obj == null || key == null) {
            return default_value;
        }

        try {
            // Parse JSON Object from String
            JSONObject dataJson = new JSONObject(obj);

            // Extract deep value from the object
            return get(dataJson, key, default_value);
        } catch (JSONException e) {
            return default_value;
        }
    }


    /**
     * Retrieve deep/nested value from a JSON Object.
     * @param obj The JSONObject to retrieve value from.
     * @param key The key for fetching value from the object. Deep/nested path is represented by dot notation.
     * @param default_value Default value to return if the key was not found or in case of any error in parsing the object.
     * @return The value
     */
    public static Object get(JSONObject obj, String key, String default_value) {
        if (obj == null || key == null) {
            return default_value;
        }

        Object val = default_value;

        if (key.contains(".")) {
            // For nested/deep keys...

            // Split key into sub-keys (using dot notation)
            String keys[] = key.split("\\.", 20);

            for (String k: keys) {
                try {
                    val = obj.get(k);
                    if (val instanceof JSONObject) {
                        obj = (JSONObject) val;
                    }
                } catch (JSONException e)  {
                    return default_value;
                }
            }
        } else {
            // For simple (non-nested) keys...
            try {
                val = obj.get(key);
            } catch (JSONException e) {}
        }

        return val;
    }


    /**
     * Set a key-value pair deep within a JSON Object.
     * @param obj String serialized JSON object to insert value into.
     * @param key The key for inserting value into the object. Deep/nested path is
     *            represented by dot notation. Unavailable object path is created.
     * @param value An object which is the value. It should be of one of these types:
     *              Boolean, Double, Integer, JSONArray, JSONObject, Long, String,
     *              or the JSONObject.NULL object.
     * @return The String serialized JSON object with the key-value pair inserted.
     */
    public static String set(String obj, String key, Object value) {
        if (obj == null || key == null) {
            return obj;
        }

        try {
            // Parse JSON Object from String
            JSONObject objJson = new JSONObject(obj);

            // Extract deep value from the object
            return set(objJson, key, value).toString();
        } catch (JSONException e) {
            return obj;
            // throw new IllegalArgumentException("Invalid expr: not a valid JSON");
        }
    }

    /**
     * Private generic method to set a key-value pair deep within a JSON Object.
     * @param obj JSON object to insert value into.
     * @param key The key for inserting value into the object. Deep/nested path is
     *            represented by dot notation. Unavailable object path is created.
     * @param value The value to set.
     * @return The String serialized JSON object with the key-value pair inserted.
     */
    private static JSONObject set(JSONObject obj, String key, Object value) {
        if (obj == null || key == null) {
            return obj;
        }

        if (key.contains(".")) {
            // For nested/deep keys...

            // Split key into sub-keys (using dot notation)
            String keys[] = key.split("\\.", 20);

            // reach the final nested object to set the key/value...
            Object val;
            JSONObject subObj = obj;
            JSONObject newObj;

            for (int j = 0; j < keys.length - 1; j++) {
                if (subObj.has(keys[j])) {
                    val = subObj.get(keys[j]);
                    if (val instanceof JSONObject) {
                        subObj = (JSONObject) val;
                    } else {
                        return obj;
                    }
                } else {
                    newObj = new JSONObject();
                    subObj.put(keys[j], newObj);
                    subObj = newObj;
                }
            }

            // Set the final key/value
            subObj.put(keys[keys.length-1], value);
        } else {
            // For simple (non-nested) keys...
            try {
                obj.put(key, value);
            } catch (JSONException e) {}
        }

        return obj;
    }
}

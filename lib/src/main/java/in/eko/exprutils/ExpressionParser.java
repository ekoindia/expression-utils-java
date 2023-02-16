/**
 * A Java library to parse and execute expressions as nested JSON Arrays.
 */
package in.eko.exprutils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.eko.exprutils.interfaces.OperatorFunction;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class consists exclusively of static methods that operate on an expression represented as JSON Arrays.
 * @author Kumar Abhishek (https://abhi.page/)
 */
public class ExpressionParser {
    private static Map<String, OperatorFunction> operatorMap;

    static {
        // Initialize the operator map
        operatorMap = new HashMap<>();
        operatorMap.put("+", ExpressionParser::add);
        operatorMap.put("-", ExpressionParser::subtract);
        operatorMap.put("*", ExpressionParser::multiply);
        operatorMap.put("/", ExpressionParser::divide);

        operatorMap.put("CONCAT", ExpressionParser::concat);

        operatorMap.put("GET", ExpressionParser::objDeepGet);
        operatorMap.put("SET", ExpressionParser::objDeepSet);

        operatorMap.put("SHA256", ExpressionParser::sha256);
        operatorMap.put("SHA512", ExpressionParser::sha512);
        operatorMap.put("MD5", ExpressionParser::md5);

        operatorMap.put("JWT", ExpressionParser::generateJwt);
    }


    // Suppress default constructor for noninstantiability
    private ExpressionParser() {
        throw new AssertionError();
    }


    /**
     * Check if an operator is supported by the expression parser.
     * @param operator The operator
     * @return True if the operator is supported
     */
    public static boolean isValidOperator(String operator) {
        return operatorMap.containsKey(operator);
    }


    /**
     * Interpolate/replace values of dollar-curly-brace-wrapped variables into a string.
     * @param expr The string where variables are to be replaced. Eg: "Hello, ${name}"
     * @param data The map of variable-value pairs
     * @return The interpolated string with the variables replaced
     */
    public static String interpolate(String expr, Map<String, String> data) {
        Pattern pattern = Pattern.compile("\\$\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(expr);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String newString = data.getOrDefault(matcher.group(1), matcher.group(0));
            matcher.appendReplacement(sb, newString);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * Execute an expression provided as a JSON array.
     * The first element is always an operator/function.
     * The remaining elements of the array are the operands/parameters or another nested expression as a JSON array.
     * @param expr The expression to execute.
     * @return The calculated value of the expression
     * @throws IllegalArgumentException If `expr` is not a valid JSON Array
     */
    public static Object parseExpression(String expr) throws JSONException {
        try {
            JSONArray exprJson = new JSONArray(expr);
            return exec(exprJson);
        } catch (JSONException e) {
            throw new IllegalArgumentException("Invalid expr: not a valid JSON");
        }
    }

    /**
     * Private recursive function to solve the expression represented as nested JSON Arrays
     * @param expr The expression to solve as JSONArray
     * @return The computed value of the expression
     * @throws IllegalArgumentException if the operator is not valid
     */
    private static Object exec(JSONArray expr) throws JSONException {
        // Get the operator
        String operator = expr.getString(0);

        // Get the operands
        Object operand1 = null;
        Object operand2 = null;
        Object operand3 = null;

        try {
            operand1 = parseOperand(expr.get(1));
            operand2 = parseOperand(expr.get(2));
            operand3 = parseOperand(expr.get(3));
        } catch (JSONException e) {}

        // Get the operator function from the map
        OperatorFunction operatorFunction = operatorMap.get(operator);
        if (operatorFunction == null) {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        }

        // Evaluate the expression using the operator function
        return operatorFunction.apply(operand1, operand2, operand3);
    }

    /**
     * Recursively return the operand.
     * If the operand is another nested expression,
     * it executes the expression before returning its value
     * @param operand The operand or a nested expression as JSON Array
     * @return The value of the operand
     * @throws JSONException
     */
    private static Object parseOperand(Object operand) throws JSONException {
        if (operand instanceof JSONArray) {
            // If the operand is a nested expression, parse it recursively
            return exec((JSONArray) operand);
        } else {
            // Otherwise, get the operand
            return operand;
        }
    }

    private static double obj2dbl(Object value) {
        try {
            return value instanceof Double ? (Double) value : new Double(value.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    private static Object add(Object operand1, Object operand2, Object operand3) {
        return (Object) (obj2dbl(operand1) + obj2dbl(operand2));
    }

    private static Object subtract(Object operand1, Object operand2, Object operand3) {
        return (Object) (obj2dbl(operand1) - obj2dbl(operand2));
    }

    private static Object multiply(Object operand1, Object operand2, Object operand3) {
        return (Object) (obj2dbl(operand1) * obj2dbl(operand2));
    }

    private static Object divide(Object operand1, Object operand2, Object operand3) {
        return (Object) (obj2dbl(operand1) / obj2dbl(operand2));
    }

    private static Object concat(Object operand1, Object operand2, Object operand3) {
        return (Object) (operand1.toString() + operand2.toString());
    }

    /**
     * Returns nested value from a Json object identified by the key.
     * @param operand1 String serialized Json Object
     * @param operand2 key (using dot notation for nested values)
     * @param operand3 null
     * @return Nested value from the object
     */
    private static Object objDeepGet(Object operand1, Object operand2, Object operand3) {
        return (Object) JsonObj.get(operand1.toString(), operand2.toString(), "");
    }

    /**
     * Returns nested value from a Json object identified by the key.
     * @param operand1 String serialized Json Object
     * @param operand2 key (using dot notation for nested values)
     * @param operand3 value
     * @return Nested value from the object
     */
    private static Object objDeepSet(Object operand1, Object operand2, Object operand3) {
        return (Object) JsonObj.set(operand1.toString(), operand2.toString(), operand3.toString());
    }


    private static Object sha256(Object operand1, Object operand2, Object operand3) {
        return (Object) Hash.sha256(operand1.toString());
    }

    private static Object sha512(Object operand1, Object operand2, Object operand3) {
        return (Object) Hash.sha512(operand1.toString());
    }

    private static Object md5(Object operand1, Object operand2, Object operand3) {
        return (Object) Hash.md5(operand1.toString());
    }

    private static Object generateJwt(Object operand1, Object operand2, Object operand3) {
        String opt = operand3.toString();
        String data = JsonObj.get(opt, "claim", "").toString();
        String issuer = JsonObj.get(opt, "issuer", "").toString();
        String audience = JsonObj.get(opt, "audience", "").toString();
        String subject = JsonObj.get(opt, "subject", "").toString();
        String jwtid = JsonObj.get(opt, "jwtid", "").toString();
        int expiresInSeconds = new Integer(JsonObj.get(opt, "expiresInSeconds", "0").toString());

        return (Object) JWT.generate(operand1.toString(),   // secretKey
                operand2.toString(),                        // Algorithm
                data,
                issuer,
                audience,
                subject,
                jwtid,
                expiresInSeconds);
    }
}

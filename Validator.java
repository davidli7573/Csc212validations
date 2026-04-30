import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Validator is an abstract superclass that provides common
 * utility methods used for validating input data across the system.
 *
 *
 * The goal of this class is to reduce code duplication and provide
 * reusable validation logic for different types of data.
 *
 * @author David Li, Masudul Shafi, Nadim Siddique, Navardo Williams
 */
public final class Validator {

    /**
     * Default constructor for Validator.
     */
    private Validator() {
    }

    /**
     * @param val    The value entered by the user.
     * @param minVal The lower bound of all valid input options available.
     * @param maxVal The upper bound of all valid input options available.
     * @return true if the value entered by the user is a valid entry, otherwise
     *         throws an IOException.
     * @throws IOException
     * @author Navardo Williams
     */
    public static boolean isValidInput(String val, int minVal, int maxVal) throws IOException {
        StringBuffer message = new StringBuffer("");
        if (!Validator.isInt(val))
            message.append("Must enter a numerical value!\n");
        else {
            try {
                int r = Integer.parseInt(val);
                if (!(r >= minVal && r <= maxVal)) {
                    message.append("Input value out of range!\n");
                }
            } catch (NumberFormatException e) {
                message.append("input value out of range!\n");
            }
        }
        if (!message.isEmpty())
            throw new IOException(message.append(String.format((maxVal - minVal) <= 1 ? "Please select %d or %d: "
                    : "Please select a choice between %d and %d: ",
                    minVal, maxVal)).toString());
        return true;
    }

    /**
     * returns whether a string's length is within a specified Range
     *
     * @param minLen The minimum length a string can be.
     * @param maxLen The maximum length a string can be.
     * @param str    The string to be evaluated.
     * @return true if a given string is of valid length. else return false.
     * @author Navardo Williams
     */
    public static boolean validStrLen(int minLen, int maxLen, String str) {
        return (minLen <= str.length() && maxLen >= str.length()) ? true : false;
    }

    /**
     * @param str String passed for meta data extractraction.
     * @return A map containing the meta data of string passed.
     * @author Navardo Williams
     */
    public static Map<String, Boolean> extractStrMeta(String str) {
        Map<String, Boolean> strHm = new HashMap<>(Map.of(
                "hasLoCase", false,
                "hasUpCase", false,
                "hasSpecChar", false,
                "hasNum", false));
        for (char c : str.toCharArray()) {
            if (!strHm.get("hasUpCase") && Character.isUpperCase(c))
                strHm.put("hasUpCase", true);
            if (!strHm.get("hasLoCase") && Character.isLowerCase(c))
                strHm.put("hasLoCase", true);
            int asciiVal = (int) c;
            if (!strHm.get("hasSpecChar")
                    && ((asciiVal >= 32 && asciiVal <= 47) || (asciiVal >= 58 && asciiVal <= 64) ||
                            (asciiVal >= 91 && asciiVal <= 96) || (asciiVal >= 123 && asciiVal <= 126)))
                strHm.put("hasSpecChar", true);
            if (!strHm.get("hasNum") && Character.isDigit(c))
                strHm.put("hasNum", true);
        }
        return strHm;
    }

    /**
     * Checks whether a given string is null, empty, or contains only whitespace.
     *
     * @param value the string to check
     * @return true if the value is null or blank, false otherwise
     * @author David Li
     */
    protected static boolean isEmpty(String value) {
        // return text == null || text.trim().isEmpty();
        return true;
    }

    /**
     * Checks whether a given string can be parsed as an integer.
     *
     * @param value the string to check
     * @return true if the value represents a valid integer, false otherwise
     * @author David Li
     */
    protected static boolean isInt(String value) {
        if (value.isEmpty()) {
            return false;
        }
        String text = value.trim();
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether a string contains invalid or disallowed characters.
     * Allowed characters may include letters, numbers, spaces, and basic
     * punctuation.
     *
     * @param value the string to validate
     * @return true if invalid characters are found, false otherwise
     * @author David Li
     */
    protected static boolean hasInvalidCharacters(String value) {
        if (value == null) {
            return true;
        }
        return !value.matches("[a-zA-Z0-9 ,./\\-?!'()]+");
    }

}

/**
 * CsvValidator is responsible for validating CSV files uploaded by the user.
 * This includes checking file name format, header correctness,
 * individual records, and overall year consistency.
 *
 * @author David Li, Masudul Shafi, Nadim Siddique, Navardo Williams
 */
class CsvValidator {
    /**
     * Default constructor for CsvValidator.
     */
    private CsvValidator() {
    }

    /**
     * Validates the entire file by checking file name, header,
     * records, and year consistency.
     *
     * @param fileName      the name of the CSV file
     * @param rows          the contents of the CSV file where each row is split
     *                      into fields
     * @param existingYears list of years already stored for the user
     * @return true if the file passes all validation checks, false otherwise
     * @author Nadim Siddique
     */
    public boolean validateFile(String fileName, List<String[]> rows, List<Integer> existingYears) {
        if (!validateFileName(fileName)) {
            return false;
        }

        if (rows == null || rows.size() < 2) {
            return false;
        }

        if (!validateHeader(rows.get(0))) {
            return false;
        }

        for (int i = 1; i < rows.size(); i++) {
            if (!validateRecord(rows.get(i))) {
                return false;
            }
        }

        if (!validateYearConsistency(rows)) {
            return false;
        }

        int year = Integer.parseInt(fileName.substring(0, 4));
        return validateUniqueFile(year, existingYears);
    }

    /**
     * Validates that the file name follows the required format YYYY.csv.
     *
     * @param fileName the name of the file
     * @return true if the file name is valid, false otherwise
     * @author Nadim Siddique
     */
    public boolean validateFileName(String fileName) {
        if (fileName == null || fileName.length() != 8) {
            return false;
        }

        if (!fileName.endsWith(".csv")) {
            return false;
        }

        String yearPart = fileName.substring(0, 4);

        for (int i = 0; i < yearPart.length(); i++) {
            if (!Character.isDigit(yearPart.charAt(i))) {
                return false;
            }
        }

        int year = Integer.parseInt(yearPart);
        return year > 0;
    }

    /**
     * Validates that the header row matches the required format:
     * Date, Category, Amount
     *
     * @param header the first row of the CSV file
     * @return true if the header is correct, false otherwise
     * @author Nadim Siddique
     */
    public boolean validateHeader(String[] header) {
        if (header == null || header.length != 3) {
            return false;
        }

        return header[0].trim().equals("Date")
                && header[1].trim().equals("Category")
                && header[2].trim().equals("Amount");
    }

    /**
     * Validates a single record (row) in the CSV file.
     * A valid record must contain exactly three values:
     * date, category, and amount.
     *
     * @param record the row to validate
     * @return true if the record is valid, false otherwise
     * @author Nadim Siddique
     *
     */
    public boolean validateRecord(String[] record) {
        if (record == null || record.length != 3) {
            return false;
        }

        String date = record[0].trim();
        String category = record[1].trim();
        String amount = record[2].trim();

        if (date.isEmpty() || category.isEmpty() || amount.isEmpty()) {
            return false;
        }

        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }

        try {
            Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * Validates that all records in the file belong to the same year.
     *
     * @param rows the contents of the CSV file
     * @return true if all dates are from the same year, false otherwise
     * @author Nadim Siddique
     */
    public boolean validateYearConsistency(List<String[]> rows) {
        if (rows == null || rows.size() < 2) {
            return false;
        }

        String firstDate = rows.get(1)[0].trim();

        if (firstDate.length() < 4) {
            return false;
        }

        String expectedYear = firstDate.substring(0, 4);

        for (int i = 1; i < rows.size(); i++) {
            String date = rows.get(i)[0].trim();

            if (date.length() < 4 || !date.substring(0, 4).equals(expectedYear)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validates that the year being uploaded does not already exist
     * for the current user.
     *
     * @param year          the year extracted from the file
     * @param existingYears list of years already stored for the user
     * @return true if the year is unique, false otherwise
     * @author Nadim Siddique
     */
    public boolean validateUniqueFile(int year, List<Integer> existingYears) {
        if (existingYears == null) {
            return true;
        }

        return !existingYears.contains(year);
    }

    /**
     * Validates whether a given date field is in the correct format and represents
     * a valid calendar date.
     * This method checks for proper formatting (e.g., YYYY-DD-MM) and ensures the
     * date exists
     * (e.g., rejects invalid dates like February 30 or 12/32/2026).
     *
     * @param date the date string to validate
     * @return true if the date is valid, false otherwise
     * @author Masudul Shafi
     */

    boolean validateDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return false;
        }

        date = date.trim();

        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }

        String[] parts = date.split("-");
        int year  = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day   = Integer.parseInt(parts[2]);

        if (year < 1000 || year > 9999) return false;
        if (month < 1 || month > 12)    return false;
        if (day < 1)                    return false;

        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // check divisible by 4, except centuries unless also div by 400
        boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
        if (isLeapYear) {
            daysInMonth[1] = 29;
        }

        if (day > daysInMonth[month - 1]) return false;

        return true;
    }

    /**
     * Validates whether a given category is acceptable.
     * This method ensures the category is not null, not empty, and exists within an
     * allowed set
     * of values.
     *
     * @param category the category string to validate
     * @return true if the category is valid, false otherwise
     * @author Masudul Shafi
     */
    boolean validateCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return false;
        }

        String[] validCategories = {
            "Compensation", "Allowance", "Investments", 
            "Other", "Home", "Utilities", "Food", "Appearance",
            "Work", "Education", "Transporation",
            "Entertainment", "Professional Services"
        };

        String trimmed = category.trim();
        for (String valid : validCategories) {
            if (valid.equalsIgnoreCase(trimmed)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Validates whether a given amount is in a correct numeric format and within
     * acceptable bounds.
     * This method checks that the amount is a valid number,
     * is not negative (if disallowed), and falls within any defined limits.
     *
     * @param amount the value as a int
     * @return true if the value is valid, false otherwise
     * @author Masudul Shafi
     */

    public boolean validateAmount(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            return false;
        }

        String trimmed = amount.trim();

        if (!Validator.isInt(trimmed)) {
            return false;
        }

        // check for overflow before the parsing
        try {
            int value = Integer.parseInt(trimmed);
            if (value < 0) return false;
        } catch (NumberFormatException e) {
            // number was valid digits but too large for int (overflow)
            return false;
        }

        return true;
    }

    /**
     * Validates that a given value matches the expected data type.
     * This method checks whether the input can be correctly interpreted as the
     * required type
     * (e.g., integer, string, etc).
     *
     * @param value        the value to validate
     * @param expectedType the expected data type (e.g., "int", "double", "string")
     * @return true if the value matches the expected data type, false otherwise
     * @author Masudul Shafi
     */
    public boolean validateDataType(String value, String expectedType) {
    	if (value == null || expectedType == null) {
    		return false;
    	}
    	
    	switch (expectedType.toLowerCase().trim()) {
    		case "int":
    			return Validator.isInt(value.trim());
    		case "string": 
    			return !Validator.isEmpty(value);
    		default:
    			return false;
    	}
    }

    /**
     * Validates that the file format is correct (a YYYY.csv file).
     *
     * @param fileName the name of the file
     * @return true if the file format is valid, false otherwise
     * @author David Li
     */
    public boolean checkFileFormat(String fileName) {// removing this and changing it
        if (Validator.isEmpty(fileName)) {
            return false;
        }
        if (!fileName.endsWith(".csv")) {
            return false;
        }
        String year = fileName.substring(0, fileName.length() - 4);
        if (year.length() != 4) {
            return false;
        }
        if (!Validator.isInt(year)) {
            return false;
        }
        return true;
    }

    /**
     * Checks for missing fields in each record.
     *
     * @param rows the contents of the CSV file
     * @return true if all records are filled in, false otherwise
     * @author David Li
     */
    public boolean checkMissingFields(List<String[]> rows) {
        if (rows == null) {
            return false;
        }
        for (String[] row : rows) {
            if (row == null) {
                return false;
            }
            for (String field : row) {
                if (Validator.isEmpty(field)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks for blank lines in the file.
     *
     * @param rows the contents of the CSV file
     * @return true if no blank lines are found, false otherwise
     * @author David Li
     */
    public boolean checkBlankLines(List<String[]> rows) {
        if (rows == null) {
            return false;
        }
        for (String[] row : rows) {
            if (row == null) {
                return false;
            }
            boolean blank = true;
            for (String field : row) {
                if (!Validator.isEmpty(field)) {
                    blank = false;
                    break;
                }
            }
            if (blank) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ensures that each row contains the correct number of columns.
     *
     * @param rows                the contents of the CSV file
     * @param expectedColumnCount the required number of columns
     * @return true if all rows have the correct number of columns, false otherwise
     * @author David Li
     */
    public boolean checkColumnCount(List<String[]> rows, int expectedColumnCount) {
        if (rows == null) {
            return false;
        }
        if (expectedColumnCount <= 0) {
            return false;
        }
        for (String[] row : rows) {
            if (row == null) {
                return false;
            }
            if (row.length != expectedColumnCount) {
                return false;
            }
        }
        return true;
    }
}

/**
 * UserValidator
 * Utility class UserValidator for validating user input.
 *
 * @author David Li, Masudul Shafi, Nadim Siddique, Navardo Williams
 */
class UserValidator {
    /**
     * Default constructor for Validator.
     */
    private UserValidator() {
    }

    /**
     * functioon to validate usernames
     *
     * @param userName The username to be validated
     * @param minLen   The minimum length the username can be.
     * @param maxLen   The Maximum length the username can be.
     * @param LowCase  Set true if userName must contain atleast one lowercase
     *                 character, false otherwise.
     * @param upCase   Set true if userName must contain atleast one uppercase
     *                 character, false otherwise.
     * @param specChar set true if userName must contain atleast one special
     *                 character, false otherwise.
     * @param num      ser true if userName must contain atleast one numerical
     *                 value, false otherwise.
     * @return true if all constraints are satisfied.
     * @throws IOException If all constraints aren't satisfied.
     * @author Navardo Williams
     */
    public static boolean validateUserName(String userName, int minLen, int maxLen, boolean LowCase,
            boolean upCase,
            boolean specChar,
            boolean num) throws IOException {

        String type = "username";
        StringBuilder message = new StringBuilder("");
        if (!Validator.validStrLen(minLen, maxLen, userName))
            message.append(String.format("%s must be between %d and %d\n", type, minLen, maxLen));

        Map<String, Boolean> strMeta = Validator.extractStrMeta(userName);
        if (LowCase && !strMeta.get("hasLoCase"))
            message.append(String.format("%s Must contain atleast one lower case character\n", type));
        if (upCase && !strMeta.get("hasUpCase"))
            message.append(String.format("%s Must contain atleast one upper case character.\n", type));
        if (specChar && !strMeta.get("hasSpecChar"))
            message.append(String.format("%s Must contain atleast one special character.\n", type));
        if (num && !strMeta.get("hasNum"))
            message.append(String.format("%s Must contain atleast one numerical value.\n", type));
        if (!message.isEmpty())
            throw new IOException(message.toString());
        return true;

    }

    /**
     * @param password Password to be validated
     * @param minLen   The minimum lingth the password can be.
     * @param maxLen   The Maximum length the password can be.
     * @param LowCase  Set true if password must contain atleast one lowercase
     *                 character, false otherwise.
     * @param upCase   Set true if password must contain atleast one uppercase
     *                 character, false otherwise.
     * @param specChar set true if password must contain atleast one special
     *                 character, false otherwise.
     * @param num      ser true if password must contain atleast one numerical
     *                 value, false otherwise.
     * @return true if all constraints are satisfied.
     * @throws IOException If all constraints aren't satisfied.
     * @author Navardo Williams
     */
    public static boolean validatePassword(String password, int minLen, int maxLen, boolean LowCase,
            boolean upCase,
            boolean specChar,
            boolean num) throws IOException {

        String type = "username";
        StringBuilder message = new StringBuilder("");
        if (!Validator.validStrLen(minLen, maxLen, password))
            message.append(String.format("%s must be between %d and %d\n", type, minLen, maxLen));

        Map<String, Boolean> strMeta = Validator.extractStrMeta(password);
        if (LowCase && !strMeta.get("hasLoCase"))
            message.append(String.format("%s Must contain atleast one lower case character\n", type));
        if (upCase && !strMeta.get("hasUpCase"))
            message.append(String.format("%s Must contain atleast one upper case character.\n", type));
        if (specChar && !strMeta.get("hasSpecChar"))
            message.append(String.format("%s Must contain atleast one special character.\n", type));
        if (num && !strMeta.get("hasNum"))
            message.append(String.format("%s Must contain atleast one numerical value.\n", type));
        if (!message.isEmpty())
            throw new IOException(message.toString());
        return true;

    }

    /**
     * validates a secret question used for authentication
     *
     * @param secretQuestion account secret question
     * @return true if an imputed secretQuestion is valid, false otherwise
     * @author Navardo Williams
     */
    public static boolean validateSecretQuestion(String secretQuestion) {
        if (!Validator.validStrLen(8, 16, secretQuestion)) {
            return false;
        }
        return true;
    }

    /**
     * validates an answer given to a secret question
     *
     * @param secretAnswer account secret Answer
     * @return return true if secretAnswer is valid, false otherwise
     * @author Navardo Williams
     */
    public static boolean validateSecretAnswer(String secretAnswer) {
        if (!Validator.validStrLen(8, 16, secretAnswer)) {
            return false;
        }
        return true;
    }
}

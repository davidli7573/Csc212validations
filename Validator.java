import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Year;
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
     *         throws an IllegalArgumentException.
     * @throws IllegalArgumentException
     * @author Navardo Williams
     */
    public static boolean isValidInput(String val, int minVal, int maxVal) throws IllegalArgumentException {
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
            throw new IllegalArgumentException(message.append(String.format(
                    (maxVal - minVal) <= 1 ? "Please select %d or %d: "
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
     * Checks whether a given string can be parsed as an integer.
     *
     * @param value the string to check
     * @return true if the value represents a valid integer, false otherwise
     * @author David Li
     */
    public static boolean isInt(String value) {
        if (value == null || value.isEmpty()) {
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
    public static boolean hasInvalidCharacters(String value) {
        if (value == null) {
            return true;
        }
        for (char c : value.toCharArray()) {
            if (c < 32 || c > 126) {
                return true;
            }
        }
        return false;
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
    final static int EXPECTED_NAME_LENGTH = 8;
    final static int MIN_YEAR= 1900;
    final static int MAX_YEAR = Year.now().getValue();
    static final String[] INCOME_CATEGORIES = {
    "Compensation", "Allowance", "Investments"
};

    static final String[] EXPENSE_CATEGORIES = {
    "Home", "Utilities", "Food", "Appearance",
    "Work", "Education", "Transportation",
    "Entertainment", "Professional Services"
};
    static final String[] VALIDCATEGORIES = {
        "Compensation", "Allowance", "Investments", 
        "Home", "Utilities", "Food", "Appearance",
        "Work", "Education", "Transportation",
        "Entertainment", "Professional Services", "Other"
    };
    /**
     * Default constructor for CsvValidator.
     */
    CsvValidator() {
    }
    /**
     * Checks whether the file at the given path exists, is readable, and is non-empty.
     *
     * @param filePath the path to the CSV file
     * @return true if the file is readable and has content and contains at least one line
     * @throws IllegalArgumentException if the path is null, empty, or points to an invalid file
     * @author David Li
     */
    public boolean checkFileReadable(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("Error:no File given");
        }
        if (filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be empty.");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist.");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("Path is not a file.");
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException("File cannot be read.");
        }
        if (file.length() == 0) {
            throw new IllegalArgumentException("File is empty.");
        }
        try (Scanner scanner = new Scanner(file)) {
            return scanner.hasNextLine();
        }
        catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File could not be opened.");
        }
    }
    /**
     * Validates the entire file by checking file name, header,
     * records, and year consistency.
     *
     * @param fileName      the name of the CSV file
     * @param rows          the contents of the CSV file where each row is split
     *                      into fields
     * @param existingYears list of years already stored for the user
     * @throws IllegalArgumentException if row doesn't exist or missing field
     * @return true if the file passes all validation checks, false otherwise
     * @author Nadim Siddique
     */
    public boolean validateFile(String fileName, List<String[]> rows, List<Integer> existingYears) {
        if (rows == null) {
            throw new IllegalArgumentException("no rows given");
        }

        if (rows.size() < 2) {
            throw new IllegalArgumentException("missing field on row");
        }

        if (!validateFileName(fileName)) {
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
            return !validateYearConsistency(rows);
        }

        int year = Integer.parseInt(fileName.substring(0, 4));
        return validateUniqueFile(year, existingYears);
    }

    /**
     * Validates that the file name follows the required format YYYY.csv.
     *
     * @param fileName the name of the file
     * @return true if the file name is valid, false otherwise
     * @bug [Issue #12] fixed crashing application on invalid file extension
     *      by returning a boolean instead of throws so MainMenu.java accepts it
     * @author Nadim Siddique
     */
    public boolean validateFileName(String fileName) {

        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("No given file name or empty");
        }

        if (!fileName.endsWith(".csv")) {
            throw new IllegalArgumentException("file type is not .csv");
        }

        // Expected format is YYYY.csv, which is exactly 8 characters.
        if (fileName.length() != EXPECTED_NAME_LENGTH) {
            return false;
        }

        String yearPart = fileName.substring(0, 4);

        if (!Validator.isInt(yearPart)) {
            return false;
        }

        int year = Integer.parseInt(yearPart);

        if (year < MIN_YEAR || year > MAX_YEAR) {
            return false;
        }

        return true;
    }

    /**
     * Validates that the header row matches the required case-sensitive format:
     * Date, Category, Amount
     *
     * @param header the first row of the CSV file
     * @throws IllegalArgumentException if the header doesn't follow the format
     * @return true if header follows Date, Category, Amount
     * @author Nadim Siddique
     */
    public boolean validateHeader(String[] header) {

        if (header == null) {
            throw new IllegalArgumentException("Header cannot be null.");
        }

        if (header.length != 3) {
            throw new IllegalArgumentException("Header must contain exactly 3 columns.");
        }

        if (!header[0].trim().equals("Date")) {
            throw new IllegalArgumentException("Invalid header column 1. Expected Date.");
        }

        if (!header[1].trim().equals("Category")) {
            throw new IllegalArgumentException("Invalid header column 2. Expected Category.");
        }

        if (!header[2].trim().equals("Amount")) {
            throw new IllegalArgumentException("Invalid header column 3. Expected Amount.");
        }
        return true;
    }

    /**
     * Validates a single record (row) in the CSV file.
     * A valid record must contain exactly three values:
     * date, category, and amount.
     * The date must be in the format DD/MM/YYYY and represent a valid calendar date.
     * The category must be one of the predefined income or expense categories.
     * The amount must be a valid integer (positive for income, negative for expenses).
     *
     * The invalid-character check is applied to every field, so its regex
     * must stay compatible with valid date, category, and amount formats.
     * The "Other" category is allowed to use either a positive or negative amount.
     *
     * @param record the row to validate
     * @return true if the record is valid, false otherwise
     * @author Nadim Siddique
     */
    public boolean validateRecord(String[] record) {

        if (record == null || record.length != 3) {
            return false;
        }

        for (String field : record) {
            if (Validator.hasInvalidCharacters(field)) {
                return false;
            }
        }

        String date = record[0].trim();
        String category = record[1].trim();
        String amount = record[2].trim();

        if (!validateDate(date)) {
            return false;
        }

        if (!validateCategory(category)) {
            return false;
        }

        if (!validateAmount(amount)) {
            return false;
        }

        // Safe to parse here since validateAmount confirms the amount is valid.
        int value = Integer.parseInt(amount);

        for (String income : INCOME_CATEGORIES) {
            if (category.equalsIgnoreCase(income) && value < 0) {
                return false;
            }
        }

        for (String expense : EXPENSE_CATEGORIES) {
            if (category.equalsIgnoreCase(expense) && value > 0) {
                return false;
            }
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

        if (rows == null) {
            throw new IllegalArgumentException("Rows cannot be null.");
        }

        if (rows.size() < 2) {
            throw new IllegalArgumentException("CSV file must contain at least one record.");
        }

        if (rows.get(1) == null || rows.get(1).length == 0) {
            return false;
        }

        String firstDate = rows.get(1)[0].trim();

        if (firstDate.length() < 10) {
            return false;
        }

        String expectedYear = firstDate.substring(6, 10);

        for (int i = 1; i < rows.size(); i++) {

            if (rows.get(i) == null || rows.get(i).length == 0) {
                return false;
            }

            String date = rows.get(i)[0].trim();

            if (date.length() < 10 ||
                !date.substring(6, 10).equals(expectedYear)) {
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

        if (year <= 0) {
            return false;
        }

        if (existingYears == null) {
            return true;
        }

        return !existingYears.contains(year);
    }
    /**
     * Validates whether a given date field is in the correct format and represents
     * a valid calendar date.
     * This method checks for proper formatting (e.g., MM/DD/YYYY) and ensures the
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
 
        // CHANGED: format is MM/DD/YYYY per the project spec
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            return false;
        }
 
        String[] parts = date.split("/");
        int month = Integer.parseInt(parts[0]);
        int day   = Integer.parseInt(parts[1]);
        int year  = Integer.parseInt(parts[2]);
 
        if (year < MIN_YEAR || year > MAX_YEAR) {
            return false;
        }
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1) {
            return false;
        }
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
 
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

        String trimmed = category.trim();
        for (String valid : VALIDCATEGORIES) {
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

        // allows a leading minus for expenses
        String digits = trimmed.startsWith("-") ? trimmed.substring(1) : trimmed;


        // must be valid integer digits after removing the sign
        if (digits.isEmpty() || !Validator.isInt(digits)) {
            return false;
        }

        // check for overflow before the parsing
        try {
            Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            return false;
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

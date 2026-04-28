import java.util.List;

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
     * Checks whether a given string is null, empty, or contains only whitespace.
     *
     * @param value the string to check
     * @return true if the value is null or blank, false otherwise
     * @author David Li
     */
    protected static boolean isEmpty(String value) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Checks whether a given string can be parsed as an integer.
     *
     * @param value the string to check
     * @return true if the value represents a valid integer, false otherwise
     * @author David Li
     */
    protected static boolean isInt(String value) {
        try {
            if (isEmpty(value)) {
                throw new IllegalArgumentException("Value is empty.");
            }
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            System.err.println("Value is not a valid integer: " + value);
            return false;

        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return false;
        }
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
    protected boolean hasInvalidCharacters(String value) {
        try {
            if (value == null) {
                throw new IllegalArgumentException("Value is null.");
            }
            if (!value.matches("[a-zA-Z0-9 ,./\\-?!'()]+")) {
                throw new IllegalArgumentException("Value contains invalid characters: " + value);
            }
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return true;
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
     * @param fileName the name of the CSV file
     * @param rows the contents of the CSV file where each row is split into fields
     * @param existingYears list of years already stored for the user
     * @return true if the file passes all validation checks, false otherwise
     * @author Nadim Siddique
     */
    public boolean validateFile(String fileName, List<String[]> rows, List<Integer> existingYears) {
        return false;
    }

    /**
     * Validates that the file name follows the required format YYYY.csv.
     *
     * @param fileName the name of the file
     * @return true if the file name is valid, false otherwise
     * @author Nadim Siddique
     */
    public boolean validateFileName(String fileName) {

        return false;
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
        return false;
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
        return false;
    }

    /**
     * Validates that all records in the file belong to the same year.
     *
     * @param rows the contents of the CSV file
     * @return true if all dates are from the same year, false otherwise
     * @author Nadim Siddique
     */
    public boolean validateYearConsistency(List<String[]> rows) {
        return false;
    }

    /**
     * Validates that the year being uploaded does not already exist
     * for the current user.
     *
     * @param year the year extracted from the file
     * @param existingYears list of years already stored for the user
     * @return true if the year is unique, false otherwise
     * @author Nadim Siddique
     */
    public boolean validateUniqueFile(int year, List<Integer> existingYears) {
        return false;
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
        // No initialization for now
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
        // No initialization for now
        return true;
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

    public boolean validateAmount(double amount) {
        // No initialization for now
        return true;
    }

    /**
     * Validates that a given value matches the expected data type.
     * This method checks whether the input can be correctly interpreted as the
     * required type
     * (e.g., integer, double, string, etc).
     *
     * @param value the value to validate
     * @param expectedType the expected data type (e.g., "int", "double", "string")
     * @return true if the value matches the expected data type, false otherwise
     * @author Masudul Shafi
     */
    public boolean validateDataType(String value, String expectedType) {
        // No initialization for now
        return true;
    }

    /**
     * Validates that the file format is correct (a YYYY.csv file).
     *
     * @param fileName the name of the file
     * @return true if the file format is valid, false otherwise
     * @author David Li
     */
    public boolean checkFileFormat(String fileName) {
        try {
            if (Validator.isEmpty(fileName)) {
                throw new IllegalArgumentException("File name cannot be empty.");
            }
            if (!fileName.endsWith(".csv")) {
                throw new IllegalArgumentException("File must end with .csv.");
            }
            String year = fileName.substring(0, fileName.length() - 4);
            if (year.length() != 4) {//if there are going to be other constraints we can add it
                throw new IllegalArgumentException("File name must be in YYYY.csv format.");
            }
            if (!Validator.isInt(year)) {
                throw new IllegalArgumentException("Year must be a valid number.");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error checking file format.");
            return false;
        }
    }

    /**
     * Checks for missing fields in each record.
     *
     * @param rows the contents of the CSV file
     * @return true if all records are filled in, false otherwise
     * @author David Li
     */
    public boolean checkMissingFields(List<String[]> rows) {
        try {
            if (rows == null) {//row pointed to null used so it would crashing if it starts with null row and this can become a dupe need validation from the rest of group
                throw new IllegalArgumentException("Rows cannot be null.");
            }
            for (String[] row : rows) {//adding this in as a safety check if the row somehow points to null
                if (row == null) {
                    throw new IllegalArgumentException("A row is null.");
                }
                for (String field : row) {//after making sure we can read check if field is empty
                    if (Validator.isEmpty(field)) {
                        throw new IllegalArgumentException("Missing field found.");
                    }
                }
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return false;

        } catch (Exception e) {
            System.err.println("Error checking missing fields.");
            return false;
        }
}

    /**
     * Checks for blank lines in the file.
     *
     * @param rows the contents of the CSV file
     * @return true if no blank lines are found, false otherwise
     * @author David Li
     */
    public boolean checkBlankLines(List<String[]> rows) {
        try {
            if (rows == null) {//used to safe guard allowing it to read
                throw new IllegalArgumentException("Rows cannot be null.");
            }
            for (String[] row : rows) {
                if (row == null) {
                    throw new IllegalArgumentException("Blank line found.");
                }
                boolean blank = true;
                for (String field : row) {
                    if (!Validator.isEmpty(field)) {
                        blank = false;
                        break;
                    }
                }
                if (blank) {
                    throw new IllegalArgumentException("Blank line found.");
                }
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error checking blank lines.");
            return false;
        }
    }

    /**
     * Ensures that each row contains the correct number of columns.
     *
     * @param rows the contents of the CSV file
     * @param expectedColumnCount the required number of columns
     * @return true if all rows have the correct number of columns, false otherwise
     * @author David Li
     */
    public boolean checkColumnCount(List<String[]> rows, int expectedColumnCount) {
        try {
            if (rows == null) {
                throw new IllegalArgumentException("Rows cannot be null.");
            }

            if (expectedColumnCount <= 0) {//if the column count is less than 0 causes error so.
                throw new IllegalArgumentException("Expected column count must be greater than 0.");
            }
            for (String[] row : rows) {
                if (row == null) {
                    throw new IllegalArgumentException("A row is null.");
                }
                if (row.length != expectedColumnCount) {
                    throw new IllegalArgumentException("Incorrect number of columns.");
                }
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error checking column count.");
            return false;
        }
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
     * validates a username for login or signup
     *
     * @param userName account username how they get it
     * @return true if inputed userName is valid, false if not valid
     * @author Navardo Williams
     */
    public static boolean validateUserName(String userName) {
        return false;
    }

    /**
     * validates a passord for user login or signup
     *
     * @param password account password
     * @return true if imputed password is valid, false otherwise
     * @author Navardo Williams
     */
    public static boolean validatePassword(String password) {
        return false;
    }

    /**
     * validates a secret question used for authentication
     *
     * @param secretQuestion account secret question
     * @return true if an imputed secretQuestion is valid, false otherwise
     * @author Navardo Williams
     */
    public static boolean validateSecretQuestion(String secretQuestion) {
        return false;
    }

    /**
     * validates an answer given to a secret question
     *
     * @param secretAnswer account secret Answer
     * @return return true if secretAnswer is valid, false otherwise
     * @author Navardo Williams
     */
    public static boolean validateSecretAnswer(String secretAnswer) {
        return false;
    }
    /*class ExecptionExtension extend IOexecption () {} //this can be used to give the exact reason of why it a error occur
                                                      //instead of just telling them unable to access file we can tel them it's locked by admin or something similar*/
}


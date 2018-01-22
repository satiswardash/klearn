package com.kortain.klearn.Utility;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by satiswardash on 21/01/18.
 */

public class StringUtility {

    private Context mContext;
    private Pattern pattern;
    private Matcher matcher;

    private StringUtility(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Get StringUtility instance for the context
     * @param context
     * @return
     */
    public static StringUtility getInstance(Context context) {
        return new StringUtility(context);

    }

    /**
     * Validate email id with regular expression
     *
     * email address must start with “_A-Za-z0-9-\\+” , optional follow by “.[_A-Za-z0-9-]”,
     * and end with a “@” symbol. The email’s domain name must start with “A-Za-z0-9-“,
     * follow by first level Tld (.com, .net) “.[A-Za-z0-9]” and
     * optional follow by a second level Tld (.com.au, .com.my) “\\.[A-Za-z]{2,}”,
     * where second level Tld must start with a dot “.” and length must equal or more than 2 characters.
     *
     * @param email email for validation
     * @return true valid email, false invalid email
     */
    public boolean isValidEmail(String email) {
        pattern = Pattern.compile(Constants.EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Validate password with regular expression
     *
     * 6 to 20 characters string with at least one digit, one upper case letter,
     * one lower case letter and one special symbol (“@#$%”)
     *
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public boolean isValidPassword(String password) {
        pattern = Pattern.compile(Constants.PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    //TODO Validate using regular expression
    public boolean isValidPhone(String phone) {
        return true;
    }
}

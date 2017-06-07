package de.sp.tools.validation;

/**
 * Created by Martin on 07.06.2017.
 */
public enum SanitizingStrategy {

    none,           // don't sanitize String -> UNSAFE!
    jsoupWhitelist, // remove all tags except whitelist
    ampersand,      // replace < with &lt; and replace > with &gt;
    remove          // remove all < and > from String

}

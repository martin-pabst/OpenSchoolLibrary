package de.sp.tools.validation;

import de.sp.main.resources.text.TS;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Martin on 07.06.2017.
 */
public class Validator {

    public static Whitelist whitelist;

    static {
        whitelist = new Whitelist()
                .addTags(
                        //"a",
                        "b",
                        //"blockquote",
                        "br",
                        //"cite",
                        // "code",
                        "dd", "dl", "dt", "em",
                        "i", "li", "ol", "p", "pre",
                        //"q",
                        "small", "span", "strike", "strong", "sub",
                        "sup", "u", "ul");

        //.addAttributes("a", "href")
        //.addAttributes("blockquote", "cite")
        //.addAttributes("q", "cite")

        //.addProtocols("a", "href", "ftp", "http", "https", "mailto")
        //.addProtocols("blockquote", "cite", "http", "https")
        //.addProtocols("cite", "cite", "http", "https")

        //.addEnforcedAttribute("a", "rel", "nofollow");
    }

    public static void validate(Object request, TS ts) throws ValidationException {
        try {

            if(request instanceof SelfValidating){
                ((SelfValidating) request).validate();
            }

            for (Field field : request.getClass().getDeclaredFields()) {

                field.setAccessible(true);

                if (field.isAnnotationPresent(Validation.class)) {

                    Validation validation = field.getAnnotation(Validation.class);

                    String fieldname = field.getName();

                    Object object = field.get(request);

                    if (validation.notNull() || validation.min() > 0) {
                        if (object == null) {
                            throwValidationException("Das Feld " + fieldname + " darf nicht leer sein.", validation, ts);
                        }
                    }

                    if (object instanceof String) {

                        String stringValue = (String) object;

                        String stringValueSanitized = validateString(stringValue, validation, fieldname, ts);

                        // for performance reasons: check if setting is necessary:
                        if (!stringValue.equals(stringValueSanitized)) {
                            field.set(request, stringValueSanitized);
                        }

                    } else if (object instanceof Integer) {
                        Integer intValue = (Integer) object;

                        validateInteger(intValue, validation, fieldname, ts);

                    } else if (object instanceof List) {

                        validateList((List) object, validation, fieldname, ts);

                    } else if (object != null && object.getClass().isArray()) {
                        int length = Array.getLength(object);

                        checkLength(validation, length, fieldname, ts);

                        for (int i = 0; i < length; i++) {

                            Object o = Array.get(object, i);

                            if(o instanceof String){
                                String sanitizedString = validateString((String)o, validation, fieldname, ts);
                                Array.set(object, i, sanitizedString);
                            } else {
                                validate(Array.get(object, i), ts);
                            }
                        }

                    } else if(object != null && !object.getClass().getName().startsWith("java")){

                        validate(object, ts);

                    }

                }
            }

            if (request instanceof List && request.getClass().isAnnotationPresent(Validation.class)) {

                Validation validation = request.getClass().getAnnotation(Validation.class);

                validateList((List) request, validation, request.getClass().getName(), ts);

            }

        } catch (IllegalAccessException ex) {
            throw new ValidationException("Interner Fehler in " + request.getClass().getName() + ": IllegalAccessException");
        }
    }

    private static void validateList(List object, Validation validation, String fieldname, TS ts) throws ValidationException {
        List list = object;

        int length = list.size();

        checkLength(validation, length, fieldname, ts);

        for (Object o : list) {
            if(o instanceof String){
                String sanitizedString = validateString((String)o, validation, fieldname, ts);
            } else {
                validate(o, ts);
            }
        }
    }

    private static void checkLength(Validation validation, int length, String fieldname, TS ts) throws ValidationException {
        if (validation.min() != Integer.MIN_VALUE && length < validation.min()) {
            throwValidationException("Die Liste " + fieldname + " enthält weniger als " + validation.min() + " Elemente.", validation, ts);
        }

        if (validation.max() != Integer.MAX_VALUE && length > validation.max()) {
            throwValidationException("Die Liste " + fieldname + " enthält mehr als " + validation.max() + " Elemente.", validation, ts);
        }
    }

    private static void validateInteger(Integer intValue, Validation validation, String fieldname, TS ts) throws ValidationException {

        if (intValue != null) {

            if (validation.min() != Integer.MIN_VALUE) {
                if (intValue < validation.min()) {
                    throwValidationException("Der Wert des Feldes " + fieldname + " muss mindestens " + validation.min() + " betragen.", validation, ts);
                }
            }

            if (validation.max() != Integer.MAX_VALUE) {
                if (intValue > validation.max()) {
                    throwValidationException("Der Wert des Feldes " + fieldname + " darf höchstens " + validation.max() + " betragen.", validation, ts);
                }
            }

        }

    }

    private static String validateString(String stringValue, Validation validation, String fieldname, TS ts) throws ValidationException {
        if (validation.notEmpty()) {
            if (stringValue == null || stringValue.isEmpty()) {
                throwValidationException("Das Feld " + fieldname + " darf nicht leer sein.", validation, ts);
            }
        }

        if (!validation.regEx().isEmpty()) {
            if (stringValue != null) {
                if (!stringValue.matches(validation.regEx())) {
                    throwValidationException("Der Wert des Feldes " + fieldname + " ist nicht korrekt.", validation, ts);
                }
            }
        }

        if (validation.maxLength() != Integer.MAX_VALUE) {
            if (stringValue != null && stringValue.length() > validation.maxLength()) {
                throwValidationException("Der Inhalt des Feldes " + fieldname + " darf nicht länger als " + validation.maxLength() + " Zeichen sein.", validation, ts);
            }
        }

        if(validation.acceptedValues().length > 0){

            boolean accepted = false;

            for (String s : validation.acceptedValues()) {
                if(s == null){
                    if(stringValue == null) {
                        accepted = true;
                        break;
                    }
                } else {
                    if(s.equals(stringValue)){
                        accepted = true;
                        break;
                    }
                }
            }

            if(!accepted){
                if(stringValue == null) stringValue = "null";
                throwValidationException("Value " + stringValue + " is not accepted in field " + fieldname, validation, ts);
            }

        }

        SanitizingStrategy sanitizingStrategy = validation.sanitizingStrategy();

        stringValue = sanitize(stringValue, sanitizingStrategy);

        return stringValue;

    }

    public static String sanitize(String stringValue, SanitizingStrategy sanitizingStrategy) {
        if (stringValue != null) {
            switch (sanitizingStrategy) {
                case ampersand:
                    stringValue = stringValue.replace("<", "&lt;");
                    stringValue = stringValue.replace(">", "&gt;");
                    break;
                case remove:
                    stringValue = stringValue.replace("<", "");
                    stringValue = stringValue.replace(">", "");
                    break;
                case jsoupWhitelist:
                    // for performance reasons: first check if it's possible that stringValue contains tags:
                    if (stringValue.contains("<")) {
                        stringValue = Jsoup.clean(stringValue, whitelist);
                    }
                    break;
                case none:
                    break;
            }
        }
        return stringValue;
    }

    private static void throwValidationException(String message, Validation validation, TS ts) throws ValidationException {

        if (!validation.message().isEmpty()) {
            message = validation.message();
        }

        if (ts != null) {
            message = ts.get(message);
        }

        throw new ValidationException(message);

    }


}

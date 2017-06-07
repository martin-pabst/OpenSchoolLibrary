package de.sp.tools.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 07.06.2017.
 */
public class BaseRequestDataTestClass extends BaseRequestData {

    public static final String message = "Es wurde keine gültige Mailadresse eingegeben.";

    @Validation(notNull = true)
    protected Object testObject;

    @Validation(notEmpty = true, maxLength = 30, message = "Beispielfehlertext")
    protected String name;

    @Validation(regEx = ".*@.*\\..*", message = BaseRequestDataTestClass.message)
    protected String mail;

    @Validation(min = 0, max = 150)
    protected int alter;

    @Validation(min = 0, max = 2)
    protected List<BaseRequestDataTestClass> list = new ArrayList<>();

    @Validation(acceptedValues = {"get", "post"})
    protected String method;

    public BaseRequestDataTestClass(Object testObject, String name, String mail, int alter, String method) {
        this.testObject = testObject;
        this.name = name;
        this.mail = mail;
        this.alter = alter;
        this.method = method;
    }

    public void addToList(BaseRequestDataTestClass brtc){
        list.add(brtc);
    }

    public String getName() {
        return name;
    }
}
package de.sp.modules.library.servlets.borrow.borrowedbooks.addstudent;

import de.sp.protocols.w2ui.forms.SimpleValueListResponse;
import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

import java.util.Date;

/**
 * Created by Martin on 22.04.2017.
 */
public class LibraryAddStudentRequestRecord extends BaseRequestData{

    @Validation(notNull = true)
    public Date date_of_birth;

    @Validation(notNull = true)
    public SimpleValueListResponse sex;

    @Validation(notEmpty = true, maxLength = 100)
    public String firstname;

    @Validation(notNull = true, maxLength = 300)
    public String surname;

    @Validation(maxLength = 100)
    public String before_surname;

    @Validation(maxLength = 100)
    public String after_surname;

    @Validation
    public SimpleValueListResponse classname;

    @Validation
    public SimpleValueListResponse curriculum;

    @Validation
    public SimpleValueListResponse religion;

    @Validation
    public SimpleValueListResponse language_1;
    public Integer from_form_1;

    @Validation
    public SimpleValueListResponse language_2;
    public Integer from_form_2;

    @Validation
    public SimpleValueListResponse language_3;
    public Integer from_form_3;
    
    public SimpleValueListResponse getLanguage(int i){
        switch (i){
            case 1: return language_1;
            case 2: return language_2;
            case 3: return language_3;
        }
        
        return null;
    }
    public Integer getFromForm(int i){
        switch (i){
            case 1: return from_form_1;
            case 2: return from_form_2;
            case 3: return from_form_3;
        }

        return null;
    }
    
    
}

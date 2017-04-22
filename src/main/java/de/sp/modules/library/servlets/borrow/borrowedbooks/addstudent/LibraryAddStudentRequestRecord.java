package de.sp.modules.library.servlets.borrow.borrowedbooks.addstudent;

import de.sp.protocols.w2ui.forms.SimpleValueListRespoonse;

import java.util.Date;

/**
 * Created by Martin on 22.04.2017.
 */
public class LibraryAddStudentRequestRecord {

    public Date date_of_birth;
    public SimpleValueListRespoonse sex;
    public String firstname;
    public String surname;
    public String before_surname;
    public String after_surname;
    public SimpleValueListRespoonse classname;
    public SimpleValueListRespoonse curriculum;

    public SimpleValueListRespoonse language_1;
    public Integer from_form_1;

    public SimpleValueListRespoonse language_2;
    public Integer from_form_2;

    public SimpleValueListRespoonse language_3;
    public Integer from_form_3;
    
    public SimpleValueListRespoonse getLanguage(int i){
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

package de.sp.tools.string;

/**
 * Created by Martin on 22.04.2017.
 */
public class FormTool {

    public static Integer formToInteger(String form){
        form = form.trim();

        int i = 0;
        while(i < form.length() && Character.isDigit(form.charAt(i))){
            i++;
        }

        form = form.substring(0, i);

        int n = 0;

        try{
            n = Integer.parseInt(form);
        } catch (NumberFormatException ex){

        }

        return n;
    }



}

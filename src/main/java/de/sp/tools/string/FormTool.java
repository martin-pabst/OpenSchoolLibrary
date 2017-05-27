package de.sp.tools.string;

/**
 * Created by Martin on 22.04.2017.
 */
public class FormTool {

    public static Integer formToInteger(String form){

        StringBuilder sb = new StringBuilder(form.length());

        boolean initial0 = true;

        for(int i = 0; i < form.length(); i++){
            char c = form.charAt(i);
            if(Character.isDigit(c)){
                if(!initial0 || c != '0') {
                    sb.append(c);
                    initial0 = false;
                }
            }
        }


        try{
            return  Integer.parseInt(sb.toString());
        } catch (NumberFormatException ex){
            return 0;
        }

    }



}

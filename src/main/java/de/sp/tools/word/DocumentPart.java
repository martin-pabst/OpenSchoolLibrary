package de.sp.tools.word;

import java.util.ArrayList;

public class DocumentPart {

    private static char[] chars = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4','5', '6', '7', '8', '9'
    };

    private String id;
    private String xml;
    private ArrayList<String> copies = new ArrayList<>();

    public DocumentPart(String xml){
        this.xml = xml;

        id = "2";
        for(int i = 0; i < 10; i++){
            id += chars[(int)(Math.random()*chars.length)];
        }

    }

    public String getId() {
        return id;
    }

    public void addCopy(String... keyValuePairs){

        String newXML = xml;

        for(int i = 0; i < keyValuePairs.length; i += 2){
            newXML = newXML.replace(keyValuePairs[i], keyValuePairs[i+1]);
        }

        copies.add(newXML);

    }

    public String getCopies(){

        StringBuilder sb = new StringBuilder();
        for(String copy: copies){
            sb.append(copy);
        }

        return sb.toString();
    }

}

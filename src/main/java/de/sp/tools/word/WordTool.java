package de.sp.tools.word;

import de.sp.tools.string.Finder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by martin on 07.04.2017.
 */
public class WordTool {

    private FileSystem fileSystem;
    private Path pathToDocumentXML;
    private String xml;
    private String filename;

    private List<DocumentPart> documentParts = new ArrayList<>();

    private HashMap<String, ArrayList<RowChanger>> rowChangers = new HashMap<>();

    public WordTool(String originalFilename, String filename) throws URISyntaxException, IOException {

        Files.copy(new File(originalFilename).toPath(), new File(filename).toPath(), StandardCopyOption.REPLACE_EXISTING);

        this.filename = filename;

        URI docxUri = URI.create("jar:file:" + Paths.get(filename).toUri().getPath());
        Map<String, String> zipProperties = new HashMap<>();
        zipProperties.put("encoding", "UTF-8");
        try (FileSystem zipFS = FileSystems.newFileSystem(docxUri, zipProperties)) {

            Path documentXmlPath = zipFS.getPath("/word/document.xml");

            byte[] content = Files.readAllBytes(documentXmlPath);
            xml = new String(content, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String getXml() {

        String newXML = xml;

        for(DocumentPart dp: documentParts){

            newXML = newXML.replace(dp.getId(), dp.getCopies());
        }

        return newXML;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public void write() throws URISyntaxException {

        commitRowChangers();

        URI docxUri = URI.create("jar:file:" + Paths.get(filename).toUri().getPath());

        Map<String, String> zipProperties = new HashMap<>();
        zipProperties.put("encoding", "UTF-8");
        try (FileSystem zipFS = FileSystems.newFileSystem(docxUri, zipProperties)) {

            Path documentXmlPath = zipFS.getPath("/word/document.xml");

            byte[] content = xml.getBytes(StandardCharsets.UTF_8);
            Files.delete(documentXmlPath);
            Files.write(documentXmlPath, content);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void commitRowChangers() {

        rowChangers.forEach((hint, rowChangerList) -> {

            String originalRow = extractTableRowXML(hint);

            ArrayList<String> newLines = new ArrayList<>();

            for (RowChanger rc : rowChangerList) {

                String newLine = originalRow;

                for (CellData cd : rc.getCellDataList()) {

                    newLine = newLine.replace(cd.getPlaceholder(), cd.getNewValue());

                }

                newLines.add(newLine);

            }

            replaceTableRowXML(hint, newLines);

        });

    }


    private String extractTableRowXML(String hint) {

        int i = xml.indexOf(hint);
        int lineBegin = xml.lastIndexOf("<w:tr ", i);
        int lineEnd = xml.indexOf("</w:tr>", i) + "</w:tr>".length();

        return xml.substring(lineBegin, lineEnd);

    }

    private void replaceTableRowXML(String hint, List<String> xmlForNewLines) {

        int i = xml.indexOf(hint);
        int lineBegin = xml.lastIndexOf("<w:tr ", i);
        int lineEnd = xml.indexOf("</w:tr>", i) + "</w:tr>".length();

        String xmlBefore = xml.substring(0, lineBegin);
        String xmlAfter = xml.substring(lineEnd);

        StringBuilder sb = new StringBuilder();

        sb.append(xmlBefore);

        for (String xml : xmlForNewLines) {
            sb.append(xml);
        }

        sb.append(xmlAfter);

        xml = sb.toString();

    }

    public RowChanger getRowChanger(String hint) {

        ArrayList<RowChanger> rowChangerList = rowChangers.get(hint);

        if (rowChangerList == null) {
            rowChangerList = new ArrayList<>();
            rowChangers.put(hint, rowChangerList);
        }

        RowChanger rc = new RowChanger(hint);
        rowChangerList.add(rc);

        return rc;

    }

    public void cancelRowChanger(RowChanger rc) {

        ArrayList<RowChanger> rowChangerList = rowChangers.get(rc.getHint());

        if (rowChangerList != null) {
            rowChangerList.remove(rc);
            if (rowChangerList.size() == 0) {
                rowChangers.remove(rc.getHint());
            }
        }

    }


    public DocumentPart getDocumentPart(String placeholderStart, String placeholderEnd) {
        Finder finder = new Finder(xml);
        finder.find(placeholderStart);
        finder.findBackward("<w:p");
        int posPlaceholderStart = finder.getPos();
        finder.find("</w:p>");
        finder.skipFoundWord();
        int partStart = finder.getPos();

        finder.find(placeholderEnd);
        finder.findBackward("<w:p");
        int partEnd = finder.getPos();
        finder.find("</w:p>");
        finder.skipFoundWord();
        int posPlaceholderEnd = finder.getPos();

        String before = xml.substring(0, posPlaceholderStart);
        String after = xml.substring(posPlaceholderEnd, xml.length());
        String partXML = xml.substring(partStart, partEnd);

        DocumentPart dp = new DocumentPart(partXML);

        xml = before + dp.getId() + after;

        documentParts.add(dp);

        return dp;

    }


    public void replace(String placeholder, String newValue) {
        if (!newValue.contains("\n")) {
            xml = xml.replace(placeholder, newValue);
        } else {

            String[] lines = newValue.split("\n");

            // find begin and end of enclosing paragraph
            Finder finder = new Finder(xml);
            finder.find(placeholder);
            finder.findBackward("<w:p");
            int posStart = finder.getPos();
            finder.find("</w:p>");
            finder.skipFoundWord();
            int posEnd = finder.getPos();


            String paragraph = xml.substring(posStart, posEnd);
            String before = xml.substring(0, posStart);
            String after = xml.substring(posEnd, xml.length());

            for (String line : lines) {
                before += paragraph.replace(placeholder, line);
            }

            xml = before + after;

        }
    }


    public boolean hasHint(String hint) {

        return xml.contains(hint);

    }


}

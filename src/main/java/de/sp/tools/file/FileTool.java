package de.sp.tools.file;

import de.sp.database.statements.StatementStore;
import de.sp.main.resources.modules.ModuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.*;
import java.nio.file.FileSystem;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class FileTool {

    public static ArrayList<String> listInternalModuleResourceFiles(String path)
            throws URISyntaxException, IOException {

        URI uri = FileTool.class.getResource(path).toURI();

        Path myPath;

        FileSystem fileSystem = null;

        if (uri.getScheme().equals("jar")) {
            fileSystem = FileSystems.newFileSystem(uri,
                    Collections.<String, Object>emptyMap());
            myPath = fileSystem.getPath(path);
        } else {
            myPath = Paths.get(uri);
        }

        int praefixLength = myPath.toString().length() - path.length();

        ArrayList<String> filenames = new ArrayList<>();

        Stream<Path> walk = Files.walk(myPath, 100);

        for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
            String filename = it.next().toString();
            if (filename.endsWith(".xml")) {
                filenames.add(filename.substring(praefixLength));
            }
        }

        walk.close();

        if (fileSystem != null) {
            fileSystem.close();
        }

        return filenames;
    }

    public static List<String> listFilesInsideJarFile(String jarPath,
                                                      String path) {

        ArrayList<String> files = new ArrayList<String>();
        try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))) {
            Enumeration<JarEntry> entries = jar.entries(); // gives ALL entries
            // in
            // jar

            while (entries.hasMoreElements()) {
                String name = "/" + entries.nextElement().getName();
                if (name.startsWith(path) && name.endsWith(".xml")) { // filter
                    // according
                    // to the
                    // path
                    files.add(name);
                }
            }
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        return files;
    }

    public static List<String> listFilesInsideExternalModules(String path) {

        List<Path> externalJarFiles = ModuleManager.getAllExternalJarfiles();

        ArrayList<String> files = new ArrayList<String>();

        for (Path p : externalJarFiles) {
            files.addAll(listFilesInsideJarFile(p.toString(), path));
        }

        return files;
    }

    public static List<String> listAllResourceFiles(String path)
            throws URISyntaxException, IOException {
        ArrayList<String> files = new ArrayList<String>();

        files.addAll(listInternalModuleResourceFiles(path));
        files.addAll(listFilesInsideExternalModules(path));

        return files;
    }

    public static List<Path> listFiles(Path path, String suffix)
            throws IOException {

        ArrayList<Path> files = new ArrayList<Path>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    files.addAll(listFiles(entry, suffix));
                } else {

                    if (entry.toString().endsWith(suffix)) {
                        files.add(entry);
                    }

                }
            }
        }

        return files;

    }

    public static String readFile(String filename) throws IOException {

        // This works inside jarfile ...
        InputStream is = getInputStream(filename);

        StringBuilder sb = new StringBuilder();

        try {
            final int bufferSize = 16384;
            final char[] buffer = new char[bufferSize];

            Reader in = new InputStreamReader(is, "UTF-8");

            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                sb.append(buffer, 0, rsz);
            }
        } finally {
            try {
                is.close();
            } catch (Throwable ignore) {
            }
        }

        return sb.toString();
    }

    public static InputStream getInputStream(String filename) {
        InputStream is = StatementStore.class.getResourceAsStream(filename);

        if (is == null) {

            // ... this works outside jarfile
            is = StatementStore.class.getClassLoader().getResourceAsStream(
                    filename);

        }
        return is;
    }

    public static List<Path> listFilesAndDirectories(Path path, boolean withDirectories,
                                                     boolean withFiles, boolean recursively,
                                                     String suffix) throws IOException {

        List<Path> list = new ArrayList<Path>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {

                if (Files.isDirectory(entry)) {

                    if (withDirectories) {
                        list.add(entry);
                    }

                    if (recursively) {
                        list.addAll(listFilesAndDirectories(entry, withDirectories,
                                withFiles, recursively, suffix));
                    }
                }

                if (withFiles) {
                    if (suffix == null || entry.toString().endsWith(suffix)) {
                        list.add(entry);
                    }
                }
            }
        } catch (NoSuchFileException ex) {
            Logger logger = LoggerFactory.getLogger(FileTool.class);
            logger.info("Nothing found in path " + path.toString());
        }

        return list;

    }


}

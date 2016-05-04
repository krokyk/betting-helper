/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.parsers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JProgressBar;
import org.apache.log4j.Logger;

/**
 *
 * @author Kroky
 */
public class ParserManager {

    private static final Logger LOG = org.apache.log4j.Logger.getLogger(ParserManager.class);

    private static Map<String, Parser> parserMap;
    private static Parser[] parsers;
    private static final String PACKAGE_NAME = ParserManager.class.getPackage().getName() + ".impl";

    /**
     * NOT THREADSAFE!
     *
     * @return
     */
    public static Parser[] getAllParsers() {
        if (parsers == null) {
            init();
        }
        return parsers;
    }

    /**
     * NOT THREADSAFE!
     *
     * @return
     */
    public static Parser getParser(String name) {
        if (parserMap == null) {
            init();
        }
        return parserMap.get(name);
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static Set<Parser> findParsers(String packageName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        TreeSet<Parser> set = new TreeSet<Parser>();
        //class that resides somewhere in the package hierarchy
        //we suppose it shares the same location (has the same root package) as the package we ultimately want to search
        Class<ParserManager> someClass = ParserManager.class;
        URL location = someClass.getResource('/' + someClass.getName().replace(".", "/") + ".class");

        String path = URLDecoder.decode(location.getPath(), "UTF-8"); //in case we have %20 or similar in it

        //we have plain classes in some dir (parentDirOfClasses)
        //path will look similar to this:
        //   /D:/WorkData/Workspaces/workspace-Actimize-RCM/XXX/bin/org/kroky/PackageClassesLoader.class
        if (path.indexOf("file:") == -1) {
            //this is the parent package of the package we want to search
            //as we are going to deal with files (either plain files on filesystem
            //or entries in the jar file) we use slashes to separate the path components
            //e.g. org/kroky
            String thisClassPackagePath = someClass.getPackage().getName().replace(".", "/");
            String parentDirOfClasses = path.substring(0, path.indexOf(thisClassPackagePath)); ///D:/WorkData/Workspaces/workspace-Actimize-RCM/XXX/bin/
            File dirToSearch = new File(parentDirOfClasses + packageName.replace(".", "/")); ///D:/WorkData/Workspaces/workspace-Actimize-RCM/XXX/bin/ + org/kroky/packageFullOfClasses
            for (String fName : dirToSearch.list()) {
                Class<? extends Parser> c = Class.forName(packageName + "." + fName.substring(0, fName.indexOf("."))).asSubclass(Parser.class);
                Parser parserObj = c.newInstance();
                set.add(parserObj);
            }
        } else { //we have a jar file
            //path will look similar to this:
            //   file:/D:/classloader.jar!/org/kroky/PackageClassesLoader.class
            JarFile jarFile = new JarFile(path.substring("file:".length(), path.lastIndexOf("!")));
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().startsWith(packageName.replace(".", "/")) && !entry.isDirectory()) {
                    String className = entry.getName().replace("/", ".");
                    Class<? extends Parser> c = Class.forName(className.substring(0, className.lastIndexOf('.'))).asSubclass(Parser.class);
                    Parser parserObj = c.newInstance();
                    set.add(parserObj);
                }
            }
        }

        return set;
    }

    private static void init() {
        String message = "Loading parsers...";
        LOG.info(message);
        try {
            parsers = findParsers(PACKAGE_NAME).toArray(new Parser[0]);
        } catch (ClassNotFoundException ex) {
            LOG.error(ex);
        } catch (InstantiationException ex) {
            LOG.error(ex);
        } catch (IllegalAccessException ex) {
            LOG.error(ex);
        } catch (IOException ex) {
            LOG.error(ex);
        }
        parserMap = new LinkedHashMap<String, Parser>(parsers.length);
        for (int i = 0; i < parsers.length; i++) {
            parserMap.put(parsers[i].getName(), parsers[i]);
        }
        message = parsers.length + " parser" + (parsers.length == 1 ? "" : "s") + " found.";
        LOG.info(message);
    }

    public static Parser getParser(String name, JProgressBar progressBar) {
        AbstractParser parser = (AbstractParser) getParser(name);
        parser.setProgressBar(progressBar);
        return parser;
    }

    public static Parser detectSuitableParser(String text) {
        for (Parser p : getAllParsers()) {
            String regex = p.getPattern().pattern();
            Matcher m = Pattern.compile(regex, Pattern.MULTILINE).matcher(text);
            if (m.find()) {
                LOG.debug("Parser detected: " + p.getClass().getSimpleName());
                return p;
            }
        }
        String substr = text.substring(0, text.length() > 200 ? 200 : text.length());
        String message = "No parser detected for the given text: " + substr;
        if (text.length() > 200) {
            message = message + "... <showing first 200 characters>";
        }
        LOG.debug(message);
        return null;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.example;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.kroky.betting.common.util.Utils;

/**
 *
 * @author Kroky
 */
public class Example2 {

    public static void main(String[] args) throws IOException {
        String htmlFromUrl = Utils.getHtmlFromUrl("http://www.betexplorer.com/soccer/iran/persian-gulf-pro-league/fixtures/");
        FileUtils.writeStringToFile(new File("fixtures.html"), htmlFromUrl);
        htmlFromUrl = Utils.getHtmlFromUrl("http://www.betexplorer.com/soccer/iran/persian-gulf-pro-league/results/");
        FileUtils.writeStringToFile(new File("results.html"), htmlFromUrl);
    }
}

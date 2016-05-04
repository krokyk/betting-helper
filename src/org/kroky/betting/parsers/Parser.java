/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.betting.parsers;

import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 * @author Kroky
 */
public interface Parser {
    public Set<ParseResult> parse(String text);
    public String getName();
    public Pattern getPattern();
}

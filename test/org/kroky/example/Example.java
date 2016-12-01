/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kroky.example;

import java.util.HashSet;
import java.util.Set;
import org.kroky.betting.common.util.Utils;

/**
 *
 * @author Kroky
 */
public class Example {

    private static interface MyInterface {

        public String getName();
    }

    private static abstract class SuperClass implements MyInterface, Comparable<MyInterface> {

        String name;

        protected SuperClass(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return name;
        }

        @Override
        public int compareTo(MyInterface o) {
            return o.getName().compareToIgnoreCase(getName());
        }
    }

    private static class SubA extends SuperClass {

        public SubA() {
            super("www.betexplorer.com results (TEXT)");
        }
    }

    private static class SubB extends SuperClass {

        public SubB() {
            super("Tipos Profil (TEXT)");
        }
    }

    private static class SubC extends SuperClass {

        public SubC() {
            super("Tipos Vy\u017erebovanie z\u00e1pasov (TEXT)");
        }
    }

    public static void main(String[] args) {
        Set<MyInterface> set = new HashSet<MyInterface>();
        set.add(new SubA());
        set.add(new SubB());
        set.add(new SubC());
        System.out.println(set);
        System.out.println("www.betexplorer.com results (TEXT)".compareTo("Tipos Profil (TEXT)"));
        System.out.println("www.betexplorer.com results (TEXT)".compareTo("Tipos Vy\u017erebovanie z\u00e1pasov (TEXT)"));
        System.out.println("Tipos Vy\u017erebovanie z\u00e1pasov (TEXT)".compareTo("Tipos Profil (TEXT)"));
        Utils.getHtmlFromUrl("http://www.betexplorer.com/soccer/iran/persian-gulf-pro-league/fixtures/");
    }
}

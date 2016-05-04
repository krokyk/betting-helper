package example;

import java.util.Set;
import java.util.TreeSet;

public class Example {
    public static void main(String[] args) {
        Set<MyInterface> set = new TreeSet<MyInterface>();
        set.add(new ClassA("w"));
        set.add(new ClassB("Ta"));
        set.add(new ClassC("Tb"));
        System.out.println(set);
    }
    
    private static interface MyInterface {
        public String getName();
    }
    
    private static class SuperClass implements MyInterface, Comparable<MyInterface> {
        private String name;
        public SuperClass(String name) {
            this.name = name;
        }
        public String getName() { //metoda z MyInterface
            return name;
        }
        public int compareTo(MyInterface o) { //metoda z Comparable
            return o.getName().compareToIgnoreCase(this.getName());
        }
        public String toString() { //aby System.out vypisal nieco zrozumitelne
            return this.getClass().getSimpleName() + "(" + name + ")";
        }
    }
    
    private static class ClassA extends SuperClass {
        public ClassA(String name) { super(name); }
    }
    
    private static class ClassB extends SuperClass {
        public ClassB(String name) { super(name); }
    }
    
    private static class ClassC extends SuperClass {
        public ClassC(String name) { super(name); }
    }
}

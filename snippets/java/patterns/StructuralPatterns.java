package snippets.java.patterns;

import java.util.*;

/**
 * Structural Design Patterns in Java
 */
public class StructuralPatterns {

    // ---- 1. Decorator Pattern ----
    interface Coffee { double cost(); String description(); }

    static class SimpleCoffee implements Coffee {
        public double cost() { return 1.0; }
        public String description() { return "Coffee"; }
    }

    static abstract class CoffeeDecorator implements Coffee {
        protected Coffee coffee;
        CoffeeDecorator(Coffee c) { this.coffee = c; }
    }

    static class MilkDecorator extends CoffeeDecorator {
        MilkDecorator(Coffee c) { super(c); }
        public double cost() { return coffee.cost() + 0.25; }
        public String description() { return coffee.description() + ", Milk"; }
    }

    static class SugarDecorator extends CoffeeDecorator {
        SugarDecorator(Coffee c) { super(c); }
        public double cost() { return coffee.cost() + 0.10; }
        public String description() { return coffee.description() + ", Sugar"; }
    }

    // ---- 2. Adapter Pattern ----
    interface USPlug { void plugInUS(); }
    interface EUPlug { void plugInEU(); }

    static class USDevice implements USPlug {
        public void plugInUS() { System.out.println("US device plugged in"); }
    }

    static class EUAdapter implements EUPlug {
        private final USPlug device;
        EUAdapter(USPlug d) { this.device = d; }
        public void plugInEU() {
            System.out.println("Adapting...");
            device.plugInUS();
        }
    }

    // ---- 3. Facade Pattern ----
    static class HomeTheaterFacade {
        private Amplifier amp;
        private DVDPlayer dvd;
        private Projector projector;

        HomeTheaterFacade(Amplifier a, DVDPlayer d, Projector p) { amp=a; dvd=d; projector=p; }

        void watchMovie(String movie) {
            projector.on(); projector.setInput("DVD");
            amp.on(); amp.setVolume(10);
            dvd.on(); dvd.play(movie);
        }

        void endMovie() { dvd.off(); amp.off(); projector.off(); }
    }

    // Placeholder classes for Facade demo
    static class Amplifier { void on(){} void off(){} void setVolume(int v){} }
    static class DVDPlayer { void on(){} void off(){} void play(String m){} }
    static class Projector { void on(){} void off(){} void setInput(String s){} }

    // ---- 4. Proxy Pattern ----
    interface Image { void display(); }

    static class RealImage implements Image {
        private String filename;
        RealImage(String f) { this.filename = f; loadFromDisk(); }
        private void loadFromDisk() { System.out.println("Loading " + filename); }
        public void display() { System.out.println("Displaying " + filename); }
    }

    static class LazyImageProxy implements Image {
        private String filename;
        private RealImage realImage;
        LazyImageProxy(String f) { this.filename = f; }
        public void display() {
            if (realImage == null) realImage = new RealImage(filename); // lazy load
            realImage.display();
        }
    }

    public static void main(String[] args) {
        // Decorator
        Coffee c = new SugarDecorator(new MilkDecorator(new SimpleCoffee()));
        System.out.println(c.description() + " $" + c.cost()); // Coffee, Milk, Sugar $1.35

        // Adapter
        EUPlug adapter = new EUAdapter(new USDevice());
        adapter.plugInEU();

        // Proxy
        Image img = new LazyImageProxy("photo.png");
        img.display(); // loads now
        img.display(); // already loaded
    }
}

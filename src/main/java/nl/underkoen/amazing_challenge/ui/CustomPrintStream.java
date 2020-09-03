package nl.underkoen.amazing_challenge.ui;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * @author Under_Koen
 */
public class CustomPrintStream extends PrintStream {
    private final Consumer<String> onOut;
    private final StringBuilder stringBuilder = new StringBuilder();

    public CustomPrintStream(PrintStream out, Consumer<String> onOut) {
        super(out);
        this.onOut = onOut;
    }

    public PrintStream getOut() {
        return (PrintStream) super.out;
    }

    private void add(String s) {
        if (s.contains("\n")) {
            String[] split = s.split("\n", 2);
            stringBuilder.append(split[0]).append("\n");
            onOut.accept(stringBuilder.toString());
            stringBuilder.setLength(0);
            add(split[1]);
        } else {
            stringBuilder.append(s);
        }
    }
    
    @Override
    public void print(boolean b) {
        super.print(b);
        add(String.valueOf(b));
    }

    @Override
    public void print(char c) {
        super.print(c);
        add(String.valueOf(c));
    }

    @Override
    public void print(int i) {
        super.print(i);
        add(String.valueOf(i));
    }

    @Override
    public void print(long l) {
        super.print(l);
        add(String.valueOf(l));
    }

    @Override
    public void print(float f) {
        super.print(f);
        add(String.valueOf(f));
    }

    @Override
    public void print(double d) {
        super.print(d);
        add(String.valueOf(d));
    }

    @Override
    public void print(char[] s) {
        super.print(s);
        add(String.valueOf(s));
    }

    @Override
    public void print(String s) {
        super.print(s);
        add(String.valueOf(s));
    }

    @Override
    public void print(Object obj) {
        super.print(obj);
        add(String.valueOf(obj));
    }

    @Override
    public void println() {
        print("\n");
    }

    @Override
    public void println(boolean x) {
        print(x + "\n");
    }

    @Override
    public void println(char x) {
        print(x + "\n");
    }

    @Override
    public void println(int x) {
        print(x + "\n");
    }

    @Override
    public void println(long x) {
        print(x + "\n");
    }

    @Override
    public void println(float x) {
        print(x + "\n");
    }

    @Override
    public void println(double x) {
        print(x + "\n");
    }

    @Override
    public void println(char[] x) {
        print(x + "\n");
    }

    @Override
    public void println(String x) {
        print(x + "\n");
    }

    @Override
    public void println(Object x) {
        print(x + "\n");
    }

    @Override
    public PrintStream printf(String format, Object... args) {
        print(String.format(format, args));
        return this;
    }

    @Override
    public PrintStream printf(Locale l, String format, Object... args) {
        print(String.format(l, format, args));
        return this;
    }
}

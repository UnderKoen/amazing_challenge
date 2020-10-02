package nl.underkoen.amazing_challenge.controllers;

import java_cup.runtime.DefaultSymbolFactory;
import java_cup.runtime.SymbolFactory;
import nl.underkoen.amazing_challenge.components.Program;
import nl.underkoen.amazing_challenge.models.Glade;

/**
 * @author Under_Koen
 */
public class Runner {
    public static void compileAndRun(String code, Glade glade, PriceTable priceTable) throws Exception {
        Program program = compile(code);
        run(program, glade, priceTable);
    }

    public static Program compile(String code) throws Exception {
        code += "\n";

        SymbolFactory symbolFactory = new DefaultSymbolFactory();
        parser.Lexer lexer = new parser.Lexer(code, symbolFactory);
        parser.Parser parser = new parser.Parser(lexer, symbolFactory);
        return (Program) parser.parse().value;
    }

    public static void run(Program program, Glade glade, PriceTable priceTable) {
        Context context = new Context(priceTable, priceTable.getStartMoney(), glade);

        try {
            program.run(context);
        } catch (CompleteException e) {
            System.out.println(e.getMessage());
        }
    }
}

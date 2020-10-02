package parser;
import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;
import java.io.StringReader;

%%

%public
%class Lexer
%unicode
%cupsym ParserSym
%cup

%{
    SymbolFactory sf;

    public Lexer(String line, SymbolFactory sf) {
        this(new StringReader(line));
        this.sf = sf;
    }
%}

Boolean = [tf]
Variable = [a-z]
Space = [\t ]+
NewLine = [\n\t ]*\n[\n\t ]*

%%

{Space}             { return sf.newSymbol("SPACE", ParserSym.SPACE); }
{NewLine}           { return sf.newSymbol("NEW_LINE", ParserSym.NEW_LINE); }

/* expressions */
"^"                 { return sf.newSymbol("^", ParserSym.AND); }
"v"                 { return sf.newSymbol("v", ParserSym.OR); }
"-"                 { return sf.newSymbol("-", ParserSym.NOT); }
"->"                { return sf.newSymbol("->", ParserSym.IMPLICATION); }
"<>"                { return sf.newSymbol("<>", ParserSym.XNOR); }
"="                 { return sf.newSymbol("=", ParserSym.ASSIGN); }
"("                 { return sf.newSymbol("=", ParserSym.LPAREN); }
")"                 { return sf.newSymbol("=", ParserSym.RPAREN); }
"use"                 { return sf.newSymbol("use", ParserSym.USE); }
{Boolean}           { return sf.newSymbol("NUMBER", ParserSym.BOOLEAN, yytext() == "t"); }
{Variable}          { return sf.newSymbol("VARIABLE", ParserSym.VARIABLE,  yytext()); }


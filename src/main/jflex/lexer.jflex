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

Number = [0-9]+
Variable = [a-z]
Space = [\t ]+
NewLine = [\n\t ]*\n[\n\t ]*

%%

{Space}             { return sf.newSymbol("SPACE", ParserSym.SPACE); }
{NewLine}           { return sf.newSymbol("NEW_LINE", ParserSym.NEW_LINE); }

/* expressions */
"+"                 { return sf.newSymbol("+", ParserSym.PLUS); }
"-"                 { return sf.newSymbol("-", ParserSym.MINUS); }
"*"                 { return sf.newSymbol("*", ParserSym.TIMES); }
"/"                 { return sf.newSymbol("/", ParserSym.DIVIDE); }
"%"                 { return sf.newSymbol("%", ParserSym.MODULO); }
"="                 { return sf.newSymbol("=", ParserSym.ASSIGN); }
{Number}            { return sf.newSymbol("NUMBER", ParserSym.NUMBER, new Integer(yytext())); }
{Variable}          { return sf.newSymbol("VARIABLE", ParserSym.VARIABLE,  yytext()); }

/* statement */
"gebruik"           { return sf.newSymbol("USE", ParserSym.USE); }
"zolang"            { return sf.newSymbol("WHILE", ParserSym.WHILE); }
"als"               { return sf.newSymbol("IF", ParserSym.IF); }
"anders"            { return sf.newSymbol("ELSE", ParserSym.ELSE); }
"{"                 { return sf.newSymbol("OPEN", ParserSym.OPEN); }
"}"                 { return sf.newSymbol("CLOSE", ParserSym.CLOSE); }

/* compare */
"=="                { return sf.newSymbol("==", ParserSym.EQUAL); }
"!="                { return sf.newSymbol("!=", ParserSym.NOT_EQUAL); }
"<"                 { return sf.newSymbol("<", ParserSym.BIGGER); }
">"                 { return sf.newSymbol(">", ParserSym.SMALLER); }

/* functions */
"zwOog"             { return sf.newSymbol("zwOog", ParserSym.EYE); }
"kleurOog"          { return sf.newSymbol("kleurOog", ParserSym.COLOR_EYE); }
"kompas"            { return sf.newSymbol("kompas", ParserSym.COMPASS); }

"stapVooruit"       { return sf.newSymbol("stapVooruit", ParserSym.FORWARD); }
"stapAchteruit"     { return sf.newSymbol("stapAchteruit", ParserSym.BACKWARD); }
"draaiLinks"        { return sf.newSymbol("draaiLinks", ParserSym.LEFT); }
"draaiRechts"       { return sf.newSymbol("draaiRechts", ParserSym.RIGHT); }

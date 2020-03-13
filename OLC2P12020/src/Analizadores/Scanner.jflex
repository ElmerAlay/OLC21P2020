package analizadores;
import java_cup.runtime.Symbol; 
import abstracto.TError;
import java.util.LinkedList;

%% 

%{
    StringBuilder NuevoString = new StringBuilder();
    char NuevoChar;
    public LinkedList<TError> TablaEL = new LinkedList<TError>();
%}

%class Lexer
%cupsym Simbolo
%public 
%line 
%char
%column 
%cup 
%unicode
%ignorecase
%state STRING

%init{ 
    yyline = 1;
%init} 

numero      =   [0-9]+
flotante    =   {numero}\.{numero}
id          =   [a-zA-Z]([_.a-zA-Z]|{numero})* | "."(("."|[_a-zA-Z])(("."|[_a-zA-Z])|{numero})*)*
comentariolinea =   "#" ~"\n"
comentariomulti =   "#*" ~"*#"

%%

<YYINITIAL> {
    "null"              { return new Symbol(Simbolo.nulo, yycolumn, yyline, yytext().toLowerCase()); }
    "true"              { return new Symbol(Simbolo.verdadero, yycolumn, yyline, yytext().toLowerCase()); }
    "false"             { return new Symbol(Simbolo.falso, yycolumn, yyline, yytext().toLowerCase()); }
    
    "if"                { return new Symbol(Simbolo.si, yycolumn, yyline, yytext().toLowerCase()); }
    "else"              { return new Symbol(Simbolo.sino, yycolumn, yyline, yytext().toLowerCase()); }
    "switch"            { return new Symbol(Simbolo.swit, yycolumn, yyline, yytext().toLowerCase()); }
    "case"              { return new Symbol(Simbolo.caso, yycolumn, yyline, yytext().toLowerCase()); }
    "while"             { return new Symbol(Simbolo.whil, yycolumn, yyline, yytext().toLowerCase()); }
    "do"                { return new Symbol(Simbolo.do_, yycolumn, yyline, yytext().toLowerCase()); }
    "for"               { return new Symbol(Simbolo.for_, yycolumn, yyline, yytext().toLowerCase()); }
    "in"                { return new Symbol(Simbolo.in_, yycolumn, yyline, yytext().toLowerCase()); }

    "return"            { return new Symbol(Simbolo.retrn, yycolumn, yyline, yytext().toLowerCase()); }
    "continue"          { return new Symbol(Simbolo.cont, yycolumn, yyline, yytext().toLowerCase()); }
    "break"             { return new Symbol(Simbolo.brek, yycolumn, yyline, yytext().toLowerCase()); }
    "default"           { return new Symbol(Simbolo.def, yycolumn, yyline, yytext().toLowerCase()); }

    "+"                 { return new Symbol(Simbolo.plus, yycolumn, yyline, yytext().toLowerCase()); }
    "-"                 { return new Symbol(Simbolo.minus, yycolumn, yyline, yytext().toLowerCase()); }
    "*"                 { return new Symbol(Simbolo.mul, yycolumn, yyline, yytext().toLowerCase()); }
    "/"                 { return new Symbol(Simbolo.div, yycolumn, yyline, yytext().toLowerCase()); }
    "^"                 { return new Symbol(Simbolo.pot, yycolumn, yyline, yytext().toLowerCase()); }
    "%%"                { return new Symbol(Simbolo.mod, yycolumn, yyline, yytext().toLowerCase()); }

    "("                 { return new Symbol(Simbolo.ipar, yycolumn, yyline, yytext().toLowerCase()); }
    ")"                 { return new Symbol(Simbolo.fpar, yycolumn, yyline, yytext().toLowerCase()); }
    "["                 { return new Symbol(Simbolo.icor, yycolumn, yyline, yytext().toLowerCase()); }
    "]"                 { return new Symbol(Simbolo.fcor, yycolumn, yyline, yytext().toLowerCase()); }
    "{"                 { return new Symbol(Simbolo.illa, yycolumn, yyline, yytext().toLowerCase()); }
    "}"                 { return new Symbol(Simbolo.flla, yycolumn, yyline, yytext().toLowerCase()); }

    ">"                 { return new Symbol(Simbolo.gt, yycolumn, yyline, yytext().toLowerCase()); }
    "<"                 { return new Symbol(Simbolo.lt, yycolumn, yyline, yytext().toLowerCase()); }
    ">="                { return new Symbol(Simbolo.gte, yycolumn, yyline, yytext().toLowerCase()); }
    "<="                { return new Symbol(Simbolo.lte, yycolumn, yyline, yytext().toLowerCase()); }
    "=="                { return new Symbol(Simbolo.eq, yycolumn, yyline, yytext().toLowerCase()); }
    "!="                { return new Symbol(Simbolo.neq, yycolumn, yyline, yytext().toLowerCase()); }
    "?"                 { return new Symbol(Simbolo.tern, yycolumn, yyline, yytext().toLowerCase()); }

    "&"                 { return new Symbol(Simbolo.and, yycolumn, yyline, yytext().toLowerCase()); }
    "|"                 { return new Symbol(Simbolo.or, yycolumn, yyline, yytext().toLowerCase()); }
    "!"                 { return new Symbol(Simbolo.not, yycolumn, yyline, yytext().toLowerCase()); }

    "="                 { return new Symbol(Simbolo.asign, yycolumn, yyline, yytext().toLowerCase()); }
    ";"                 { return new Symbol(Simbolo.pyc, yycolumn, yyline, yytext().toLowerCase()); }
    ","                 { return new Symbol(Simbolo.colon, yycolumn, yyline, yytext().toLowerCase()); }
    ":"                 { return new Symbol(Simbolo.dp, yycolumn, yyline, yytext().toLowerCase()); }

    \"                  { 
                            yybegin(STRING); 
                            NuevoString.setLength(0);
                        }

    {numero}            { return new Symbol(Simbolo.numero, yycolumn, yyline, yytext().toLowerCase()); }
    {flotante}          { return new Symbol(Simbolo.flotante, yycolumn, yyline, yytext().toLowerCase()); }
    {id}                { return new Symbol(Simbolo.id, yycolumn, yyline, yytext().toLowerCase()); }
    
    {comentariolinea}   { /* ignore */ }
    {comentariomulti}   { /* ignore */ }
    
    [ \t\r\n\f]         {}
    /* Cualquier Otro */
    .                   { 
                            System.out.println("El caracter '"+yytext()+"' no pertenece al lenguaje.");
                            TError error = new TError(yytext(),"Léxico","Símbolo no reconocido",yyline,yycolumn);
                            TablaEL.add(error);
                        }
}

<STRING> {
    \"                  { 
                            yybegin(YYINITIAL); 
                            return new Symbol(Simbolo.cadena, NuevoString.toString());
                        }
    \\\"                { NuevoString.append('\"'); }
    \\\\                { NuevoString.append("\\"); } 
    \\n                 { NuevoString.append('\n'); }
    \\r                 { NuevoString.append('\r'); }
    \\t                 { NuevoString.append('\t'); }
    [ ]                 { NuevoString.append(' '); }
    [\r|\n|\r\n]        { 
                            yybegin(YYINITIAL);
                            System.out.println("String sin finalizar."); 
                            TError error = new TError(yytext(),"Léxico","Símbolo no reconocido",yyline,yycolumn);
                            TablaEL.add(error);
                        }
    .                   { NuevoString.append(yytext()); }
}

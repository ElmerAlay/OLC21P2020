package analizadores;
import java_cup.runtime.Symbol; 

%% 

%{
    StringBuilder NuevoString = new StringBuilder();
    char NuevoChar;
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
id          =   [a-zA-Z]([_.a-zA-Z]|{numero})* | [.a-zA-Z](([_.a-zA-Z]){numero})*
comentariolinea =   "#" ~"\n"
comentariomulti =   "#*" ~"*#"

%%

<YYINITIAL> {
    "null"              { return new Symbol(Simbolo.nulo, yycolumn, yyline, yytext().toLowerCase()); }
    "true"              { return new Symbol(Simbolo.verdadero, yycolumn, yyline, yytext().toLowerCase()); }
    "false"             { return new Symbol(Simbolo.falso, yycolumn, yyline, yytext().toLowerCase()); }

    "+"                 { return new Symbol(Simbolo.plus, yycolumn, yyline, yytext().toLowerCase()); }
    "-"                 { return new Symbol(Simbolo.minus, yycolumn, yyline, yytext().toLowerCase()); }
    "*"                 { return new Symbol(Simbolo.mul, yycolumn, yyline, yytext().toLowerCase()); }
    "/"                 { return new Symbol(Simbolo.div, yycolumn, yyline, yytext().toLowerCase()); }
    "^"                 { return new Symbol(Simbolo.pot, yycolumn, yyline, yytext().toLowerCase()); }
    "%%"                { return new Symbol(Simbolo.mod, yycolumn, yyline, yytext().toLowerCase()); }

    "("                 { return new Symbol(Simbolo.ipar, yycolumn, yyline, yytext().toLowerCase()); }
    ")"                 { return new Symbol(Simbolo.fpar, yycolumn, yyline, yytext().toLowerCase()); }

    ">"                 { return new Symbol(Simbolo.gt, yycolumn, yyline, yytext().toLowerCase()); }
    "<"                 { return new Symbol(Simbolo.lt, yycolumn, yyline, yytext().toLowerCase()); }
    ">="                { return new Symbol(Simbolo.gte, yycolumn, yyline, yytext().toLowerCase()); }
    "<="                { return new Symbol(Simbolo.lte, yycolumn, yyline, yytext().toLowerCase()); }
    "=="                { return new Symbol(Simbolo.eq, yycolumn, yyline, yytext().toLowerCase()); }
    "!="                { return new Symbol(Simbolo.neq, yycolumn, yyline, yytext().toLowerCase()); }

    "&"                 { return new Symbol(Simbolo.and, yycolumn, yyline, yytext().toLowerCase()); }
    "|"                 { return new Symbol(Simbolo.or, yycolumn, yyline, yytext().toLowerCase()); }
    "!"                 { return new Symbol(Simbolo.not, yycolumn, yyline, yytext().toLowerCase()); }

    "="                 { return new Symbol(Simbolo.asign, yycolumn, yyline, yytext().toLowerCase()); }
    ";"                 { return new Symbol(Simbolo.pyc, yycolumn, yyline, yytext().toLowerCase()); }

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
    [ \r|\n|\r\n]       { 
                            yybegin(YYINITIAL);
                            System.out.println("String sin finalizar."); 
                        }
    .                   { NuevoString.append(yytext()); }
}

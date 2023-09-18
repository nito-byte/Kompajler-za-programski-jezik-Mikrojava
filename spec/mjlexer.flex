package rs.ac.bg.etf.pp1;

////////////////////////////////////// IMPORT SEKCIJA
import java_cup.runtime.Symbol;

%%


////////////////////////////////////// SEKCIJA DIREKTIVA 
%{
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}
%}

%cup    
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

/////////////////////////////////////// REGULARNI IZRAZI

" " 	{ }    //nema akcije za bele i kontrolne znakove
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program"   { return new_symbol(sym.PROGRAM,  yytext()); }    //yytext vraca sekvencu znakova koja je trenutno procitana!
"break"		{ return new_symbol(sym.BREAK, yytext()); } 
"else"		{ return new_symbol(sym.ELSE,  yytext()); } 
"const"		{ return new_symbol(sym.CONST, yytext()); } 
"if"		{ return new_symbol(sym.IF,    yytext()); } 
"new"		{ return new_symbol(sym.NEW,   yytext()); } 
"print"		{ return new_symbol(sym.PRINT, yytext()); } 
"read"		{ return new_symbol(sym.READ,  yytext()); } 
"return"	{ return new_symbol(sym.RETURN,yytext()); } 
"void"		{ return new_symbol(sym.VOID,  yytext()); }

//"extends"		 { return new_symbol(sym.EXTENDS, yytext()); } 
//"class"		 { return new_symbol(sym.CLASS,   yytext()); } 
//"enum"		 { return new_symbol(sym.ENUM,    yytext()); } 
//"switch"		 { return new_symbol(sym.SWITCH,  yytext()); } 
//"do"			 { return new_symbol(sym.DO,      yytext()); } 
//"while"		 { return new_symbol(sym.WHILE,   yytext()); } 
//"continue"	 { return new_symbol(sym.CONTINUE,yytext()); } 
//"case"		 { return new_symbol(sym.CASE,    yytext()); } 


<YYINITIAL> "+"  { return new_symbol(sym.PLUS,        yytext()); }
"-" 		     { return new_symbol(sym.MINUS,       yytext()); }	
"*" 			 { return new_symbol(sym.MUL, 	      yytext()); }	
"/" 			 { return new_symbol(sym.DIV, 	      yytext()); }	
"%" 			 { return new_symbol(sym.MOD, 	      yytext()); }	
"==" 			 { return new_symbol(sym.EQUALEQUAL,  yytext()); }	
"!=" 			 { return new_symbol(sym.NOTEQUAL,    yytext()); }		
">" 			 { return new_symbol(sym.GREATER,     yytext()); }	
">=" 			 { return new_symbol(sym.GREATEREQUAL,yytext());}	
"<" 			 { return new_symbol(sym.LESS,        yytext()); }	
"<=" 			 { return new_symbol(sym.LESSEQUAL,   yytext()); }		
"&&" 			 { return new_symbol(sym.AND,         yytext()); }	
"||" 			 { return new_symbol(sym.OR,          yytext()); }

"=" 			 { return new_symbol(sym.EQUAL,        yytext()); }	
"++" 			 { return new_symbol(sym.PLUSPLUS,     yytext()); }	
"--" 			 { return new_symbol(sym.MINUSMINUS,   yytext()); }	
";" 			 { return new_symbol(sym.SEMI,         yytext()); }	
"," 			 { return new_symbol(sym.COMMA,        yytext()); }	
"." 			 { return new_symbol(sym.DOT,          yytext()); }	
"(" 			 { return new_symbol(sym.LEFTPAREN,    yytext()); }
")" 			 { return new_symbol(sym.RIGHTPAREN,   yytext()); }	
"{" 			 { return new_symbol(sym.LEFTBRACE,    yytext()); }	
"}" 			 { return new_symbol(sym.RIGHTBRACE,   yytext()); }
"[" 			 { return new_symbol(sym.LEFTSQUAREBRACE,yytext()); }
"]" 			 { return new_symbol(sym.RIGHTSQUAREBRACE,yytext()); }
"?" 			 { return new_symbol(sym.QUESTION,     yytext()); }
":" 			 { return new_symbol(sym.DOTDOT,       yytext()); }



<YYINITIAL> "//" { yybegin(COMMENT);   }     //prelazimo u stanje obrade komentara
<COMMENT> .      { yybegin(COMMENT);   }     //tacka predstavlja bilo koji znak
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

[0-9]+ 						    { return new_symbol (sym.CONSTANTNUM, new Integer (yytext())); }
("true"|"false")				{ return new_symbol (sym.CONSTANTBOOL, new Integer(java.lang.Boolean.parseBoolean(yytext())?1:0)); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{ return new_symbol (sym.IDENT, yytext()); }
"'"[\x20-\x7E]"'" 				{ return new_symbol (sym.CONSTANTCHAR, new Character (yytext().charAt(1)));}
//"\""[\x20-\x7E]*"\"" 			{ return new_symbol (sym.CONSTANTSTRING, new String (yytext().substring(1, yytext().length()-1))); }

. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)+" i koloni "+(yycolumn)); return new_symbol (sym.error, yytext());  }







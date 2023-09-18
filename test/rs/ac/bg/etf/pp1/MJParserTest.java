package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class MJParserTest {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(MJParserTest.class);
		
		Reader br = null;
		try {
			File sourceCode = new File("test/program.mj");
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  //pocetak parsiranja
	        
	        Program prog = (Program)(s.value); 
	        Tab2.init();
			// ispis sintaksnog stabla
			log.info(prog.toString(""));
			log.info("===================================");

			// ispis prepoznatih programskih konstrukcija
			SemanticCheck v = new SemanticCheck();
			//RuleVisitor v = new RuleVisitor();
			prog.traverseBottomUp(v); 
			Tab.dump();  //Tab.dump(new DSTV());
	      
			//log.info(" Print count calls = " + v.printCallCount);
			//log.info(" Deklarisanih promenljivih ima = " + v.varDeclCount);
			
			//if (!p.errorDetected && !sem.errorDetected) {
			if (!p.errorDetected && v.passed()) {
				
				File objFile = new File("test/program.obj");
	        	log.info("Generating bytecode file: " + objFile.getAbsolutePath());
	        	if (objFile.exists())
	        		objFile.delete();
	        	
				CodeGenerator cg = new CodeGenerator(); 
				prog.traverseBottomUp(cg);
				Code.write(new FileOutputStream(objFile));
				
	        	log.info("Parsiranje uspesno zavrseno!");
			}
			else {
				log.error("Parsiranje nije uspesno zavrseno!");
			}
			
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

	}
}
	
	
package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;
import jdk.internal.org.objectweb.asm.commons.Remapper;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.ac.bg.etf.pp1.*;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.test.Compiler;
import rs.ac.bg.etf.pp1.test.CompilerError;
import rs.ac.bg.etf.pp1.test.CompilerError.CompilerErrorType;

public class CompilerImpl implements Compiler {
	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}



	public boolean sintaksnaGreskaPoruka = false;

	public static void main(String[] args) throws IOException {
		CompilerImpl c = new CompilerImpl();
		
		if (args.length < 2) {
			System.out.println("Not enough arguments!");
			return;
		} 
		//File sourceFilePath = new File(args[0]);
		//File outputFilePath = new File(args[1]);

		List<CompilerError> lista = c.compile(args[0], args[1]);
		//List<CompilerError> lista = c.compile("", "");

	}

	@Override
	public List<CompilerError> compile(String sourceFilePath, String outputFilePath) {
		List<CompilerError> listLex = new ArrayList<CompilerError>();
		List<CompilerError> listSin = new ArrayList<CompilerError>();
		List<CompilerError> listSem = new ArrayList<CompilerError>();

		listLex = lexickaProvera(sourceFilePath);
		if (!listLex.isEmpty()) {
			/// ********LEKSICKA OBRADA*********///
			String izlaz = listLex.toString();
			System.out.println("Lexicka analiza nije uspesno zavrsena!");
			System.out.println(izlaz);
		} else {
			/// ********SINTAKSNA OBRADA*********///
			System.out.println("Lexicka analiza uspesno zavrsena!");
			listSin = sintaksnaProvera(sourceFilePath,outputFilePath);
			if (!listSin.isEmpty()) {
				String izlaz = listSin.toString();
				if (sintaksnaGreskaPoruka) {
					System.out.println("Sintaksna analiza nije uspesno zavrsena!");
					sintaksnaGreskaPoruka = false;
				}
				System.out.println(izlaz);
			} else {
				System.out.println("Sintaksna analiza uspesno zavrsena!");

				/// ********SEMANTICKA OBRADA*********///
				// listSem = semantickaProvera();
				// if (!listSem.isEmpty()) {
				// String izlaz = listSem.toString();
				// System.out.println("Semanticka analiza nije uspesno zavrsena!");
				// System.out.println(izlaz);
				// } else {
				// System.out.println("Semanticka analiza je uspesno zavrsena!");

				// }

			}

		}

		
		List<CompilerError> combined = new ArrayList<CompilerError>();
		combined.addAll(listLex);
		combined.addAll(listSin);
		combined.addAll(listSem);
		return combined;
		// return null;

	}

	private List<CompilerError> lexickaProvera(String sourceFilePath) {
		Logger log = Logger.getLogger(MJTest.class);
		Reader br = null;

		List<CompilerError> list = new ArrayList<CompilerError>();

		try {
			File sourceCode = new File(sourceFilePath);
			//File sourceCode = new File("test/program.mj");
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());

			try {
				br = new BufferedReader(new FileReader(sourceCode));
				Yylex lexer = new Yylex(br);
				Symbol currToken = null;
				while ((currToken = lexer.next_token()).sym != sym.EOF) {

					if (currToken != null && currToken.value != null)
						log.info(currToken.toString() + " " + currToken.value.toString());

					// ako je doslo do lexicke greske ulazim ovde
					if (currToken.sym == sym.error) {
						// System.out.println(currToken.value.toString() + " u redu " + currToken.left);

						int linija = currToken.left;
						CompilerErrorType type = CompilerErrorType.LEXICAL_ERROR;
						String poruka = "Leksicka greska (" + currToken.value.toString() + ") u liniji "
								+ currToken.left +", a u koloni "+ currToken.right;

						CompilerError ispisLexGreske = new CompilerError(linija, poruka, type);
						//list.clear();
						list.add(ispisLexGreske);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e1) {
					log.error(e1.getMessage(), e1);
				}
		}
		return list;

	}

	private List<CompilerError> sintaksnaProvera(String sourceFilePath, String outputFilePath) {

		Logger log = Logger.getLogger(MJParserTest.class);
		Reader br = null;

		List<CompilerError> list = new ArrayList<CompilerError>();
		try {
			/*
			 * if (args.length < 2) { log.error("Not enough arguments!"); return; }
			 */
			
			
			File sourceCode = new File(sourceFilePath);
			//File sourceCode = new File("test/program.mj");
			// File sourceCode = new File(args[0]);
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());

			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);

			MJParser p = new MJParser(lexer);

			Symbol s = p.parse(); // pocetak parsiranja; ovde ce da se ispozivaju report_error...
			int redGreske = s.left;
			String ss = log.toString();
			int red = lexer.next_token().left;
			
			if (p.errorDetected) {

				CompilerErrorType type = CompilerErrorType.SYNTAX_ERROR;
				String poruka = "Sintaksna greska (" + s.value.toString() + ") u liniji " + redGreske;   //umesto ...+ ") u liniji " + red;
				CompilerError ispisSintaksneGreske = new CompilerError(red, poruka, type);
				list.clear();
				list.add(ispisSintaksneGreske);
				sintaksnaGreskaPoruka = true;
				// log.info("Parsiranje nije uspesno zavrseno!");

			} else {
				Program prog = (Program) (s.value);
				log.info(prog.toString("")); // ispis sintaksnog stabla
				log.info("===================================");
				Tab2.init();
				System.out.println("Sintaksna analiza uspesno izvrsena!");

				SemanticCheck sem = new SemanticCheck();
				prog.traverseBottomUp(sem);
				
				log.info("Broj simbolickih konstanti je: "+sem.constDeclarationCount);
				log.info("Broj globalnih konstanti je: "+sem.varDeclarationCount);
				log.info("Broj lokalnih promenljivih konstanti je: "+sem.locDeclarationCount);
				log.getLoggerRepository().getCurrentLoggers();

				Tab.dump();  //Tab.dump (new DSTV());
				Enumeration<Logger> allLoggers = log.getLoggerRepository().getCurrentLoggers();

				if (!p.errorDetected && !sem.errorDetected) {
					//File objFile = new File(outputFilePath);
					File objFile = new File("test/program.obj");
					log.info("Generating bytecode file: " + objFile.getAbsolutePath());
					if (objFile.exists())
						objFile.delete();

					CodeGenerator cg = new CodeGenerator();
					prog.traverseBottomUp(cg);
					Code.write(new FileOutputStream(objFile));
					log.info("Parsiranje uspesno zavrseno!");
				} else {
					list.clear();
					CompilerErrorType type = CompilerErrorType.SEMANTIC_ERROR;
					// String poruka = "Semanticka greska (" + s.value.toString() + ") u liniji " +
					// red;
					for (int i = 0; i < sem.listaSemantickihGresaka.size(); i++) {
						String poruka = sem.listaSemantickihGresaka.get(i);
						String linija = poruka.substring(poruka.lastIndexOf(" ") + 1);
						CompilerError ispisSintaksneGreske = new CompilerError(Integer.parseInt(linija), poruka + '\n',
								type);
						System.out.println();
						list.add(ispisSintaksneGreske);
					}

					log.info("Parsiranje nije uspesno zavrseno!");
				}

			}
			// log.info(" Deklarisanih promenljivih ima = " + v.varDeclCount);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e1) {
					log.error(e1.getMessage(), e1);
				}
		}
		return list;
	}

}

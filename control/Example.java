package control;

import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

public class Example {

	public static void main(String[] args) {
		TerminalApp test=new TerminalApp();
		test.addCommand("hello", Example::hello);
		test.addCommand("param", Example::param);
		test.addCommand("nom", Example::nom);
		test.addKeyWords(new String[] {"a", "b","c"},"hello");
		test.importKeywords(new File("./src/control/commandArgs"));
		test.start();
	}
	
	public static void hello(String command1,Scanner sc,PrintStream out) {
		out.println("HELLO WORLD !!!");
	}

	public static void param(String command1,Scanner sc,PrintStream out) {
		for(String arg:command1.split(" ")) {
			out.println("argument "+arg);
		}
	}
	

	public static void nom(String command1,Scanner sc,PrintStream out) {
		out.println("quel est votre nom ?");
		String nom=sc.nextLine();
		out.println("bonjour "+nom);
	}
}

package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * 
 * this class is used to quickly create terminal-like application,
 * you just have to create the commands method and link keywords to them and this class take care of the rest
 * 
 * @author PhysicDev
 * 
 * @version 1.0
 *
 */
public class TerminalApp extends Thread {
	/**
	 * this variable store every command, and every keyword associated with this command
	 * 
	 * the command is an instance of TriConsumer
	 * @see control.TriConsumer
	 */
	private HashMap<Set<String>,TriConsumer<String,Scanner,PrintStream>> commands=new HashMap<Set<String>,TriConsumer<String,Scanner,PrintStream>>();
	
	/**
	 * the scanner used to retrieve the command info
	 */
	private Scanner control;
	
	/**
	 * the Input stream used by the application
	 */
	private InputStream source;
	
	/**
	 * the output stream used by the application
	 */
	private PrintStream output;
	
	/**
	 * this message is shown when the application is started
	 */
	private String welcomeMessage="";
	
	/**
	 * this prefix is shown to indicate the user that the app is ready to receive a command
	 */
	private String awaitCommand=">>> ";
	
	/**
	 * this message is shown when the command is not found
	 */
	private	String defaultMessage="ERROR : unkown command";
	
	/**
	 * this variable control if you want to find keyword with or without ignoreCase.
	 */
	private boolean ignoreCase=false;
	
	
	/**
	 * to avoid conflict between thread, the terminal is lock when it's running. If you have to bring modification while it's running, set this variable to true
	 */
	public boolean unlockMod=false;
	
	/**
	 * set the Ignore case variable
	 * @param IC new Ignore case variable
	 */
	public void setIgnoreCase(boolean IC) {
		if(!AliveCheck()||unlockMod)
			return;
		ignoreCase=IC;
	}
	
	/**
	 * set the input Stream (this method reset the Scanner)
	 * @param input the input Stream
	 */
	public void setSource(InputStream input) {
		if(!AliveCheck()||unlockMod)
			return;
		source=input;
		control=new Scanner(source);
	}
	
	/**
	 * set the output Stream
	 * @param output the input Stream
	 */
	public void setOutput(PrintStream output) {
		if(!AliveCheck()||unlockMod)
			return;
		this.output=output;
	}
	
	/**
	 * set the Default Message
	 * @param DM the message
	 */
	public void setDefaultMessage(String DM) {
		if(!AliveCheck()||unlockMod)
			return;
		defaultMessage=DM;
	}

	/**
	 * set the Welcome Message
	 * @param WM the message
	 */
	public void setWelcomeMessage(String WM) {
		if(!AliveCheck()||unlockMod)
			return;
		welcomeMessage=WM;
	}

	/**
	 * set the prefix of the command, the prefix can't contain line break
	 * @param comLine the prefix
	 */
	public void setComLine(String comLine) {
		if(!AliveCheck()||unlockMod)
			return;
		if(comLine.contains("\n"))
			throw new IllegalArgumentException("can't have new line in comLine");
		awaitCommand=comLine;
	}
	
	
	//incroyable la doc des getters et setters ...
	/**
	 * get the source
	 * @return the source
	 */
	public InputStream getSource() {
		return source;
	}
	
	/**
	 * get the output
	 * @return the output
	 */
	public OutputStream getOuptut() {
		return output;
	}
	
	/**
	 * get the welcome message
	 * @return the welcome message
	 */
	public String getWelcomeMessage() {
		return welcomeMessage;
	}
	
	/**
	 * get default message
	 * @return the default message
	 */
	public String getDefaultMessage() {
		return defaultMessage;
	}
	
	/**
	 * get the command prefix
	 * @return the command prefix
	 */
	public String getComLine() {
		return awaitCommand;
	}
	
	/**
	 * get all the keyword associated with one keyword
	 * @param keyword a keyword of the list
	 * @return the list of all the command
	 */
	public String[] GetCommandKeyword(String keyword) {
		for(Set<String> keys:commands.keySet())
			if(keys.contains(keyword))
				return (String[]) keys.toArray();
		return null;
	}
	
	/**
	 * create a terminal app, by default use the standart input and output stream (System.in and System.out)
	 * 
	 * @see #TerminalApp(InputStream, PrintStream)
	 * @see #TerminalApp(InputStream, PrintStream, String)
	 */
	public TerminalApp() {
		this(System.in,System.out,"");
	}
	
	/**
	 * create a terminal app using input and output stream (the output Stream is a PrintStream as the app will use text)
	 * @param input the input stream
	 * @param output the output stream
	 * 
	 * @see #TerminalApp()
	 * @see #TerminalApp(InputStream, PrintStream, String)
	 */
	public TerminalApp(InputStream input,PrintStream output) {
		this(input,output,"");
	}
	
	/**
	 * create a terminal app using input and output stream (the output Stream is a PrintStream as the app will use text)
	 * @param input the input stream
	 * @param output the output stream
	 * @param message the message printed by the app on startup
	 * 
	 * @see #TerminalApp()
	 * @see #TerminalApp(InputStream, PrintStream)
	 */
	public TerminalApp(InputStream input,PrintStream output,String message) {
		control=new Scanner(input);
		source=input;
		this.output=output;
		welcomeMessage=message;
	}
	
	/**
	 * add a new command with one keyword 
	 * @param keyword the keyword
	 * @param command the command
	 * 
	 * @see #addCommand(String[], TriConsumer)
	 */
	public void addCommand(String keyword,TriConsumer<String,Scanner,PrintStream> command){
		addCommand(new String[] {keyword},command);
	}
	
	/**
	 * add a command with multiple keyword
	 * @param keywords an array containing all the keywords
	 * @param command the command
	 * 
	 * @see #addCommand(String, TriConsumer)
	 */
	public void addCommand(String[] keywords,TriConsumer<String,Scanner,PrintStream> command){
		if(!AliveCheck()||unlockMod)
			return;
		Set<String> keys=new HashSet<String>();
		keys.addAll(Arrays.asList(keywords));
		commands.put(keys,command);
	}
	
	/**
	 * change the command associated to one keyword
	 * @param keyword the keyboard used for searching the command
	 * @param command the new command
	 * 
	 * 
	 */
	public void changeCommand(String keyword,TriConsumer<String,Scanner,PrintStream> command) {
		if(!AliveCheck()||unlockMod)
			return;
		for(Set<String> keys:commands.keySet())
			if(keys.contains(keyword)) {
				commands.put(keys, command);
				return;
			}
	}
	
	/**
	public void test() {
		for(Set<String> keys:commands.keySet())
			System.out.println(commands.get(keys));
	}**/
	
	
	/**
	 * add new keywords to a command, an additionel keyword is used to find the corresponding commands
	 * @param newKeyword an array containing the new keywords
	 * @param keyword the keyword used for searching the command
	 * 
	 * @see #importKeywords(File)
	 * @see #importKeywords(File,char)
	 */
	public void addKeyWords(String[] newKeyword,String keyword) {
		if(!AliveCheck()||unlockMod)
			return;
		for(Set<String> keys:commands.keySet())
			if(keys.contains(keyword)) {
				HashSet<String> newkeys=new HashSet<String>(keys);
				newkeys.addAll(Arrays.asList(newKeyword));
				commands.put(newkeys, commands.get(keys));
				commands.remove(keys);
				return;
			}
	}
	
	/**
	 * import a keyword from a file,
	 * 
	 * the file structure constist of a succession of word separated by a specific character ("," by default)
	 * each line represent the keywords of one command, the first keywords is used to search for the corresponding command.
	 * @param file the file containing the data
	 * 
	 * @see #addKeyWords(String[], String)
	 * @see #importKeywords(File, char)
	 */
	public void importKeywords(File file) {
		importKeywords(file,',');
	}
	

	/**
	 * import a keyword from a file,
	 * 
	 * the file structure constist of a succession of word separated by a specific character ("," by default)
	 * each line represent the keywords of one command, the first keywords is used to search for the corresponding command.
	 * @param file the file  containing the data
	 * @param sep the character used as separator
	 * 
	 * @see #addKeyWords(String[], String)
	 * @see #importKeywords(File)
	 */
	public void importKeywords(File file,char sep) {
		BufferedReader buffer;
		try {
			buffer = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String line;
			while((line=buffer.readLine())!=null) {
				String[] keywords=line.split(" ");
				this.addKeyWords(keywords, keywords[0]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * this method is  run the teminal application. you can call it by doing apt.start() where apt is an instance of this class
	 * 
	 * @see java.lang.Thread#start()
	 */
	public void run() {
		//the terminal application
		control.reset();
		output.println("");
		output.println(welcomeMessage);
		loop:while(true) {
			output.println();
			output.print(awaitCommand);
			String cmd=control.nextLine().trim();
			String keyword=cmd.split(" ")[0];
			//finding keyword
			for(Set<String> keys:commands.keySet()) {
				if(ignoreCase){//case ignore case
					for(String word:keys)
						if(word.equalsIgnoreCase(keyword)){//if keyword found executing command
							commands.get(keys).accept(cmd, control, output);
							continue loop;
						}
				}else{//case not ignore case
					if(keys.contains(keyword)) {
						commands.get(keys).accept(cmd, control, output);
						continue loop;
					}
				}
			}
			//if no command found : 
			output.println(defaultMessage);
		}
		
	}
	
	/**
	 * this private method check if the terminal app is started (created for making alive check more space efficient)
	 * @return whenever the terminal is on or off
	 */
	private boolean AliveCheck() {
		if(this.isAlive()) {
			System.err.println("stop the terminal before making modification to the application");
			return false;
		}
		return true;
	}
	
	/**
	 * this static method could be usefull to split the command line in an array of parameter (including the command name)
	 * @param command the command line
	 * @return an array containing all the parameter
	 */
	public static String[] splitArgs(String command) {
		return command.trim().replaceAll(" +"," ").split(" ");
	}
	
	/**
	 * built in Help command if you want (just list the command of the app without description)
	 * @param cmd the command line
	 * @param sc the app scanner
	 * @param out the app output
	 */
	public void Help(String cmd,Scanner sc,PrintStream out) {
		for(Set<String> keys:commands.keySet()) {
			for(String key:keys)
				out.print(key+" ");
			out.println("");
		}
	}
	
	/**
	 * build in keywords list for the help command
	 * 
	 * @see #Help(String, Scanner, PrintStream)
	 */
	public static String[] helpKeyWord=new String[] {"help","H","--help"};
}

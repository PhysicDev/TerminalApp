# TerminalApp

A simple java tool to create rapidly terminal-like application

# How to use ?

to use the library, download the jar file and add it to the ClassPath environment variable. 
then import the package to your java code with the line

```java
import control.*;
```

you can create then your application, create the method of your command one by one then add them to your App like so

```java
TerminalApp test=new TerminalApp(); //create instance of terminal app
test.addCommand("hello", Example::hello); //add commands (hello is a static method of the class Example)
test.addCommand("param", Example::param);
```

the terminal inherit the java Thread class so you can lanch the App like a thread

```java
test.start();
```

commands recieve 3 parametters : 
1. the full command line with the command name and all the potential parameter of the commands
2. the Scanner instance of the TerminalApp in case more informations are needed from the user
3. the PrintStream of the TerminalApp to print result

you can attach as many keyword as you want to each commands.

you can add multiple keywords easily using the importFile method of TerminalApp and a file with the following structure : 
```txt
mainKeyword,keyword,keyword,keyword,....
mainKeyword,keyword,keyword,keyword,....
etc ...
```

the main keyword is a keyword that is already attach to the corresponding command.

For more information about the code, i will add a javadoc soon that explain it in detail (but you can already read it in the code).

# Future update

I've created this for my personal use at first but i think it could be usefull to someone, the program is not very elaborated so if you have suggestion of addition (or if you find bugs) don't hesitate to tell me about it in the correspoding section.
For now, no addition is planed for this library.

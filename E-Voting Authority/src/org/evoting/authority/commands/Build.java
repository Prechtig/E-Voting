package org.evoting.authority.commands;

import org.evoting.security.Security;

import jolie.runtime.JavaService;

public class Build extends Command{
	public final static String KEYWORD = "build";
	private static String invalidArguments = "Invalid arguments.";
	
	public Build(String[] args) {
		super(args);
	}
	
	public String execute(JavaService js) {
		if(args.length < 2 || !isNumber(args[1])) {
			return invalidArguments;
		}
		
		Security.buildCache(Long.parseLong(args[1]));
		return "Build cache.";
	}
	
	private static boolean isNumber(String str)
	{
		if(str.equals("")) {
			return false;
		}
	    for(char c : str.toCharArray()) {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
}

package org.evoting.authority.commands;

import jolie.runtime.JavaService;

public class Exit extends Command {
	
	public static final String KEYWORD = "exit";
	private String message = "Exiting";
	
	public Exit(String[] args) {
		super(args);
	}
	
	public String execute(JavaService js) {
		return message;
	}
}

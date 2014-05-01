package org.evoting.authority;

import jolie.runtime.JavaService;

import org.evoting.authority.commands.Command;

public class Controller extends JavaService{
	
	public void run()
	{
		Command command;
		while(true) {
			command = ConsoleIO.getUserInput();
			System.out.println(command.execute(this));
		}
	}
}

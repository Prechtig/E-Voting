package org.evoting.authority;

import jolie.runtime.JavaService;

import org.evoting.authority.commands.Command;

public class Controller extends JavaService{
	
	public void run()
	{
		Command command;
		while(true) {
			//TODO Exit the loop by modifiying exit somehow.
			command = ConsoleIO.getUserInput();
			System.out.println(command.execute(this));
		}
	}
}

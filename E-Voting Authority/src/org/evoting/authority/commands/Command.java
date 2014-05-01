package org.evoting.authority.commands;

import jolie.runtime.JavaService;

public abstract class Command {
	public String[] args;
	
	public Command(String[] args) {
		this.args = args;
	}
	
	public abstract String execute(JavaService js);
}

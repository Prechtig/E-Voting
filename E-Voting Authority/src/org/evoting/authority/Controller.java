package org.evoting.authority;

import jolie.runtime.JavaService;

import org.evoting.authority.commands.Command;
import org.evoting.authority.commands.Exit;

public class Controller extends JavaService {

	private static final String UNKNOWN_COMMAND = "Could not find any command matching the input.";

	public void run() {
		Command command;
		while (true) {
			command = ConsoleIO.getUserInput();
			// Command not found
			if (command == null) {
				System.out.println(UNKNOWN_COMMAND);
			} else {
				System.out.println(command.execute(this));
				// Special case
				if (command instanceof Exit) {
					break;
				}
			}
		}
	}

	public static void main(String[] args) {
		// For making it executable

		// FOR DEBUGGING START// TODO:REMOVE WHEN DONE

		// Controller c = new Controller();
		// c.run();

		// FOR DEBUGGING END// TODO:REMOVE WHEN DONE
	}
}

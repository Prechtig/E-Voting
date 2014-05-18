package org.evoting.authority;

import org.evoting.authority.commands.Build;
import org.evoting.authority.commands.Command;
import org.evoting.authority.commands.CountVotes;
import org.evoting.authority.commands.Exit;
import org.evoting.authority.commands.Generate;
import org.evoting.authority.commands.Load;
import org.evoting.authority.commands.Send;
import org.evoting.authority.commands.Start;

public class ConsoleIO {

	/**
	 * Main method. Used to get users input
	 */
	public static Command getUserInput() {
		System.out.println("Enter commmand: ");
		String input = System.console().readLine().toLowerCase();

		// FOR DEBUGGING START// TODO:REMOVE WHEN DONE

		//String input = "";

		// FOR DEBUGGING END// TODO:REMOVE WHEN DONE

		String[] args = input.split(" ");

		switch (args[0]) {
		// Start election
		case Start.KEYWORD:// TODO: Jolie needs
			return new Start(args);
			// Load electionOptions or keys
		case Load.KEYWORD:
			return new Load(args);
			// Generate keys
		case Generate.KEYWORD:
			return new Generate(args);
			// Send electionOptions
		case Send.KEYWORD:
			return new Send(args);
			// Count votes
		case CountVotes.KEYWORD:
			return new CountVotes(args);
			// Builds the cache
		case Build.KEYWORD:
			return new Build(args);
			// Terminate program
		case Exit.KEYWORD:
			return new Exit(args);
		default:
			return null;
		}
	}
}
package org.evoting.authority;

import org.evoting.authority.commands.Command;
import org.evoting.authority.commands.CountVotes;
import org.evoting.authority.commands.Exit;
import org.evoting.authority.commands.Generate;
import org.evoting.authority.commands.Load;
import org.evoting.authority.commands.Send;
import org.evoting.authority.commands.StartElection;
import org.evoting.authority.commands.Status;
//import org.evoting.authority.commands.Command; //TODO: Does not exist

public class ConsoleIO {
	
	/**
	 * Main method. Used to get users input
	 */
	public static Command getUserInput() {
		System.out.println("Enter commmand: ");
		String input = System.console().readLine().toLowerCase();
		String[] args = input.split(" ");
		
		if(!args[0].equals("")) {
			return null;
		}
		
		switch (args[0]) {
		// Start election
		case StartElection.KEYWORD://TODO: Jolie needs
			return new StartElection(args);
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
		// Update the election status
		case Status.KEYWORD:
			return new Status(args);
		// Terminate program
		case Exit.KEYWORD:
			return new Exit(args);
		default:
			return null;
		}
	}
}
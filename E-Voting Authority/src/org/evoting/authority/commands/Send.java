package org.evoting.authority.commands;

import java.io.IOException;

import org.evoting.authority.Model;

import jolie.net.CommMessage;
import jolie.runtime.JavaService;

public class Send extends Command
{
	public static final String KEYWORD = "send";
	
	public Send(String[] args) {
		super(args);
	}
	
	public String execute(JavaService js)
	{
		return sendElectionoptions(js);
	}
	
	/**
	 * Sends the electionOptions list to the bulletinboard. Only when election is not running
	 */
	private String sendElectionoptions(JavaService js) {
		if (Model.geteOptions() != null) {
			// TODO:Create value containing all the electionoptions
			CommMessage request = CommMessage.createRequest("sendElectionOptionList", Model.getaCommunicationPath(), null); // TODO:
																													// null
																													// skal
																													// være
																													// election
																													// options
			try {
				CommMessage response = js.sendMessage(request).recvResponseFor(request);// Den skal tage imod en
																						// value? som indeholder
																						// confirmation
				if (response.value().boolValue()) {
					return "Successfully sent list of election options";
				} else {
					return "Error in bullitinboard when trying to send list of election options";
				}
			} catch (IOException e) {
				e.printStackTrace();
				return "Error communicating with bullitinboard";
			}
			// TODO: Handle response
			// show error if not confirmed
		} else {
			return "Command failed. Load election options";
		}
	}
}

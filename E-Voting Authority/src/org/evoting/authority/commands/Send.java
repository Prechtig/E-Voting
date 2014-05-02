package org.evoting.authority.commands;

import java.io.IOException;

import jolie.net.CommMessage;
import jolie.runtime.JavaService;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;

import org.evoting.authority.Model;
import org.evoting.common.ValueIdentifiers;

public class Send extends Command {
	public static final String KEYWORD = "send";

	public Send(String[] args) {
		super(args);
	}

	public String execute(JavaService js) {
		return sendElectionoptions(js);
	}

	/**
	 * Sends the electionOptions list to the bulletinboard. Only when election is not running
	 */
	private String sendElectionoptions(JavaService js) {
		if (Model.getElectionOptions() != null) {
			// TODO:Create value containing all the electionoptions
			Value optionsValue = Model.getElectionOptionsValue();

			Value validator = Model.getNewValidator();
			ValueVector children = optionsValue.getChildren(ValueIdentifiers.getValidator());
			children.set(0, validator);

			CommMessage request = CommMessage.createRequest("sendElectionOptionList", Model.getaCommunicationPath(), optionsValue);
			try {
				CommMessage response = js.sendMessage(request).recvResponseFor(request);
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

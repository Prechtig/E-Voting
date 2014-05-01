package org.evoting.authority.commands;

import java.io.IOException;
import java.util.Date;

import jolie.net.CommMessage;
import jolie.runtime.JavaService;
import jolie.runtime.Value;

import org.evoting.authority.Model;
import org.evoting.common.ValueIdentifiers;

public class Status extends Command
{
	public static final String KEYWORD = "status";
	
	public Status(String[] args) {
		super(args);
	}
	
	public String execute(JavaService js)
	{
		return updateElectionStatus(js);
	}
	
	/**
	 * Used to get the initial information about the election. Sets if the election is running and, if it is, then what time it will end
	 */
	public String updateElectionStatus(JavaService js) {
		// Create request to jolie
		CommMessage request = CommMessage.createRequest("getElectionStatus", Model.getaCommunicationPath(), Value.create());
		try {
			// Send jolie request
			CommMessage response = js.sendMessage(request).recvResponseFor(request);

			// Set information
			long sTime = response.value().getFirstChild(ValueIdentifiers.getStartTime()).longValue();
			long eTime = response.value().getFirstChild(ValueIdentifiers.getEndTime()).longValue();
			Date start = new Date(sTime);
			Date end = new Date(eTime);
			
			// if endTime is -1 if some error happened in bullitinboard
			if (eTime > -1) {
				return "Election is running from " + start.toString() + " to " + end.toString() + ".";
			} else {
				return "Error in bullitinboard when trying to update election details";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "Error communicating with bullitinboard";
		}
	}
}

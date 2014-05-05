package org.evoting.authority.commands;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jolie.net.CommMessage;
import jolie.runtime.JavaService;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;

import org.evoting.authority.Model;
import org.evoting.common.ValueIdentifiers;

public class Start extends Command {
	public static final String KEYWORD = "start";

	public Start(String[] args) {
		super(args);
	}

	public String execute(JavaService js) {
		try {
			Date startDate = new SimpleDateFormat("yyyy-MM-ddHH:mm").parse(this.args[1]);
			Date endDate = new SimpleDateFormat("yyyy-MM-ddHH:mm").parse(this.args[2]);
			
			//Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2014-04-30 20:00");//TODO: for debugging
			//Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2014-05-30 20:00");
			return startElection(startDate, endDate, js);
		} catch (ParseException e) {
			return "Could not parse the entered date and time. Use yyyy-MM-ddHH:mm format";
		} catch (ArrayIndexOutOfBoundsException e) {
			return "Please enter start and end date. Use yyyy-MM-ddHH:mm format";
		}
	}

	private String startElection(Date startDate, Date endDate, JavaService js) {
		Value result = Value.create();
		result.getNewChild(ValueIdentifiers.getStartTime()).setValue(startDate.getTime());
		result.getNewChild(ValueIdentifiers.getEndTime()).setValue(endDate.getTime());
		
		Value validator = Model.getNewValidator();
		if(validator == null){
			return "";
		}
		ValueVector children = result.getChildren(ValueIdentifiers.getValidator());
		children.set(0, validator);

		CommMessage request = CommMessage.createRequest("startElection", Model.getaCommunicationPath(), result);
		try {
			CommMessage response = js.sendMessage(request).recvResponseFor(request);

			if (response.value().boolValue()) {
				return "Election has started";
			} else {
				return "Error in bullitinboard when trying to start election";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "Error communicating with bullitinboard";
		}
	}
}

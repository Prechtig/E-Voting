package org.evoting.common;

import java.util.List;

import org.evoting.database.entities.ElectionOption;

/**
 * Contains names of the electionOptions available for voting and a time stamp.
 * @author Mark
 *
 */
public class ElectionOptions
{
	private List<ElectionOption> electionOptions;
	private int electionId;
	
	public ElectionOptions(List<ElectionOption> electionOptions, int electionId)
	{
		this.electionOptions = electionOptions;
		this.electionId = electionId;
	}
	
	public int getElectionId() {
		return electionId;
	}

	public List<ElectionOption> getElectionOptions() {
		return electionOptions;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Election id: ");
		sb.append(electionId);
		for(ElectionOption e : electionOptions) {
			sb.append("\n");
			sb.append(e.toString());
		}
		return sb.toString();
	}
}

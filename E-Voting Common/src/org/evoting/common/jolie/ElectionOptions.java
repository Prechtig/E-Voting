package org.evoting.common.jolie;

import java.util.List;

import org.evoting.database.entities.ElectionOption;

/**
 * Contains names of the electionOptions available for voting and a time stamp.
 *
 */
public class ElectionOptions
{
	private List<ElectionOption> electionOptions;
	
	public ElectionOptions(List<ElectionOption> electionOptions)
	{
		this.electionOptions = electionOptions;
	}
	
	public List<ElectionOption> getElectionOptions() {
		return electionOptions;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(ElectionOption e : electionOptions) {
			sb.append("\n");
			sb.append(e.toString());
		}
		return sb.toString();
	}
}

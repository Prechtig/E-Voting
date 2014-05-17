include "Types.iol"

interface IAuthority {
	RequestResponse: startElection( ElectionStart )( bool ),
					 sendElectionOptionList( ElectionOptionsList )( bool ),
					 getAllVotesAuthority( Validation )( SignedBallotList )
}
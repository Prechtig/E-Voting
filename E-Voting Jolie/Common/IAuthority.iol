include "Types.iol"

interface IAuthority {
	RequestResponse: startElection( ElectionStart )( bool )
	RequestResponse: sendElectionOptionList( ElectionOptionsList )( bool )
	RequestResponse: getAllVotesAuthority( Validation )( SignedBallotList )
}
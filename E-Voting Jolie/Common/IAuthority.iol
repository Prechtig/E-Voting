include "Types.iol"

interface IAuthority {
	RequestResponse: getElectionStatus( void )( ElectionStatus )
	RequestResponse: startElection( ElectionStart )( bool )
	RequestResponse: sendElectionOptionList( ElectionOptionsList )( bool )
	RequestResponse: getAllVotesAuthority( Validation )( EncryptedBallotList )
}
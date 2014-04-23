include "Types.iol"

interface IAuthorityCommunication {
	RequestResponse: getElectionStatus( void )( ElectionStatus )
	RequestResponse: startElection( ElectionStart )( bool )
	RequestResponse: stopElection( Validation )( bool )
	RequestResponse: sendElectionOptionList( SignedElectionOptions )( bool )
}
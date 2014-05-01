include "Types.iol"

interface IAuthorityCommunication {
	RequestResponse: getElectionStatus( void )( electionDetails )
	RequestResponse: startElection( ElectionStart )( confirmation )
	RequestResponse: sendElectionOptionList( AuthElectionOptions )( confirmation )
}
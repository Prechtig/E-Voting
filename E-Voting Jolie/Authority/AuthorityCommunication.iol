include "Types.iol"

interface IAuthorityCommunication {
	RequestResponse: getElectionStatus( )( electionDetails )
	RequestResponse: startElection( void )( confirmation )
	RequestResponse: stopElection( )( confirmation )
	RequestResponse: sendElectionOptionList( electionOptions )( confirmation )
	RequestResponse: sendPublicKey ( elGamalPublicKey )( confirmation )
}
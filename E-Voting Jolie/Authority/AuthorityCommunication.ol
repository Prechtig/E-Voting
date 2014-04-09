include "../Common/Types.iol"
include "../Common/IBulletinBoard.iol"

// Enables concurrent execution
execution { 
	concurrent
}

inputPort IP {
	Location: "local"
	Interfaces: IAuthorityCommunication
}

outputPort BulletinBoardService {
    Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: IBulletinBoard
}

main
{
	[ getElectionStatus( )( electionDetails ) // Used to get information regarding the election
		getElectionStatus@BulletinBoardService( )( electionDetails )
	] { nullProcess }

    [ startElection( )( confirmation ) { //Start the election
		startElection@BulletinBoardService( )( confirmation )
	} ] { nullProcess }

	[ stopElection( )( confirmation ) { //Stop the election
		stopElection@BulletinBoardService( )( confirmation )
	} ]  { nullProcess }

	[ sendElectionOptionList( electionOptions )( confirmation ) { // Send the list of electionoptions
		sendElectionOptionList@BulletinBoardService( electionOptions )( confirmation );
	} ]  { nullProcess }

	[ sendPublicKey ( elGamalPublicKey )( confirmation ) { // Send authorities public ElGamal key
		senPublicKey@BulletinBoardService( elGamalPublicKey )( confirmation )
	} ] { nullProcess }
}
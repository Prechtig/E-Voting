include "../Common/Types.iol"
include "../Common/IBulletinBoard.iol"
include "../Common/IAuthorityCommunication.iol"
include "IAuthorityController.iol"

// Enables concurrent execution
execution { 
	concurrent
}

inputPort IP {
	Location: "local"
	Interfaces: IAuthorityCommunication
}

inputPort IPP {
	Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: IAuthorityController, IAuthorityCommunication
}

outputPort BulletinBoardService {
    Location: "socket://localhost:7000"
    Protocol: sodep
    Interfaces: IBulletinBoard, IAuthorityCommunication
}

outputPort Controller {
    Interfaces: IAuthorityController
}

embedded {
    Java: "org.evoting.authority.ConsoleIO" in Controller
}

main
{
	[ getUserInput( )( ) {
		getUserInput@Controller( )( )
	} ] { nullProcess }

	[ getElectionStatus( )( electionDetails ) {// Used to get information regarding the election
		getElectionStatus@BulletinBoardService( )( electionDetails )
	} ] { nullProcess }

    [ startElection( )( confirmation ) { //Start the election
		startElection@BulletinBoardService( )( confirmation )
	} ] { nullProcess }

	[ stopElection( )( confirmation ) { //Stop the election
		stopElection@BulletinBoardService( )( confirmation )
	} ]  { nullProcess }

	[ sendElectionOptionList( electionOptions )( confirmation ) { // Send the list of electionoptions
		sendElectionOptionList@BulletinBoardService( electionOptions )( confirmation )
	} ]  { nullProcess }
}
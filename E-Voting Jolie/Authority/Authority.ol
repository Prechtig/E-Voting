include "../Common/Types.iol"
include "../Common/IAuthority.iol"
include "IAuthorityController.iol"

// Enables concurrent execution
execution { 
	concurrent
}

inputPort IPJava {
	Location: "local"
	Interfaces: IAuthority
}

outputPort BulletinBoardService {
    Location: "socket://localhost:7000"
    Protocol: sodep
    Interfaces: IAuthority
}

outputPort Controller {
    Interfaces: IAuthorityController
}

embedded {
    Java: "org.evoting.authority.Controller" in Controller
}

init
{
	run@Controller( )( )
}

main
{
	[ getElectionStatus( )( ElectionStatus ) {// Used to get information regarding the election
		getElectionStatus@BulletinBoardService( )( ElectionStatus )
	} ] { nullProcess }

    [ startElection( ElectionStart )( bool ) { //Start the election
		startElection@BulletinBoardService( ElectionStart )( bool )
	} ] { nullProcess }

	[ sendElectionOptionList( ElectionOptionsList )( confirmation ) { // Send the list of electionoptions
		sendElectionOptionList@BulletinBoardService( ElectionOptionsList )( bool )
	} ]  { nullProcess }
}
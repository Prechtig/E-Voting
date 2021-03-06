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
    Location: "socket://localhost:8000"
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
	run@Controller( )
}

main
{
    [ startElection( startElection )( confirmation ) { //Start the election
		startElection@BulletinBoardService( startElection )( confirmation )
	} ] { nullProcess }

	[ sendElectionOptionList( eOptionsList )( confirmation ) { // Send the list of electionoptions
		sendElectionOptionList@BulletinBoardService( eOptionsList )( confirmation )
	} ]  { nullProcess }
	[ getAllVotesAuthority( validator )( allVotes ) {
		getAllVotesAuthority@BulletinBoardService( validator )( allVotes )
	} ]  { nullProcess }
}
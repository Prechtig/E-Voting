include "console.iol"
include "IClient.iol"

embedded {
    Java: "org.evoting.client.Controller" in Controller
}

outputPort Controller {
    Interfaces: Interface
}

main
{
	println@Console("requesting...")();
	getCandidates@BulletinBoardService( )( a );
	println@Console("got candidates...")();
	setCandidateList@Controller( a );
    getBallot@Controller()( b )
    
}
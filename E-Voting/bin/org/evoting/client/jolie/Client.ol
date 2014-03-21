include "console.iol"
include "IClient.iol"

outputPort Controller {
    Interfaces: Interface
}

embedded {
    Java: "org.evoting.client.Controller" in Controller
}

main
{
	println@Console("requesting...")();
	getCandidates@BulletinBoardService( )( a );
	println@Console("got candidates...")();
	setCandidateList@Controller( a );
    getBallot@Controller()( b )
    
}
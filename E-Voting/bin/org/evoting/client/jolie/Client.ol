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
	a = "Ezalor";
		
	setCandidateList@Controller( a );
    getBallot@Controller()( b );
}
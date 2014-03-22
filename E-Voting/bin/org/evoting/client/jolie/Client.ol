include "console.iol"
include "IClient.iol"

outputPort Controller {
    Interfaces: IClientController
}

embedded {
    Java: "org.evoting.client.Controller" in Controller
}

main
{
	println@Console( "Requesting candidate list..." )(  );
	getCandidates@BulletinBoardService( )( candidateList );
	println@Console("Got candidate list of size " + #candidateList.candidates)();
	
	for(i = 0, i < #candidateList.candidates, i++) {
    	println@Console("Candidate " + i + ": " + candidateList.candidates[i] )()
    };
	
	setCandidateList@Controller( candidateList );
    getBallot@Controller()( b )
    
}
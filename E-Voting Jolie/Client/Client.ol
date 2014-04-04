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
	getPublicKeys@BulletinBoardService()( publicKeys );
	println@Console( publicKeys.elgamalPublicKey.y )(  );
	println@Console( publicKeys.elgamalPublicKey.parameters.p )(  );
	println@Console( publicKeys.elgamalPublicKey.parameters.g )(  );
	println@Console( publicKeys.elgamalPublicKey.parameters.l )(  );
	println@Console( publicKeys.rsaPublicKey )(  );
	setPublicKeys@Controller( publicKeys )();
	getCandidateList@BulletinBoardService( )( candidateList );
    setCandidateListAndGetBallot@Controller( candidateList )( ballot );
    vote@BulletinBoardService( ballot )( registered );
    println@Console( "The vote is registered: " + registered )( )
	/*
	println@Console( "Requesting candidate list..." )(  );
	getCandidateList@BulletinBoardService( )( candidateList );
	println@Console("Got candidate list of size " + #candidateList.candidates)();
	
	for(i = 0, i < #candidateList.candidates, i++) {
    	println@Console("Candidate " + i + ": " + candidateList.candidates[i] )()
    };
	
	setCandidateList@Controller( candidateList )();
    getBallot@Controller()( b )
    */
    
}
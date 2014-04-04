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

    println@Console( "userId: " + ballot.userId )( );
    println@Console( "passwordHash: " + ballot.passwordHash )( );
    println@Console( "timestamp: " + ballot.timestamp )( );
    println@Console( "vote[0]: " + ballot.vote[0] )( );
    println@Console( "vote[1]: " + ballot.vote[1] )( );
    println@Console( "vote[2]: " + ballot.vote[2] )( );

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
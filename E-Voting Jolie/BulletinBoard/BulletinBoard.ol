include "../Common/IBulletinBoard.iol"
include "console.iol"

// Enables concurrent execution
execution { 
	concurrent
}

outputPort BBJavaController {
    Interfaces: IBulletinBoard
}

inputPort BulletinBoardService {
    Location: "socket://localhost:8000/"
    Protocol: sodep
    Interfaces: IBulletinBoard
}

embedded {
    Java: "org.evoting.bulletinboard.Controller" in BBJavaController
}

main {
	[ getPublicKeys( )( publicKeys ) {
		println@Console("Someone is requesting the public keys")(  );
		getPublicKeys@BBJavaController( )( publicKeys )
	} ] { nullProcess }

	[ getCandidates( )( candidateList ) {
		println@Console("Someone is requesting the candidate list")(  );
		//Get the candidatelist from the embedded Java service
		getCandidateList@BBJavaController( )( candidateList );
		println@Console( "Received candidate list of size " + #candidateList.candidates )(  )
	} ]  { nullProcess }

	[ vote( encryptedBallot )( registered ) {
		//Process the vote in the embedded Java service
		processVote@BBJavaController( encryptedBallot )( registered );
		println@Console( "Registered: " + registered )()
	} ]  { nullProcess }
}
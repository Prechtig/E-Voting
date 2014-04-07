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

	[ getCandidateList( )( candidateList ) {
		println@Console("Someone is requesting the candidate list")(  );
		//Get the candidatelist from the embedded Java service
		getCandidateList@BBJavaController( )( candidateList )
	} ]  { nullProcess }

	[ processVote( encryptedBallot )( registered ) {
		//Process the vote in the embedded Java service
		processVote@BBJavaController( encryptedBallot )( registered );
		println@Console( "Registered vote: " + registered )()
	} ]  { nullProcess }

	[ getAllVotes( )( allVotes ) {
		getAllVotes@BBJavaController( )( allVotes )
	} ] { nullProcess }
}
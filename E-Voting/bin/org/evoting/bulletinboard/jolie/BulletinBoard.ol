include "../../common/jolie/IBulletinBoard.iol"
include "console.iol"

// Enables concurrent execution
execution { 
	concurrent
}

outputPort BBJavaController {
    Interfaces: IBBJavaController
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
	getCandidates( )( candidates ) {
		println@Console("Someone is requesting the candidate list")();
		getCandidateList@BBJavaController( )( candidates );
		println@Console("Got answer from embedded java service")();
		println@Console( "Received candidate list of size " + #candidates.candidates )()
	};
	vote( encryptedBallot )( registered ) {
		processVote@BBJavaController( encryptedBallot )( registered );
		println@Console( "Registered: " + registered )()
	}
}
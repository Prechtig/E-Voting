include "../../common/jolie/IBulletinBoard.iol"
include "console.iol"

embedded {
    Java: "org.evoting.bulletinboard.Controller" in BBJavaController
}

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

main {
	getCandidates( )( candidates ) {
		getCandidates@BBJavaController( )( candidates );
		println@Console( "Received candidate list of size " + #candidates.candidates )()
	};
	vote( encryptedBallot )( registered ) {
		processVote@BBJavaController( encryptedBallot )( registered );
		println@Console( "Registered: " + registered )()
	}
}
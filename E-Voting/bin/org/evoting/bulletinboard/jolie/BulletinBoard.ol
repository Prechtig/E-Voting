include "../../common/jolie/IBulletinBoard.iol"
include "console.iol"

embedded {
    Java: "org.evoting.bulletinboard.Controller" in BulletinBoardController
}

// Enables concurrent execution
execution { 
	concurrent
}

outputPort BulletinBoardController {
    Interfaces: IBulletinBoardController
}

inputPort BulletinBoardService {
    Location: "socket://localhost:8000/"
    Protocol: sodep
    Interfaces: IBulletinBoard
}

main {
	getCandidates( )( result ) {
		println@Console("Someone is requesting the candidate list")();
		getCandidates@BulletinBoardController( )( candidates );
		println@Console("Got answer from embedded java service")();
		result.candidates << candidates.candidates;
		println@Console( "Received candidate list of size " + #candidates.candidates )()
	};
	vote( encryptedBallot )( registered ) {
		processVote@BulletinBoardController( encryptedBallot )( registered );
		println@Console( "Registered: " + registered )()
	}
}
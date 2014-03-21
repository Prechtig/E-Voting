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
    Location: "socket://localhost:8000/"
    Protocol: sodep
    Interfaces: IBulletinBoardController
}

inputPort BulletinBoardService {
    Location: "socket://localhost:8000/"
    Protocol: sodep
    Interfaces: IBulletinBoard
}

main {
	getCandidates( )( result ) {
		getCandidates@BulletinBoardController( )( candidates );
		result.candidates << candidates.candidates;
		println@Console( "Received candidate list of size " + #candidates.candidates )()
	};
	vote( encryptedBallot )( registered ) {
		processVote@BulletinBoardController( encryptedBallot )( registered );
		println@Console( "Registered: " + registered )()
	}
}
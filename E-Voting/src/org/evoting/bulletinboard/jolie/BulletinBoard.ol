include "../../common/jolie/IBulletinBoard.iol"
include "console.iol"

inputPort BulletinBoardService {
    Location: "socket://localhost:8000/"
    Protocol: sodep
    Interfaces: IBulletinBoard
}

outputPort BulletinBoardController {
    Location: "socket://localhost:8000/"
    Protocol: sodep
    Interfaces: IBulletinBoardController
}

embedded {
    Java: "org.evoting.bulletinboard.Controller" in BulletinBoardController
}

// Enables concurrent execution
execution { 
	concurrent
}

main {
	getCandidates( )( result ) {
		getCandidates@BulletinBoardController( )( candidates );
		result.candidates << candidates.candidates;
		println@Console( "Received candidate list of size " + #candidates.candidates )()
	};
	vote( ballot )( registered ) {
		processVote@BulletinBoardController( ballot )( response );
		registered = response;
		println@Console( "Registered: " + registered )()
	}
}
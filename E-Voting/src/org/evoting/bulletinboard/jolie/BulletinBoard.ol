include "IBulletinBoard.iol"
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

// Used to identify the incoming process
cset {
	sid: VoteRequest.sid
}

main {
	getCandidates( )( result ) {
		getCandidates@BulletinBoardController( )( candidates );
		result.candidates << candidates.candidates;
		println@Console( "Received candidate list of size " + #candidates.candidates )();
		csets.sid = new;
		result.sid = csets.sid
	};
	vote( request )( registered ) {
		processVote@BulletinBoardController( )( response );
		registered = response;
		println@Console( "Registered: " + registered )()
	}
}
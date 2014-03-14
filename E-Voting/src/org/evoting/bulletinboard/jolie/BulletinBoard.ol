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

// Enables concurrent execution
execution { 
	concurrent
}

// Used to identify the incoming process
cset {
	sid: VoteRequest.sid
}

//Embed Java classes
embedded {
	Java: "org.evoting.bulletinboard.Controller" in BulletinBoardController
}

main {
	getCandidates( )( result ) {
		getCandidates@BulletinBoardController(  )( candidates );
		println@Console("Number of candidates: " + #candidates.candidates)();
		csets.sid = new;
		result.sid = csets.sid;
		result.candidates << candidates.candidates
	};
	processVote@BulletinBoardController(  )( registered );
	println@Console( "Vote is registered: " + registered)()
}
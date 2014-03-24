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
	[ getCandidates( )( candidateList ) {
		println@Console("Someone is requesting the candidate list")(  );
		getCandidateList@BBJavaController( )( candidateList );
		println@Console( "Received candidate list of size " + #candidateList.candidates )(  )
	} ]  { nullProcess }
	[ vote( encryptedBallot )( registered ) {
		processVote@BBJavaController( encryptedBallot )( registered );
		println@Console( "Registered: " + registered )()
	} ]  { nullProcess }
}
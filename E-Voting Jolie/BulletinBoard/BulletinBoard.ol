include "../Common/IBulletinBoard.iol"
include "../Common/IAuthorityCommunication.iol"
include "console.iol"

// Enables concurrent execution
execution { 
	concurrent
}

outputPort BBJavaController {
    Interfaces: IBulletinBoard, IAuthorityCommunication
}

inputPort BulletinBoardService {
    Location: "socket://localhost:7000/"
    Protocol: sodep
    Interfaces: IBulletinBoard, IAuthorityCommunication
}

embedded {
    Java: "org.evoting.bulletinboard.Controller" in BBJavaController
}

//cset {
//	sid: VoteRequest.sid
//}

main {
	[ getPublicKeys( )( publicKeys ) {
		println@Console("Someone is requesting the public keys")(  );
		getPublicKeys@BBJavaController( )( publicKeys )
	} ] { nullProcess }

	[ getElectionOptions( )( electionOptions ) {
		println@Console("Someone is requesting the election options")(  );
		//Get the election options from the embedded Java service
		getElectionOptions@BBJavaController( )( electionOptions )
	} ] { nullProcess }

	[ login( userInformation )( loginResponse ) {
		login@BBJavaController( userInformation )( confirmation );
		loginResponse.sid = csets.sid = new
	}
	//processVote( encryptedBallot )( registered ) {
	//	encryptedBallot.userId = userInformation.userId;
	//	processVote@BBJavaController( encryptedBallot )( registered );
	//	println@Console( "Registered vote: " + registered )()
	//}
	] { nullProcess }

	[ processVote( encryptedBallot )( registered ) {
		//Process the vote in the embedded Java service
		processVote@BBJavaController( encryptedBallot )( registered );
		println@Console( "Registered vote: " + registered )()
	} ] { nullProcess }

	[ getAllVotes( )( allVotes ) {
		getAllVotes@BBJavaController( )( allVotes )
	} ] { nullProcess }

	[ getElectionStatus( )( confirmation ) {
		getElectionStatus@BBJavaController( )( confirmation )
	} ] { nullProcess }

	[ startElection( )( confirmation ) {
		startElection@BBJavaController( )( confirmation )
	} ] { nullProcess }

	[ stopElection( )( confirmation ) {
		stopElection@BBJavaController( )( confirmation )
	} ] { nullProcess }

	[ sendElectionOptionList( options )( confirmation ) {
		sendElectionOptionList@BBJavaController( options )( confirmation )
	} ] { nullProcess }
}
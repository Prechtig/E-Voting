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

	[ login( userInformation )( confirmation ) {
		login@BBJavaController( userInformation )( confirmation )
	};
	processVote( encryptedBallot )( registered ) {
		processVote@BBJavaController( encryptedBallot )( registered );
		println@Console( "Registered vote: " + registered )()
	} ]

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
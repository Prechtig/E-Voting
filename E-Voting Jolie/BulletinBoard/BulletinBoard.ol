include "ILoadKeys.iol"
include "../Common/IBulletinBoard.iol"
include "../Common/IAuthority.iol"
include "console.iol"

// Enables concurrent execution
execution { 
	concurrent
}

outputPort BBJavaController {
    Interfaces: IBulletinBoard, IAuthority, ILoadKeys
}

inputPort BulletinBoardService {
    Location: "socket://localhost:7000/"
    Protocol: sodep
    Interfaces: IBulletinBoard, IAuthority
}

embedded {
    Java: "org.evoting.bulletinboard.Controller" in BBJavaController
}

cset {
	sid: EncryptedBallot.sid
}

init {
	loadRSAKeys@BBJavaController( )( rsaSuccessful );
	if(rsaSuccessful) {
		println@Console("RSA keys successfully set")( )
	} else {
		println@Console("RSA keys not successfully set")( )
	};
	loadElGamalKey@BBJavaController( )( elGamalSuccessful );
	if(elGamalSuccessful) {
		println@Console("ElGamal key successfully set")( )
	} else {
		println@Console("ElGamal key not successfully set")( )
	};
	if(rsaSuccessful && elGamalSuccessful) {
		println@Console("The BulletinBoard is now ready to be set up by the authority")( )
	}
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

	[ login( userInformation )( loginResponse ) {
		login@BBJavaController( userInformation )( successful );
		if(successful) {
			loginResponse.sid = csets.sid = new	
		}
	} ] { processVote( encryptedBallot )( registered ) {
		encryptedBallot.userId = userInformation.userId;
		processVote@BBJavaController( encryptedBallot )( registered );
		println@Console( "Registered vote: " + registered )()
	} }

	//[ processVote( encryptedBallot )( registered ) {
	//	//Process the vote in the embedded Java service
	//	processVote@BBJavaController( encryptedBallot )( registered );
	//	println@Console( "Registered vote: " + registered )()
	//} ] { nullProcess }

	[ getAllVotes( )( allVotes ) {
		getAllVotes@BBJavaController( )( allVotes )
	} ] { nullProcess }

	[ getAllVotesAuthority( validator )( allVotes ) {
		getAllVotesAuthority@BBJavaController( validator )( allVotes )
	} ] { nullProcess }

	[ getElectionStatus( )( confirmation ) {
		getElectionStatus@BBJavaController( )( confirmation )
	} ] { nullProcess }

	[ startElection( electionStart )( confirmation ) {
		startElection@BBJavaController( electionStart )( confirmation )
	} ] { nullProcess }

	[ sendElectionOptionList( options )( confirmation ) {
		sendElectionOptionList@BBJavaController( options )( confirmation )
	} ] { nullProcess }
}
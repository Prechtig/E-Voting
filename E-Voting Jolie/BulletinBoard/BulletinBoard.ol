include "console.iol"
include "JavaBBController.iol"
include "../Common/IBulletinBoard.iol"
include "../Common/IAuthority.iol"

// Enables concurrent execution
execution { 
	concurrent
}

inputPort BulletinBoardService {
    Location: "socket://localhost:8000/"
    Protocol: sodep
    Interfaces: IBulletinBoard, IAuthority
}

outputPort BBJavaController {
    Interfaces: JavaBBController, IAuthority
}

embedded {
    Java: "org.evoting.bulletinboard.Controller" in BBJavaController
}

cset {
	sid:	EncryptedBallot.sid
			SessionRequest.sid
			LoginRequest.sid
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
			getPublicKeys@BBJavaController( )( publicKeys );
			publicKeys.sid = csets.sid = new
	} ] {
		login( userInformation )( loginResponse ) {
			login@BBJavaController( userInformation )( loginResponse )
		};
		getElectionOptions( sessionRequest )( electionOptions ) {
			getElectionOptions@BBJavaController( )( electionOptions )
		};
		processVote( encryptedBallot )( registered ) {
			processVote@BBJavaController( encryptedBallot )( registered );
			if(registered) {
				println@Console( "Registered a vote")()
			}
	} }

	[ getAllVotes( )( allVotes ) {
		getAllVotes@BBJavaController( )( allVotes )
	} ] { nullProcess }

	[ getAllVotesAuthority( validator )( allVotes ) {
		getAllVotesAuthority@BBJavaController( validator )( allVotes )
	} ] { nullProcess }

	[ startElection( electionStart )( confirmation ) {
		startElection@BBJavaController( electionStart )( confirmation )
	} ] { println@Console("The Authorities has started the election")() }

	[ sendElectionOptionList( options )( confirmation ) {
		sendElectionOptionList@BBJavaController( options )( confirmation )
	} ] { println@Console( "Recieved the list of election options" )( ) }
}
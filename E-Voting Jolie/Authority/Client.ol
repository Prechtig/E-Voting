include "console.iol"
include "IClient.iol"

outputPort Controller {
    Interfaces: IClientController
}

embedded {
    Java: "org.evoting.client.Controller" in Controller
}

main
{
	getPublicKeys@BulletinBoardService()( publicKeys );
	println@Console( publicKeys.elgamalPublicKey.y )(  );
	println@Console( publicKeys.elgamalPublicKey.parameters.p )(  );
	println@Console( publicKeys.elgamalPublicKey.parameters.g )(  );
	println@Console( publicKeys.elgamalPublicKey.parameters.l )(  );
	println@Console( publicKeys.rsaPublicKey )(  );
	setPublicKeys@Controller( publicKeys )();
	getElectionOptions@BulletinBoardService( )( electionOptions );
    setElectionOptionsAndGetBallot@Controller( electionOptions )( ballot );

    println@Console( "userId: " + ballot.userId )( );
    println@Console( "passwordHash: " + ballot.passwordHash )( );
    println@Console( "timestamp: " + ballot.timestamp )( );
    println@Console( "vote[0]: " + ballot.vote[0] )( );
    println@Console( "vote[1]: " + ballot.vote[1] )( );
    println@Console( "vote[2]: " + ballot.vote[2] )( );

    processVote@BulletinBoardService( ballot )( registered );
    println@Console( "The vote is registered: " + registered )( );

    getAllVotes@BulletinBoardService( )( allVotes );
    println@Console( "The number of election options are " + allVotes.numberOfElectionOptions )( );
    for(i = 0, i < #allVotes.votes, i++) {
    	for(j = 0, j < #allVotes.votes.vote, j++) {
    		println@Console("vote[" + i + "][" + j + "] = " + allVotes.votes[i].vote[j].encryptedVote )( )
    	}
    }
}
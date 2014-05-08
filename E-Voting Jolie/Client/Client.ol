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
	loginResponse.success = false;
	while( loginResponse.success == false ) {
		getLoginInformation@Controller() ( loginRequest );
		login@BulletinBoardService( loginRequest ) ( loginResponse )
	};
	getPublicKeys@BulletinBoardService()( publicKeys );
	setPublicKeys@Controller( publicKeys )();
	getElectionOptions@BulletinBoardService( )( electionOptions );
    setElectionOptionsAndGetBallot@Controller( electionOptions )( ballot );
	ballot.sid = loginResponse.sid;
	println@Console(ballot.userId)();
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
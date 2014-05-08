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
    getCommand@Controller( )( command );
    if(command == "vote") {
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
    } else if(command == "get") {
        getAllVotes@BulletinBoardService( )( allVotes )
        for(i = 0, i < #allVotes.votes, i++) {
            println@Console( "Vote #" + i )( );
            for(j = 0, j < #allVotes.votes.vote, j++) {
                println@Console("vote[" + i + "][" + j + "] = " + allVotes.votes[i].vote[j].encryptedVote )( )
            };
            println@Console( )( )
        }
    }
	
}
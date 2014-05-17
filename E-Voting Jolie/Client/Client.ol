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
        //Set the session id
        sessionRequest.sid = loginResponse.sid;
        getPublicKeys@BulletinBoardService( sessionRequest )( publicKeys );
        setPublicKeys@Controller( publicKeys )();
        getElectionOptions@BulletinBoardService( sessionRequest )( electionOptions );
        setElectionOptionsAndGetBallot@Controller( electionOptions )( ballot );
        //Set the session id
        ballot.sid = loginResponse.sid;
        processVote@BulletinBoardService( ballot )( registered );
        if(registered) {
            println@Console( "The vote is registered" )( )    
        } else {
            println@Console( "The vote is not registered" )( )    
        }
    } else if(command == "get") {
        getAllVotes@BulletinBoardService( )( allVotes );
        for(i = 0, i < #allVotes.votes, i++) {
            println@Console( "Vote #" + i )( );
            for(j = 0, j < #allVotes.votes.vote, j++) {
                println@Console("vote[" + i + "][" + j + "] = " + allVotes.votes[i].vote[j].encryptedVote )( )
            };
            println@Console( )( )
        }
    }
	
}
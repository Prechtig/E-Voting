include "console.iol"
include "IClient.iol"
include "../Common/IBulletinBoard.iol"

outputPort Controller {
    Interfaces: IClientController
}

outputPort BulletinBoardService {
    Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: IBulletinBoard
}

embedded {
    Java: "org.evoting.client.Controller" in Controller
}

define doubleLineBreak
{
        println@Console( )( );
        println@Console( )( )
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

        //Print the ballot
        println@Console( "Your ballot:" )( );
        for (i = 0, i < #ballot.vote, i++) {
            print@Console( ballot.vote[i] )()   
        };
        doubleLineBreak;

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
                print@Console( allVotes.votes[i].vote[j].encryptedVote )()
            };
            doubleLineBreak
        }
    }
}
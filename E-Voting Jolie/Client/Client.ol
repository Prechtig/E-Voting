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
        getPublicKeys@BulletinBoardService( )( publicKeys );
        sid = publicKeys.sid;

        setPublicKeys@Controller( publicKeys )();
        success = false;
        while( !success ) {
            getLoginInformation@Controller() ( loginRequest );
            loginRequest.sid = sid;
            login@BulletinBoardService( loginRequest ) ( loginResponse );
            validateLoginResponse@Controller( loginResponse )( response );
            success = response
        };
        //Set the session id
        sessionRequest.sid = sid;
        getElectionOptions@BulletinBoardService( sessionRequest )( electionOptions );

        //Print the election options
        println@Console ( )( );
        println@Console ( "Options are given in the following format:")( );
        println@Console ( "id - name - party")( );
        println@Console ( "If the option is a candidate under a party")( );
        println@Console ( "id - name - [NO_PARTY]")( );
        println@Console ( "If the option is a candidate without a party")( );
        println@Console ( "id - party")( );
        println@Console ( "If the option is a party")( );
        println@Console ( )( );
        
        options -> electionOptions.electionOptions;
        for (i = 0, i < #options, i++) {
            if(options[i].partyId == -1) {
                //Candidate with no party
                println@Console( options[i].id + " - " + options[i].name + " - [NO_PARTY]")()
            } else if(options[i].partyId == i) {
                //Party
                println@Console( options[i].id + " - " + options[options[i].partyId].name )()
            } else {
                //Candidate with party
                println@Console( options[i].id + " - " + options[i].name + " - " + options[options[i].partyId].name )()
            }
        };
        println@Console ( )( );

        setElectionOptionsAndGetBallot@Controller( electionOptions )( ballot );

        //Print the ballot
        println@Console ( )( );
        println@Console( "Your ballot:" )( );
        for (i = 0, i < #ballot.vote, i++) {
            print@Console( ballot.vote[i] )()   
        };
        doubleLineBreak;

        //Set the session id
        ballot.sid = sid;
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
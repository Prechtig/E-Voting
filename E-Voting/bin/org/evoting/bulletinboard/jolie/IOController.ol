include "IBulletinBoard.iol"
include "console.iol"
 
outputPort BulletinBoardService {
    Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: IBulletinBoard
}
 
main
{
	getCandidates@BulletinBoardService( void )( result );
    println@Console( "Length of array " + #result.candidates )();
    for(i = 0, i < #result.candidates, i++) {
    	println@Console("Candidate " + i + ": " + result.candidates[i] )()
    };
    println@Console( "Done" )();

	println@Console( result.sid )();
	x.sid = result.sid;
	x.votes[0] = true;
	x.votes[1] = false;
	x.votes[2] = true;
	vote@BulletinBoardService( x )()
}
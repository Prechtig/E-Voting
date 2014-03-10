include "BulletinBoardInterface.iol"
include "console.iol"
 
outputPort BulletinBoardService {
    Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: BulletinBoardInterface
}
 
main
{
    getCandidates@BulletinBoardService( void )( result );
    println@Console( "Length of array " + #result )();
    for(i = 0, i < #result, i++) {
    	println@Console( result[i] )()
    };
    println@Console( "Done" )()
}
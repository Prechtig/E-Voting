include "../../common/jolie/Types.iol"
include "../../common/jolie/IBulletinBoard.iol"

interface Interface {
    RequestResponse: getBallot( void )( EncryptedBallot )
    OneWay: setCandidateList( string )
}

outputPort BulletinBoardService {
    Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: IBulletinBoard
}
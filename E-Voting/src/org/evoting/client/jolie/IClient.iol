include "../../common/jolie/Types.iol"
include "../../common/jolie/IBulletinBoard.iol"

interface IClientController {
    RequestResponse: getBallot( void )( EncryptedBallot )
    RequestResponse: setCandidateList( string )( )
}

outputPort BulletinBoardService {
    Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: IBulletinBoard
}
include "../Common/Types.iol"
include "../Common/IBulletinBoard.iol"

interface IClientController {
    RequestResponse: getBallot( void )( EncryptedBallot )
    RequestResponse: setCandidateList( EncryptedCandidateList )( void )
}

outputPort BulletinBoardService {
    Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: IBulletinBoard
}
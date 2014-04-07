include "../Common/Types.iol"
include "../Common/IBulletinBoard.iol"

interface IClientController {
    RequestResponse: getBallot( void )( EncryptedBallot )
    RequestResponse: setCandidateList( EncryptedCandidateList )( void )
    RequestResponse: setPublicKeys( PublicKeys )( void )
    RequestResponse: setCandidateListAndGetBallot( EncryptedCandidateList )( EncryptedBallot )
}

outputPort BulletinBoardService {
    Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: IBulletinBoard
}
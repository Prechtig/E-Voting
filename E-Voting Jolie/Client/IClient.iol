include "../Common/Types.iol"
include "../Common/IBulletinBoard.iol"

interface IClientController {
    RequestResponse: getBallot( void )( EncryptedBallot )
    RequestResponse: setElectionOptions( EncryptedElectionOptions )( void )
    RequestResponse: setPublicKeys( PublicKeys )( void )
    RequestResponse: setElectionOptionsAndGetBallot( EncryptedElectionOptions )( EncryptedBallot )
    RequestResponse: getElectionStatus( void )( ElectionStatus )
}

outputPort BulletinBoardService {
    Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: IBulletinBoard
}
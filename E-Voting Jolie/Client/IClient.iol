include "../Common/Types.iol"
include "../Common/IBulletinBoard.iol"

interface IClientController {
    RequestResponse: getBallot( void )( EncryptedBallot )
    RequestResponse: setElectionOptions( SignedElectionOptions )( void )
    RequestResponse: setPublicKeys( PublicKeys )( void )
    RequestResponse: setElectionOptionsAndGetBallot( SignedElectionOptions )( EncryptedBallot )
    RequestResponse: getElectionStatus( void )( ElectionStatus )
}

outputPort BulletinBoardService {
    Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: IBulletinBoard
}
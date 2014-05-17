include "../Common/Types.iol"
include "../Common/IBulletinBoard.iol"

interface IClientController {
	RequestResponse: getLoginInformation( void )( LoginRequest )
    RequestResponse: getBallot( void )( EncryptedBallot )
    RequestResponse: setElectionOptions( SignedElectionOptions )( void )
    RequestResponse: setPublicKeys( PublicKeys )( void )
    RequestResponse: setElectionOptionsAndGetBallot( SignedElectionOptions )( EncryptedBallot )
    RequestResponse: getCommand( void )( string )
}

outputPort BulletinBoardService {
    Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: IBulletinBoard
}
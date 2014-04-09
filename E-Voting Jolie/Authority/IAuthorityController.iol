include "../Common/Types.iol"
include "../Common/IBulletinBoard.iol"

interface IAuthorityController {
    RequestResponse: getUserInput( void )( void )
}

outputPort BulletinBoardService {
    Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: IBulletinBoard
}
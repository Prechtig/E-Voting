include "../../common/jolie/Types.iol"
include "../../common/jolie/IBulletinBoard.iol"
include "../../common/jolie/TimeService.iol"

interface Interface {
    RequestResponse: getBallot( void )( EncryptedBallot )
    OneWay: setCandidateList( string )
}
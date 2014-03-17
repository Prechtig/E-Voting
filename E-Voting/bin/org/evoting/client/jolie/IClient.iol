include "../../common/jolie/Types.iol"

type EncryptedBallot:void {
    .userInfo:string
    .votes:string
}

interface Interface {
    RequestResponse: getBallot( void )( EncryptedBallot )
    OneWay: setCandidateList( string )
}
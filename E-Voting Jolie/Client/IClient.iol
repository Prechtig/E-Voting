include "../Common/Types.iol"

interface IClientController {
	RequestResponse: getLoginInformation( void )( LoginRequest ),
                     setElectionOptions( SignedElectionOptions )( void ),
                     setPublicKeys( PublicKeys )( void ),
                     setElectionOptionsAndGetBallot( SignedElectionOptions )( EncryptedBallot ),
                     getCommand( void )( string ),
                     validateLoginResponse( LoginResponse )( bool )
}
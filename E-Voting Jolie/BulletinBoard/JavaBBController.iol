include "../Common/Types.iol"

interface JavaBBController {
	RequestResponse: loadElGamalKey( void )( bool ),
					 loadRSAKeys( void )( bool ),
					 getElectionOptions( void )( SignedElectionOptions ),
					 processVote( EncryptedBallot )( bool ),
					 getPublicKeys( void )( PublicKeys ),
					 getAllVotes( void )( SignedBallotList ),
					 login( LoginRequest )( LoginResponse )
}
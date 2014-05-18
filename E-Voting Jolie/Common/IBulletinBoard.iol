include "Types.iol"

interface IBulletinBoard {
	RequestResponse: getElectionOptions( SessionRequest )( SignedElectionOptions ),
					 processVote( EncryptedBallot )( bool ),
					 getPublicKeys( void )( PublicKeys ),
					 getAllVotes( void )( SignedBallotList ),
					 login( LoginRequest )( LoginResponse )
}
include "Types.iol"

interface IBulletinBoard {
	RequestResponse: getElectionOptions( SessionRequest )( SignedElectionOptions )
	RequestResponse: processVote( EncryptedBallot )( bool )
	RequestResponse: getPublicKeys( SessionRequest )( PublicKeys )
	RequestResponse: getAllVotes( void )( SignedBallotList )
	RequestResponse: login( LoginRequest )( LoginResponse )
}
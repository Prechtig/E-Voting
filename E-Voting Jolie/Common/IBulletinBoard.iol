include "Types.iol"

interface IBulletinBoard {
	RequestResponse: getCandidateList( void )( EncryptedCandidateList )
	RequestResponse: processVote( EncryptedBallot )( bool )
	RequestResponse: getPublicKeys( void )( PublicKeys )
}
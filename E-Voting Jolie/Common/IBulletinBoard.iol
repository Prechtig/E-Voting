include "Types.iol"

interface IBulletinBoard {
	RequestResponse: getCandidateList( void )( EncryptedCandidateList )
	RequestResponse: vote( EncryptedBallot )( bool )
	RequestResponse: getPublicKeys( void )( PublicKeys )
}
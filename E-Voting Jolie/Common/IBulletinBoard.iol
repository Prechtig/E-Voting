include "Types.iol"

interface IBulletinBoard {
	RequestResponse: getCandidates( void )( EncryptedCandidateList )
	RequestResponse: vote( EncryptedBallot )( bool )
	RequestResponse: getPublicKeys( void )( PublicKeys )
}
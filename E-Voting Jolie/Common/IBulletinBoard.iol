include "Types.iol"

interface IBulletinBoard {
	RequestResponse: getCandidates( void )( EncryptedCandidateList )
	RequestResponse: vote( EncryptedBallot )( bool )
	RequestResponse: getPublicKeys( void )( PublicKeys )
}

interface IBBJavaController {
	RequestResponse: getCandidateList( void )( EncryptedCandidateList )
	RequestResponse: processVote( EncryptedBallot )( bool )
	RequestResponse: getPublicKeys( void )( PublicKeys )
}
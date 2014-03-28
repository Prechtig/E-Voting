include "Types.iol"

interface IBulletinBoard {
	RequestResponse: getCandidates( void )( EncryptedCandidateList )
	RequestResponse: vote( EncryptedBallot )( bool )
}

interface IBBJavaController {
	RequestResponse: getCandidateList( void )( EncryptedCandidateList )
	RequestResponse: processVote( EncryptedBallot )( bool )
}
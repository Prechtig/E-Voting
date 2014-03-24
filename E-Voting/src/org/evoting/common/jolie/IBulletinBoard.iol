include "Types.iol"

interface IBulletinBoard {
	RequestResponse: getCandidates( void )( CandidateList )
	RequestResponse: vote( EncryptedBallot )( bool )
}

interface IBBJavaController {
	RequestResponse: getCandidateList( void )( CandidateList )
	RequestResponse: processVote( EncryptedBallot )( bool )
}

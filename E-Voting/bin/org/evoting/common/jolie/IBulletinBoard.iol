include "Types.iol"

type CandidateList:void {
	.candidates*: string
}

type VoteRequest:void {
	.votes*: bool
}

interface IBulletinBoard {
	RequestResponse: getCandidates( void )( CandidateList )
	RequestResponse: vote( EncryptedBallot )( bool )
}

interface IBBJavaController {
	RequestResponse: getCandidates( void )( CandidateList )
	RequestResponse: processVote( EncryptedBallot )( bool )
}

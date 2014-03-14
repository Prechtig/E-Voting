include "../../common/jolie/Types.iol"

type GetCandidatesResponse:void {
	.sid:string
	.candidates*: string
}

type CandidateList:void {
	.candidates*: string
}

type VoteRequest:void {
	.sid:string
	.votes*: bool
}

interface IBulletinBoard {
	RequestResponse: getCandidates( void )( GetCandidatesResponse )
	RequestResponse: vote( Ballot )( bool )
}

interface IBulletinBoardController {
	RequestResponse: getCandidates( void )( CandidateList )
	RequestResponse: processVote( Ballot )( bool )
}
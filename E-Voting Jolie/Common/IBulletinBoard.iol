include "Types.iol"

interface IBulletinBoard {
	RequestResponse: getElectionOptions( void )( SignedElectionOptions )
	RequestResponse: processVote( EncryptedBallot )( bool )
	RequestResponse: getPublicKeys( void )( PublicKeys )
	RequestResponse: getAllVotes( void )( EncryptedBallotList )
	RequestResponse: getAllVotesAuthority( Validator )( EncryptedBallotList )
	RequestResponse: login( LoginRequest )( LoginResponse )
}
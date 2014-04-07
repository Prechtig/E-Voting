include "Types.iol"

interface IBulletinBoard {
	RequestResponse: getElectionOptions( void )( EncryptedElectionOptions )
	RequestResponse: processVote( EncryptedBallot )( bool )
	RequestResponse: getPublicKeys( void )( PublicKeys )
	RequestResponse: getAllVotes( void )( EncryptedBallotList )
}
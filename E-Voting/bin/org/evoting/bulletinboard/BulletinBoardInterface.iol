interface BulletinBoardInterface {
	RequestResponse: getCandidates( void )( string )
	RequestResponse: vote( int )( bool )
}
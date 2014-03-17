type Ballot: void {
	.userId:int
	.password:string
	.vote*:bool
	.sid:string
}

type EncryptedBallot: void {
    .userInfo: string
    .vote: string
    .sid: string
}
type EncryptedBallot: void {
    .userId*: byte
    .passwordHash*: byte
    .timeStamp*: byte
    .vote*: byte
}

type EncryptedCandidateList:void {
	.candidates*: byte
}
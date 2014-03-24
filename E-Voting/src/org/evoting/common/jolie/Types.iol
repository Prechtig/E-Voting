type EncryptedBallot: void {
    .userId*: raw
    .passwordHash*: raw
    .timeStamp*: raw
    .vote*: raw
}

type EncryptedCandidateList:void {
	.candidates*: raw
}
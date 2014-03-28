type EncryptedBallot: void {
    .userId: raw
    .passwordHash: raw
    .timeStamp: raw
    .vote: raw
}

type EncryptedCandidateList: void {
	.candidates: raw
}

type PublicKeys: void {
	.elgamalPublicKey: void {
		.y: string
		.parameters: void {
			.p: string
			.g: string
			.l: int
		}
	}
	.rsaPublicKey: raw
}
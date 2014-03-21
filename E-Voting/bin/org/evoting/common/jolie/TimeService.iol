type NOTATIONType:any

type LocalTimeByZipCode:void {
	.ZipCode?:string
}

type LocalTimeByZipCodeResponse:void {
	.LocalTimeByZipCodeResult?:string
}

interface LocalTimeSoap {
RequestResponse:
	LocalTimeByZipCode(LocalTimeByZipCode)(LocalTimeByZipCodeResponse)
}

outputPort LocalTimeSoap {
Location: "socket://www.ripedevelopment.com:80/webservices/LocalTime.asmx"
Protocol: soap {
	.wsdl = "http://www.ripedevelopment.com/webservices/LocalTime.asmx?WSDL";
	.wsdl.port = "LocalTimeSoap"
}
Interfaces: LocalTimeSoap
}

outputPort LocalTimeSoap12 {
Location: "socket://localhost:80/"
Protocol: soap
Interfaces: LocalTimeSoap
}



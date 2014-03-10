interface Interface {
    OneWay: getBallot()
}
 
outputPort ConsoleIO {
    Interfaces: Interface
}
 
embedded {
    Java: "org.evoting.client.ConsoleIO" in Controller
}
 
main
{
    getBallot@ConsoleIO()
}
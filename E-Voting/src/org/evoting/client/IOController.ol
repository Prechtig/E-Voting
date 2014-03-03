interface Interface {
    OneWay: println( string )
}
 
outputPort Controller {
    Interfaces: Interface
}
 
embedded {
    Java: "org.evoting.client.Controller" in Controller
}
 
main
{
    println@Controller("Hello World!")
}
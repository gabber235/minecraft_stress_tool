package me.gabber235.serverstresstool

import org.apache.commons.cli.*
import kotlin.system.exitProcess

object Loader {
  @JvmStatic
  fun main(args: Array<String>) {
    //Stresser st = new Stresser("survivalcore.eu",25565,20,"bottest",5000);
    val options = Options()
    val serverAdress = Option("a", "ip", true, "server adress")
    serverAdress.isRequired = true
    options.addOption(serverAdress)
    val port = Option("p", "port", true, "port number")
    port.isRequired = true
    options.addOption(port)
    val threadnum = Option("t", "threadnum", true, "threads (bots) number")
    threadnum.isRequired = true
    options.addOption(threadnum)
    val basenick = Option("n", "nick", true, "base string for nicknames")
    basenick.isRequired = true
    options.addOption(basenick)
    val delay = Option("d", "delay", true, "delay in ms between bot joins (default 5000ms)")
    delay.isRequired = true
    options.addOption(delay)

    val parser: CommandLineParser = DefaultParser()
    val formatter = HelpFormatter()
    val cmd: CommandLine = try {
      parser.parse(options, args)
    } catch (e: ParseException) {
      println(e.message)
      formatter.printHelp("utility-name", options)
      exitProcess(0)
    }

    val st = Stresser(
      cmd.getOptionValue("ip"), cmd.getOptionValue("port").toInt(), cmd.getOptionValue("threadnum").toInt(),
      cmd.getOptionValue("nick"), cmd.getOptionValue("delay").toInt()
    )
    //st.getServerInfo();
    st.startStressTest()
  }
}
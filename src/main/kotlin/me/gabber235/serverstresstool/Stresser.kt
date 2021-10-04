package me.gabber235.serverstresstool

import com.github.steveice10.mc.protocol.MinecraftProtocol
import com.github.steveice10.mc.protocol.data.game.entity.player.HandPreference
import com.github.steveice10.mc.protocol.data.game.setting.ChatVisibility
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientSettingsPacket
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket
import com.github.steveice10.packetlib.event.session.*
import com.github.steveice10.packetlib.packet.Packet
import com.github.steveice10.packetlib.tcp.TcpClientSession
import kotlinx.coroutines.*
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

private const val spread = .3f

/**
 * Creates new stresser object (with register and login)
 */
class Stresser
  (
  private val serverAdress: String,
  private val port: Int, //Final variables
  private val threadsNum: Int,
  private val nick: String,
  private val delay: Int,
) {
  var currentCount = AtomicInteger()

  /**
   * Start stressing server
   */
  fun startStressTest() {
    runBlocking {
      for (i in 0 until threadsNum) {
        println("Started new bot:$i")
        joinserver(i)
        delay(delay.toLong())
      }
      while (true) {
      }
    }
  }

  private fun joinserver(id: Int) {
    val protocol = MinecraftProtocol(nick + id)

    val session = TcpClientSession(serverAdress, port, protocol)
    session.addListener(object : SessionAdapter() {
      var scheduled = false
      var x = 0f
      var z = 0f
      override fun packetReceived(event: PacketReceivedEvent) {
        if (event.getPacket<Packet>() is ServerPlayerPositionRotationPacket) {
          val packet = event.getPacket<ServerPlayerPositionRotationPacket>()
          val session = event.session
          println("Connected")
          currentCount.getAndIncrement()
          x = packet.x.toFloat()
          z = packet.z.toFloat()
          session.send(ClientTeleportConfirmPacket(packet.teleportId))
          if (!scheduled) {
            session.send(
              ClientSettingsPacket(
                "en_US",
                8,
                ChatVisibility.FULL,
                true,
                ArrayList(),
                HandPreference.RIGHT_HAND,
                false,
              )
            )
            GlobalScope.launch {
              while (true) {
                x += if (ThreadLocalRandom.current()
                    .nextBoolean()
                ) Random.nextFloat() * spread else Random.nextFloat() * -spread
                z += if (ThreadLocalRandom.current()
                    .nextBoolean()
                ) Random.nextFloat() * spread else Random.nextFloat() * -spread

                session.send(ClientPlayerPositionPacket(false, x.toDouble(), 41.0, z.toDouble()))

                delay(100L)
              }
            }
            scheduled = true
          }
        }
      }

      override fun disconnected(event: DisconnectedEvent) {
        println("Disconnected: " + event.reason)
        currentCount.decrementAndGet()
        if (event.cause != null) {
          event.cause.printStackTrace()
        }
      }
    })
    session.connect(true)
  }
}
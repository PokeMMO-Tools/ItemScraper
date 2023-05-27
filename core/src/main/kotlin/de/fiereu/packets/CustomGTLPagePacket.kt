package de.fiereu.packets

import de.fiereu.ItemScraper
import de.fiereu.pokemmo.headless.network.packets.game.deserializable.GTLPagePacket
import de.fiereu.pokemmo.headless.network.server.AbstractServer

class CustomGTLPagePacket(id: Byte): GTLPagePacket(id) {

    companion object {
        var itemScraper: ItemScraper? = null
    }

    override fun handle(server: AbstractServer) {
        if(itemScraper == null) return super.handle(server)
        itemScraper!!.handleGTLPage(this)
    }
}
package de.fiereu

import org.slf4j.LoggerFactory

data class CacheEntry(val price: Int, val quantity: Short)

object ItemCache {
  private val logger = LoggerFactory.getLogger(ItemCache::class.java)
  private val entries: HashMap<Short, ArrayList<CacheEntry>> = HashMap()

  fun addEntry(itemID: Short, entry: CacheEntry) {
    entries[itemID]?.add(entry) ?: entries.put(itemID, arrayListOf(entry))
  }

  fun persist(database: Database): Int {
    logger.info("Persisting cache")
    val snapshotID = database.createSnapshot()
    if (snapshotID == -1) {
      logger.error("Failed to create snapshot")
      return -1
    }
    for (itemID in entries.keys) {
      if (entries[itemID] == null || entries[itemID]!!.isEmpty()) continue
      database.addTradableItem(itemID)
      val minPrice = entries[itemID]!!.minBy { it.price }.price
      val maxPrice = entries[itemID]!!.maxBy { it.price }.price
      val avgPrice = entries[itemID]!!.map { it.price }.average().toInt()
      val totalEntries = entries[itemID]!!.size
      val totalQuantity = entries[itemID]!!.map { it.quantity }.sum()
      database.addEntry(
        snapshotID,
        itemID,
        minPrice,
        maxPrice,
        avgPrice,
        totalEntries,
        totalQuantity
      )
    }
    return snapshotID
  }
}

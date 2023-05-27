package de.fiereu

import org.slf4j.LoggerFactory
import java.sql.Date
import java.sql.DriverManager
import java.sql.Statement
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.TemporalField

class Database(private val config: DatabaseConfig) {
  private val logger = LoggerFactory.getLogger(Database::class.java)
  private val connection =
      DriverManager.getConnection(
          "jdbc:mysql://${config.host}:${config.port}/${config.database}",
          config.username,
          config.password)
  
  fun createSnapshot(): Int {
    val statement = connection.prepareStatement("INSERT INTO items_snapshots (id, created_at, created_at_epoch) VALUES (NULL, ?, ?)", Statement.RETURN_GENERATED_KEYS)
    val instant = ZonedDateTime.now(ZoneId.systemDefault()).toInstant()
    statement.setDate(1, Date(instant.toEpochMilli()))
    statement.setInt(2, instant.epochSecond.toInt())
    statement.executeUpdate()
    val resultSet = statement.generatedKeys
    return if(resultSet.next()) {
      resultSet.getInt(1)
    } else {
      -1
    }
  }
  
  fun addTradableItem(itemID: Short) {
    val statement = connection.prepareStatement("INSERT IGNORE INTO items_tradable (item_id) VALUES (?)")
    statement.setShort(1, itemID)
    statement.executeUpdate()
  }
  
  fun addEntry(
    snapshotID: Int,
    itemID: Short,
    minPrice: Int,
    maxPrice: Int,
    avgPrice: Int,
    totalEntries: Int,
    totalQuantity: Int
  ) {
    val statement = connection.prepareStatement("INSERT IGNORE INTO items_entries (snapshot_id, item_id, min_price, max_price, avg_price, total_entries, total_quantity) VALUES (?, ?, ?, ?, ?, ?, ?)")
    statement.setInt(1, snapshotID)
    statement.setShort(2, itemID)
    statement.setInt(3, minPrice)
    statement.setInt(4, maxPrice)
    statement.setInt(5, avgPrice)
    statement.setInt(6, totalEntries)
    statement.setInt(7, totalQuantity)
    statement.executeUpdate()
  }
}

package de.fiereu

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatterBuilder
import java.util.*

class WebHook(private val url: String) {
  
  class WebHookException(message: String) : Exception(message)
  
  class WebHookResponse(val code: Int, val message: String)
  
  class WebHookEmbed() {
    
    companion object {
      private val DATE_FORMATTER = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .toFormatter()
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())
    }
    
    val jsonObject: JsonObject = JsonParser.parseString("{}").asJsonObject
    
    fun setTitle(title: String): WebHookEmbed {
      jsonObject.addProperty("title", title)
      return this
    }
    
    fun setColor(color: Int): WebHookEmbed {
      jsonObject.addProperty("color", color)
      return this
    }
    
    fun setDescription(description: String): WebHookEmbed {
      jsonObject.addProperty("description", description)
      return this
    }
    
    fun setTimestamp(timestamp: Instant): WebHookEmbed {
      jsonObject.addProperty("timestamp", DATE_FORMATTER.format(timestamp))
      return this
    }
    
    fun setUrl(url: String): WebHookEmbed {
      jsonObject.addProperty("url", url)
      return this
    }
    
    fun setAuthorName(authorName: String): WebHookEmbed {
      if (!jsonObject.has("author")) {
        jsonObject.add("author", JsonObject())
      }
      jsonObject.getAsJsonObject("author").addProperty("name", authorName)
      return this
    }
    
    fun setAuthorUrl(authorUrl: String): WebHookEmbed {
      if (!jsonObject.has("author")) {
        jsonObject.add("author", JsonObject())
      }
      jsonObject.getAsJsonObject("author").addProperty("url", authorUrl)
      return this
    }
    
    fun setAuthor(authorName: String, authorUrl: String): WebHookEmbed {
      setAuthorName(authorName)
      setAuthorUrl(authorUrl)
      return this
    }
    
    fun setImageUrl(imageUrl: String): WebHookEmbed {
      if (!jsonObject.has("image")) {
        jsonObject.add("image", JsonObject())
      }
      jsonObject.getAsJsonObject("image").addProperty("url", imageUrl)
      return this
    }
    
    fun setThumbnailUrl(thumbnailUrl: String): WebHookEmbed {
      if (!jsonObject.has("thumbnail")) {
        jsonObject.add("thumbnail", JsonObject())
      }
      jsonObject.getAsJsonObject("thumbnail").addProperty("url", thumbnailUrl)
      return this
    }
    
    fun setFooterText(footerText: String): WebHookEmbed {
      if (!jsonObject.has("footer")) {
        jsonObject.add("footer", JsonObject())
      }
      jsonObject.getAsJsonObject("footer").addProperty("text", footerText)
      return this
    }
    
    fun setFooterIconUrl(footerIconUrl: String): WebHookEmbed {
      if (!jsonObject.has("footer")) {
        jsonObject.add("footer", JsonObject())
      }
      jsonObject.getAsJsonObject("footer").addProperty("icon_url", footerIconUrl)
      return this
    }
    
    fun setFooter(footerText: String, footerIconUrl: String): WebHookEmbed {
      setFooterText(footerText)
      setFooterIconUrl(footerIconUrl)
      return this
    }
    
    fun addField(name: String, value: String, inline: Boolean): WebHookEmbed {
      val field = JsonObject()
      field.addProperty("name", name)
      field.addProperty("value", value)
      field.addProperty("inline", inline)
      if (!jsonObject.has("fields")) {
        jsonObject.add("fields", JsonArray())
      }
      jsonObject.getAsJsonArray("fields").add(field)
      return this
    }
    
    fun addField(name: String, value: String): WebHookEmbed {
      return addField(name, value, false)
    }
    
  }
  
  class WebHookMessage() {
    
    val jsonObject: JsonObject = JsonParser.parseString("{}").asJsonObject
    
    fun setUsername(username: String): WebHookMessage {
      jsonObject.addProperty("username", username)
      return this
    }
    
    fun setAvatarUrl(avatarUrl: String): WebHookMessage {
      jsonObject.addProperty("avatar_url", avatarUrl)
      return this
    }
    
    fun setContent(content: String): WebHookMessage {
      jsonObject.addProperty("content", content)
      return this
    }
    
    fun addEmbed(embed: WebHookEmbed): WebHookMessage {
      if (!jsonObject.has("embeds")) {
        jsonObject.add("embeds", JsonArray())
      }
      jsonObject.getAsJsonArray("embeds").add(embed.jsonObject)
      return this
    }
  }
  
  // send raw data to discord webhook
  private fun send(data: String) {
    val client = OkHttpClient()
    val body = data.toRequestBody("application/json".toMediaTypeOrNull())
    val request = Request.Builder()
      .url(url)
      .post(body)
      .build()
    val response = client.newCall(request).execute()
    response.close()
  }
  
  fun sendMessage(message: WebHookMessage) {
    send(message.jsonObject.toString())
  }
  
  fun createMessage(): WebHookMessage {
    return WebHookMessage()
  }
}
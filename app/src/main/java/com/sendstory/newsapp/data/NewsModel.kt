package com.sendstory.newsapp.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewsModel(

    @field:SerializedName("news")
    val news: List<NewsItem>? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("status_code")
    val statusCode: Int? = null,

    @field:SerializedName("pages")
    val pages: Int? = null,

    @field:SerializedName("publisher")
    val publisher: List<Any?>? = null,

    @field:SerializedName("category")
    val category: String? = null
) : Serializable

data class NewsItem(

    @field:SerializedName("summary")
    val summary: String? = null,

    @field:SerializedName("sourceurl")
    val sourceurl: String? = null,

    @field:SerializedName("formatted_summary")
    val formattedSummary: String? = null,

    @field:SerializedName("publisherid")
    val publisherid: String? = null,

    @field:SerializedName("featured")
    val featured: Int? = null,

    @field:SerializedName("subtitle")
    val subtitle: String? = null,

    @field:SerializedName("imageurl")
    val imageurl: String? = null,

    @field:SerializedName("publisher")
    val publisher: NewsPublisher? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("shortcode")
    val shortcode: String? = null,

    @field:SerializedName("headline")
    val headline: String? = null,

    @field:SerializedName("pubtimestamp")
    val pubtimestamp: String? = null
): Serializable

data class NewsPublisher(

    @field:SerializedName("pubname")
    val pubname: String? = null,

    @field:SerializedName("favicon")
    val favicon: String? = null,

    @field:SerializedName("websiteurl")
    val websiteurl: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("shortcode")
    val shortcode: String? = null,

    @field:SerializedName("logourl")
    val logourl: String? = null
) : Serializable

package com.sendstory.newsapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewsDetailModel(

    @field:SerializedName("news")
    val news: News? = null,

    @field:SerializedName("status_code")
    val statusCode: Int? = null
) : Serializable

data class Publisher(

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("feedurl")
    val feedurl: String? = null,

    @field:SerializedName("timezone")
    val timezone: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("language")
    val language: String? = null,

    @field:SerializedName("shortcode")
    val shortcode: String? = null,

    @field:SerializedName("logourl")
    val logourl: String? = null,

    @field:SerializedName("lastscrapetimestamp")
    val lastscrapetimestamp: String? = null,

    @field:SerializedName("foundingdate")
    val foundingdate: String? = null,

    @field:SerializedName("scrape_blocker")
    val scrapeBlocker: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("international")
    val international: String? = null,

    @field:SerializedName("replace_text")
    val replaceText: String? = null,

    @field:SerializedName("favicon")
    val favicon: String? = null,

    @field:SerializedName("topics")
    val topics: String? = null,

    @field:SerializedName("scrape_images")
    val scrapeImages: String? = null,

    @field:SerializedName("strip_text")
    val stripText: String? = null,

    @field:SerializedName("pubname")
    val pubname: String? = null,

    @field:SerializedName("ownership")
    val ownership: String? = null,

    @field:SerializedName("websiteurl")
    val websiteurl: String? = null,

    @field:SerializedName("showsummary")
    val showsummary: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("filter_keywords")
    val filterKeywords: String? = null,

    @field:SerializedName("scrape_author_class")
    val scrapeAuthorClass: String? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Serializable

data class RelatedItem(

    @field:SerializedName("wordcount")
    val wordcount: String? = null,

    @field:SerializedName("sentiment")
    val sentiment: String? = null,

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("featured")
    val featured: String? = null,

    @field:SerializedName("keywords")
    val keywords: String? = null,

    @field:SerializedName("scraped_subtitle")
    val scrapedSubtitle: String? = null,

    @field:SerializedName("factchecked")
    val factchecked: String? = null,

    @field:SerializedName("language")
    val language: String? = null,

    @field:SerializedName("scraped_image")
    val scrapedImage: String? = null,

    @field:SerializedName("shortcode")
    val shortcode: String? = null,

    @field:SerializedName("scraped_headline")
    val scrapedHeadline: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("international")
    val international: String? = null,

    @field:SerializedName("pubtimestamp")
    val pubtimestamp: String? = null,

    @field:SerializedName("headline")
    val headline: String? = null,

    @field:SerializedName("views")
    val views: String? = null,

    @field:SerializedName("pubdate")
    val pubdate: String? = null,

    @field:SerializedName("subtitle_sentences")
    val subtitleSentences: String? = null,

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("sponsored")
    val sponsored: String? = null,

    @field:SerializedName("scrapetimestamp")
    val scrapetimestamp: String? = null,

    @field:SerializedName("sourceurl")
    val sourceurl: String? = null,

    @field:SerializedName("nativereading")
    val nativereading: String? = null,

    @field:SerializedName("publisherid")
    val publisherid: String? = null,

    @field:SerializedName("pubtime")
    val pubtime: String? = null,

    @field:SerializedName("subtitle")
    val subtitle: String? = null,

    @field:SerializedName("imageurl")
    val imageurl: String? = null,

    @field:SerializedName("publisher")
    val publisher: Publisher? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Serializable

data class News(

    @field:SerializedName("wordcount")
    val wordcount: String? = null,

    @field:SerializedName("sentiment")
    val sentiment: String? = null,

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("formatted_summary")
    val formattedSummary: String? = null,

    @field:SerializedName("featured")
    val featured: String? = null,

    @field:SerializedName("keywords")
    val keywords: String? = null,

    @field:SerializedName("scraped_subtitle")
    val scrapedSubtitle: String? = null,

    @field:SerializedName("sentences")
    val sentences: String? = null,

    @field:SerializedName("factchecked")
    val factchecked: String? = null,

    @field:SerializedName("language")
    val language: String? = null,

    @field:SerializedName("scraped_image")
    val scrapedImage: String? = null,

    @field:SerializedName("shortcode")
    val shortcode: String? = null,

    @field:SerializedName("body")
    val body: String? = null,

    @field:SerializedName("related")
    val related: List<RelatedItem?>? = null,

    @field:SerializedName("finalhtml")
    val finalhtml: String? = null,

    @field:SerializedName("scraped_headline")
    val scrapedHeadline: String? = null,

    @field:SerializedName("structured_data")
    val structuredData: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("international")
    val international: String? = null,

    @field:SerializedName("pubtimestamp")
    val pubtimestamp: String? = null,

    @field:SerializedName("headline")
    val headline: String? = null,

    @field:SerializedName("views")
    val views: String? = null,

    @field:SerializedName("pubdate")
    val pubdate: String? = null,

    @field:SerializedName("summary")
    val summary: String? = null,

    @field:SerializedName("subtitle_sentences")
    val subtitleSentences: String? = null,

    @field:SerializedName("images")
    val images: String? = null,

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("sponsored")
    val sponsored: String? = null,

    @field:SerializedName("scrapetimestamp")
    val scrapetimestamp: String? = null,

    @field:SerializedName("sourceurl")
    val sourceurl: String? = null,

    @field:SerializedName("nativereading")
    val nativereading: String? = null,

    @field:SerializedName("publisherid")
    val publisherid: String? = null,

    @field:SerializedName("pubtime")
    val pubtime: String? = null,

    @field:SerializedName("subtitle")
    val subtitle: String? = null,

    @field:SerializedName("imageurl")
    val imageurl: String? = null,

    @field:SerializedName("publisher")
    val publisher: Publisher? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Serializable

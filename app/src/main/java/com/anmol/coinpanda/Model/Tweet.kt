package com.anmol.coinpanda.Model

import java.sql.Date
import java.sql.Time

/**
 * Created by anmol on 3/2/2018.
 */
class Tweet{
    var coin:String?=null
    var coin_symbol:String?=null
    var tweet:String?=null
    var url:String?=null
    var keyword:String?=null
    var tweetid:String?=null
    var booked:Boolean = false
    var dates : String? = null
    var source : String?=null

    constructor()
    constructor(coin: String?, coin_symbol: String?, tweet: String?, url: String?, keyword: String?, tweetid: String?, booked: Boolean, dates: String?, source: String?) {
        this.coin = coin
        this.coin_symbol = coin_symbol
        this.tweet = tweet
        this.url = url
        this.keyword = keyword
        this.tweetid = tweetid
        this.booked = booked
        this.dates = dates
        this.source = source
    }


}
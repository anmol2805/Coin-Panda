package com.anmol.coinpanda.Model

class Sqltweet {
    var coin:String?=null
    var coin_symbol:String?=null
    var tweet:String?=null
    var url:String?=null
    var keyword:String?=null
    var tweetid:String?=null
    var dates : String? = null
    var coinpage : String?=null
    var mytweet: Int?=null
    var booked:Int?=null

    constructor(coin: String?, coin_symbol: String?, tweet: String?, url: String?, keyword: String?, tweetid: String?, dates: String?, coinpage: String?, mytweet: Int?,booked:Int?) {
        this.coin = coin
        this.coin_symbol = coin_symbol
        this.tweet = tweet
        this.url = url
        this.keyword = keyword
        this.tweetid = tweetid
        this.dates = dates
        this.coinpage = coinpage
        this.mytweet = mytweet
        this.booked = booked
    }

    constructor()
}
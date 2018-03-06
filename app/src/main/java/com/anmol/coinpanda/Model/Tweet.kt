package com.anmol.coinpanda.Model

/**
 * Created by anmol on 3/2/2018.
 */
class Tweet{
    var coin:String?=null
    var coin_symbol:String?=null
    var tweet:String?=null
    var url:String?=null


    constructor()
    constructor(coin: String?, coin_symbol: String?, tweet: String?, url: String?) {
        this.coin = coin
        this.coin_symbol = coin_symbol
        this.tweet = tweet
        this.url = url
    }


}
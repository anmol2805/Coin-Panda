package com.anmol.coinpanda.Model

class Sqlcoin {
    var coin:String?=null
    var coin_symbol:String?=null
    var coinpage : String?=null

    constructor(coin: String?, coin_symbol: String?, coinpage: String?) {
        this.coin = coin
        this.coin_symbol = coin_symbol
        this.coinpage = coinpage
    }

    constructor()
}
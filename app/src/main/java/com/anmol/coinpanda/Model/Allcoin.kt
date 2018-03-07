package com.anmol.coinpanda.Model

/**
 * Created by anmol on 3/1/2018.
 */
class Allcoin{
    var coinname:String? = null
    var coin:String?=null
    var coinpage:String?=null
    constructor()
    constructor(coinname: String?, coin: String?,coinpage:String?) {
        this.coinname = coinname
        this.coin = coin
        this.coinpage = coinpage
    }

}
package com.anmol.coinpanda.Model

/**
 * Created by anmol on 2/27/2018.
 */
class Coin {
    var coinname:String? = null
    var coinnotify: Boolean = false

    constructor()
    constructor(coinname: String, coinnotify: Boolean) {
        this.coinname = coinname
        this.coinnotify = coinnotify
    }


}
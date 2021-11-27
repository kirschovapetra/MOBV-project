package sk.stuba.fei.uim.mobv_project.data

class Constants {
    //    enum class AssetType {
//        native,credit_alphanum4,credit_alphanum12,liquidity_pool_shares;
//    }
    // TODO mozno AssetType ani nebude treba, ked tak potom zmazat
   class AssetType {
        companion object {
            const val NATIVE = "native"
            const val CREDIT_ALPHANUM4 = "credit_alphanum4"
            const val CREDIT_ALPHANUM12 = "credit_alphanum12"
            const val LIQUIDITY_POOL_SHARES = "liquidity_pool_shares"
        }
   }
}
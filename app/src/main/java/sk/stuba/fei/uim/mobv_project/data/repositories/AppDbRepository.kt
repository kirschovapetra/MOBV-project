package sk.stuba.fei.uim.mobv_project.data.repositories

abstract class AppDbRepository {
// TODO popridavat do kazdeho repozitara komunikaciu s api like dis:

//    suspend fun loadMars(onError: (error: String) -> Unit) {
//
//        try {
//            val response = api.getProperties()
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    return cache.insertImages(it.map { item ->
//                        MarsItem(
//                            item.price,
//                            item.id,
//                            item.type,
//                            item.img_src
//                        )
//                    })
//                }
//            }
//
//            onError("Load images failed. Try again later please.")
//        } catch (ex: ConnectException) {
//            onError("Off-line. Check internet connection.")
//            ex.printStackTrace()
//            return
//        } catch (ex: Exception) {
//            onError("Oops...Change failed. Try again later please.")
//            ex.printStackTrace()
//            return
//        }
//    }

}
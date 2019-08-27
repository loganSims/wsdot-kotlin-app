package gov.wa.wsdot.android.wsdot.repository

import androidx.lifecycle.LiveData
import gov.wa.wsdot.android.wsdot.db.traffic.FavoriteLocation
import gov.wa.wsdot.android.wsdot.db.traffic.FavoriteLocationDao
import gov.wa.wsdot.android.wsdot.util.AppExecutors
import gov.wa.wsdot.android.wsdot.util.network.DBResource
import gov.wa.wsdot.android.wsdot.util.network.Resource
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteLocationRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val favoriteLocationDao: FavoriteLocationDao
) {

    fun getFavoriteLocations(): LiveData<Resource<List<FavoriteLocation>>> {
        return object : DBResource<List<FavoriteLocation>>() {
            override fun loadFromDb() = favoriteLocationDao.loadFavoriteLocations()
        }.asLiveData()
    }

    fun addFavoriteLocation(name: String, lat: Double, lng: Double, zoom: Float) {
        appExecutors.diskIO().execute {
            val location = FavoriteLocation(name, lat, lng, zoom, Date())
            favoriteLocationDao.insertNewFavoriteLocation(location)
        }
    }

    fun removeFavoriteLocation(creationDate: Date){
        appExecutors.diskIO().execute {
            favoriteLocationDao.deleteFavoriteLocation(creationDate)
        }
    }

    fun removeAllFavoriteLocations(){
        appExecutors.diskIO().execute {
            favoriteLocationDao.deleteAllFavoriteLocations()
        }
    }

}
